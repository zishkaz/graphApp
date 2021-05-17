package ru.team10.graphApp.view

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ru.team10.graphApp.controller.GraphZoomDragController
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.view.gui.*
import tornadofx.*

internal var showEdges = ToggleButton()
internal var graphView = GraphView(Graph())
internal var layoutAnim = Layout.applyForceAtlas2(graphView)

internal fun TextField.createTextField(constantName: String, prop: SimpleDoubleProperty): TextField {

    val doubleValue = SimpleDoubleProperty(0.0)
    doubleValue.bindBidirectional(prop)
    prop.onChange { text = it.toString() }
    text = prop.value.toString()
    filterInput { it.controlNewText.isDouble() }
    setOnKeyReleased { event ->
        if (event.code == KeyCode.ENTER) {
            if (text == null || text.trim().isBlank()) {
                alert(Alert.AlertType.WARNING, "Please, input $constantName constant!")
            } else if (text.toDouble() <= 0.0) {
                alert(Alert.AlertType.WARNING, "$constantName constant must be positive!")
            } else {
                parent.requestFocus()
                prop.value = text.toDouble()
                text = prop.value.toString()
            }
        }
    }
    return this
}

internal class MainView : View("Graph Application") {

    override val root = borderpane {
        stylesheets.add("1.css")

        val controller = GraphZoomDragController()
        this.setOnScroll {
            controller.scroll(it, graphView)
        }
        this.setOnMousePressed {
            controller.pressed(it)
        }
        this.setOnMouseDragged {
            controller.dragged(it, graphView)
        }

        center {
            add(graphView)
        }

        top = vbox(10) {
            textflow {
                text("GRAPH") {
                    fill = Color.PURPLE
                    font = Font(20.0)
                }
                text("App") {
                    fill = Color.ORANGE
                    font = Font(28.0)
                }
            }
        }
        left = vbox {
            titledpane("Graph").createGraphMenu(this@borderpane)
            titledpane("Layout").createLayoutMenu(this@MainView)
            titledpane("Community").createCommunityMenu()
            titledpane("Centrality").createCentralityMenu()
            titledpane("Settings").createSettingsMenu()
        }
        style {
            backgroundColor += Color.AZURE
        }
    }
}