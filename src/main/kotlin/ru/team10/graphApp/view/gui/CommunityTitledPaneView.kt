package ru.team10.graphApp.view.gui

import javafx.scene.control.Alert
import javafx.scene.control.TitledPane
import javafx.stage.FileChooser
import javafx.stage.Popup
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.controller.algorithms.Leiden
import ru.team10.graphApp.controller.algorithms.colorAccordingToCommunity
import ru.team10.graphApp.controller.algorithms.leidenResolution
import ru.team10.graphApp.utils.buildCommunityDetectionReport
import ru.team10.graphApp.view.createTextField
import ru.team10.graphApp.view.graphView
import tornadofx.*

fun TitledPane.createCommunityMenu() {

    isExpanded = false
    vbox(10) {
        label("Resolution")
        textfield().createTextField("Resolution", Layout.jitterTolerance)
        button("Start Leiden algorithm") {
            action {
                lateinit var leiden: Leiden
                runAsync {
                    leiden = Leiden(graphView)
                    leiden.startLeiden(leidenResolution)
                    leiden.setCommunity()
                }
            }
        }

        button("SAVE") {
            action {
                val window = Popup()
                val fileChooser = FileChooser()
                val importFilter =
                    FileChooser.ExtensionFilter("Text file (*.txt)", "*.txt")
                fileChooser.extensionFilters.add(importFilter)
                fileChooser.title = "Save result"
                val file = fileChooser.showSaveDialog(window)
                file?.let {
                    buildCommunityDetectionReport(graphView, file)
                }
            }
        }

        button("COLOR") {
            action {
                if (graphView.vertices().map { it.vertex.communityID }.contains(-1)) {
                    alert(Alert.AlertType.ERROR, "ERROR!\nRun the algorithm before using its result!")
                } else {
                    colorAccordingToCommunity(graphView)
                }
            }
        }
    }
}