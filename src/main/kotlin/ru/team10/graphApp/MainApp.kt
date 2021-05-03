package ru.team10.graphApp

import javafx.stage.Stage
import ru.team10.graphApp.view.MainView
import tornadofx.App
import tornadofx.launch

class MainApp: App(MainView::class) {
    override fun start(stage: Stage) {
        //starting
        with(stage) {
            width = 800.0
            height = 600.0
        }
        super.start(stage)

        //started
    }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}