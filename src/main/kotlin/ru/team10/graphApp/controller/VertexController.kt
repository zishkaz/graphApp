package ru.team10.graphApp.controller

import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller

internal class VertexController : Controller() {

    fun entered(event: MouseEvent) {

        val v = check(event)
        if (!event.isPrimaryButtonDown) {
            v.radiusSummand.value += 0.5 * v.radius
        }
    }

    fun pressed(event: MouseEvent) {

        val v = check(event)
        if (!event.isPrimaryButtonDown) return
        v.scene.cursor = Cursor.CLOSED_HAND
        v.window.layoutX = v.centerX + 10
        v.window.layoutY = v.centerY - 50
        v.infoText.text = """
            ID: ${v.vertex.id}
            Community ID: ${v.vertex.communityID}
            Centrality rang: ${String.format("%.3f", v.vertex.centralityRang)}
        """.trimIndent()
        v.window.isVisible = true
        v.window.toFront()
        event.consume()
    }

    fun exited(event: MouseEvent) {

        val v = check(event)
        if (!event.isPrimaryButtonDown) {
            v.scene.cursor = Cursor.DEFAULT
            v.radiusSummand.value -= (v.radius / 3)
        }
    }

    fun dragged(event: MouseEvent) {

        val v = check(event)
        if (event.isPrimaryButtonDown) {
            v.scene.cursor = Cursor.DEFAULT
            v.radiusSummand.value -= (v.radius / 3)
        }
    }

    private fun check(event: MouseEvent): VertexView {

        require(event.target is VertexView)
        return event.target as VertexView
    }
}