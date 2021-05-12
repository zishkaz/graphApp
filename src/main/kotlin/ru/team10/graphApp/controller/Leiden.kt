package ru.team10.graphApp.controller

import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import nl.cwts.networkanalysis.run.RunNetworkClustering
import java.io.File


class Leiden(
    private val inputFilename: String,
    private val outputFilename: String
) {
    fun startLeiden(resolution: Double) {
        val args = arrayOf("-r", "$resolution", "-o", outputFilename, inputFilename)
        RunNetworkClustering.main(args)
    }
}