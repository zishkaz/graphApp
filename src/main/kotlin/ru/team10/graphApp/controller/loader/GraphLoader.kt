package ru.team10.graphApp.controller.loader

import ru.team10.graphApp.view.GraphView

internal interface GraphLoader {

    fun loadGraph(data: String): GraphView?
    fun saveGraph(graph: GraphView, data: String)
}