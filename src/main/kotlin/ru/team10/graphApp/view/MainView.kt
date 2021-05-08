package ru.team10.graphApp.view


import ru.team10.graphApp.controller.Layout
import tornadofx.*

class MainView : View("Graph Application") {

    private val graph = GraphView(props.sample)
    private val layout = Layout()

    override val root = borderpane {

        center {

            add(graph)
        }

        left = vbox(10) {

            button {
                action {
                    runAsync {
                        apply()
                    }
                }
            }
        }
    }

    init {

        currentStage?.apply {

            layout.randomLayout(width, height, graph)
        }
    }

    fun apply() {

        currentStage?.apply {

            layout.applyForceAtlas2(graph)
        }
    }
}