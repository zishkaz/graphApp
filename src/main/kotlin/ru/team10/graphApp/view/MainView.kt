package ru.team10.graphApp.view

import javafx.collections.FXCollections
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ru.team10.graphApp.controller.Layout
import tornadofx.*
import javafx.scene.input.KeyCode
import ru.team10.graphApp.controller.Centrality

var constant1: String = "123"

class MainView : View("Graph Application") {

    private val graph = GraphView(props.sample)
    private val layout1 = Layout()
    private val centr = Centrality()

    //var constant: TextField by singleAssign()
    private val layout = Layout().applyForceAtlas2(graph)

    override val root = borderpane {
        this.getStylesheets().add("1.css")
        fun apply() {

            currentStage?.apply {

                layout.start()
                centr.harmonic(props.sample, graph.vertices())
            }
        }

        fun applyStop() {

            currentStage?.apply {

                layout.stop()
                centr.harmonic(props.sample, graph.vertices())
            }
        }

        center {

            add(graph)
        }
        left= vbox(10) {

            textflow {
                text("BIG") {
                    fill = Color.PURPLE
                    font = Font(20.0)
                }
                text("Dick") {
                    fill = Color.ORANGE
                    font = Font(28.0)
                }
            }

            button("Reset default settings") {
                this.getStyleClass().add("button1")
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
                    r.setText("a = $constant1")
                    r.style = "-fx-background-color: white; -fx-border-color: grey; -fx-border-radius: 5;"
                    r.setOnMouseReleased {
                        r.isDisable = true
                        hbox {
                            //label("Constant")
                            //constant = textfield()
                            val filterTextField: TextField = textfield()
                            filterTextField.fitToSize(r)
                            filterTextField.setOnKeyReleased { event ->
                                if (event.getCode() == KeyCode.ENTER) {
                                    println("Logging in as ${filterTextField.text} ")
                                    constant1 = filterTextField.text
                                    r.setText("a = $constant1")
                                    hide()
                                    r.isDisable = false
                                }
                            }
                        }
                    }

                }
                fold("Some other editor") {
                    stackpane {
                        label("Nothing here")

                    }
                }
            }
        }

        style {
            backgroundColor += Color.AZURE
            //backgroundColor+=LinearGradient(0.0, 0.0, 50.0, 50.0, false, CycleMethod.REPEAT, Stop(0.0, c(137, 163, 147)), Stop(1.0, c(255, 163, 147)))
        }


    }

    init {
        currentStage?.apply {
            layout1.randomLayout(width, height, graph)
            centr.harmonic(props.sample, graph.vertices())
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