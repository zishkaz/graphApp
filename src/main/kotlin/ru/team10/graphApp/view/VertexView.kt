package ru.team10.graphApp.view

import javafx.beans.property.DoubleProperty
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import ru.team10.graphApp.model.Vertex

class VertexView(
    val vertex: Vertex,
    x: Double,
    y: Double,
    r: DoubleProperty,
    color: Color,
) : Circle(x, y, r.get(), color) {

    init {
        radiusProperty().bind(r)
    }

    var position: Pair<Double, Double>
        get() = centerX to centerY
        set(value) {
            centerX = value.first
            centerY = value.second
            layoutCenterY = value.second
            layoutCenterX = value.first
        }

    var color: Color
        get() = fill as Color
        set(value) {
            fill = value
        }
    var layoutCenterX = 0.0
    var layoutCenterY = 0.0
}