package kruskal_mst;



class Edge {

	
	private int vertex1;
	private int vertex2;
	private double weight;
	Edge next;

	
	public Edge(int v, int u, double w, Edge n) {
		vertex1 = v;
		vertex2 = u;
		weight = w;
		next = n;
	}
	
	public int getVertexOne() {
		return vertex1;
	}
	
	public int getVertexTwo() {
		return vertex2;
	}
	
	public double getWeight() {
		return weight;
	}

	/**
	 * Converts the edge to a string representation
	 */
	public String toString() {
		String s = "";
		return s + vertex1 + " " + vertex2;
	}

	/**
	 * Compares the weight values of the given node with this node
	 * 
	 * @param p
	 *            the node for comparison
	 * @return whether or not the nodes and values are equal
	 */
	public boolean equals(Edge p) {
		if ((vertex1 == p.vertex1) && (vertex2 == p.vertex2)) {
			return true;
		}
		return false;
	}

}