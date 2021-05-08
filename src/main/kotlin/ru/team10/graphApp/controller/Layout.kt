package ru.team10.graphApp.controller

import javafx.animation.AnimationTimer
import javafx.scene.paint.Color
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

private const val antiCollisionCoeff = 0.5
private const val scaling = 10
private const val speed = 0.1
private const val gravity = 0.05

class Layout: Controller() {

    fun randomLayout(width: Double, height: Double, graph: GraphView) {

        for (v in graph.vertices()) {

            val randomX = Random.nextDouble(50.0, width - 50.0)
            val randomY = Random.nextDouble(50.0, height - 50.0)
            v.position = randomX to randomY
            v.color = Color.AQUA
        }
    }

    inner class Anim(private val graph: GraphView): AnimationTimer() {
        override fun handle(now: Long) {

            for (edge in graph.edges()) {
                applyAttractionForce(edge.first, edge.second, edge.edge.weight)
            }
            val pairs = getAllNodePairs(graph.vertices())
            for (pair in pairs) {
                applyRepulsionForce(pair.first, pair.second)
            }
            for (node in graph.vertices()) {
                applyGravity(node)
            }
            for (node in graph.vertices()) {
                node.position = node.layoutCenterX to node.layoutCenterY
            }
            Thread.sleep(50)
        }
    }

    fun applyForceAtlas2(graph: GraphView) {

        val kek = Anim(graph)
        kek.start()
    }


    private fun computeDistance(v: VertexView, u: VertexView) = sqrt((v.centerX - u.centerX).pow(2) + (v.centerY - u.centerY).pow(2))

    private fun applyAttractionForce(v: VertexView, u: VertexView, e: Double) {

        val xDist = v.centerX - u.centerX
        val yDist = v.centerY - u.centerY
        val dist = computeDistance(v, u) - v.radius - u.radius
        if (dist > 0) {
            val factor = speed * -e / (v.vertex.incidents + 1)

            v.layoutCenterX += xDist * factor
            v.layoutCenterY += yDist * factor

            u.layoutCenterX -= xDist * factor
            u.layoutCenterY -= yDist * factor
        }
    }

    private fun applyRepulsionForce(v: VertexView, u: VertexView) {
        //Make less code when this shit works
        val xDist = v.centerX - u.centerX
        val yDist = v.centerY - u.centerY
        val degVInced = v.vertex.incidents + 1
        val degUInced = u.vertex.incidents + 1
        val dist = computeDistance(v, u) - v.radius - u.radius

        if (dist > 0) {
            val factor = speed * scaling * degVInced * degUInced / dist / dist

            v.layoutCenterX += xDist * factor
            v.layoutCenterY += yDist * factor

            u.layoutCenterX -= xDist * factor
            u.layoutCenterY -= yDist * factor
        } else if (dist < 0) {
            val factor = speed * antiCollisionCoeff * degVInced * degUInced

            v.layoutCenterX += xDist * factor
            v.layoutCenterY += yDist * factor

            u.layoutCenterX -= xDist * factor
            u.layoutCenterY -= yDist * factor
        }
    }

    fun applyGravity(v: VertexView) {

        val xCenter = primaryStage.width / 2
        val yCenter = primaryStage.height / 2
        val xDist = v.centerX - xCenter
        val yDist = v.centerY - yCenter
        val dist = sqrt(xDist.pow(2) + yDist.pow(2)) - v.radius
        if (dist > 0) {
            val factor = speed * (v.vertex.incidents + 1) * gravity
            v.layoutCenterX -= xDist * factor
            v.layoutCenterY -= yDist * factor
        }
    }

    private fun getAllNodePairs(nodes: Collection<VertexView>): Collection<Pair<VertexView, VertexView>> {

        val listNodes = nodes.toList()
        val pairs = mutableListOf<Pair<VertexView, VertexView>>()
        for (i in listNodes.indices) {
            for (j in i+1 until listNodes.size) {
                pairs.add(listNodes[i] to listNodes[j])
            }
        }
        return pairs
    }
}