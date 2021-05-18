package ru.team10.graphApp.controller.loader

import javafx.scene.control.Alert
import mu.KLogging
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import tornadofx.alert
import tornadofx.getDouble
import tornadofx.loadJsonObject
import java.io.File
import javax.json.Json
import kotlin.io.path.Path
import kotlin.random.Random


class FileLoader : GraphLoader, Controller() {

    companion object : KLogging()

    override fun loadGraph(data: String): GraphView? {
        logger.info("Started loading graph via .json")
        val json = loadJsonObject(Path(data))
        val vertices = json.getJsonArray("vertices")?.map {
            val jsonObject = it.asJsonObject()
            val id =
                if (jsonObject.containsKey("id")) jsonObject.getString("id") else {
                    logger.error("Some vertex from the file $data has no ID!")
                    alert(Alert.AlertType.ERROR, "ERROR!\nVertex doesn't have id!")
                    return null
                }
            val communityID = if (jsonObject.containsKey("communityID")) jsonObject.getInt("communityID") else -1
            val centralityRang =
                if (jsonObject.containsKey("centralityRang")) jsonObject.getDouble("centralityRang") else -1.0
            val posX = if (jsonObject.containsKey("posX")) jsonObject.getDouble("posX") else Random.nextDouble(1000.0)
            val posY = if (jsonObject.containsKey("posY")) jsonObject.getDouble("posY") else Random.nextDouble(1000.0)
            val vertex = Vertex(id, centralityRang, communityID)
            VertexView(vertex, posX, posY)
        } ?: run {
            logger.error("Empty graph can't be loaded!")
            alert(Alert.AlertType.ERROR, "ERROR!\nEmpty graph can't be loaded!")
            return null
        }
        val edges = json.getJsonArray("edges")?.map {
            val jsonObject = it.asJsonObject()
            val first = jsonObject.getInt("first")
            if (first < 0 || first >= vertices.size) {
                logger.error("Wrong edge first node number!")
                alert(Alert.AlertType.ERROR, "ERROR\nWrong edge first node number!")
                return null
            }
            val second = jsonObject.getInt("second")
            if (second >= vertices.size || second < 0) {
                logger.error("Wrong edge second node number!")
                alert(Alert.AlertType.ERROR, "ERROR\nWrong edge second node number!")
                return null
            }
            val weight = if (jsonObject.containsKey("weight")) jsonObject.getDouble("weight") else 1.0
            Edge(vertices[first].vertex, vertices[second].vertex, weight)
        }
        val graph = Graph()
        vertices.forEach { graph.addVertex(it.vertex) }
        edges?.forEach { graph.addEdge(it) }
        val graphView = GraphView(graph, vertices)
        logger.info("Graph has been loaded via .json.")
        return if (validateGraph(graphView)) graphView else null
    }

    override fun saveGraph(graph: GraphView, data: String) {
        logger.info("Started saving graph via .json")
        val json = Json.createObjectBuilder()
        val verticesJson = Json.createArrayBuilder()
        val vertexToNumber = hashMapOf<VertexView, Int>()
        for (node in graph.vertices()) {
            vertexToNumber[node] = vertexToNumber.size
            val vertexJson = Json.createObjectBuilder()
            vertexJson.add("id", node.vertex.id)
            if (node.vertex.centralityRang != -1.0) vertexJson.add("centralityRang", node.vertex.centralityRang)
            if (node.vertex.communityID != -1) vertexJson.add("communityID", node.vertex.communityID)
            vertexJson.add("posX", node.centerX)
            vertexJson.add("posY", node.centerY)
            verticesJson.add(vertexJson)
        }
        json.add("vertices", verticesJson)
        val edgesJson = Json.createArrayBuilder()
        for (edge in graph.edges()) {
            val edgeJson = Json.createObjectBuilder()
            edgeJson.add("first", vertexToNumber.getOrDefault(edge.first, 0))
            edgeJson.add("second", vertexToNumber.getOrDefault(edge.second, 0))
            if (edge.edge.weight != 1.0) edgeJson.add("weight", edge.edge.weight)
            edgesJson.add(edgeJson)
        }
        json.add("edges", edgesJson)
        val file = File(data)
        file.createNewFile()
        file.writeText(json.build().toString())
        logger.info("Graph has been saved via .json.")
    }
}