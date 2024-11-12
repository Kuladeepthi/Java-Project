import java.util.*;

class DijkstraAlgorithm {
    static class Edge {
        String destination;
        int weight;

        public Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class Graph {
        Map<String, List<Edge>> adjList = new HashMap<>();

        public void addEdge(String origin, String destination, int weight) {
            adjList.computeIfAbsent(origin, k -> new ArrayList<>()).add(new Edge(destination, weight));
            adjList.computeIfAbsent(destination, k -> new ArrayList<>()).add(new Edge(origin, weight)); // Assuming undirected graph
        }

        public Map<String, Integer> dijkstra(String start, Map<String, String> previous) {
            Map<String, Integer> distances = new HashMap<>();
            PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));

            for (String node : adjList.keySet()) {
                distances.put(node, Integer.MAX_VALUE);
            }
            distances.put(start, 0);
            pq.add(new Node(start, 0));

            while (!pq.isEmpty()) {
                Node currentNode = pq.poll();
                String current = currentNode.name;

                for (Edge edge : adjList.get(current)) {
                    int newDist = distances.get(current) + edge.weight;
                    if (newDist < distances.get(edge.destination)) {
                        distances.put(edge.destination, newDist);
                        previous.put(edge.destination, current);
                        pq.add(new Node(edge.destination, newDist));
                    }
                }
            }

            return distances;
        }

        public List<String> getPath(String start, String end, Map<String, String> previous) {
            LinkedList<String> path = new LinkedList<>();
            String current = end;

            while (current != null) {
                path.addFirst(current);
                current = previous.get(current);
            }

            if (path.getFirst().equals(start)) {
                return path;
            } else {
                return new ArrayList<>(); // No path exists
            }
        }
    }

    static class Node {
        String name;
        int distance;

        public Node(String name, int distance) {
            this.name = name;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        // Sample data (You will replace this with your CSV parsed data)
        graph.addEdge("Tokyo", "Shinagawa", 6);
        graph.addEdge("Shinagawa", "Shin-Yokohama", 10);
        graph.addEdge("Shinagawa", "Odawara", 200);
        graph.addEdge("Nagoya", "Kyoto", 170);

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter origin station: ");
        String start = sc.nextLine();

        System.out.print("Enter destination station: ");
        String destination = sc.nextLine();

        Map<String, String> previous = new HashMap<>();
        Map<String, Integer> distances = graph.dijkstra(start, previous);

        if (distances.get(destination) == Integer.MAX_VALUE) {
            System.out.println("No path found between " + start + " and " + destination);
        } else {
            List<String> path = graph.getPath(start, destination, previous);
            System.out.println("\nShortest path from " + start + " to " + destination + ": " + path);
            System.out.println("Total distance: " + distances.get(destination));
        }
    }
}
