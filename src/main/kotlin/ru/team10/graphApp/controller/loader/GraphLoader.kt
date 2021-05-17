package ru.team10.graphApp.controller.loader

import ru.team10.graphApp.view.GraphView

interface GraphLoader {

    fun loadGraph(data: String): GraphView?
    fun saveGraph(graph: GraphView, data: String)
}