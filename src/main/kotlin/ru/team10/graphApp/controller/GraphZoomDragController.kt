package ru.team10.graphApp.controller

import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import ru.team10.graphApp.view.GraphView
import tornadofx.Controller

class GraphZoomDragController(graphView: GraphView) : Controller(){
    fun scroll(e: ScrollEvent, graphView: GraphView) {
        //if (!e.isControlDown) return
        val delta = e.deltaY / 1000
        if (graphView.scaleX + delta >= 0) graphView.scaleX += delta
        if (graphView.scaleY + delta >= 0) graphView.scaleY += delta
        println("nice")
    }

    private var previousPoint = Point2D(0.0, 0.0)

    fun pressed(e: MouseEvent) {
        if (!e.isPrimaryButtonDown) return

        previousPoint = Point2D(e.x, e.y)
        println(previousPoint)
    }

    fun pressed(e: MouseEvent, graphView: GraphView) {
        if (!e.isPrimaryButtonDown) return
        val currentPoint = Point2D(e.x, e.y)
        graphView.translateX += currentPoint.x - previousPoint.x
        graphView.translateY += currentPoint.y - previousPoint.y
        println(currentPoint)
        previousPoint = currentPoint
    }
}