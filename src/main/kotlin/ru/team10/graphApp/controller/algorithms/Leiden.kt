package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import nl.cwts.networkanalysis.run.RunNetworkClustering
import ru.team10.graphApp.utils.DataConverter
import ru.team10.graphApp.view.GraphView
import java.io.File

internal var leidenResolution: Double = 0.2

const val leidenPathname = "leidenOutput.csv"
const val utilityPathname = "leidenUtil.txt"

class Leiden(
    private val graph: GraphView
) {
    fun startLeiden(resolution: Double) {

        val dataConverter = DataConverter(graph)
        dataConverter.prepareDataForClustering()
        RunNetworkClustering.main(
            arrayOf("-r", "$resolution", "-o", leidenPathname, utilityPathname)
        )
    }

    fun setCommunity() {

        val communityIDs = mutableMapOf<Int, Int>()
        File(leidenPathname).readLines().map { line ->
            val (vertex , community) = line.split("\t").toMutableList()
            communityIDs[vertex.toInt()] = community.toInt()
        }
        val vertices = graph.vertices()
        val verticesID = graph.getVerticesId().let { it.zip(it.indices) }.toMap()
        vertices.forEach{el->
            el.vertex.communityID = communityIDs[verticesID[el.vertex]]?:-1
        }
        deleteUtilityFiles()
    }

    companion object {
        fun deleteUtilityFiles() {

            File(utilityPathname).delete()
            File(leidenPathname).delete()
        }
    }
}

internal fun colorAccordingToCommunity(graph: GraphView) {

    val communityCount = 1 + graph.vertices().maxOf { it.vertex.communityID }
    val step = 16777216 / communityCount
    var colorNow = step
    val communityToColor = hashMapOf<Int, Color>()
    repeat(communityCount) {
        val b = colorNow % 256
        val g = ((colorNow - b) / 256) % 256
        communityToColor[it] = Color.rgb(10, g, b)
        colorNow += step
    }
    for (node in graph.vertices()) {
        node.color = communityToColor[node.vertex.communityID]!!
    }
}

