import javafx.application.Platform
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest
import ru.team10.graphApp.MainApp
import ru.team10.graphApp.controller.algorithms.Layout
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.*
import tornadofx.add
import tornadofx.getChildList

/**
 * By zishkaz
 */

internal class Import : ApplicationTest() {

    private fun generateGraph(): GraphView {

        val graph = Graph()
        val sergey = Vertex("Sergey")
        val alice = Vertex("Alice")
        val vlados = Vertex("Vlados")
        graph.addVertex(sergey)
        graph.addVertex(alice)
        graph.addVertex(vlados)
        graph.addEdge(Edge(sergey, alice))
        graph.addEdge(Edge(sergey, vlados))
        graph.addEdge(Edge(vlados, alice))
        return GraphView(
            graph,
            listOf(VertexView(sergey, 200.0, 200.0), VertexView(alice, 250.0, 200.0), VertexView(vlados, 300.0, 100.0))
        )
    }

    override fun start(stage: Stage) {
        MainApp().start(stage)
    }

    override fun stop() {
        MainApp().stop()
    }

    @Test
    fun `Vertex data window works correctly`() {
        Platform.runLater {
            graphView = generateGraph()
            rootBorderPan.center.getChildList()!!.clear()
            rootBorderPan.center.add(graphView)
            layoutAnim = Layout.applyForceAtlas2(graphView)
        }
        clickOn("Layout")
        Thread.sleep(300)
        clickOn("START")
        Thread.sleep(5000)
        clickOn("STOP")
        Thread.sleep(1000)
        val temp = graphView.vertices().first()
        clickOn(temp)
        Thread.sleep(2000)
        assertTrue(temp.window.isVisible)
        val data = temp.infoText.text
        var stringTemp = data.removePrefix("ID: ").takeWhile { it != '\n' }
        assertEquals(stringTemp, temp.vertex.id)
        stringTemp = data.drop(data.indexOf('\n') + 1)
        assertEquals(stringTemp.removePrefix("Community ID: ").takeWhile { it != '\n' }.toInt(), temp.vertex.communityID)
        stringTemp = stringTemp.drop(stringTemp.indexOf('\n') + 1).removePrefix("Centrality rang: ")
        assertEquals(stringTemp, String.format("%.3f", temp.vertex.centralityRang))
    }
}