package ru.team10.graphApp.view.gui

import javafx.scene.control.Alert
import javafx.scene.control.TitledPane
import javafx.stage.FileChooser
import javafx.stage.Popup
import ru.team10.graphApp.controller.algorithms.Centrality
import ru.team10.graphApp.utils.buildCentralityReport
import ru.team10.graphApp.view.graphView
import tornadofx.*

internal fun TitledPane.createCentralityMenu() {

    isExpanded = false
    vbox(10) {
        button("START") {
            id = ("startCentrality")
            action {
                runAsync {
                    Centrality.applyHarmonicCentrality(graphView)
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
                    buildCentralityReport(graphView, file)
                }
            }
        }

        button("COLOR") {
            id = ("colorCentrality")
            action {
                if (graphView.vertices().map { it.vertex.centralityRang }.contains(-1.0)) {
                    alert(Alert.AlertType.ERROR, "ERROR!\nRun the algorithm before using its result!")
                } else {
                    val min = graphView.vertices().map { it.vertex.centralityRang }.minOrNull()
                    val max = graphView.vertices().map { it.vertex.centralityRang }.maxOrNull()
                    Centrality.setColor(
                        (max!! - min!!) / graphView.vertices().size,
                        255.0 / graphView.vertices().size,
                        min,
                        graphView.vertices()
                    )
                }
            }
        }
    }
}