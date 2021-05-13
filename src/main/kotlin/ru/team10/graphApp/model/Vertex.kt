package ru.team10.graphApp.model

data class Vertex(val id: String, var centralityRang: Double, var communityID: Int): Comparable<Vertex> {

    var shortestDist: Double = Double.MAX_VALUE
    var mass = 1

    override fun compareTo(other: Vertex): Int {
        if (shortestDist == other.shortestDist) return id.compareTo(other.id)
        return shortestDist.compareTo(other.shortestDist)
    }
}