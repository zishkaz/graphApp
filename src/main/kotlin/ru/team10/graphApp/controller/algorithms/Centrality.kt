package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import mu.KotlinLogging
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import java.util.*

object Centrality : Controller() {

    private val logger = KotlinLogging.logger {}
    var wasDone = false

    private data class ExtraVertexData(val vertex: Vertex) : Comparable<ExtraVertexData> {

        var shortestDist: Double = Double.MAX_VALUE
        var mass = 1

        override fun compareTo(other: ExtraVertexData): Int {
            if (shortestDist == other.shortestDist) return vertex.id.compareTo(other.vertex.id)
            return shortestDist.compareTo(other.shortestDist)
        }

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

        logger.info("The algorithm for finding key vertices has been started.")
        val edges = graph.edges().map { it.edge }.toList()
        val vert = graph.vertices().map { it.vertex }.toList()
        val verticesExtraData = hashMapOf<String, ExtraVertexData>()
        for (i in vert) {
            verticesExtraData[i.id] = ExtraVertexData(i)
        }
        setNeighbours(edges, verticesExtraData)
        for (i in vert) {
            i.centralityRang = setUpVertices(i, vert, verticesExtraData)
        }
        if (!wasDone) setRadius(graph)

        wasDone = true
        logger.info("The algorithm for finding key vertices has been completed.")
    }

    private fun setUpVertices(
        start: Vertex,
        vert: List<Vertex>,
        verticesExtraData: HashMap<String, ExtraVertexData>
    ): Double {

        val q = TreeSet<ExtraVertexData>()
        for (i in vert.indices) {
            verticesExtraData[vert[i].id]!!.previous = if (vert[i] == start) start else null
            verticesExtraData[vert[i].id]!!.shortestDist = if (vert[i] == start) 0.0 else Double.MAX_VALUE
            q.add(verticesExtraData[vert[i].id]!!)
        }
        return runDijkstra(q, verticesExtraData)
    }

    private fun runDijkstra(q: TreeSet<ExtraVertexData>, verticesExtraData: HashMap<String, ExtraVertexData>): Double {

        var sumOfShortestPaths = 0.0
        while (!q.isEmpty()) {
            val u = q.pollFirst()
            if (u.shortestDist == Double.MAX_VALUE) break
            for (a in u.neighbours) {
                val v = verticesExtraData[a.key.id]!!
                val alternateDist = u.shortestDist + a.value
                if (alternateDist < v.shortestDist) {
                    q.remove(v)
                    v.shortestDist = alternateDist
                    sumOfShortestPaths += 1.0 / alternateDist
                    v.previous = u.vertex
                    q.add(v)
                }
            }
        }
        return sumOfShortestPaths
    }

    fun setRadius(graph: GraphView) {

        val min = graph.vertices().map { it.vertex.centralityRang }.minOrNull()
        val max = graph.vertices().map { it.vertex.centralityRang }.maxOrNull()

        val step = (max!! - min!!) / graph.vertices().size
        for (v in graph.vertices()) {
            val x = (v.vertex.centralityRang - min) * 10
            v.radiusSummand.value += x * step
        }
    }


    fun setColor(step1: Double, step2: Double, min: Double, vertices: Collection<VertexView>) {

        for (v in vertices) {
            val x = (v.vertex.centralityRang - min) / step1
            v.color = Color.rgb(100, 255 - (step2 * x).toInt(), 255)
        }
    }
}