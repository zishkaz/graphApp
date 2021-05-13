package ru.team10.graphApp.controller.loader

import ru.team10.graphApp.view.GraphView

fun validateGraph(graph: GraphView): Boolean {

    var centralityRangCheck = graph.vertices().map { it.vertex.centralityRang }.count { it <= 0 && it != -1.0 }
    if (centralityRangCheck > 0) {
        //@pionep, an error for negative (but not default -1) rang. Graph won't open
        return false
    }
    centralityRangCheck = graph.vertices().map {it.vertex.centralityRang}.count {it == -1.0}
    if (!(centralityRangCheck == graph.vertices().size || centralityRangCheck == 0)) {
        graph.vertices().forEach { it.vertex.centralityRang = -1.0 }
        //@pionep, make a warning that not all vertices have rang and centrality algo is needed to be rerun
    }
    var communityIDCheck = graph.vertices().map {it.vertex.communityID}.count { it == -1 }
    if (!(centralityRangCheck == graph.vertices().size || centralityRangCheck == 0)) {
        graph.vertices().forEach { it.vertex.communityID = -1 }
        //@pionep, also a warning, the same as for the centrality
    }
    communityIDCheck = graph.vertices().map {it.vertex.communityID}.count {it <= 0 && it != -1}
    if (communityIDCheck > 0) {
        //@pionep, an error for negative (but not default -1) communityID. Graph won't open
        return false
    }
    if (graph.edges().map {it.edge.weight}.count { it <= 0 } > 0) {
        //@pionep, an error for negative edge weight. This time the graph won't open.
        return false
    }
    return true
}