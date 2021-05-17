package ru.team10.graphApp.view

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import ru.team10.graphApp.model.Vertex

var vertexRadius: SimpleDoubleProperty = SimpleDoubleProperty(4.0)
var vertexColor: Color = Color.AQUA

class VertexView(
    val vertex: Vertex,
    x: Double = 0.0,
    y: Double = 0.0,
    color: Color = vertexColor,
) : Circle(x, y, vertexRadius.value, color) {

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