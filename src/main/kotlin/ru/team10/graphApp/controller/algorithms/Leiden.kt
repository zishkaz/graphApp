package ru.team10.graphApp.controller.algorithms

import javafx.scene.paint.Color
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import nl.cwts.networkanalysis.run.RunNetworkClustering
import ru.team10.graphApp.view.GraphView
import java.io.File
import kotlin.math.pow

internal var leidenResolution: Double = 0.2

class Leiden(
    private val inputFilename: String,
    private val outputFilename: String
) {
    fun startLeiden(resolution: Double) {
        val args = arrayOf("-r", "$resolution", "-o", outputFilename, inputFilename)
        RunNetworkClustering.main(args)
    }
}