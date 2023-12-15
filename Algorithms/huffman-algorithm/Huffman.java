import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.w3c.dom.Node;

public class Huffman {

    public static class Node {
        private String data; // Data (character or chunk of characters)
        private int frequency; // Frequency of the data

        // Constructor for leaf nodes
        public Node(String data, int frequency) {
            this.data = data;
            this.frequency = frequency;
        }

        // Constructor for internal nodes
        public Node(Node left, Node right) {
            this.frequency = left.getFrequency() + right.getFrequency();
        }
    
        public int getFrequency() {
            return frequency;
        }

        public String getData() {
            return data;
        }
    }

    private static long getFileSize(String filePath) {
        File file = new File(filePath);
        return file.length();
    }

    private static double calculateReductionRate(long originalSize, long compressedSize) {
        return ((double) (originalSize - compressedSize) / originalSize) * 100;
    }


    private static Map<String, Integer> buildFreqneucnyMap(String filePath, int bytes){
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Map<String, Integer> frequencyMap = new HashMap<>();
            byte[] buffer = new byte[bytes];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                String currentChunk = new String(buffer, 0, bytesRead);
                frequencyMap.put(currentChunk, frequencyMap.getOrDefault(currentChunk, 0) + 1);
            }
            return frequencyMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Node buildHuffmanTree(Map<String, Integer> frequencyMap) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getFrequency));

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node mergedNode = new Node(left, right);
            priorityQueue.add(mergedNode);
        }

        return priorityQueue.poll();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Huffman <method> <file> <bytes>");
            System.out.println("• Method: c for Compression, d for Decompression");
            System.out.println("• File: absolute path to input file");
            System.out.println("• Bytes: number of bytes that will be considered together (required for compression only)");
        }

        String method = args[0];
        String filePath = args[1];

        long startTime = System.currentTimeMillis();
        long originalFileSize = getFileSize(filePath);

        if (method.equals("c")) {
            System.out.println("Compressing file: " + filePath);
            int bytes = Integer.parseInt(args[2]);
            Map<String, Integer> frequencyMap = buildFreqneucnyMap(filePath, bytes);
            Node huffmanTree = buildHuffmanTree(frequencyMap);
            // To be continued

        } else if (method.equals("d")) {
            // To be implemented
        }

        // Print Time and Ratio
        long endTime = System.currentTimeMillis();
        long compressedFileSize = getFileSize(filePath);
        System.out.println("Time: " + (endTime - startTime) + "ms");
        System.out.println("Ratio: " + calculateReductionRate(originalFileSize, compressedFileSize) + "%");
    }

}