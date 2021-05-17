package ru.team10.graphApp.view

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.FileChooser
import javafx.stage.Popup
import ru.team10.graphApp.controller.algorithms.*
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.utils.buildCentralityReport
import ru.team10.graphApp.utils.buildCommunityDetectionReport
import ru.team10.graphApp.utils.loadConfigFile
import ru.team10.graphApp.utils.saveConfig
import tornadofx.*

internal var showEdges = ToggleButton()
internal var graphView = GraphView(Graph())

class MainView : View("Graph Application") {

    private val centrality = Centrality
    private var anim = Layout.applyForceAtlas2(graphView)



    private fun TextField.createTextField(constantName: String, prop: SimpleDoubleProperty): TextField {

        val doubleValue = SimpleDoubleProperty(0.0)
        doubleValue.bindBidirectional(prop)
        prop.onChange { text = it.toString() }
//        doubleValue.onChange { text = it.toString() }
        text = prop.value.toString()
        filterInput { it.controlNewText.isDouble() }
        setOnKeyReleased { event ->
            if (event.code == KeyCode.ENTER) {
                if (text == null || text.trim().isBlank()) {
                    alert(Alert.AlertType.WARNING, "Please, input $constantName constant!")
                } else if (text.toDouble() <= 0.0) {
                    alert(Alert.AlertType.WARNING, "$constantName constant must be positive!")
                } else {
                    parent.requestFocus()
                    prop.value = text.toDouble()
                    text = prop.value.toString()
                }
            }
        }
        return this
    }

    override val root = borderpane {
        this.stylesheets.add("1.css")

        center {
            add(graphView)
        }
        top = vbox(10) {

            textflow {
                text("GRAPH") {
                    fill = Color.PURPLE
                    font = Font(20.0)
                }
                text("App") {
                    fill = Color.ORANGE
                    font = Font(28.0)
                }
            }
        }
        left = vbox {
            titledpane("Graph") {
                isExpanded = false
                vbox(5) {
                    label("IMPORT")
                    hbox(5) {
                        button("JSON").createJsonImportButton {
                            center.getChildList()!!.clear()
                            center.add(graphView)
                            anim = Layout.applyForceAtlas2(graphView)
                        }

                        button("SQLite").createSQLiteImportButton {
                            center.getChildList()!!.clear()
                            center.add(graphView)
                            anim = Layout.applyForceAtlas2(graphView)
                        }

                        button("Neo4j").createNeo4jImportButton {
                            center.getChildList()!!.clear()
                            center.add(graphView)
                            anim = Layout.applyForceAtlas2(graphView)
                        }

                    }

                    label("EXPORT")
                    hbox(5) {
                        button("JSON").createJsonExportButton()
                        button("SQLite").createSQLiteExportButton()
                        button("Neo4j").createNeo4jExportButton()
                    }
                }
            }
            titledpane("Layout") {
                isExpanded = false
                vbox(5) {
                    togglebutton("START") {
                        isSelected = false
                        action {
                            text = if (isSelected) {
                                runAsync {
                                    currentStage?.apply {
                                        anim.start()
                                    }
                                }
                                "STOP"
                            } else {
                                anim.stop()
                                "START"
                            }
                        }
                    }

                    label("Scaling")
                    textfield().createTextField("Scaling", Layout.scaling)

                    label("Gravity")
                    textfield().createTextField( "Gravity", Layout.gravity)

                    label("Jitter tolerance")
                    textfield().createTextField( "Jitter tolerance", Layout.jitterTolerance)
                    checkbox("BarnesHut optimisation") {
                        selectedProperty().bindBidirectional(Layout.isBarnesHutActive)
                    }
                }
            }
            titledpane("Community") {
                isExpanded = false
                vbox(10) {
                    label("Resolution")
                    textfield().createTextField("Resolution", Layout.jitterTolerance)

                    button("Start Leiden algorithm") {
                        action {
                            lateinit var leiden: Leiden
                            runAsync {
                                leiden = Leiden(graphView)
                                leiden.startLeiden(leidenResolution)
                                leiden.setCommunity()
                            }
                        }
                    }
                    button("SAVE") {
                        action {
                            val window = Popup()
                            val fileChooser = FileChooser()
                            val importFilter =
                                FileChooser.ExtensionFilter("Text file (*.txt)", "*.txt")
                            fileChooser.extensionFilters.add(importFilter)
                            fileChooser.title = "Save result"
                            val file = fileChooser.showSaveDialog(window)
                            file?.let {
                                buildCommunityDetectionReport(graphView, file)
                            }
                        }
                    }
                    button("COLOR") {
                        action {
                            if (graphView.vertices().map { it.vertex.communityID }.contains(-1)) {
                                alert(Alert.AlertType.ERROR, "ERROR!\nRun the algorithm before using its result!")
                            } else {
                               colorAccordingToCommunity(graphView)
                            }
                        }
                    }

                }
            }
            titledpane("Centrality") {
                this.isExpanded = false
                vbox(10) {
                    button("START") {
                        action {
                            runAsync {
                                centrality.applyHarmonicCentrality(graphView)
                            }
                        }
                    }
                    button("SAVE") {
                        action {
                            val window = Popup()
                            val fileChooser = FileChooser()
                            val importFilter =
                                FileChooser.ExtensionFilter("Text file (*.txt)", "*.txt")
                            fileChooser.extensionFilters.add(importFilter)
                            fileChooser.title = "Save result"
                            val file = fileChooser.showSaveDialog(window)
                            file?.let {
                                buildCentralityReport(graphView, file)
                            }
                        }
                    }
                    button("COLOR") {
                        action {
                            if (graphView.vertices().map { it.vertex.centralityRang }.contains(-1.0)) {
                                alert(Alert.AlertType.ERROR, "ERROR!\nRun the algorithm before using its result!")
                            } else {
                                val min = graphView.vertices().map { it.vertex.centralityRang }.minOrNull()
                                val max = graphView.vertices().map { it.vertex.centralityRang }.maxOrNull()
                                Centrality.setColor(
                                    (max!! - min!!) / graphView.vertices().size,
                                    255.0 / graphView.vertices().size,
                                    min,
                                    graphView.vertices()
                                )
                            }
                        }
                    }
                }
            }
            titledpane("Settings") {
                this.isExpanded = false
                vbox(10) {
                    button("Load configuration file") {
                        action {
                            val window = Popup()
                            val fileChooser = FileChooser()
                            val importFilter = FileChooser.ExtensionFilter("Configuration file (*.yaml)", "*.yaml")
                            fileChooser.extensionFilters.add(importFilter)
                            fileChooser.title = "Open Configuration File"
                            val file = fileChooser.showOpenDialog(window)
                            file?.let {
                                loadConfigFile(file)
                            }
                        }
                    }

                    button("Save configuration file") {
                        action {
                            val window = Popup()
                            val fileChooser = FileChooser()
                            val importFilter = FileChooser.ExtensionFilter("Configuration file (*.yaml)", "*.yaml")
                            fileChooser.extensionFilters.add(importFilter)
                            fileChooser.title = "Save Configuration File"
                            val file = fileChooser.showSaveDialog(window)
                            file?.let {
                                saveConfig(file)
                            }
                        }
                    }
                    showEdges = togglebutton("Hide edges") {
                        selectedProperty().onChange {
                            text = if (!isSelected) {
                                for (edge in graphView.edges()) edge.hide()
                                "Show edges"
                            } else {
                                for (edge in graphView.edges()) edge.show()
                                "Hide edges"
                            }
                        }
                    }
                    label("Vertex color:")
                    val q = colorpicker(Color.AQUA)
                    q.setOnHiding {
                        for (v in graphView.vertices()) v.color = q.value
                        vertexColor = q.value
                    }
                    label("Vertex radius")
                    textfield().createTextField("Radius", vertexRadius)
                }
            }
        }
        style {
            backgroundColor += Color.AZURE
        }
    }
}