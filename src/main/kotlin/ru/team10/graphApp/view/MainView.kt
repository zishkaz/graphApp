package ru.team10.graphApp.view

import javafx.scene.control.Alert
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.FileChooser
import javafx.stage.Popup
import ru.team10.graphApp.controller.algorithms.Centrality
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.controller.algorithms.scaling
import ru.team10.graphApp.controller.algorithms.gravity
import ru.team10.graphApp.controller.algorithms.isBarnesHutActive
import ru.team10.graphApp.controller.algorithms.Leiden
import ru.team10.graphApp.controller.loader.FileLoader
import ru.team10.graphApp.controller.loader.SQLiteLoader
import ru.team10.graphApp.model.Graph
import tornadofx.*

private var graphFilename: String? = "src/input.txt"
private lateinit var leiden: Leiden

class MainView : View("Graph Application") {

    private var graphView = GraphView(Graph())
    private val centrality = Centrality()
    private var layout = Layout().applyForceAtlas2(graphView)

    override val root = borderpane {
        this.stylesheets.add("1.css")
        fun apply() {

            currentStage?.apply {

                layout.start()
//               centrality.applyHarmonicCentrality(props.sample, graphView.vertices())
            }
        }

        fun applyStop() {

            currentStage?.apply {

                layout.stop()
//                centrality.applyHarmonicCentrality(props.sample, graphView.vertices())
            }
        }

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
            titledpane("GRAPH") {
                vbox(5) {
                    label("IMPORT")
                    hbox(5) {
                        button("JSON") {
                            action {
//                                val window = Popup()
//                                val fileChooser = FileChooser()
//                                val importFilter = FileChooser.ExtensionFilter("Graph file (*.json)", "*.json")
//                                fileChooser.extensionFilters.add(importFilter)
//                                fileChooser.title = "Open Resource File"
//                                val file = fileChooser.showOpenDialog(window)
//                                file?.let {
//                                    graphView = FileLoader().loadGraph(file.path)
//                                    center.getChildList()!!.clear()
//                                    center.add(graphView)
//                                    layout = Layout().applyForceAtlas2(graphView)
//                                }
//                            }
//                        }
                                SQLiteLoader().loadGraph("D:\\Dev\\graphApp\\identifier.sqlite")?.let {
                                    graphView = it
                                    center.getChildList()!!.clear()
                                    center.add(graphView)
                                    layout = Layout().applyForceAtlas2(graphView)
                                }
                            }
                        }
                    }

                    right {
                        button("Export") {
                            action {
                                SQLiteLoader().saveGraph(graphView, "saved.sqlite")
                            }
                        }
                    }
                    bottom {
                        togglebutton("Hide edges") {
                            isSelected = false
                            action {
                                text = if (isSelected) {
                                    for (edge in graphView.edges()) edge.hide()
                                    "Show edges"
                                } else {
                                    for (edge in graphView.edges()) edge.show()
                                    "Hide edges"
                                }
                            }

                        }
                    }


//                        val r = button()
//                        r.text = "SQLite"
//                        r.setOnMouseReleased {
//                            r.isDisable = true
//                            hbox {
//                                val input = textfield()
//                                input.promptText = ("URI or ...")
//                                var data: String
//                                input.setOnKeyReleased { event ->
//                                    if (event.code == KeyCode.ENTER) {
//                                        data = input.text
//                                        hide()
//                                        r.isDisable = false
//                                    }
//                                }
//                            }
//                        }
                    button("Neo4j") {}
                }

                label("EXPORT")
                hbox(5) {
                    button("JSON") {
                        action {
                            val window = Popup()
                            val fileChooser = FileChooser()
                            val importFilter = FileChooser.ExtensionFilter("Graph file (*.json)", "*.json")
                            fileChooser.extensionFilters.add(importFilter)
                            fileChooser.title = "Save Graph"
                            val file = fileChooser.showSaveDialog(window)
                            file?.let {
                                FileLoader().saveGraph(graphView, file.path)
                            }
                        }
                    }
                    button("SQLite") {}
                    button("Neo4j") {}
                }
            }
        }
        titledpane("LAYOUT") {
            vbox(5) {
                val kek = togglebutton("START") {
                    this.isSelected = false
                    action {
                        text = if (isSelected) {
                            runAsync {
                                apply()
                            }
                            "STOP"
                        } else {
                            runAsync {
                                applyStop()
                            }
                            "START"
                        }
                    }
                }
                label("Scaling")
                textfield {
                    this.promptText = " = $scaling"
                    filterInput { it.controlNewText.isDouble() }
                    this.setOnKeyReleased { event ->
                        if (event.code == KeyCode.ENTER) {
                            if (this.text == null || this.text.trim().isBlank()) {
                                alert(Alert.AlertType.WARNING, "Please, input Scaling constant !")
                            } else if (this.text.toDouble() <= 0.0) {
                                alert(Alert.AlertType.WARNING, "Scaling constant must be positive !")
                            } else {
                                scaling = this.text.toDouble()
                                println("scal $scaling")
                                this.parent.requestFocus()
                            }
                        }
                    }
                }

                label("Gravity")
                textfield {
                    this.promptText = " = $gravity"
                    filterInput { it.controlNewText.isDouble() }
                    this.setOnKeyReleased { event ->
                        if (event.code == KeyCode.ENTER) {
                            if (this.text == null || this.text.trim().isBlank()) {
                                alert(Alert.AlertType.WARNING, "Please, input Gravity constant !")
                            } else if (this.text.toDouble() < 0.0) {
                                alert(Alert.AlertType.WARNING, "Gravity constant must be not negative !")
                            } else {
                                gravity = this.text.toDouble()
                                println("grav: $gravity")
                                this.parent.requestFocus()
                            }
                        }
                    }

                }

                checkbox("BarnesHut optimisation") {
                    action {
                        isBarnesHutActive = isSelected
                        println(isBarnesHutActive)
                    }
                }
            }
        }
        titledpane("Community") {
            vbox(10) {
                button("Start Leiden algorithm") {
                    action {
                        runAsync {
                            graphFilename?.let {
                                leiden = Leiden(it, "src/output.txt")
                                leiden.startLeiden(0.2)
                            }
                        }
                    }
                }
                val e = button()
                e.text("RESULT")
                e.setOnMouseReleased {
                    //e.isDisable = true
                    scrollpane {
                        textflow {
                            for (v in Graph().vertices())
                                text("ID: ${v.id} ----\n community ID: ${v.communityID}\n")
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
                        file?.let {}
                    }
                }

            }
        }
        titledpane("Centrality") {

            vbox(10) {
                val e = button()
                e.text("RESULT")
                e.setOnMouseReleased {
                    //e.isDisable = true
                    scrollpane {
                        textflow {
                            for (v in Graph().vertices())
                                text("ID: ${v.id} ----\n rank: ${v.centralityRang}\n")
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
                        file?.let {}
                    }
                }

            }
        }



        style {
            backgroundColor += Color.AZURE
        }
    }
}

class ErrorWindow(text: String) : View() {
    override val root = stackpane {

        label(text)
    }
}
