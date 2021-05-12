package ru.team10.graphApp.controller.loader

import ru.team10.graphApp.model.Graph
import tornadofx.Controller

interface GraphLoader {

    fun loadGraph(data: String): Graph
    fun saveGraph(graph: Graph)
}