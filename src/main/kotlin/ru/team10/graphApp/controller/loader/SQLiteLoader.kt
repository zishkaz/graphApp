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
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.random.Random

internal class SQLiteLoader : GraphLoader, Controller() {

    companion object : KLogging()

    override fun loadGraph(data: String): GraphView? {
        logger.info("Started loading graph via SQLite")
        val connection =
            DriverManager.getConnection("jdbc:sqlite:$data") ?: throw SQLException("Cannot connect to database")
        val getVerticesStatement = try {
            connection.prepareStatement("SELECT id, posX, posY, centralityRang, radiusSummand, communityID FROM vertices")
        } catch (ex: Exception) {
            logger.info("Connection to SQLite failed or DataBase is empty.")
            alert(
                Alert.AlertType.INFORMATION,
                "Information.\nCannot connect to SQLite or DataBase is empty.\nCheck out URI."
            )
            return null
        }
        val getEdgesStatement = connection.prepareStatement("SELECT first, second, weight FROM edges")
        val vertices = mutableListOf<VertexView>()
        val edges = mutableListOf<Edge>()
        try {
            val result = getVerticesStatement.executeQuery()
            while (result.next()) {
                val vertex = Vertex(
                    result.getString("id"),
                    result.getObject("centralityRang")?.toString()?.toDouble() ?: -1.0,
                    result.getObject("communityID")?.toString()?.toInt() ?: -1,
                )
                val vertexView = VertexView(
                    vertex,
                    result.getObject("posX")?.toString()?.toDouble() ?: Random.nextDouble(1000.0),
                    result.getObject("posY")?.toString()?.toDouble() ?: Random.nextDouble(1000.0)
                ).apply {
                    this.radiusSummand.value = result.getObject("radiusSummand")?.toString()?.toDouble() ?: 0.0
                }
                vertices.add(vertexView)
            }
        } catch (e: Exception) {
            logger.error("Vertices can't be read from a specified database!")
            alert(Alert.AlertType.ERROR, "ERROR\nVertices can't be read from a specified database!")
            return null
        }
        try {
            val result = getEdgesStatement.executeQuery()
            while (result.next()) {
                if (result.getInt("first") !in 0 until vertices.size) return null
                if (result.getInt("second") !in 0 until vertices.size) return null
                val edge = Edge(
                    vertices[result.getInt("first")].vertex,
                    vertices[result.getInt("second")].vertex,
                    result.getObject("weight")?.toString()?.toDouble() ?: 1.0
                )
                edges.add(edge)
            }
        } catch (e: Exception) {
            logger.error("Edges can't be read from a specified database!")
            alert(Alert.AlertType.ERROR, "ERROR\nEdges can't be read from a specified database!")
            return null
        }
        val graph = Graph()
        vertices.forEach { graph.addVertex(it.vertex) }
        edges.forEach { graph.addEdge(it) }
        val graphView = GraphView(graph, vertices)
        logger.info { "Graph has been loaded via SQLite." }
        return if (validateGraph(graphView)) graphView else null
    }

    override fun saveGraph(graph: GraphView, data: String) {
        logger.info("Started saving graph via SQLite")
        val connection =
            DriverManager.getConnection("jdbc:sqlite:$data") ?: throw SQLException("Cannot connect to database")
        connection.createStatement().also {
            try {
                it.execute("CREATE TABLE IF NOT EXISTS vertices(num INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id TEXT NOT NULL, posX DOUBLE, posY DOUBLE, centralityRang DOUBLE, radiusSummand DOUBLE, communityID INTEGER);")
                it.execute("CREATE TABLE IF NOT EXISTS edges(edgeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, first INTEGER NOT NULL, second INTEGER NOT NULL, weight DOUBLE);")
            } catch (e: Exception) {
                logger.error("Couldn't create tables!")
                alert(Alert.AlertType.ERROR, "ERROR\nCouldn't create tables!")
                return
            }
        }
        val vertexToNumber = hashMapOf<VertexView, Int>()
        for (node in graph.vertices()) {
            vertexToNumber[node] = vertexToNumber.size
            connection.createStatement().also {
                try {
                    it.execute(
                        "INSERT INTO vertices (id, posX, posY, centralityRang, radiusSummand, communityID) VALUES ('${
                            node.vertex.id
                        }', ${node.centerX},${node.centerY}, ${
                            if (node.vertex.centralityRang != -1.0) node.vertex.centralityRang else null
                        }, ${
                            if (node.radiusSummand.value != 0.0) node.radiusSummand.value else null
                        }, ${
                            if (node.vertex.communityID != -1) node.vertex.communityID else null
                        });"
                    )
                } catch (e: Exception) {
                    alert(Alert.AlertType.ERROR, "ERROR\nVertices can't be added to a specified database!")
                    it.execute("DROP TABLE vertices")
                }
            }
        }
        for (edge in graph.edges()) {
            connection.createStatement().also {
                try {
                    it.execute(
                        "INSERT INTO edges (first, second, weight) VALUES (${
                            vertexToNumber.getOrDefault(
                                edge.first,
                                0
                            )
                        }, ${
                            vertexToNumber.getOrDefault(
                                edge.second,
                                0
                            )
                        }, ${if (edge.edge.weight != 1.0) edge.edge.weight else null});"
                    )
                } catch (e: Exception) {
                    alert(Alert.AlertType.ERROR, "ERROR\nEdges can't be added to a specified database!")
                    it.execute("DROP TABLE vertices")
                    it.execute("DROP TABLE edges")
                }
            }
        }
        logger.info { "Graph has been saved via SQLite." }
    }
}