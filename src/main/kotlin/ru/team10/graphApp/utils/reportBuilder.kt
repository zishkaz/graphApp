package ru.team10.graphApp.utils

import ru.team10.graphApp.view.GraphView
import java.io.File

fun buildCommunityDetectionReport(graph: GraphView, file: File) {

    val communityIdToNodes = hashMapOf<Int, HashSet<String>>()
    for (node in graph.vertices().map { it.vertex }) {
        if (communityIdToNodes.containsKey(node.communityID)) communityIdToNodes[node.communityID]!!.add(node.id) else communityIdToNodes[node.communityID] =
            hashSetOf(node.id)
    }
    for (keys in communityIdToNodes.keys) {
        file.writeText("$keys:\n")
        for (id in communityIdToNodes[keys]!!) {
            file.writeText("\tid\n")
        }
    }
}

fun buildCentralityReport(graph: GraphView, file: File) {

    val nodes = graph.vertices().map { it.vertex }.sortedByDescending { it.centralityRang }
    for (node in nodes) {
        file.writeText("${node.id} = ${node.centralityRang}\n")
    }
}