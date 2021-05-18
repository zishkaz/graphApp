package ru.team10.graphApp.view.gui

import javafx.scene.control.TitledPane
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.view.layoutAnim
import ru.team10.graphApp.view.createTextField
import tornadofx.*

internal fun TitledPane.createLayoutMenu(root: View) {

    isExpanded = false
    vbox(5) {
        togglebutton("START") {
            isSelected = false
            action {
                text = if (isSelected) {
                    runAsync {
                        root.currentStage?.apply {
                            layoutAnim.start()
                        }
                    }
                    "STOP"
                } else {
                    layoutAnim.stop()
                    "START"
                }
            }
        }

        label("Scaling")
        textfield().createTextField("Scaling", Layout.scaling)

        label("Gravity")
        textfield().createTextField("Gravity", Layout.gravity)

        label("Jitter tolerance")
        textfield().createTextField("Jitter tolerance", Layout.jitterTolerance)
        checkbox("BarnesHut optimisation") {
            selectedProperty().bindBidirectional(Layout.isBarnesHutActive)
        }
    }
}