package ru.team10.graphApp.controller.loader

import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.view.GraphView
import tornadofx.Controller

interface GraphLoader {

    fun loadGraph(data: String): GraphView?
    fun saveGraph(graph: GraphView, data: String)
}