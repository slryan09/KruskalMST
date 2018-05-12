package kruskal_mst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Uses Kruskal's Algorithm to build a Minimum Spanning Tree from an input of
 * graph edges
 * 
 * @author Samantha Ryan
 *
 */
public class KruskalMST {

	/** Heap of edges */
	static Edge[] heap = new Edge[5000];
	/** Up Tree with array implementation */
	static int[] upTree;
	/** minimum spanning tree */
	static Edge[] mst;
	/** keep track of the number of vertices in the graph */
	static int vertexCount = 0;
	/** keep track of the number of edges in the graph */
	static int edgeCount = 0;
	/** Adjacency List made of Edges */
	static Edge[] adjacencyList = new Edge[1000];
	/** Output file */
	public static PrintStream output;

	public static void main(String[] args) throws FileNotFoundException {
		// Get the file names for the input and output file
		Scanner in = new Scanner(System.in);
		System.out.print("Input File: ");
		String inputFile = in.next();
		System.out.print("Output File: ");
		String outputFile = in.next();
		// set the output printstream to the output file
		output = new PrintStream(new File(outputFile));

		// Read the lines from the input and add the edges to the edge record
		Scanner fileScan = new Scanner(new File(inputFile));
		while (fileScan.hasNextLine()) {
			int v1 = fileScan.nextInt();
			if (v1 == -1) {
				break;
			}
			int v2 = fileScan.nextInt();

			double weight = fileScan.nextDouble();

			if (v1 > vertexCount) {
				vertexCount = v1;
			}
			if (v2 > vertexCount) {
				vertexCount = v2;
			}
			Edge newest = new Edge(Math.min(v1, v2), Math.max(v1, v2), weight, null);

			addToAdjacencyList(newest);
			heapInsert(newest);
		}
		in.close();
		fileScan.close();

		

		// Build the Heap Structure based on the input file
		buildHeap();
		// run kruskal's algorithm on the heap
		kruskalsAlgorithm();
		// build the MST based on the kruskal's algorithm
		buildMST();
		// build the adjacency list
		buildAdjacencyList();
	}

	// -----------------------------
	// ------KRUSKAL'S ALGORITHM ---
	// -----------------------------
	private static void kruskalsAlgorithm() {
		mst = new Edge[vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			mst[i] = new Edge(0, 0, 0.0, null);
		}
		uptree();
		int count = 0; // Counts vertices added
		while (count < vertexCount) {
			Edge min = deleteMin();
			if (find(min.getVertexOne()) != find(min.getVertexTwo())) {
				insertMST(min);
				union(find(min.getVertexOne()), find(min.getVertexTwo()));
				count++;
			}
		}

	}

	// -----------------------------
	// ----------- MST -------------
	// -----------------------------

	private static void insertMST(Edge min) {
		Edge p = mst[min.getVertexOne()].next;
		if (p == null) { // Empty list
			mst[min.getVertexOne()].next = min;
			// Front of list
		} else if (min.getVertexTwo() < p.getVertexTwo()) {
			min.next = p;
			mst[min.getVertexOne()].next = min;
		} else {
			Edge q = p;
			p = p.next;
			while (p != null) {
				// Middle of list
				if (min.getVertexTwo() < p.getVertexTwo()) {
					min.next = p;
					q.next = min;
					break;
				}
				q = p;
				p = p.next;
			}
			// End of list
			if (p == null) {
				q.next = min;
			}
		}
	}

	public static void buildMST() {
		String s = "";
		for (int i = 0; i < vertexCount; i++) {
			Edge p = mst[i].next;
			while (p != null) {
				s = s + String.format("%4d %4d\n", p.getVertexOne(), p.getVertexTwo());
				p = p.next;
			}
		}

		output.println(s.substring(0, s.length() - 1));
	}

	// -----------------------------
	// ---------- HEAP -------------
	// -----------------------------
	public static void heapInsert(Edge e) {
		heap[edgeCount] = e;
		upHeap(edgeCount++);
	}

	public static void upHeap(int index) {
		if (index > 0) {
			int i2 = (index - 1) / 2;
			if (heap[i2].getWeight() > heap[index].getWeight()) {
				Edge temp = heap[index];
				heap[index] = heap[i2];
				heap[i2] = temp;
				upHeap(i2);
			}
		}
	}

	public static Edge deleteMin() {
		Edge min = heap[0];
		edgeCount--;
		heap[0] = heap[edgeCount];
		downHeap(0);
		return min;
	}

	public static void downHeap(int index) {
		int rank = 0;
		if ((2 * index + 2) < edgeCount) {
			if (heap[2 * index + 2].getWeight() <= heap[2 * index + 1].getWeight()) {
				rank = 2 * index + 2;
			} else {
				rank = 2 * index + 1;
			}
		} else if (2 * index + 1 < edgeCount) {
			rank = 2 * index + 1;
		}
		if ((rank > 0) && (heap[index].getWeight() > heap[rank].getWeight())) {
			Edge temp = heap[rank];
			heap[rank] = heap[index];
			heap[index] = temp;
			downHeap(rank);
		}
	}

	private static void buildHeap() {
		for (int i = 0; i < edgeCount; i++) {
			output.printf("%4d", heap[i].getVertexOne());
			output.printf(" %4d\n", heap[i].getVertexTwo());
		}
	}

	// -----------------------------
	// ------- UP TREE -------------
	// -----------------------------

	public static void uptree() {
		upTree = new int[vertexCount + 1];
		for (int i = 0; i < vertexCount + 1; i++) {
			upTree[i] = -1;
		}
	}

	/**
	 * Take two components and join them together
	 * 
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static int union(int set1, int set2) {
		if (upTree[set1] <= upTree[set2]) {
			upTree[set1] = upTree[set1] + upTree[set2];
			upTree[set2] = set1;
			return set1;
		} else {
			upTree[set2] = upTree[set2] + upTree[set1];
			upTree[set1] = set2;
			return set2;
		}
	}

	/**
	 * Find the root of the tree containing the element node
	 * 
	 * @param node
	 * @return
	 */
	public static int find(int node) {
		while (upTree[node] >= 0) {
			node = upTree[node];
		}
		return node;
	}

	/**
	 * Determines which vertices need to be updated with the edge
	 * 
	 * @param e
	 */
	public static void addToAdjacencyList(Edge e) {
		int v1 = e.getVertexOne();
		int v2 = e.getVertexTwo();
		if (adjacencyList[v1] == null) {
			adjacencyList[v1] = new Edge(0, 0, 0.0, null);
		}
		if (adjacencyList[v2] == null) {
			adjacencyList[v2] = new Edge(0, 0, 0.0, null);
		}
		adjacencyListInsert(new Edge(v1, v2, e.getWeight(), e.next), v1, v2);
		adjacencyListInsert(new Edge(v1, v2, e.getWeight(), e.next), v2, v1);

	}

	/**
	 * Inserts the edge data into the vertex's adjacency list
	 * 
	 * @param e
	 * @param index
	 * @param v
	 */
	public static void adjacencyListInsert(Edge e, int index, int v) {
		Edge p = adjacencyList[index].next;
		if (p == null) {
			adjacencyList[index].next = e;
		} else if (v < compareTo(p, index)) {
			e.next = p;
			adjacencyList[index].next = e;
		} else {
			Edge q = p;
			p = p.next;
			while (p != null) {
				if (v < compareTo(p, index)) {
					e.next = p;
					q.next = e;
					break;
				}
				q = p;
				p = p.next;
			}
			if (p == null) {
				q.next = e;
			}
		}
	}

	/**
	 * Compares the edge's verticies to the value in the index
	 * 
	 * @param e
	 * @param index
	 * @return
	 */
	public static int compareTo(Edge e, int index) {
		if (index == e.getVertexOne()) {
			return e.getVertexTwo();
		} else {
			return e.getVertexOne();
		}
	}

	// --------------------------
	// ----- Adjacency List -----
	// --------------------------

	public static void buildAdjacencyList() {
		for (int i = 0; i < vertexCount + 1; i++) {
			String s = "";
			Edge p = adjacencyList[i].next;
			while (p != null) {
				s = s + String.format("%4d ", compareTo(p, i));
				p = p.next;
			}
			output.println(s.substring(0, s.length() - 1));
		}
	}

}
