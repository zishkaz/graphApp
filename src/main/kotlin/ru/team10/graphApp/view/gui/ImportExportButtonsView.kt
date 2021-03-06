package ru.team10.graphApp.view.gui

import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.stage.FileChooser
import javafx.stage.Popup
import ru.team10.graphApp.controller.algorithms.Centrality
import ru.team10.graphApp.controller.loader.FileLoader
import ru.team10.graphApp.controller.loader.Neo4jLoader
import ru.team10.graphApp.controller.loader.SQLiteLoader
import ru.team10.graphApp.view.graphView
import tornadofx.*

internal fun Button.createJsonImportButton(op: () -> Unit) {

    action {
        val window = Popup()
        val fileChooser = FileChooser()
        val importFilter = FileChooser.ExtensionFilter("Graph file (*.json)", "*.json")
        fileChooser.extensionFilters.add(importFilter)
        fileChooser.title = "Open Resource File"
        val file = fileChooser.showOpenDialog(window)
        file?.let {
            FileLoader().loadGraph(file.path)?.let {
                Centrality.wasDone = false
                graphView = it
                op()
            }
        }
    }
}

internal fun Button.createSQLiteImportButton(op: () -> Unit) {

    text = "SQLite"
    setOnMouseReleased {
        hbox {
            val input = textfield()
            input.promptText = ("Enter URI")
            var data: String
            input.setOnKeyReleased { event ->
                if (event.code == KeyCode.ENTER) {
                    data = input.text
                    SQLiteLoader().loadGraph(data)?.let {
                        graphView = it
                        op()
                        hide()

                    }
                }
            }
        }
    }
}

internal fun Button.createNeo4jImportButton(op: () -> Unit) {

    setOnMouseReleased {
        vbox(5) {
            var data: String

            label("URI")
            val uri = textfield()

            label("Username")
            val username = textfield()

            label("Password")
            val password = textfield()

            val k = button()
            k.text = "OK"
            k.setOnMouseReleased {
                if (uri.text.isBlank() || username.text.isBlank() || password.text.isBlank()) {
                    alert(
                        Alert.AlertType.WARNING,
                        "You have forgotten write in port or login or password!"
                    )
                } else {
                    data = uri.text.plus("\n").plus(username.text).plus("\n").plus(password.text)
                    Neo4jLoader().loadGraph(data)?.let {
                        graphView = it
                        op()
                    }
                }
                hide()
            }
        }
    }
}

internal fun Button.createJsonExportButton() {

    action {
        val window = Popup()
        val fileChooser = FileChooser()
        val importFilter = FileChooser.ExtensionFilter("Graph file (*.json)", "*.json")
        fileChooser.extensionFilters.add(importFilter)
        fileChooser.title = "Save Graph"
        val file = fileChooser.showSaveDialog(window)
        file?.let {
            FileLoader().saveGraph(graphView, file.path)
        }
    }
}

internal fun Button.createSQLiteExportButton() {

    setOnMouseReleased {
        hbox {
            val input = textfield()
            input.promptText = ("Enter URI")
            var data: String
            input.setOnKeyReleased { event ->
                if (event.code == KeyCode.ENTER) {
                    data = input.text
                    SQLiteLoader().saveGraph(graphView, data)
                    hide()

                }
            }
        }
    }
}

internal fun Button.createNeo4jExportButton() {

    setOnMouseReleased {

        vbox(5) {
            var data: String

            label("URI")
            val uri = textfield()

            label("Username")
            val username = textfield()

            label("Password")
            val password = textfield()

            val k = button()
            k.text = "OK"
            k.setOnMouseReleased {
                if (uri.text.isBlank() || username.text.isBlank() || password.text.isBlank()) {
                    alert(
                        Alert.AlertType.WARNING,
                        "You have forgotten write in port or login or password!"
                    )
                } else {
                    data = uri.text.plus("\n").plus(username.text).plus("\n").plus(password.text)
                    Neo4jLoader().saveGraph(graphView, data)
                }
                hide()
            }
        }
    }
}