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

class VertexView(
    val vertex: Vertex,
    x: Double = 0.0,
    y: Double = 0.0,
    color: Color = vertexColor,
) : Circle(x, y, vertexRadius.value, color) {

    init {
        radiusProperty().bind(vertexRadius)
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
    val st = StackPane().apply {
        add(Rectangle(190.0, 100.0, Color.CORAL))
        add(borderpane {
            center {
                add(text("""
                ID: ${vertex.id}
                Community ID: ${vertex.communityID}
                Centrality rang: ${vertex.centralityRang}
                """.trimIndent()) {
                    isVisible = true
                })
            }
            right {
                button("X") {
                    action {
                        labels.value = !labels.value
                    }
                }
            }
        })
        visibleProperty().bind(labels)
        this.layoutXProperty().bind(this@VertexView.centerXProperty().subtract(layoutBounds.width / 2))
        this.layoutYProperty().bind(this@VertexView.centerYProperty().add(radiusProperty()).add(layoutBounds.height))
    }
}
