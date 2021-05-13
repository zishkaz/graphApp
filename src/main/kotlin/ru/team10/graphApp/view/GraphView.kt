package ru.team10.graphApp.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import ru.team10.graphApp.model.Graph
import tornadofx.add
import java.util.*

class GraphView(graph: Graph, private val verticesView: List<VertexView> = emptyList()): Pane() {

    private val vertices by lazy {

        if (verticesView.isEmpty()) graph.vertices().associateWith { VertexView(it, 0.0, 0.0, Color.AQUA) }
        else verticesView.associateBy { it.vertex }
    }

    private val edges by lazy {

        graph.edges().associateWith {

            EdgeView(it, vertices[it.first] ?: throw IllegalStateException("Shit"), vertices[it.second] ?: throw IllegalStateException("Shit"))
        }
    }

    fun vertices(): Collection<VertexView> = vertices.values
    fun edges(): Collection<EdgeView> = edges.values

    init {
        edges().forEach {

            add(it)
        }
        vertices().forEach {

            add(it)
        }
    }
}