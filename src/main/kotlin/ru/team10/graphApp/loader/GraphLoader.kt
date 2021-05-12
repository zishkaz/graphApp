package ru.team10.graphApp.loader

import ru.team10.graphApp.model.Graph

interface GraphLoader {

    fun loadGraph(data: String): Graph
    fun saveGraph(graph: Graph)
}