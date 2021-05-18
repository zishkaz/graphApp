package ru.team10.graphApp.model

class Graph {

    private val vertices = hashMapOf<String, Vertex>()
    private val edges = hashMapOf<Pair<Vertex, Vertex>, Edge>()

    fun vertices(): Collection<Vertex> = vertices.values

    fun edges(): Collection<Edge> = edges.values

    fun addVertex(v: Vertex) = vertices.put(v.id, v)

    fun addEdge(e: Edge) {

        if (!edges.containsKey(e.first to e.second) && !edges.containsKey(e.second to e.first)) {
            edges[Pair(e.first, e.second)] = e
            e.first.mass++
            e.second.mass++
        }
    }
}