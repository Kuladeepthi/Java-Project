import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Edge implements Comparable<Edge> {
    String origin, destination;
    double distance;

    public Edge(String origin, String destination, double distance) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.distance, other.distance);
    }
}

class KruskalAlgorithm {
    private Map<String, String> parent = new HashMap<>();

    public List<Edge> kruskalMST(List<Edge> edges) {
        List<Edge> mst = new ArrayList<>();
        Collections.sort(edges);

        // Initialize each station's parent to itself
        for (Edge edge : edges) {
            parent.putIfAbsent(edge.origin, edge.origin);
            parent.putIfAbsent(edge.destination, edge.destination);
        }

        for (Edge edge : edges) {
            String rootOrigin = find(edge.origin);
            String rootDest = find(edge.destination);

            // Only add edge if it doesn't create a cycle
            if (!rootOrigin.equals(rootDest)) {
                mst.add(edge);
                parent.put(rootOrigin, rootDest); // Union operation
            }
        }
        return mst;
    }

    private String find(String station) {
        if (!station.equals(parent.get(station))) {
            parent.put(station, find(parent.get(station)));
        }
        return parent.get(station);
    }
}

public class ShortestPathKruskal {
    public static void main(String[] args) {
        String csvFile = "C:\\Users\\admin\\IdeaProjects\\project\\src\\Shinkansen_stations_inJapan.csv"; // Replace with the actual file path
        String line;
        String csvSplitBy = ",";
        List<Edge> edges = new ArrayList<>();

        // Step 1: Read CSV file and skip the header
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { // Skip the first row as header
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(csvSplitBy);
                String origin = data[0].trim();                  // First column as origin station
                String destination = data[3].trim();             // Fourth column as destination station
                double distance = Double.parseDouble(data[4].trim()); // Fifth column as distance
                edges.add(new Edge(origin, destination, distance));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // Step 2: Apply Kruskal's Algorithm
        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        List<Edge> mst = kruskal.kruskalMST(edges);

        // Step 3: Display the MST
        System.out.println("Kruskal's Minimum Spanning Tree:");
        double totalDistance = 0;
        System.out.printf("%-20s %-20s %-10s\n", "Origin", "Destination", "Distance");
        for (Edge edge : mst) {
            System.out.printf("%-20s %-20s %-10.2f\n", edge.origin, edge.destination, edge.distance);
            totalDistance += edge.distance;
        }
        System.out.println("Total Minimum Distance: " + totalDistance);
    }
}
