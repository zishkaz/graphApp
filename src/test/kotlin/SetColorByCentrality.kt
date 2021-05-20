import javafx.application.Platform
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest
import ru.team10.graphApp.MainApp
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.*
import tornadofx.add
import tornadofx.getChildList

internal class CentralityColor: ApplicationTest() {

    private fun generateGraph(): GraphView {

        val graph = Graph()
        val a = Vertex("A")
        val b = Vertex("B")
        val c = Vertex("C")
        val d = Vertex("D")
        val e = Vertex("E")
        val f = Vertex("F")
        graph.addVertex(a)
        graph.addVertex(b)
        graph.addVertex(c)
        graph.addVertex(d)
        graph.addVertex(e)
        graph.addVertex(f)
        graph.addEdge(Edge(a, b))
        graph.addEdge(Edge(a, c))
        graph.addEdge(Edge(a, d))
        graph.addEdge(Edge(a, f))
        graph.addEdge(Edge(b, c))
        graph.addEdge(Edge(d, e))

        return GraphView(
            graph,
            listOf(
                VertexView(a, 200.0, 200.0), VertexView(b, 250.0, 200.0), VertexView(c, 300.0, 100.0),
                VertexView(d, 200.0, 250.0), VertexView(e, 100.0, 100.0), VertexView(f, 150.0, 150.0)
            )
        )
    }

    override fun start(stage: Stage) {
        MainApp().start(stage)
    }

    override fun stop() {
        MainApp().stop()
    }

    @Test
    fun `Setting colors by centrality measure works correctly`() {
        Platform.runLater {
            graphView = generateGraph()
            rootBorderPan.center.getChildList()!!.clear()
            rootBorderPan.center.add(graphView)
        }

        clickOn("Centrality")
        Thread.sleep(300)
        clickOn("#startCentrality")
        Thread.sleep(300)

        val e: MutableMap<String, VertexView> = mutableMapOf()
        for (v in graphView.vertices()) {
            e[v.vertex.id] = v
        }

        clickOn("#colorCentrality")

        graphView.vertices().forEach {
            if(it.vertex.id != "A")
                assertTrue(it.color.green > e["A"]!!.color.green && it.vertex.centralityRang < e["A"]!!.vertex.centralityRang)
        }
    }
}