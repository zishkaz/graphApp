package ru.team10.graphApp.view

import javafx.scene.control.Alert
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.FileChooser
import javafx.stage.Popup
import ru.team10.graphApp.controller.algorithms.Centrality
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.controller.algorithms.Leiden
import ru.team10.graphApp.controller.loader.FileLoader
import ru.team10.graphApp.controller.loader.SQLiteLoader
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.utils.buildCentralityReport
import ru.team10.graphApp.utils.buildCommunityDetectionReport
import ru.team10.graphApp.utils.loadConfigFile
import ru.team10.graphApp.utils.saveConfig
import tornadofx.*

private var graphFilename: String? = "src/input.txt"
private lateinit var leiden: Leiden

class MainView : View("Graph Application") {

    private var graphView = GraphView(Graph())
    private val centrality = Centrality()
    private var layoutAnim = Layout.applyForceAtlas2(graphView)
    private var barnesHutCheckbox = CheckBox()
    private var scalingTextField = TextField()
    private var gravityTextField = TextField()
    private var jitterTextField = TextField()

    override val root = borderpane {
        this.stylesheets.add("1.css")
        fun apply() {

            currentStage?.apply {

                layoutAnim.start()
//               centrality.applyHarmonicCentrality(props.sample, graphView.vertices())
            }
        }

        fun applyStop() {

            currentStage?.apply {

                layoutAnim.stop()
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
                this.isExpanded = false
                vbox(5) {
                    label("IMPORT")
                    hbox(5) {
                        button("JSON") {
                            action {
                                val window = Popup()
                                val fileChooser = FileChooser()
                                val importFilter = FileChooser.ExtensionFilter("Graph file (*.json)", "*.json")
                                fileChooser.extensionFilters.add(importFilter)
                                fileChooser.title = "Open Resource File"
                                val file = fileChooser.showOpenDialog(window)
                                file?.let {
                                    FileLoader().loadGraph(file.path)?.let {
                                        graphView = it
                                        center.getChildList()!!.clear()
                                        center.add(graphView)
                                        layoutAnim = Layout.applyForceAtlas2(graphView)
                                    }
                                }
                            }
                        }

                        val sqliteButton = button()
                        sqliteButton.text = "SQLite"
                        sqliteButton.setOnMouseReleased {
                            sqliteButton.isDisable = true
                            hbox {
                                val input = textfield()
                                input.promptText = ("Enter URI")
                                var data: String
                                input.setOnKeyReleased { event ->
                                    if (event.code == KeyCode.ENTER) {
                                        data = input.text
                                        SQLiteLoader().loadGraph(data)?.let {
                                            graphView = it
                                            center.getChildList()!!.clear()
                                            center.add(graphView)
                                            layoutAnim = Layout.applyForceAtlas2(graphView)
                                        }
                                        hide()
                                        sqliteButton.isDisable = false
                                    }
                                }
                            }
                        }
                        val neo4jButton = button()
                        neo4jButton.text = ("Neo4j")
                        neo4jButton.setOnMouseReleased {
                            neo4jButton.isDisable = true
                            vbox(5) {
                                var data = String()

                                label("URI")
                                val uri = textfield()

                                label("Udername")
                                val username = textfield()

                                label("Password")
                                val password = textfield()

                                val k = button()
                                k.text = "OK"
                                k.setOnMouseReleased {
                                    if (uri.text.isBlank() || username.text.isBlank() || password.text.isBlank()) {
                                        alert(
                                            Alert.AlertType.WARNING,
                                            "You have forgotten write in port or login or password!"
                                        )
                                    }
                                    data = uri.text.plus("\n").plus(username.text).plus("\n").plus(password.text)
                                    println(data)
                                    hide()
                                    neo4jButton.isDisable = false
                                }

                            }
                        }

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
                        val sqliteButton = button()
                        sqliteButton.text = "SQLite"
                        sqliteButton.setOnMouseReleased {
                            sqliteButton.isDisable = true
                            hbox {
                                val input = textfield()
                                input.promptText = ("Enter URI")
                                var data: String
                                input.setOnKeyReleased { event ->
                                    if (event.code == KeyCode.ENTER) {
                                        data = input.text
                                        SQLiteLoader().saveGraph(graphView, data)
                                        hide()
                                        sqliteButton.isDisable = false
                                    }
                                }
                            }
                        }
                        val neo4jButton = button()
                        neo4jButton.text = ("Neo4j")
                        neo4jButton.setOnMouseReleased {
                            neo4jButton.isDisable = true
                            vbox(5) {
                                var data = String()

                                label("URI")
                                val uri = textfield()

                                label("Username")
                                val username = textfield()

                                label("Password")
                                val password = textfield()

                                val k = button()
                                k.text = "OK"
                                k.setOnMouseReleased {
                                    if (uri.text.isBlank() || username.text.isBlank() || password.text.isBlank()) {
                                        alert(
                                            Alert.AlertType.WARNING,
                                            "You have forgotten write in port or login or password!"
                                        )
                                    }
                                    data = uri.text.plus("\n").plus(username.text).plus("\n").plus(password.text)
                                    println(data)
                                    hide()
                                    neo4jButton.isDisable = false
                                }

                            }
                        }
                    }
                }
            }
            titledpane("Layout") {
                this.isExpanded = false
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
                    scalingTextField = textfield {
                        this.promptText = " = ${Layout.scaling}"
                        filterInput { it.controlNewText.isDouble() }
                        this.setOnKeyReleased { event ->
                            if (event.code == KeyCode.ENTER) {
                                if (this.text == null || this.text.trim().isBlank()) {
                                    alert(Alert.AlertType.WARNING, "Please, input Scaling constant!")
                                } else if (this.text.toDouble() <= 0.0) {
                                    alert(Alert.AlertType.WARNING, "Scaling constant must be positive!")
                                } else {
                                    text = text.toDouble().toString()
                                    Layout.scaling = this.text.toDouble()
                                    this.parent.requestFocus()
                                }
                            }
                        }
                    }

                    label("Gravity")
                    gravityTextField = textfield {
                        this.promptText = " = ${Layout.gravity}"
                        filterInput { it.controlNewText.isDouble() }
                        this.setOnKeyReleased { event ->
                            if (event.code == KeyCode.ENTER) {
                                if (this.text == null || this.text.trim().isBlank()) {
                                    alert(Alert.AlertType.WARNING, "Please, input Gravity constant!")
                                } else if (this.text.toDouble() < 0.0) {
                                    alert(Alert.AlertType.WARNING, "Gravity constant must be not negative!")
                                } else {
                                    text = text.toDouble().toString()
                                    Layout.gravity = this.text.toDouble()
                                    this.parent.requestFocus()
                                }
                            }
                        }

                    }

                    label("Jitter tolerance")
                    jitterTextField = textfield {
                        this.promptText = " = ${Layout.jitterTolerance}"
                        filterInput { it.controlNewText.isDouble() }
                        this.setOnKeyReleased { event ->
                            if (event.code == KeyCode.ENTER) {
                                if (this.text == null || this.text.trim().isBlank()) {
                                    alert(Alert.AlertType.WARNING, "Please, input Jitter tolerance constant!")
                                } else if (this.text.toDouble() < 0.0) {
                                    alert(Alert.AlertType.WARNING, "Jitter tolerance constant must be not negative!")
                                } else {
                                    text = text.toDouble().toString()
                                    Layout.jitterTolerance = this.text.toDouble()
                                    this.parent.requestFocus()
                                }
                            }
                        }

                    }

                    barnesHutCheckbox = checkbox("BarnesHut optimisation") {
                        action {
                            Layout.isBarnesHutActive = isSelected
                        }
                    }
                }
            }
            titledpane("Community") {
                this.isExpanded = false
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
                            file?.let {
                                buildCommunityDetectionReport(graphView, file)
                            }
                        }
                    }

                }
            }
            titledpane("Centrality") {
                this.isExpanded = false
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
                            file?.let {
                                buildCentralityReport(graphView, file)
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
                                barnesHutCheckbox.isSelected = Layout.isBarnesHutActive
                                scalingTextField.text = "${Layout.scaling}"
                                gravityTextField.text = "${Layout.gravity}"
                                jitterTextField.text = "${Layout.jitterTolerance}"
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