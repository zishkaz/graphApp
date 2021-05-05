package ru.team10.graphApp.view

import javafx.scene.shape.Line
import ru.team10.graphApp.model.Edge

class EdgeView(
    val edge: Edge,
    val first: VertexView,
    val second: VertexView,)
    : Line() {

    init {
        startXProperty().bind(first.centerXProperty())
        startYProperty().bind(first.centerYProperty())
        endXProperty().bind(second.centerXProperty())
        endYProperty().bind(second.centerYProperty())
    }
}