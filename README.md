# Graph & Network visualization

## Layout

Layout algorithms give the shape to the graph. 
This app provides [ForceAtlas2](https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0098679) layout algorithms. 
The Layout tab allows user to change layout settings while running.

**Settings:**
- Scaling
- Gravity 
- Jitter tolerance
- Barnes Hut optimization

## Community detection
Community detection, also called graph partition,
helps us to reveal the hidden relations among the nodes in the network.
So where the goal is to find groups of nodes 
that are, in some sense, more similar to each other than to the other nodes.
This app provides Leiden algorithm.

**Settings:**
- Resolution - resolution parameter of the quality function
## Centrality

Centrality measures calculate the importance 
of any given node in a network.
This app provides Harmonic centrality (variant of closeness centrality).

This measure:
- scores each node based on their ‘closeness’ to
all other nodes in the network.

- calculates the shortest paths between all nodes,
then assigns each node a score based on its sum of the shortest paths.

- finds the individuals who are best placed to influence
the entire network most quickly.

## Features

- Saving results of Centrality and Community detection analyzes to TXT file
- Showing the main information (ID, Centrality rank, Community ID) by pressing the node
- Saving configurations(such as settings, radius, colour values)


## Import / Export
- JSON
- SQLite
- Neo4j
