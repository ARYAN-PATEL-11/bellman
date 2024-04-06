package com.spe.major.project.campuspathfinderbellmanford.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spe.major.project.campuspathfinderbellmanford.model.ShortestPathResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShortestPathService {

    private static final Map<String, List<Edge>> graph = new HashMap<>();

    static {
        String graphData = "{\n" +
                "  \"p1\": [{\"destination\": \"p2\", \"weight\": 48}, {\"destination\": \"MG\", \"weight\": 64}, {\"destination\": \"FIG\", \"weight\": 10}],\n" +
                "  \"p2\": [{\"destination\": \"p1\", \"weight\": 48}, {\"destination\": \"p3\", \"weight\": 36}],\n" +
                "  \"p3\": [{\"destination\": \"p2\", \"weight\": 36}, {\"destination\": \"p4\", \"weight\": 19}, {\"destination\": \"p19\", \"weight\": 55}],\n" +
                "  \"p4\": [{\"destination\": \"p3\", \"weight\": 19}, {\"destination\": \"p5\", \"weight\": 25}, {\"destination\": \"IC\", \"weight\": 29}],\n" +
                "  \"p5\": [{\"destination\": \"p4\", \"weight\": 25}, {\"destination\": \"p6\", \"weight\": 76}],\n" +
                "  \"p6\": [{\"destination\": \"p5\", \"weight\": 76}, {\"destination\": \"p7\", \"weight\": 46}],\n" +
                "  \"p7\": [{\"destination\": \"p6\", \"weight\": 46}, {\"destination\": \"p8\", \"weight\": 58}, {\"destination\": \"p12\", \"weight\": 38}, {\"destination\": \"p13\", \"weight\": 51}],\n" +
                "  \"p8\": [{\"destination\": \"p7\", \"weight\": 58}, {\"destination\": \"p9\", \"weight\": 91}],\n" +
                "  \"p9\": [{\"destination\": \"Exit\", \"weight\": 15}, {\"destination\": \"p8\", \"weight\": 91}],\n" +
                "  \"p10\": [{\"destination\": \"Exit\", \"weight\": 31}, {\"destination\": \"p11\", \"weight\": 39}],\n" +
                "  \"p11\": [{\"destination\": \"p13\", \"weight\": 18}, {\"destination\": \"p10\", \"weight\": 39}],\n" +
                "  \"p12\": [{\"destination\": \"p7\", \"weight\": 38}, {\"destination\": \"p13\", \"weight\": 13}, {\"destination\": \"Gym\", \"weight\": 5}],\n" +
                "  \"p13\": [{\"destination\": \"p7\", \"weight\": 51}, {\"destination\": \"p11\", \"weight\": 18}, {\"destination\": \"p12\", \"weight\": 13}, {\"destination\": \"p14\", \"weight\": 70}, {\"destination\": \"TC\", \"weight\": 49}],\n" +
                "  \"p14\": [{\"destination\": \"p13\", \"weight\": 70}, {\"destination\": \"IC\", \"weight\": 75}, {\"destination\": \"Ram\", \"weight\": 45}, {\"destination\": \"Recep\", \"weight\": 25}, {\"destination\": \"TC\", \"weight\": 21}],\n" +
                "  \"p15\": [{\"destination\": \"Ram\", \"weight\": 52}, {\"destination\": \"p16\", \"weight\": 46}],\n" +
                "  \"p16\": [{\"destination\": \"p15\", \"weight\": 46}, {\"destination\": \"p17\", \"weight\": 90}],\n" +
                "  \"p17\": [{\"destination\": \"p16\", \"weight\": 90}, {\"destination\": \"p18\", \"weight\": 47}],\n" +
                "  \"p18\": [{\"destination\": \"p17\", \"weight\": 47}, {\"destination\": \"Ram\", \"weight\": 43}, {\"destination\": \"p19\", \"weight\": 67}, {\"destination\": \"MG\", \"weight\": 41}, {\"destination\": \"FG\", \"weight\": 17}],\n" +
                "  \"p19\": [{\"destination\": \"FIG\", \"weight\": 3}, {\"destination\": \"Recep\", \"weight\": 5}, {\"destination\": \"p18\", \"weight\": 67}, {\"destination\": \"p3\", \"weight\": 55}],\n" +
                "  \"MG\": [{\"destination\": \"FG\", \"weight\": 23}, {\"destination\": \"p1\", \"weight\": 64}, {\"destination\": \"p18\", \"weight\": 41}],\n" +
                "  \"IC\": [{\"destination\": \"p4\", \"weight\": 29}, {\"destination\": \"p14\", \"weight\": 75}],\n" +
                "  \"Recep\": [{\"destination\": \"p19\", \"weight\": 5}, {\"destination\": \"p14\", \"weight\": 25}],\n" +
                "  \"FIG\": [{\"destination\": \"p1\", \"weight\": 10}, {\"destination\": \"p19\", \"weight\": 3}],\n" +
                "  \"FG\": [{\"destination\": \"MG\", \"weight\": 23}, {\"destination\": \"p18\", \"weight\": 17}],\n" +
                "  \"Ram\": [{\"destination\": \"p14\", \"weight\": 45}, {\"destination\": \"p15\", \"weight\": 52}, {\"destination\": \"p18\", \"weight\": 43}],\n" +
                "  \"TC\": [{\"destination\": \"p13\", \"weight\": 49}, {\"destination\": \"p14\", \"weight\": 21}],\n" +
                "  \"Gym\": [{\"destination\": \"Mess\", \"weight\": 15}, {\"destination\": \"p12\", \"weight\": 5}],\n" +
                "  \"Mess\": [{\"destination\": \"Gym\", \"weight\": 15}],\n" +
                "  \"Exit\": [{\"destination\": \"p9\", \"weight\": 15}, {\"destination\": \"p10\", \"weight\": 31}]\n" +
                "}";


        Map<String, List<Map<String, Object>>> parsedGraph = parseGraphData(graphData);
        for (Map.Entry<String, List<Map<String, Object>>> entry : parsedGraph.entrySet()) {
            String vertex = entry.getKey();
            List<Edge> edges = new ArrayList<>();
            for (Map<String, Object> edgeData : entry.getValue()) {
                String destination = (String) edgeData.get("destination");
                int weight = (int) edgeData.get("weight");
                edges.add(new Edge(destination, weight));
            }
            graph.put(vertex, edges);
        }
    }

    public ShortestPathResult findShortestPath(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        for (String vertex : graph.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, null);
        }
        distances.put(start, 0);

        // Bellman-Ford algorithm
        for (int i = 0; i < graph.size() - 1; i++) {
            for (String u : graph.keySet()) {
                for (Edge edge : graph.get(u)) {
                    String v = edge.destination;
                    int weight = edge.weight;
                    if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {
                        distances.put(v, distances.get(u) + weight);
                        predecessors.put(v, u);
                    }
                }
            }
        }

        // Check for negative cycles
        for (int i = 0; i < graph.size() - 1; i++) {
            for (String u : graph.keySet()) {
                for (Edge edge : graph.get(u)) {
                    String v = edge.destination;
                    int weight = edge.weight;
                    if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {
                        // Negative cycle found
                        throw new RuntimeException("Negative cycle detected");
                    }
                }
            }
        }

        List<String> shortestPath = new ArrayList<>();
        String current = end;
        while (current != null) {
            shortestPath.add(current);
            current = predecessors.get(current);
        }
        Collections.reverse(shortestPath);

        return new ShortestPathResult(shortestPath);
    }

    private static Map<String, List<Map<String, Object>>> parseGraphData(String graphData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<Map<String, Object>>> parsedGraph = objectMapper.readValue(
                    graphData,
                    new TypeReference<Map<String, List<Map<String, Object>>>>() {}
            );
            return parsedGraph;
        } catch (JsonProcessingException e) {
            // Handle parsing exception
            e.printStackTrace();
            return null;
        }
    }


    private static class Edge {
        String destination;
        int weight;

        public Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }
}


