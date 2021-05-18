package ru.team10.graphApp.view.gui

import javafx.scene.control.TitledPane
import javafx.scene.layout.BorderPane
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.view.layoutAnim
import ru.team10.graphApp.view.graphView
import tornadofx.*

internal fun TitledPane.createGraphMenu(root: BorderPane) {

    isExpanded = false
    vbox(5) {
        label("IMPORT")
        hbox(5) {
            button("JSON").createJsonImportButton {
                root.center.getChildList()!!.clear()
                root.center.add(graphView)
                layoutAnim = Layout.applyForceAtlas2(graphView)
            }

            button("SQLite").createSQLiteImportButton {
                root.center.getChildList()!!.clear()
                root.center.add(graphView)
                layoutAnim = Layout.applyForceAtlas2(graphView)
            }

            button("Neo4j").createNeo4jImportButton {
                root.center.getChildList()!!.clear()
                root.center.add(graphView)
                layoutAnim = Layout.applyForceAtlas2(graphView)
            }
        }

        label("EXPORT")
        hbox(5) {
            button("JSON").createJsonExportButton()
            button("SQLite").createSQLiteExportButton()
            button("Neo4j").createNeo4jExportButton()
        }
    }
}