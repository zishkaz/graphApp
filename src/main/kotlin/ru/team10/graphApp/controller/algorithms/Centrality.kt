package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import java.util.*

class Centrality : Controller() {

    private data class ExtraVertexData(val vert: Vertex) {
        var previous: Vertex? = null
        var neighbours = HashMap<Vertex, Double>()

    }

    private fun setNeighbours(edges: List<Edge>, verticesExtraData: HashMap<Vertex, ExtraVertexData>) {
        for (i in edges.indices) {
            verticesExtraData[edges[i].first]!!.neighbours[edges[i].second] = edges[i].weight
            verticesExtraData[edges[i].second]!!.neighbours[edges[i].first] = edges[i].weight
        }
    }

    fun applyHarmonicCentrality(graph: Graph, vertices: Collection<VertexView>) {

        val edges = graph.edges().toList()
        val vert = graph.vertices().toList()
        val verticesExtraData = hashMapOf<Vertex, ExtraVertexData>()
        for (i in vert) {
            verticesExtraData[i] = ExtraVertexData(i)
        }

        setNeighbours(edges, verticesExtraData)

        var max = 0.0
        var min: Double = Double.MAX_VALUE

        for (i in vert) {
            i.centralityRang = setUpVertices(i, vert, verticesExtraData)
            if (i.centralityRang.compareTo(min) < 0) min = i.centralityRang
            if (i.centralityRang.compareTo(max) > 0) max = i.centralityRang
        }

        val step1 = (max - min) / vert.size
        val step2 = 255.0 / vert.size
        setColor(step1, step2, min, vertices)
        println("nice")
    }

    private fun setUpVertices(start: Vertex, vert: List<Vertex>, verticesExtraData: HashMap<Vertex, ExtraVertexData>): Double {

        val q = TreeSet<Vertex>()

        for (i in vert.indices) {
            verticesExtraData[vert[i]]!!.previous = if (vert[i] == start) start else null
            vert[i].shortestDist = if (vert[i] == start) 0.0 else Double.MAX_VALUE
            q.add(vert[i])
        }

        return runDijkstra(q, verticesExtraData)
    }


    private fun runDijkstra(q: TreeSet<Vertex>, verticesExtraData: HashMap<Vertex, ExtraVertexData>): Double {

        var sumOfShortestPaths = 0.0
        while (!q.isEmpty()) {
            val u = q.pollFirst()
            if (u!!.shortestDist == Double.MAX_VALUE) break
            for (a in verticesExtraData[u]!!.neighbours) {
                val v = a.key
                val alternateDist = u.shortestDist + a.value
                if (alternateDist < v.shortestDist) {
                    q.remove(v)
                    v.shortestDist = alternateDist
                    sumOfShortestPaths += 1.0 / alternateDist
                    verticesExtraData[v]!!.previous = u
                    q.add(v)
                }
            }
        }

        return sumOfShortestPaths
    }

    private fun setColor(step1: Double, step2: Double, min: Double, vertices: Collection<VertexView>) {

        for (v in vertices) {// SET COLORS
            val x = (v.vertex.centralityRang - min) / step1
            v.color = Color.rgb(100, 255 - (step2 * x).toInt(), 255)
        }
    }

}