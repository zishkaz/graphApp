package ru.team10.graphApp.view

import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import kotlin.random.Random

object props {

    val sample = Graph().apply {

        val nodes = mutableListOf<Vertex>()
        repeat (1000) {

            nodes.add(Vertex("kek $it"))
            addVertex(nodes[it])
        }
        val repeats = Random.nextInt(5000, 9000)
        repeat(repeats) {

            val i = Random.nextInt(0, 1000)
            val j = Random.nextInt(0, 1000)
            if (i != j) addEdge(Edge(nodes[i], nodes[j]))
        }
//        for (i in 0..998) {
//
//            for (j in i+1..999) addEdge(Edge(nodes[i], nodes[j]))
//        }
//        val a = Vertex("A")
//        val b = Vertex("B")
//        val c = Vertex("C")
//        val d = Vertex("D")
//        val e = Vertex("E")
//        addVertex(a)
//        addVertex(b)
//        addVertex(c)
//        addVertex(d)
//        addVertex(e)
//        addEdge(Edge(a, b))
//        addEdge(Edge(a, c))
//        addEdge(Edge(a, d))
//        addEdge(Edge(a, e))
//        addEdge(Edge(b, c))
//        addEdge(Edge(b, d))
//        addEdge(Edge(b, e))
//        addEdge(Edge(c, d))
//        addEdge(Edge(c, e))
    }
}