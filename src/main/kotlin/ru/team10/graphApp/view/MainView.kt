package ru.team10.graphApp.view

import javafx.collections.FXCollections
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
import ru.team10.graphApp.model.Graph
import tornadofx.*

var constant1: String = "123"
private var graphFilename: String? = "src/input.txt"
private lateinit var leiden: Leiden

class MainView : View("Graph Application") {

    private var graphView = GraphView(Graph())
    private val centrality = Centrality()

    //var constant: TextField by singleAssign()
    private val layout by lazy { Layout().applyForceAtlas2(graphView) }

    override val root = borderpane {
        this.stylesheets.add("1.css")
        fun apply() {

            currentStage?.apply {

                layout.start()
//                centr.applyHarmonicCentrality(props.sample, graphView.vertices())
            }
        }

        fun applyStop() {

            currentStage?.apply {

                layout.stop()
//                centr.applyHarmonicCentrality(props.sample, graphView.vertices())
            }
        }

//        center {
//
//            add(graphView)
//        }
        left= vbox(10) {

            textflow {
                text("LITTLE") {
                    fill = Color.PURPLE
                    font = Font(20.0)
                }
                text("Big") {
                    fill = Color.ORANGE
                    font = Font(28.0)
                }
            }

            button("Reset default settings") {
                this.styleClass.add("button1")
                action {

                }
            }
            button("раскадка") {
                action {
                    new1().openWindow()
                }
            }
            this.add(new1())
            button("сообщества") {
                action {
                    new2().openWindow()
                }
            }
            button("централити") {
                style = ("-fx-background-color: white; -fx-border-color: grey; -fx-border-radius: 5;")
                action {
                    new3().openWindow()
                }
            }
            val texasCities = FXCollections.observableArrayList(
                "Austin",
                "Dallas", "Midland", "San Antonio", "Fort Worth"
            )

            combobox(values = texasCities)
        }


        right {
            accordion {
                fold("GRAPH"){
                    button("Import"){
                        action{
                            val window = Popup()
                            val fileChooser = FileChooser()
                            val importFilter = FileChooser.ExtensionFilter("Graph file", "*.json")
                            fileChooser.extensionFilters.add(importFilter)
                            fileChooser.title = "Open Resource File"
                            val file = fileChooser.showOpenDialog(window)
                            file?.let {
                                graphView = FileLoader().loadGraph(file.path)
                                center {

                                    add(graphView)
                                }
                            }
                        }
                    }
                }
                fold("Раскладка") {
                    checkbox("Atlas on/off") {
                        action {
                            if (isSelected) {
                                runAsync {
                                    apply()
                                }
                            }
                        }
                    }
                    togglebutton("START") {
                        style = "-fx-background-color: white; -fx-border-color: grey; -fx-border-radius: 5;}"
                        this.isSelected = false

                        action {
                            if (isSelected) {
                                runAsync {
                                    apply()
                                }
                                text = "STOP"
                            } else {
                                runAsync {
                                    applyStop()
                                }
                                text = "START"
                            }
                        }
                    }
                    val r = button()
                    r.text = "a = $constant1"
                    r.style = "-fx-background-color: white; -fx-border-color: grey; -fx-border-radius: 5;"
                    r.setOnMouseReleased {
                        r.isDisable = true
                        hbox {
                            //label("Constant")
                            //constant = textfield()
                            val filterTextField: TextField = textfield()
                            filterTextField.fitToSize(r)
                            filterTextField.setOnKeyReleased { event ->
                                if (event.code == KeyCode.ENTER) {
                                    println("Logging in as ${filterTextField.text} ")
                                    constant1 = filterTextField.text
                                    r.text = "a = $constant1"
                                    hide()
                                    r.isDisable = false
                                }
                            }
                        }
                    }

                }
                fold("Some other editor") {
                    stackpane {
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
                    }
                }
            }
        }

        style {
            backgroundColor += Color.AZURE
            //backgroundColor+=LinearGradient(0.0, 0.0, 50.0, 50.0, false, CycleMethod.REPEAT, Stop(0.0, c(137, 163, 147)), Stop(1.0, c(255, 163, 147)))
        }


    }
}


class new1 : View("ddd") {
    override val root = borderpane {
        left = vbox(10) {

            button("ппппппппп") {
                action {

                }
            }

        }

        style {
            backgroundColor += Color.AZURE
        }
    }
}

class new2 : View("ddd") {
    override val root = borderpane {
        left = vbox(10) {

            button("ппппппппп") {
                action {

                }
            }

        }

        style {
            backgroundColor += Color.AZURE
        }
    }
}

class new3 : View("ddd") {
    override val root = borderpane {
        left = vbox(10) {

            button("пппппппппппп") {
                action {

                }
            }

        }

        style {
            backgroundColor += Color.AZURE

        }
    }
}