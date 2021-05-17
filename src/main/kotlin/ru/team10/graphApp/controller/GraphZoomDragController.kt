package ru.team10.graphApp.controller

import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller

class GraphZoomDragController : Controller(){

    fun scroll(e: ScrollEvent, graphView: GraphView) {

        val delta = e.deltaY / 1000
        if (graphView.scaleX + delta >= 0) graphView.scaleX += delta
        if (graphView.scaleY + delta >= 0) graphView.scaleY += delta
    }

    private var previousPoint = Point2D(0.0, 0.0)

    fun pressed(e: MouseEvent) {

        if (!e.isPrimaryButtonDown) return
        previousPoint = Point2D(e.x, e.y)
    }

    fun dragged(e: MouseEvent, graphView: GraphView) {

        if (!e.isPrimaryButtonDown) return
        if (e.target is VertexView) return
        val currentPoint = Point2D(e.x, e.y)
        graphView.translateX += currentPoint.x - previousPoint.x
        graphView.translateY += currentPoint.y - previousPoint.y
        previousPoint = currentPoint
    }
}