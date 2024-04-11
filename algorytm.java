import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {

    private Map<Integer, Map<Integer, Integer>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public void addEdge(int u, int v, int weight) {
        // Dodajemy krawędź między wierzchołkami u i v z wagą
        graph.computeIfAbsent(u, k -> new HashMap<>()).put(v, weight);
        graph.computeIfAbsent(v, k -> new HashMap<>()).put(u, weight); // Graf nieskierowany - dodajemy krawędź w obu kierunkach
    }

    public Map<Integer, Integer> getNeighbors(int u) {
        // Zwracamy sąsiadów wierzchołka u wraz z wagami
        return graph.getOrDefault(u, new HashMap<>());
    }

    public Integer getWeight(int u, int v) {
        // Zwracamy wagę krawędzi między wierzchołkami u i v
        if (graph.containsKey(u) && graph.get(u).containsKey(v)) {
            return graph.get(u).get(v);
        }
        return null;
    }

    public Map<Integer, Integer> getShortestPath(int source, int destination) {
        // Implementacja algorytmu Dijkstry

        // Mapa przechowująca dystanse od wierzchołka źródłowego do pozostałych
        Map<Integer, Integer> distances = new HashMap<>();

        // Mapa przechowująca poprzednie wierzchołki na najkrótszej ścieżce
        Map<Integer, Integer> predecessors = new HashMap<>();

        // Inicjalizacja dystansów i poprzedników
        for (int vertex : graph.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, -1);
        }
        distances.put(source, 0);

        // Priorytetowa kolejka przechowująca wierzchołki do odwiedzenia
        PriorityQueue<Integer> queue = new PriorityQueue<>(
            (a, b) -> distances.get(a) - distances.get(b)
        );

        // Dodanie wierzchołka źródłowego do kolejki
        queue.add(source);

        // Pętla relaksacyjna
        while (!queue.isEmpty()) {
            int u = queue.poll();

            // Przejście po wszystkich sąsiadach wierzchołka u
            for (Map.Entry<Integer, Integer> neighbor : graph.getNeighbors(u).entrySet()) {
                int v = neighbor.getKey();
                int weight = neighbor.getValue();

                // Relaksacja krawędzi (u, v)
                if (distances.get(v) > distances.get(u) + weight) {
                    distances.put(v, distances.get(u) + weight);
                    predecessors.put(v, u);
                    queue.add(v);
                }
            }
        }

        // Rekonstrukcja najkrótszej ścieżki
        List<Integer> path = new ArrayList<>();
        int current = destination;
        while (current != source) {
            path.add(current);
            current = predecessors.get(current);
        }
        path.add(source);
        Collections.reverse(path);

        // Zwrócenie mapy z dystansami i listą wierzchołków na najkrótszej ścieżce
        Map<Integer, Integer> result = new HashMap<>();
        result.put("distance", distances.get(destination));
        result.put("path", path);
        return result;
    }

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdge(1, 2, 5);
        g.addEdge(2, 3, 3);
        g.addEdge(3, 4, 7);
        g.addEdge(1, 4, 2);

        // Sprawdzamy sąsiadów wierzchołka 1 wraz z wagami
        System.out.println("Sąsiedzi wierzchołka 1:");
        System.out