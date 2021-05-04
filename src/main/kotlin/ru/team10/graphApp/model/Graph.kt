package ru.team10.graphApp.model

class Graph {

    private val vertices = hashMapOf<String, Vertex>()
    private val edges = hashMapOf<Pair<Vertex, Vertex>, Edge>()

    fun vertices(): Collection<Vertex> = vertices.values

    fun edges(): Collection<Edge> = edges.values

    fun addVertex(v: Vertex) = vertices.put(v.id, v)

    fun addEdge(e: Edge) = edges.put(Pair(e.first, e.second), e)
}