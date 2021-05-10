package ru.team10.graphApp.controller

import javafx.animation.AnimationTimer
import javafx.geometry.Point2D
import kotlinx.coroutines.ObsoleteCoroutinesApi
import ru.team10.graphApp.view.EdgeView
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.system.measureTimeMillis

private const val antiCollisionCoeff = 100.0
private var scaling = 2.0
private const val gravity = 5.0
private var jitterTolerance = 1.0
private const val minSpeedEfficiency = 0.05
private var speedEfficiency = 1.0
private var globalSpeed = 1.0

class Layout: Controller() {

    fun randomLayout(width: Double, height: Double, graph: GraphView) {

        for (v in graph.vertices()) {

            val randomX = Random.nextDouble(50.0, width - 50.0)
            val randomY = Random.nextDouble(50.0, height - 50.0)
            v.position = randomX to randomY
        }
    }

    inner class Anim(private val graph: GraphView): AnimationTimer() {
        @ObsoleteCoroutinesApi
        override fun handle(now: Long) {
            speedEfficiency = 1.0
            jitterTolerance = 1.0

            println(
                "${measureTimeMillis {  applyAttractionForce(graph.edges())
            applyRepulsionForce(getAllNodePairs(graph.vertices()))
            applyGravity(graph.vertices())}}")
            var totalSwinging = 0.0
            var totalEffectiveTraction = 0.0
            for (node in graph.vertices()) {

                val swinging = sqrt((node.dxOld - node.dx).pow(2) + (node.dyOld - node.dy).pow(2))
                totalSwinging += (node.vertex.incidents + 1) * swinging
                totalEffectiveTraction += (node.vertex.incidents + 1) * 0.5 * sqrt((node.dxOld + node.dx).pow(2) + (node.dyOld + node.dy).pow(2))
            }
            val estimatedOptimalJitterTolerance = 0.05 * sqrt(graph.vertices().size.toDouble())
            val minJT = sqrt(estimatedOptimalJitterTolerance)
            val maxJT = 10
            var jt = jitterTolerance * max(
                minJT,
                min(
                    maxJT.toDouble(),
                    estimatedOptimalJitterTolerance * totalEffectiveTraction / graph.vertices().size.toDouble()
                        .pow(2)
                )
            )
            if (totalSwinging / totalEffectiveTraction > 2.0) {
                if (speedEfficiency > minSpeedEfficiency) speedEfficiency *= 0.5
                jt = max(jt, jitterTolerance)
            }
            val targetSpeed = jt * speedEfficiency * totalEffectiveTraction / totalSwinging
            if (totalSwinging > jt * totalEffectiveTraction) {
                if (speedEfficiency > minSpeedEfficiency) speedEfficiency *= 0.7
                else if (globalSpeed < 1000) speedEfficiency *= 1.3
            }
            val maxRise = 0.5
            globalSpeed += min(targetSpeed - globalSpeed, maxRise * globalSpeed)
            for (node in graph.vertices()) {
                val swinging =
                    (node.vertex.incidents + 1) * sqrt((node.dxOld - node.dx).pow(2) + (node.dyOld - node.dy).pow(2))
                var factor = 0.1 * globalSpeed / (1.0 + sqrt(globalSpeed * swinging))
                val df = sqrt(node.dx.pow(2) + node.dy.pow(2))
                factor = min(factor * df, 10.0) / df
                node.position = node.centerX + node.dx * factor to node.centerY + node.dy * factor
                node.dxOld = node.dx
                node.dyOld = node.dy
                node.dx = 0.0
                node.dy = 0.0
            }
        }
    }

    fun applyForceAtlas2(graph: GraphView) {

        val kek = Anim(graph)
        kek.start()
    }


    private fun computeDistance(v: VertexView, u: VertexView) = sqrt((v.centerX - u.centerX).pow(2) + (v.centerY - u.centerY).pow(2))

    private fun applyAttractionForce(edges: Collection<EdgeView>) {

        for (edge in edges) {

            val v = edge.first
            val u = edge.second
            val e = edge.edge.weight
            val xDist = v.centerX - u.centerX
            val yDist = v.centerY - u.centerY
            val dist = computeDistance(v, u) - v.radius - u.radius
            if (dist > 0) {
                val factor = -e / (v.vertex.incidents + 1)

                v.dx += xDist * factor
                v.dy += yDist * factor

                u.dx -= xDist * factor
                u.dy -= yDist * factor
            }
        }
    }

    private fun applyRepulsionForce(pairs: Collection<Pair<VertexView, VertexView>>) {
        //Make less code when this shit works
        for (pair in pairs) {

            val v = pair.first
            val u = pair.second
            val xDist = v.centerX - u.centerX
            val yDist = v.centerY - u.centerY
            val degVInced = v.vertex.incidents + 1
            val degUInced = u.vertex.incidents + 1
            val dist = computeDistance(v, u) - v.radius - u.radius

            if (dist > 0) {
                val factor = scaling * degVInced * degUInced / dist / dist

                v.dx += xDist * factor
                v.dy += yDist * factor

                u.dx -= xDist * factor
                u.dy -= yDist * factor
            } else if (dist < 0) {
                val factor = antiCollisionCoeff * degVInced * degUInced

                v.dx += xDist * factor
                v.dy += yDist * factor

                u.dx -= xDist * factor
                u.dy -= yDist * factor
            }
        }

    }

    private fun applyGravity(nodes: Collection<VertexView>) {

        for (v in nodes) {
            val center = Point2D(primaryStage.width / 2, primaryStage.height / 2)
            val xDist = v.centerX - center.x
            val yDist = v.centerY - center.y
            val dist = sqrt(xDist.pow(2) + yDist.pow(2)) - v.radius
            if (dist > 0) {
                val factor = (v.vertex.incidents + 1) * gravity
                v.dx -= xDist * factor
                v.dy -= yDist * factor
            }
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