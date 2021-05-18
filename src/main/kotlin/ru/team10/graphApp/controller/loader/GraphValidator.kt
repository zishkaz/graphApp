package ru.team10.graphApp.controller.loader

import javafx.scene.control.Alert
import mu.KotlinLogging
import ru.team10.graphApp.view.GraphView
import tornadofx.alert

internal fun validateGraph(graph: GraphView): Boolean {

    val logger = KotlinLogging.logger {}

    var centralityRangCheck = graph.vertices().map { it.vertex.centralityRang }.count { it <= 0 && it != -1.0 }
    if (centralityRangCheck > 0) {
        logger.error("The $centralityRangCheck vertices have negative (but not default -1) rank. Graph won't open!")
        alert(
            Alert.AlertType.ERROR,
            "ERROR!\nThe $centralityRangCheck vertices have negative (but not default -1) rank. Graph won't open!"
        )
        return false
    }

    centralityRangCheck = graph.vertices().map { it.vertex.centralityRang }.count { it == -1.0 }
    if (!(centralityRangCheck == graph.vertices().size || centralityRangCheck == 0)) {
        graph.vertices().forEach { it.vertex.centralityRang = -1.0 }
        logger.warn("Not all vertices have rank. Rerun centrality algorithm!")
        alert(
            Alert.AlertType.WARNING,
            "WARNING!\n Not all vertices have rank.\n Rerun centrality algorithm!"
        )
    }

    var communityIDCheck = graph.vertices().map { it.vertex.communityID }.count { it <= 0 && it != -1 }
    if (communityIDCheck > 0) {
        logger.error("The $communityIDCheck vertices have negative (but not default -1) community ID. Graph won't open!")
        alert(
            Alert.AlertType.ERROR, "ERROR!\n" +
                    " The $communityIDCheck vertices have negative (but not default -1) community ID. Graph won't open! "
        )
        return false
    }

    communityIDCheck = graph.vertices().map { it.vertex.communityID }.count { it == -1 }
    if (!(communityIDCheck == graph.vertices().size || centralityRangCheck == 0)) {
        graph.vertices().forEach { it.vertex.communityID = -1 }
        logger.warn("Not all vertices have community ID. Rerun community detection algorithm!")
        alert(
            Alert.AlertType.WARNING, "WARNING!\n" +
                    " Not all vertices have community ID.\n" +
                    " Rerun community detection algorithm!"
        )
    }

    if (graph.edges().map { it.edge.weight }.count { it <= 0 } > 0) {
        logger.error("The graph has edge with negative weight. Graph won't open!")
        alert(
            Alert.AlertType.ERROR,
            "ERROR!\n The graph has edge with negative weight. Graph won't open!"
        )
        return false
    }
    for (edge in graph.edges().map { it.edge }) {
        if (edge.first == edge.second) {
            logger.error("The graph includes loop. Graph won't open!")
            alert(
                Alert.AlertType.ERROR,
                "ERROR!\n The graph includes loop. Graph won't open!"
            )
            return false
        }
    }
    if (graph.vertices().sumOf { it.vertex.mass } - graph.vertices().size != graph.edges().size * 2) {
        logger.error("The graph includes multiple edges. Graph won't open!")
        alert(
            Alert.AlertType.ERROR,
            "ERROR!\n The graph includes multiple edges. Graph won't open!"
        )
        return false
    }
    return true
}