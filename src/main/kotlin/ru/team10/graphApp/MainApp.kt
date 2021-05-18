package ru.team10.graphApp

//import ru.team10.graphApp.utils.generateForHarmonic1
//import ru.team10.graphApp.utils.generateForHarmonic2
//import ru.team10.graphApp.utils.genereateFullGraph
import javafx.stage.Stage
import mu.KLogging
import ru.team10.graphApp.view.MainView
import tornadofx.App
import tornadofx.launch

class MainApp : App(MainView::class) {

    companion object : KLogging()

    override fun start(stage: Stage) {
        logger.info("Launching the app.")
        with(stage) {
            width = 1280.0
            height = 720.0
        }
        super.start(stage)
        logger.info("The application was successfully launched.")
    }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}