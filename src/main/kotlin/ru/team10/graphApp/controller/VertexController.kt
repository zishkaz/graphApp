package ru.team10.graphApp.controller

import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import ru.team10.graphApp.view.VertexView
import ru.team10.graphApp.view.labels
import ru.team10.graphApp.view.vertexRadius
import tornadofx.Controller

class VertexController : Controller() {
    fun entered(event: MouseEvent) {
        val v = check(event)
        if (!event.isPrimaryButtonDown) {
            v.scene.cursor = Cursor.HAND
            v.radiusSummand.value += 0.5 * v.radius
        }
    }

    fun released(event: MouseEvent) {
        val v = check(event)
        v.scene.cursor = Cursor.HAND
        event.consume()
    }

    fun pressed(event: MouseEvent) {
        val v = check(event)
        if (!event.isPrimaryButtonDown)
            return
        v.scene.cursor = Cursor.CLOSED_HAND
        println(vertexRadius)
        println(v.radius)
        event.consume()
    }

    fun exited(event: MouseEvent) {
        val v = check(event)
        if (!event.isPrimaryButtonDown) {
            v.scene.cursor = Cursor.DEFAULT
            v.radiusSummand.value -= (v.radius / 3)
        }
    }

    private fun check(event: MouseEvent): VertexView {
        require(event.target is VertexView)
        return event.target as VertexView
    }
}