package ru.team10.graphApp.view

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import ru.team10.graphApp.model.Graph
import tornadofx.add
import java.lang.IllegalStateException

class GraphView(graph: Graph): Pane() {

    private val vertices by lazy {

        graph.vertices().associateWith { VertexView(it, 0.0, 0.0, tornadofx.doubleProperty(10.0), Color.BLACK) }
    }

    private val edges by lazy {

        graph.edges().associateWith {

            EdgeView(it, vertices[it.first] ?: throw IllegalStateException("Shit"), vertices[it.second] ?: throw IllegalStateException("Shit"))
        }
    }

    fun vertices(): Collection<VertexView> = vertices.values
    fun edges(): Collection<EdgeView> = edges.values

    init {
        vertices().forEach {

            add(it)
        }
        edges().forEach {

            add(it)
        }
    }
}