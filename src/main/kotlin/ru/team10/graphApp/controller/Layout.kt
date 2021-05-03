package ru.team10.graphApp.controller

import javafx.scene.paint.Color
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import kotlin.random.Random

class Layout: Controller() {

    fun place(width: Double, height: Double, vertices: Collection<VertexView>) {

        for (v in vertices) {

            val randomX = Random.nextDouble(50.0, width - 50.0)
            val randomY = Random.nextDouble(50.0, height - 50.0)
            v.position = randomX to randomY
            v.color = Color.AQUA
        }
    }
}