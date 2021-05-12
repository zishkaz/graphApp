package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import java.util.*

class Centrality : Controller() {

    private data class ExtraVertexData(val vert: String) {
        var previous: Vertex? = null
        var neighbours: MutableList<Vertex> = mutableListOf()
    }

    private fun setNeighbours(edges: List<Edge>, dopVert: HashMap<String, ExtraVertexData>) {
        for (i in edges.indices) {
            dopVert[edges[i].first.id]!!.neighbours.add(edges[i].second)
            dopVert[edges[i].second.id]!!.neighbours.add(edges[i].first)
        }
    }

    fun applyHarmonicCentrality(graph: Graph, vertices: Collection<VertexView>) {

        val edges = graph.edges().toList()
        val vert = graph.vertices().toList()
        val verticesExtraData = hashMapOf<String, ExtraVertexData>()
        for (i in vert) {
            verticesExtraData[i.id] = ExtraVertexData(i.id)
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

    private fun setUpVertices(start: Vertex, vert: List<Vertex>, verticesExtraData: HashMap<String, ExtraVertexData>): Double {

        val q = TreeSet<Vertex>()

        for (i in vert.indices) {
            verticesExtraData[vert[i].id]!!.previous = if (vert[i] == start) start else null
            vert[i].shortestDist = if (vert[i] == start) 0 else Int.MAX_VALUE
            q.add(vert[i])
        }

        return runDijkstra(q, verticesExtraData)
    }


    private fun runDijkstra(q: TreeSet<Vertex>, verticesExtraData: HashMap<String, ExtraVertexData>): Double {

        var sumOfShortesPaths = 0.0
        while (!q.isEmpty()) {
            val u = q.pollFirst()
            if (u.shortestDist == Int.MAX_VALUE) break
            for (a in verticesExtraData[u.id]!!.neighbours) {
                val alternateDist = u.shortestDist + 1
                if (alternateDist < a.shortestDist) {
                    q.remove(a)
                    a.shortestDist = alternateDist
                    sumOfShortesPaths += 1.0 / alternateDist
                    verticesExtraData[a.id]!!.previous = u
                    q.add(a)
                }
            }
        }

        return sumOfShortesPaths
    }

    private fun setColor(step1: Double, step2: Double, min: Double, vertices: Collection<VertexView>) {

        for (v in vertices) {// SET COLORS
            val x = (v.vertex.centralityRang - min) / step1
            v.color = Color.rgb(100, 255 - (step2 * x).toInt(), 255)
        }
    }

}