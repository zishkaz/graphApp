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
    }

    init {

        currentStage?.apply {

            layout.place(width, height, graph.vertices())
        }
    }
}