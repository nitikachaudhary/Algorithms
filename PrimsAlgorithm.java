package com.project1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimsAlgorithm {

	public static void main(String[] args) {
		Map<Integer, List<Integer>> neighborsMap = new HashMap<>();
		Map<String, Integer> edgeWeightsMap = new HashMap<>();
		String inputFile = "input_.txt";
		String outputFile = "output.txt";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			int vertex1, vertex2;
			String line;
			String[] lineElements;
			String inputEdge;
			int inputEdgeWeight;
			line = reader.readLine();
			lineElements = line.split(" ");
			int noOfVertices = Integer.valueOf(lineElements[0]);
			int noOfEdges = Integer.valueOf(lineElements[1]);
			for (int i = 1; i <= noOfEdges; i++) {
				List<Integer> neighborsList = new ArrayList<>();
				neighborsMap.put(i, neighborsList);
			}
			while ((line = reader.readLine()) != null) {
				lineElements = line.split(" ");
				inputEdge = lineElements[0] + " " + lineElements[1];
				inputEdgeWeight = Integer.valueOf(lineElements[2]);
				vertex1 = Integer.valueOf(lineElements[0]);
				vertex2 = Integer.valueOf(lineElements[1]);
				neighborsMap.get(vertex1).add(vertex2);
				neighborsMap.get(vertex2).add(vertex1);
				edgeWeightsMap.put(inputEdge, inputEdgeWeight);
			}

			List<Integer> ResultSet = new ArrayList<>();
			List<String> minimalSpanningTree = new ArrayList<>();
			int totalMstWeight = 0;
			Heap graph = new Heap(noOfVertices);
			graph.insert(1, 0);
			int i;
			for (i = 2; i <= noOfVertices; i++) {
				graph.insert(i, Integer.MAX_VALUE);
			}
			String edge;
			int edgeWeight;
			Vertex neighborVertex;
			while (ResultSet.size() != noOfVertices) {
				Vertex minVertex = graph.extractMin();
				Integer minVertexNo = minVertex.vertexNo;
				List<Integer> neighbors = neighborsMap.get(minVertexNo);
				for (Integer neighbor : neighbors) {
					if (!ResultSet.contains(neighbor)) {
						edge = minVertexNo.toString() + " " + neighbor.toString();
						if (edgeWeightsMap.containsKey(edge))
							edgeWeight = edgeWeightsMap.get(edge);
						else {
							edge = neighbor.toString() + " " + minVertexNo.toString();
							edgeWeight = edgeWeightsMap.get(edge);
						}
						neighborVertex = graph.findVertex(neighbor);
						if (edgeWeight < neighborVertex.weight) {
							neighborVertex.pi = minVertexNo;
							graph.decreaseWeight(neighborVertex.position, edgeWeight);
						}
					}
				}
				Integer minVertex2 = minVertex.pi;
				String mstEdge = minVertex2.toString() + " " + minVertexNo.toString();
				ResultSet.add(minVertexNo);
				if (!mstEdge.equals("0 1")) {
					minimalSpanningTree.add(mstEdge);
					if (edgeWeightsMap.containsKey(mstEdge))
						totalMstWeight = totalMstWeight + edgeWeightsMap.get(mstEdge);
					else {
						mstEdge = minVertexNo.toString() + " " + minVertex2.toString();
						totalMstWeight = totalMstWeight + edgeWeightsMap.get(mstEdge);
					}
				}
			}
			writer.write(totalMstWeight + "\r\n");
			for (String outputEdge : minimalSpanningTree) {
				writer.write(outputEdge + "\r\n");
			}
			writer.flush();
			writer.close();
			System.out.println("Prim's algorithm executed successfully");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static class Vertex {
		int weight;
		int position;
		int vertexNo;
		int pi;

		public Vertex() {
	    // Default Constructor
		}

		public Vertex(int ver, int pos, int w) {
			this.weight = w;
			this.position = pos;
			this.vertexNo = ver;
		}

		public void eqauteVertex(Vertex ver) {
			this.weight = ver.weight;
			this.position = ver.position;
			this.vertexNo = ver.vertexNo;
			this.pi = ver.pi;
		}
	}

	public static class Heap {
		Vertex[] heap;
		int size = 0;

		public Heap(int size) {
			heap = new Vertex[size + 1];
		}

		public void insert(int v, int weight) {
			size = size + 1;
			Vertex vertex = new Vertex(v, size, weight);
			heap[size] = vertex;
			heapifyUp(size);
		}

		public void heapifyUp(int i) {
			Vertex temp = new Vertex();
			while (i > 1) {
				int j = Math.floorDiv(i, 2);
				if (heap[i].weight < heap[j].weight) {
					temp.eqauteVertex(heap[i]);
					heap[i].eqauteVertex(heap[j]);
					heap[j].eqauteVertex(temp);
					heap[i].position = i;
					heap[j].position = j;
					i = j;
				} else {
					break;
				}
			}

		}

		public Vertex extractMin() {
			Vertex minValue = new Vertex();
			minValue.eqauteVertex(heap[1]);
			heap[1].eqauteVertex(heap[size]);
			heap[1].position = 1;
			size = size - 1;
			if (size >= 1) {
				heapifyDown(1);
			}
			return minValue;
		}

		public void heapifyDown(int i) {
			int j;
			Vertex temp = new Vertex();
			while ((2 * i) <= size) {
				if (((2 * i) == size) || (heap[2 * i].weight <= heap[2 * i + 1].weight)) {
					j = 2 * i;
				} else {
					j = 2 * i + 1;
				}
				if (heap[j].weight < heap[i].weight) {
					temp.eqauteVertex(heap[j]);
					heap[j].eqauteVertex(heap[i]);
					heap[i].eqauteVertex(temp);
					heap[i].position = i;
					heap[j].position = j;
					i = j;
				} else {
					break;
				}
			}

		}

		public void decreaseWeight(int v, int w) {
			heap[v].weight = w;
			heapifyUp(heap[v].position);
		}

		public Vertex findVertex(int vertexNo) {
			for (int i = 1; i <= size; i++) {
				if (heap[i].vertexNo == vertexNo) {
					return heap[i];
				}
			}
			return null;
		}
	}
}

