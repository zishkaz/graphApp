package ru.team10.graphApp.controller.algorithms

import javafx.animation.AnimationTimer
import javafx.geometry.Point2D
import ru.team10.graphApp.view.EdgeView
import ru.team10.graphApp.view.GraphView
import ru.team10.graphApp.view.VertexView
import tornadofx.Controller
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.pow
import kotlin.math.sqrt

private const val antiCollisionCoeff = 100.0
internal var scaling = 1000.0
internal var gravity = 0.1
internal var jitterTolerance = 0.1
private const val minSpeedEfficiency = 0.05
private const val speedEfficiencyDefault = 1.0
private const val globalSpeedDefault = 1.0
private const val barnesHutTheta = 1.2
var isBarnesHutActive = false

class Layout: Controller() {

    private val dx = hashMapOf<VertexView, Double>()
    private val dy = hashMapOf<VertexView, Double>()
    private val dxOld = hashMapOf<VertexView, Double>()
    private val dyOld = hashMapOf<VertexView, Double>()

    inner class Anim(private val graph: GraphView): AnimationTimer() {

        override fun handle(now: Long) {

            var speedEfficiency = speedEfficiencyDefault
            var globalSpeed = globalSpeedDefault
            if (isBarnesHutActive) {
                val rootRegion = Region(graph.vertices())
                rootRegion.buildSubregions()
                for (node in graph.vertices()) applyBarnesHutRepulsionForce(node, rootRegion)
            } else applyRepulsionForce(getAllNodePairs(graph.vertices()))
            applyAttractionForce(graph.edges())
            applyGravity(graph.vertices())
            var totalSwinging = 0.0
            var totalEffectiveTraction = 0.0
            for (node in graph.vertices()) {
                val swinging = sqrt((dxOld[node]!! - dx[node]!!).pow(2) + (dyOld[node]!! - dy[node]!!).pow(2))
                totalSwinging += node.vertex.mass * swinging
                totalEffectiveTraction += node.vertex.mass * 0.5 * sqrt((dxOld[node]!! + dx[node]!!).pow(2) + (dyOld[node]!! + dy[node]!!).pow(2))
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
                    node.vertex.mass * sqrt((dxOld[node]!! - dx[node]!!).pow(2) + (dyOld[node]!! - dy[node]!!).pow(2))
                var factor = 0.1 * globalSpeed / (1.0 + sqrt(globalSpeed * swinging))
                val df = sqrt(dx[node]!!.pow(2) + dy[node]!!.pow(2))
                factor = min(factor * df, 10.0) / df
                node.position = node.centerX + dx[node]!! * factor to node.centerY + dy[node]!! * factor
                dxOld[node] = dx[node]!!
                dyOld[node] = dy[node]!!
                dx[node] = 0.0
                dy[node] = 0.0
            }
        }
    }

    private class Region(val nodes: Collection<VertexView>) {

        var mass = 0.0
        var massCenterX = 0.0
        var massCenterY = 0.0
        var size = 0.0
        val subregions = ArrayList<Region>()
        init {
            updateMassAndGeometry()
        }

        private fun updateMassAndGeometry() {

            if (nodes.size > 1) {
                mass = 0.0
                var massSumX = 0.0
                var massSumY = 0.0
                for (node in nodes) {
                    mass += node.vertex.mass
                    massSumX += node.centerX * node.vertex.mass
                    massSumY += node.centerY * node.vertex.mass

                }
                massCenterX = massSumX / mass
                massCenterY = massSumY / mass
                size = Double.MIN_VALUE
                for (node in nodes) {
                    val dist = sqrt((node.centerX - massCenterX).pow(2) + (node.centerY - massCenterY).pow(2))
                    size = max(size, 2 * dist)
                }
            }
        }

        fun buildSubregions() {

            if (nodes.size > 1) {
                val leftNodes = ArrayList<VertexView>()
                val rightNodes = ArrayList<VertexView>()
                for (node in nodes) {
                    if (node.centerX < massCenterX) leftNodes.add(node) else rightNodes.add(node)
                }
                val topLeftNodes = ArrayList<VertexView>()
                val bottomLeftNodes = ArrayList<VertexView>()
                for (node in leftNodes) {
                    if (node.centerY < massCenterY) bottomLeftNodes.add(node) else topLeftNodes.add(node)
                }
                val topRightNodes = ArrayList<VertexView>()
                val bottomRightNodes = ArrayList<VertexView>()
                for (node in rightNodes) {
                    if (node.centerY < massCenterY) bottomRightNodes.add(node) else topRightNodes.add(node)
                }
                buildSubregionsFromQuarters(bottomLeftNodes)
                buildSubregionsFromQuarters(topLeftNodes)
                buildSubregionsFromQuarters(bottomRightNodes)
                buildSubregionsFromQuarters(topRightNodes)
                for (subregion in subregions) subregion.buildSubregions()
            }
        }

        private fun buildSubregionsFromQuarters(vertices: ArrayList<VertexView>) {

            if (vertices.size > 0) {
                if (vertices.size < nodes.size) {
                    val subregion = Region(vertices)
                    subregions.add(subregion)
                } else {
                    for (node in vertices) {
                        val subregion = Region(arrayListOf(node))
                        subregions.add(subregion)
                    }
                }
            }
        }
    }

    fun applyForceAtlas2(graph: GraphView): Anim {

        graph.vertices().forEach {
            dx[it] = 0.0
            dy[it] = 0.0
            dxOld[it] = 0.0
            dyOld[it] = 0.0
        }
        return Anim(graph)
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
                val factor = -e / v.vertex.mass

                dx[v] = dx[v]!! + xDist * factor
                dy[v] = dy[v]!! + yDist * factor

                dx[u] = dx[u]!! - xDist * factor
                dy[u] = dy[u]!! - yDist * factor
            }
        }
    }

    private fun applyRepulsionForce(pairs: Collection<Pair<VertexView, VertexView>>) {
        for (pair in pairs) {
            val v = pair.first
            val u = pair.second
            val xDist = v.centerX - u.centerX
            val yDist = v.centerY - u.centerY
            val dist = computeDistance(v, u) - v.radius - u.radius
            
            val factor = if (dist > 0) scaling * v.vertex.mass * u.vertex.mass / dist.pow(2)
            else antiCollisionCoeff * v.vertex.mass * u.vertex.mass
            dx[v] = dx[v]!! + xDist * factor
            dy[v] = dy[v]!! + yDist * factor

            dx[u] = dx[u]!! - xDist * factor
            dy[u] = dy[u]!! - yDist * factor
        }
    }

    private fun applyBarnesHutRepulsionForce(node: VertexView, region: Region) {

        if (region.nodes.size < 2) {
            val regionNode = region.nodes.first()
            applyRepulsionForce(arrayListOf(node to regionNode))
        } else {
            val dist = sqrt((node.centerX - region.massCenterX).pow(2) + (node.centerY - region.massCenterY).pow(2))
            if (dist * barnesHutTheta > region.size) {
                applyRegionRepulsion(node, region)
            } else {
                for (subregion in region.subregions) {
                    applyBarnesHutRepulsionForce(node, subregion)
                }
            }
        }
    }

    private fun applyRegionRepulsion(node: VertexView, region: Region) {

        val xDist = node.centerX - region.massCenterX
        val yDist = node.centerY - region.massCenterY
        val dist = sqrt(xDist.pow(2) + yDist.pow(2))
        if (dist > 0) {
            val factor = scaling * node.vertex.mass * region.mass / dist.pow(2)
            dx[node] = dx[node]!! - xDist * factor
            dy[node] = dy[node]!! - yDist * factor
        } else if (dist < 0) {
            val factor = -antiCollisionCoeff * node.vertex.mass * region.mass / dist
            dx[node] = dx[node]!! - xDist * factor
            dy[node] = dy[node]!! - yDist * factor
        }
    }

    private fun applyGravity(nodes: Collection<VertexView>) {

        for (v in nodes) {
            val center = Point2D(primaryStage.width / 2, primaryStage.height / 2)
            val xDist = v.centerX - center.x
            val yDist = v.centerY - center.y
            val dist = sqrt(xDist.pow(2) + yDist.pow(2)) - v.radius
            if (dist > 0) {
                val factor = v.vertex.mass * gravity
                dx[v] = dx[v]!! -  xDist * factor
                dy[v] = dy[v]!! -  yDist * factor
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