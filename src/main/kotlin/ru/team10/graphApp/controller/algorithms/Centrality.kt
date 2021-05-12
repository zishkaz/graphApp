package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import java.util.*

class Centrality: Controller() {
    private data class dopVertex(val vert: Vertex) {
        var previous: Vertex? = null
        val neigh: MutableList<Vertex> = mutableListOf()
    }

    private fun setNeigh(edges: List<Edge>) {
        for (i in edges.indices) {
            if (!dopVertex(edges[i].first).neigh.contains(edges[i].first)) dopVertex(edges[i].first).neigh.add(edges[i].second)
            else dopVertex(edges[i].first).neigh.add(edges[i].second)
            if (!dopVertex(edges[i].second).neigh.contains(edges[i].second)) dopVertex(edges[i].second).neigh.add(edges[i].first)
            else dopVertex(edges[i].second).neigh.add(edges[i].first)
        }
    }

    fun harmonic(graph: Graph, vertices: Collection<VertexView>) {
        val edges = graph.edges().toList()
        val vert = graph.vertices().toList()

        setNeigh(edges)

        val res: MutableMap<String, Double> = mutableMapOf()
        var max = 0.0
        var min: Double = Double.MAX_VALUE
        for (i in vert.indices) {
            res[vert[i].id] = g1(vert[i], vert)
            if (res[vert[i].id]!!.compareTo(min) < 0) min = res[vert[i].id]!!
            if (res[vert[i].id]!!.compareTo(max) > 0) max = res[vert[i].id]!!
        }
        val step1 = (max - min) / vert.size
        val step2 = 255.0 / vert.size
        setColor(step1, step2, min, vertices, res)

    }

    private fun g1(start: Vertex, vert: List<Vertex>): Double {
        val q = TreeSet<Vertex>()
        for (i in vert.indices) {
            dopVertex(vert[i]).previous = if (vert[i] == start) start else null
            vert[i].dist = if (vert[i] == start) 0 else Int.MAX_VALUE
            q.add(vert[i])
        }
        return g2(q)
    }


    private fun g2(q: TreeSet<Vertex>): Double {
        var sum = 0.0
        while (!q.isEmpty()) {
            val u = q.pollFirst()
            if (u.dist == Int.MAX_VALUE) break
            for (a in dopVertex(u).neigh) {
                val alternateDist = u.dist + 1
                if (alternateDist < a.dist) {
                    q.remove(a)
                    a.dist = alternateDist
                    sum += 1.0 / alternateDist
                    dopVertex(a).previous = u
                    q.add(a)
                }
            }
        }
        return sum
    }

    private fun setColor(step1: Double, step2: Double, min: Double, vertices: Collection<VertexView>, res: MutableMap<String, Double>) {

        for (v in vertices) {// SET COLORS
            val x = (res[v.vertex.id]!! - min) / step1
            v.color = Color.rgb(100, 255 - (step2 * x).toInt(), 255)

        }
    }

}