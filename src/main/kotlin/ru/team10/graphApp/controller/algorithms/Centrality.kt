package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import java.util.*

object Centrality : Controller() {

    private data class ExtraVertexData(val vertID: String) {
        var previous: Vertex? = null
        var neighbours = HashMap<Vertex, Double>()

    }

    private fun setNeighbours(edges: List<Edge>, verticesExtraData: HashMap<String, ExtraVertexData>) {

        for (i in edges.indices) {
            verticesExtraData[edges[i].first.id]!!.neighbours[edges[i].second] = edges[i].weight
            verticesExtraData[edges[i].second.id]!!.neighbours[edges[i].first] = edges[i].weight
        }
    }

    fun applyHarmonicCentrality(graph: GraphView) {

        val edges = graph.edges().map { it.edge }.toList()
        val vert = graph.vertices().map { it.vertex }.toList()
        val verticesExtraData = hashMapOf<String, ExtraVertexData>()
        for (i in vert) {
            verticesExtraData[i.id] = ExtraVertexData(i.id)
        }
        setNeighbours(edges, verticesExtraData)
        for (i in vert) {
            i.centralityRang = setUpVertices(i, vert, verticesExtraData)
        }
    }

    private fun setUpVertices(
        start: Vertex,
        vert: List<Vertex>,
        verticesExtraData: HashMap<String, ExtraVertexData>
    ): Double {

        val q = TreeSet<Vertex>()
        for (i in vert.indices) {
            verticesExtraData[vert[i].id]!!.previous = if (vert[i] == start) start else null
            vert[i].shortestDist = if (vert[i] == start) 0.0 else Double.MAX_VALUE
            q.add(vert[i])
        }
        return runDijkstra(q, verticesExtraData)
    }


    private fun runDijkstra(q: TreeSet<Vertex>, verticesExtraData: HashMap<String, ExtraVertexData>): Double {

        var sumOfShortestPaths = 0.0
        while (!q.isEmpty()) {
            val u = q.pollFirst()
            if (u!!.shortestDist == Double.MAX_VALUE) break
            for (a in verticesExtraData[u.id]!!.neighbours) {
                val v = a.key
                val alternateDist = u.shortestDist + a.value
                if (alternateDist < v.shortestDist) {
                    q.remove(v)
                    v.shortestDist = alternateDist
                    sumOfShortestPaths += 1.0 / alternateDist
                    verticesExtraData[v.id]!!.previous = u
                    q.add(v)
                }
            }
        }
        return sumOfShortestPaths
    }

    fun setColor(step1: Double, step2: Double, min: Double, vertices: Collection<VertexView>) {

        for (v in vertices) {
            val x = (v.vertex.centralityRang - min) / step1
            v.color = Color.rgb(100, 255 - (step2 * x).toInt(), 255)
        }
    }
}