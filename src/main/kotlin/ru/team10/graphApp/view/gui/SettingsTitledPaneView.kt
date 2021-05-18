package ru.team10.graphApp.view.gui

import javafx.scene.control.TitledPane
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Popup
import ru.team10.graphApp.utils.loadConfigFile
import ru.team10.graphApp.utils.saveConfig
import ru.team10.graphApp.view.*
import ru.team10.graphApp.view.graphView
import ru.team10.graphApp.view.showEdges
import tornadofx.*

fun TitledPane.createSettingsMenu() {

    isExpanded = false
    vbox(10) {
        button("Load configuration file") {
            action {
                val window = Popup()
                val fileChooser = FileChooser()
                val importFilter = FileChooser.ExtensionFilter("Configuration file (*.yaml)", "*.yaml")
                fileChooser.extensionFilters.add(importFilter)
                fileChooser.title = "Open Configuration File"
                val file = fileChooser.showOpenDialog(window)
                file?.let {
                    loadConfigFile(file)
                }
            }
        }

        button("Save configuration file") {
            action {
                val window = Popup()
                val fileChooser = FileChooser()
                val importFilter = FileChooser.ExtensionFilter("Configuration file (*.yaml)", "*.yaml")
                fileChooser.extensionFilters.add(importFilter)
                fileChooser.title = "Save Configuration File"
                val file = fileChooser.showSaveDialog(window)
                file?.let {
                    saveConfig(file)
                }
            }
        }

        showEdges = togglebutton("Hide edges") {
            selectedProperty().onChange {
                text = if (!isSelected) {
                    for (edge in graphView.edges()) edge.hide()
                    "Show edges"
                } else {
                    for (edge in graphView.edges()) edge.show()
                    "Hide edges"
                }
            }
        }
        label("Vertex color:")
        val q = colorpicker(Color.AQUA)
        q.setOnHiding {
            for (v in graphView.vertices()) v.color = q.value
            vertexColor = q.value
        }
        label("Vertex radius")
        textfield().createTextField("Radius", vertexRadius)
    }
}