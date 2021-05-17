package ru.team10.graphApp

import javafx.stage.Stage
import ru.team10.graphApp.utils.generateForHarmonic1
import ru.team10.graphApp.utils.generateForHarmonic2
import ru.team10.graphApp.utils.genereateFullGraph
import ru.team10.graphApp.view.MainView
import tornadofx.App
import tornadofx.launch

class MainApp : App(MainView::class) {
    override fun start(stage: Stage) {
        //starting
        with(stage) {
            width = 1280.0
            height = 720.0
        }
        super.start(stage)
        //started
    }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}