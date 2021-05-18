package ru.team10.graphApp.controller.algorithms

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Alert
import javafx.scene.paint.Color
import mu.KotlinLogging
import nl.cwts.networkanalysis.run.RunNetworkClustering
import ru.team10.graphApp.utils.DataConverter
import ru.team10.graphApp.view.GraphView
import tornadofx.alert
import java.io.File

internal var leidenResolution = SimpleDoubleProperty(0.2)
const val leidenPathname = "leidenOutput.csv"
const val utilityPathname = "leidenUtil.txt"


class Leiden(
    private val graph: GraphView
) {
    private val logger = KotlinLogging.logger {}
    fun startLeiden(resolution: Double) {
        logger.info("Started community detection algorithm.")
        val dataConverter = DataConverter(graph)
        dataConverter.prepareDataForClustering()
        try {
            RunNetworkClustering.main(
                arrayOf("-r", "$resolution", "-o", leidenPathname, utilityPathname)
            )
        } catch (ex: Exception) {
            logger.error(ex) { "An error occurred when the clustering algorithm was working." }
            alert(
                Alert.AlertType.ERROR,
                "ERROR!\nAn error occurred when the clustering algorithm was working.\nFor more information, check the logs."
            )
        }
        logger.info("Community detection algorithm has been completed")
    }

    fun setCommunity() {
        logger.info("Setting of community values for vertexes has started")
        try {
            val communityIDs = mutableMapOf<Int, Int>()
            File(leidenPathname).readLines().map { line ->
                val (vertex, community) = line.split("\t").toMutableList()
                communityIDs[vertex.toInt()] = community.toInt()
            }
            val vertices = graph.vertices()
            val verticesID = graph.getVerticesId().let { it.zip(it.indices) }.toMap()
            vertices.forEach { el ->
                el.vertex.communityID = communityIDs[verticesID[el.vertex]] ?: -1
            }
            logger.info("Setting of community values for vertexes has completed")
        } catch (ex: Exception) {
            logger.error(ex) { "Something went wrong when setting community values to vertexes." }
            alert(
                Alert.AlertType.ERROR,
                "ERROR!\nSomething went wrong when setting community values to vertexes.\nFor more information, check the logs."
            )
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
    val logger = KotlinLogging.logger {}
    logger.info("The painting of the vertexes depending on the community has started.")
    try {
        val communityCount = 1 + graph.vertices().maxOf { it.vertex.communityID }
        val step = 16777216 / (communityCount + 179)
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
        logger.info("The painting of the vertexes depending on the community has completed.")
    } catch (ex: Exception) {
        logger.info(ex) { "An error occurred when painting vertexes depending on the community." }
        alert(
            Alert.AlertType.ERROR,
            "ERROR!\nAn error occurred when painting vertexes depending on the community.\nFor more information, check the logs."
        )
    }

}

