package ru.team10.graphApp.view

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import ru.team10.graphApp.model.Vertex
import tornadofx.*

var vertexRadius: SimpleDoubleProperty = SimpleDoubleProperty(4.0)
var vertexColor: Color = Color.AQUA

internal class VertexView(
    val vertex: Vertex,
    x: Double = 0.0,
    y: Double = 0.0,
    color: Color = vertexColor,
) : Circle(x, y, vertexRadius.value, color) {

    var radiusSummand: SimpleDoubleProperty = SimpleDoubleProperty(0.0)

    init {
        radiusProperty().bind(vertexRadius + radiusSummand)
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

    var infoText = text {}
    val window = StackPane().apply {
        isVisible = false
        add(Rectangle(190.0, 100.0, Color.CORAL))
        add(borderpane {
            center {
                add(infoText)
            }
            right {
                button("X") {
                    action {
                        this@apply.isVisible = !this@apply.isVisible
                    }
                }
            }
        })
    }
}
