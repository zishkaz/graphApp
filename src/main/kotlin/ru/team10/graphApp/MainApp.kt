package ru.team10.graphApp

import javafx.stage.Stage
import ru.team10.graphApp.loader.FileLoader
import ru.team10.graphApp.loader.GraphLoader
import ru.team10.graphApp.view.MainView
import tornadofx.App
import tornadofx.launch
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path

class MainApp: App(MainView::class) {
    override fun start(stage: Stage) {
        //starting
        with(stage) {
            width = 1280.0
            height = 720.0
        }
        super.start(stage)
        //started
//        stage.widthProperty().addListener { _, _, _ -> run { stage.centerOnScreen() } }
//        stage.heightProperty().addListener { _, _, _ -> run {stage.centerOnScreen()} }
    }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}