package ru.team10.graphApp.view

import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import ru.team10.graphApp.model.Vertex
import tornadofx.doubleProperty

var vertexRadius = 4.0
var vertexColor: Color = Color.AQUA

class VertexView(
    val vertex: Vertex,
    x: Double = 0.0,
    y: Double = 0.0,
    color: Color = vertexColor,
) : Circle(x, y, vertexRadius, color) {


    init {
        radiusProperty().bind(doubleProperty(radius))
    }

    var position: Pair<Double, Double>
        get() = centerX to centerY
        set(value) {
            centerX = value.first
            centerY = value.second
        }

    var color: Color
        get() = fill as Color
        set(value) {
            fill = value
        }
}