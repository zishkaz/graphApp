package ru.team10.graphApp.model

data class Vertex(val id: String): Comparable<Vertex> {
    var dist: Int = Int.MAX_VALUE
    var mass = 1
    override fun compareTo(other: Vertex): Int {
        if (dist == other.dist) return id.compareTo(other.id)
        return dist.compareTo(other.dist)
    }

}