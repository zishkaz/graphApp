package util

import ru.team10.graphApp.view.GraphView
import java.io.File

class DataConverter(
    private val graph: GraphView,
) {
    fun prepareDataForClustering() {
        val vertices = graph.getVerticesId().let { it.zip(it.indices) }.toMap()
        val edges = graph.edges()
        File("src/main/kotlin/leidenUtil.txt").writeText(
            edges.joinToString("\n") { edge ->
                listOf(
                    vertices[edge.edge.first],
                    vertices[edge.edge.second],
                    edge.edge.weight
                ).joinToString("\t")
            }
        )
    }
}
