package ru.team10.graphApp.utils

import com.sksamuel.hoplite.ConfigLoader
import javafx.scene.control.Alert
import javafx.scene.paint.Color
import org.yaml.snakeyaml.Yaml
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.view.vertexColor
import ru.team10.graphApp.view.vertexRadius
import tornadofx.alert
import java.io.File
import java.io.StringWriter

data class VertexColor(val r: Int, val g: Int, val b: Int)

data class LayoutConstants(
    val scaling: Double,
    val gravity: Double,
    val jitterTolerance: Double,
    val isBarnesHutActive: Boolean
)

data class CommunityDetectionConstants(val kek: Double)

data class Config(
    val vertexColor: VertexColor,
    val vertexRadius: Double,
    val layoutConstants: LayoutConstants,
    val communityDetectionConstants: CommunityDetectionConstants
)

fun loadConfigFile(file: File) {
    val config: Config
    try {
        config = ConfigLoader().loadConfigOrThrow(file)
    } catch (e: Exception) {
        alert(Alert.AlertType.ERROR, "ERROR\nInvalid config file!")
        e.printStackTrace()
        return
    }
    vertexRadius = config.vertexRadius
    Layout.jitterTolerance = config.layoutConstants.jitterTolerance
    Layout.gravity = config.layoutConstants.gravity
    Layout.isBarnesHutActive = config.layoutConstants.isBarnesHutActive
    Layout.scaling = config.layoutConstants.scaling
    vertexColor = Color.rgb(config.vertexColor.r, config.vertexColor.g, config.vertexColor.b)
}

fun saveConfig(file: File) {

    val yaml = Yaml()
    val generalMap = hashMapOf<String, Any>()
    val vertexColorMap = hashMapOf<String, Int>()
    vertexColorMap["r"] = (vertexColor.red * 255).toInt()
    vertexColorMap["g"] = (vertexColor.green * 255).toInt()
    vertexColorMap["b"] = (vertexColor.blue * 255).toInt()
    generalMap["vertexColor"] = vertexColorMap
    generalMap["vertexRadius"] = vertexRadius
    val layoutConstantsMap = hashMapOf<String, Any>()
    layoutConstantsMap["scaling"] = Layout.scaling
    layoutConstantsMap["gravity"] = Layout.gravity
    layoutConstantsMap["jitterTolerance"] = Layout.jitterTolerance
    layoutConstantsMap["isBarnesHutActive"] = Layout.isBarnesHutActive
    generalMap["layoutConstants"] = layoutConstantsMap
    val communityDetectionConstantsMap = hashMapOf<String, Any>()
    communityDetectionConstantsMap["kek"] = 1.0
    generalMap["communityDetectionConstants"] = communityDetectionConstantsMap
    val writer = StringWriter()
    yaml.dump(generalMap, writer)
    file.writeText(writer.toString())
}