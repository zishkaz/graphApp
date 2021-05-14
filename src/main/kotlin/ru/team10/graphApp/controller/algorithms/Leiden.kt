package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import nl.cwts.networkanalysis.run.RunNetworkClustering
import ru.team10.graphApp.view.GraphView
import java.io.File
import kotlin.math.pow


class Leiden(
    private val inputFilename: String,
    private val outputFilename: String
) {
    fun startLeiden(resolution: Double) {
        val args = arrayOf("-r", "$resolution", "-o", outputFilename, inputFilename)
        RunNetworkClustering.main(args)
    }
}

internal fun colorAccordingToCommunity(graph: GraphView) {
    val communityCount = 1 + graph.vertices().maxOf { it.vertex.communityID }
    val step = 16777216 / communityCount
    var colorNow = 0
    val communityToColor = hashMapOf<Int, Color>()
    repeat(communityCount) {

        communityToColor[it] = Color.rgb(colorNow / 65536, (colorNow % 65536) / 256, colorNow % 256 )
        colorNow += step
    }
    for (node in graph.vertices()) {

        node.color = communityToColor[node.vertex.communityID]!!
    }
}