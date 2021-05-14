package ru.team10.graphApp.controller.loader

import javafx.scene.control.Alert
import mu.KotlinLogging
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import ru.team10.graphApp.model.Edge
import ru.team10.graphApp.model.Graph
import ru.team10.graphApp.model.Vertex
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import tornadofx.alert
import kotlin.random.Random

private val logger = KotlinLogging.logger { }

class Neo4jLoader : GraphLoader, Controller() {

    override fun loadGraph(data: String): GraphView? {
        val (uri, user, password) = data.split("\n")
        val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        val session = driver.session()
        val vertices = mutableMapOf<String, VertexView>()
        val graph = Graph()
        var graphView = GraphView(graph, vertices.values.toList())
        session.readTransaction { tx ->
            try {
                val getVerticesStatement =
                    tx.run(
                        "MATCH(v:vertices) RETURN v.id AS id, v.posX AS posX, v.posY AS posY," +
                                " v.centralityRang AS centralityRang, v.communityID AS communityID"
                    )
                val getEdgesStatemen =
                    tx.run("MATCH(first)-[weight]-(second) RETURN first.id AS first, second.id AS second, weight.weight AS weight")

                val edges = mutableListOf<Edge>()

                getVerticesStatement.forEach { rec ->
                    val vertex = Vertex(
                        rec["id"].asString(),
                        rec["centralityRang"]?.toString()?.toDouble() ?: -1.0,
                        rec["communityID"]?.toString()?.toInt() ?: -1
                    )
                    val vertexView = VertexView(
                        vertex,
                        rec["posX"]?.toString()?.toDouble() ?: Random.nextDouble(1000.0),
                        rec["posY"]?.toString()?.toDouble() ?: Random.nextDouble(1000.0)
                    )
                    vertices[rec["id"].asString()] = vertexView
                    // TODO(LOGGER)
                }

                getEdgesStatemen.forEach { rec ->
                    val edge = vertices[rec["second"].asString()]?.let {
                        vertices[rec["first"].asString()]?.let { it1 ->
                            Edge(
                                it1.vertex,
                                it.vertex,
                                rec["weight"]?.toString()?.toDouble() ?: 1.0
                            )
                        }
                    }
                    edge?.let { edges.add(it) }
                    // TODO(LOGGER)
                }

                vertices.forEach { graph.addVertex(it.value.vertex) }
                edges.forEach { graph.addEdge(it) }
                graphView = GraphView(graph, vertices.values.toList())
                // TODO(LOGGER)
            } catch (ex: Exception) {
                tx.rollback()
                logger.error(ex) { "Cannot load all graph" }
                alert(Alert.AlertType.ERROR, "ERROR\nCannot load all graph!")
            }
        }
        return if (validateGraph(graphView)) graphView else null
    }

    override fun saveGraph(graph: GraphView, data: String) {
        val (uri, user, password) = data.split("\n")
        val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        val session = driver.session()
        session.writeTransaction { tx ->
            try {
                val vertexToNumber = hashMapOf<VertexView, Int>()
                for (node in graph.vertices()) {
                    vertexToNumber[node] = vertexToNumber.size
                    tx.run(
                        "CREATE(:vertices{id:\$id, posX:\$posX, posY:\$posY, centralityRang:\$centralityRang, communityID:\$communityID})",
                        mutableMapOf(
                            "id" to node.vertex.id,
                            "posX" to node.centerX,
                            "posY" to node.centerY,
                            "centralityRang" to node.vertex.centralityRang,
                            "communityID" to node.vertex.communityID
                        ) as Map<String, Any?>?
                    )
                    // TODO(LOGGER)
                }
                for (edge in graph.edges()) {
                    tx.run(
                        "MATCH(first:vertices{id:\$id_first}), (second:vertices{id:\$id_second}) " +
                                "MERGE(first)-[:edge{weight:\$weight}]-(second)",
                        mutableMapOf(
                            "id_first" to edge.edge.first.id,
                            "id_second" to edge.edge.second.id,
                            "weight" to edge.edge.weight

                        ) as Map<String, Any?>?
                    )
                }
            } catch (ex: Exception) {
                tx.rollback()
                logger.error(ex) { "Cannot save all graph" }
                alert(Alert.AlertType.ERROR, "ERROR\nCannot save all graph!")
            }
        }

        session.close()
        driver.close()
    }


}