package ru.team10.graphApp.model

internal data class Vertex(val id: String, var centralityRang: Double = -1.0, var communityID: Int = -1) {

    var mass = 1
}