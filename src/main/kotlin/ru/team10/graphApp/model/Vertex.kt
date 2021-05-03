package ru.team10.graphApp.model

data class Vertex(val id: String) {

    var incidents = mutableSetOf<Vertex>()
}