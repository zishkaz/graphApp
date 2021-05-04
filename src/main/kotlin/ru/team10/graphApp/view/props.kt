package ru.team10.graphApp.view

import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import kotlin.math.E

object props {

    val sample = Graph().apply {

        val a = Vertex("A")
        val b = Vertex("B")
        val c = Vertex("C")
        val d = Vertex("A")
        val e = Vertex("E")
        val f = Vertex("F")
        addVertex(a)
        addVertex(b)
        addVertex(c)
        addVertex(d)
        addVertex(e)
        addVertex(f)
        addEdge(Edge(a, b))
        addEdge(Edge(a, c))
        addEdge(Edge(c, d))
        addEdge(Edge(e, f))
    }
}