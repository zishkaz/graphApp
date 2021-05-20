import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest
import ru.team10.graphApp.MainApp
import ru.team10.graphApp.controller.algorithms.Leiden
import ru.team10.graphApp.controller.loader.FileLoader
import ru.team10.graphApp.view.graphView

internal class LeidenWithMaxResolutionTest : ApplicationTest() {

    override fun start(stage: Stage) {
        MainApp().start(stage)
    }

    override fun stop() {
        MainApp().stop()
    }

    @Test
    fun `Each node is in different cluster after Leiden with resolution = 1`() {
        FileLoader().loadGraph(InputFilePath)?.let {
            graphView = it
        }
        val leiden = Leiden(graphView)
        leiden.startLeiden(resolution = 1.0)
        leiden.setCommunity()
        val communityIDs = graphView.vertices().map { it.vertex.communityID }.toList()
        assertTrue(communityIDs.size == communityIDs.toSet().size)
    }

    companion object {
        const val InputFilePath = "samples/harmonicSample1.json"
    }
}
