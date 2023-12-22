import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.RandomAccessFile;

public class Huffman {

    private static final int DEFAULT_BUFFER_SIZE = 16384;

    public static class Node {
        private ByteArray data;
        private int frequency; // Frequency of the data
        private Node left;
        private Node right;

        // Constructor for leaf nodes
        public Node(ByteArray data, int frequency) {
            this.data = data;
            this.frequency = frequency;
        }

        // Constructor for internal nodes
        public Node(Node left, Node right) {
            this.frequency = left.getFrequency() + right.getFrequency();
            this.left = left;
            this.right = right;
        }
        
        // Methods
        public int getFrequency() {return frequency;}
        public ByteArray getData() {return data;}
        public Node getLeft() {return left;}
        public Node getRight() {return right;}
        public boolean isLeaf() {return data != null;}
    }

    public static class ByteArray {
        private byte[] data;

        public ByteArray(byte[] data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ByteArray byteArray = (ByteArray) obj;
            return Arrays.equals(data, byteArray.data);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(data);
        }

        @Override
        public String toString() {
            return new String(data);
        }

        public byte[] getData() {return data;}
    }

    private static long getFileSize(String filePath) {
        File file = new File(filePath);
        return file.length();
    }

    private static double calculateReductionRate(long originalSize, long compressedSize) {
        return ((double) (originalSize - compressedSize) / originalSize) * 100;
    }

    private static Map<ByteArray, Integer> buildFrequencyMap(String filePath, int bytes){
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            Map<ByteArray, Integer> frequencyMap = new HashMap<>();

            // Add Terminal
            // byte[] terminator = "SEMO".getBytes();
            // frequencyMap.put(new ByteArray(terminator), 1);

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i=0; i<bytesRead; i+=bytes) {
                    ByteArray chunk = new ByteArray(Arrays.copyOfRange(buffer, i, i+bytes));
                    frequencyMap.put(chunk, frequencyMap.getOrDefault(chunk, 0) + 1);
                }
            }

            return frequencyMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Node buildHuffmanTree(Map<ByteArray, Integer> frequencyMap) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getFrequency));
        for (Map.Entry<ByteArray, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new Node(left, right));
        }
        return priorityQueue.poll();
    }

    private static void getHuffmanCodes(Node node, String code, Map<ByteArray, String> codes) {
        if (node.isLeaf()) {
            // System.out.println(node.getData() + " " + code);
            codes.put(new ByteArray(node.getData().getData()), code);
            return;
        }
    
        getHuffmanCodes(node.getLeft(), code + "0", codes);
        getHuffmanCodes(node.getRight(), code + "1", codes);
    }

    private static void compressFile(String filePath, Map<ByteArray, String> codes, String outputFilePath, int bytes, int originalFileSize) {
        try (FileInputStream inputStream = new FileInputStream(filePath);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFilePath))){

            // HEADERS
            outputStream.write(bytes);                                                                  // Write number of bytes
            outputStream.write(ByteBuffer.allocate(4).putInt(originalFileSize).array());       // Write original file size
            outputStream.write(ByteBuffer.allocate(4).putInt(codes.size()).array());           // Write number of codes
            writeCodes(outputStream, codes);                                                            // Write codes

            // System.out.println("\n• COMPRESSION:");
            // System.out.println("Number of bytes: " + bytes);
            // System.out.println("Original file size: " + originalFileSize);
            // System.out.println("Number of codes: " + codes.size());

            // System.out.println(codes);

            // CONTENT
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;
            StringBuilder bitString = new StringBuilder();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i += bytes) {
                    ByteArray chunk = new ByteArray(Arrays.copyOfRange(buffer, i, i + bytes));
                    String code = codes.get(chunk);
                    bitString.append(code);
                }
            }

            // Write the bit string to the output stream in groups of 8 bits
            for (int i = 0; i < bitString.length(); i += 8) {
                String eightBits = bitString.substring(i, Math.min(i + 8, bitString.length()));
                byte byteValue = (byte) Integer.parseInt(eightBits, 2);
                outputStream.write(byteValue);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decompressFile(String filePath, String outputFilePath) {
        try (InputStream inputStream = new FileInputStream(filePath);
            BitInputStream bitInputStream = new BitInputStream(inputStream);
            //  DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(outputFilePath))){
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFilePath))){

            // HEADERS
            int bytes = inputStream.read();                                                 // Read number of bytes
            byte[] originalFileSizeBytes = new byte[4];                                     // Read original file size
            inputStream.read(originalFileSizeBytes);
            int originalFileSize = ByteBuffer.wrap(originalFileSizeBytes).getInt();
            byte[] numberOfCodesBytes = new byte[4];                                        // Read number of codes
            inputStream.read(numberOfCodesBytes);
            int numberOfCodes = ByteBuffer.wrap(numberOfCodesBytes).getInt();
            Map<String, ByteArray> codes = readCodes(inputStream, numberOfCodes);           // Read codes
            
            // System.out.println("\n• DECOMPRESSION:");
            // System.out.println("Number of bytes: " + bytes);
            // System.out.println("Original file size: " + originalFileSize);
            // System.out.println("Number of codes: " + numberOfCodes);    
                
            // CONTENT
            
            StringBuilder currentBits = new StringBuilder();
            int totalBitsRead = 0;
            
            // Current file size
            
            long currentFileSize = 0;
            while (true) {
                int bit = bitInputStream.readBit();
                if (bit != -1) {
                    currentBits.append(bit);
                    totalBitsRead += 1;
                }
                
                if (totalBitsRead == 8 || bit == -1) {
                    int j = 0;
                    for (int i = 1; i <= currentBits.length(); i += 1) {
                        String bitString = currentBits.substring(j, i);
                        ByteArray decodedByte = codes.get(bitString);
                        if (decodedByte != null && currentFileSize < originalFileSize) {
                            outputStream.write(decodedByte.getData());
                            outputStream.flush();
                            currentFileSize = getFileSize(outputFilePath);
                            j = i;
                        }
                    }
                    // Remove substring that was already decoded
                    currentBits = new StringBuilder(currentBits.substring(j, currentBits.length()));
                    totalBitsRead = 0;
                }
                
                if (bit == -1) {
                    break; // End of stream
                }
                                        
                }
                                        
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    private static class BitInputStream extends FilterInputStream {
        private int currentByte;
        private int numBitsRemaining;

        public BitInputStream(InputStream in) {
            super(in);
            this.currentByte = 0;
            this.numBitsRemaining = 0;
        }

        public int readBit() throws IOException {
            if (currentByte == -1) {
                return -1; // End of stream
            }

            if (numBitsRemaining == 0) {
                currentByte = in.read();
                if (currentByte == -1) {
                    return -1; // End of stream
                }
                numBitsRemaining = 8;
            }

            int bit = (currentByte >> (numBitsRemaining - 1)) & 1;
            numBitsRemaining--;

            return bit;
        }
    }


    private static void writeCodes(OutputStream outputStream, Map<ByteArray, String> codes) throws IOException {
        for (Map.Entry<ByteArray, String> entry : codes.entrySet()) {
            writeByteArray(outputStream, entry.getKey().getData());
            writeString(outputStream, entry.getValue());
        }
    }

    private static Map<String, ByteArray> readCodes(InputStream inputStream, int numberOfCodes) throws IOException {
        Map<String, ByteArray> codes = new HashMap<>();

        for (int i = 0; i < numberOfCodes; i++) {
            byte[] byteArrayData = readByteArray(inputStream);
            ByteArray byteArray = new ByteArray(byteArrayData);
            String code = readString(inputStream);
            codes.put(code, byteArray);
        }

        return codes;
    }

    private static void writeByteArray(OutputStream outputStream, byte[] array) throws IOException {
        outputStream.write(array.length);
        outputStream.write(array);
    }

    private static byte[] readByteArray(InputStream inputStream) throws IOException {
        int length = inputStream.read();
        byte[] data = new byte[length];
        inputStream.read(data);
        // Convert to unsigned
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] & 0xFF);
        }
        return data;
    }

    private static void writeString(OutputStream outputStream, String str) throws IOException {
        byte[] strBytes = str.getBytes();
        outputStream.write(strBytes.length);
        outputStream.write(strBytes);
    }

    private static String readString(InputStream inputStream) throws IOException {
        int length = inputStream.read();
        byte[] strBytes = new byte[length];
        inputStream.read(strBytes);
        return new String(strBytes);
    }

    public static void main(String[] args) {
        // I acknowledge that I am aware of the academic integrity guidelines of this course, and that I worked on this assignment independently without any unauthorized help
        // Mamhoud Tarek Mahmoud Embaby - 20011800

        if (args.length < 2) {
            System.out.println("Usage: java Huffman <method> <file> <bytes>");
            System.out.println("• Method: c for Compression, d for Decompression");
            System.out.println("• File: absolute path to input file");
            System.out.println("• Bytes: number of bytes that will be considered together (required for compression only)");
            return;
        }

        
        String method = args[0];
        String file_in = args[1];
        
        long startTime = System.currentTimeMillis();
        long originalFileSize = getFileSize(file_in);
        String file_out = "";

        if (method.equals("c")) {
            System.out.println("Compressing file: " + file_in + "...");
            int bytes = Integer.parseInt(args[2]);
            Map<ByteArray, Integer> frequencyMap = buildFrequencyMap(file_in, bytes);
            if (frequencyMap == null) return;
            Node huffmanRoot = buildHuffmanTree(frequencyMap);

            Map<ByteArray, String> codes = new HashMap<>();
            getHuffmanCodes(huffmanRoot, "", codes);

            String directory = file_in.substring(0, file_in.lastIndexOf(File.separator));
            file_out = directory + File.separator + "20011800." + bytes + "." + file_in.substring(file_in.lastIndexOf(File.separator) + 1) + ".hc";

            compressFile(file_in, codes, file_out, bytes, (int) originalFileSize);
            System.out.println("Compressed file successfully! (" + file_out + ")");            
        } else if (method.equals("d")) {
            System.out.println("Decompressing file: " + file_in + "...");
            String directory = file_in.substring(0, file_in.lastIndexOf(File.separator));
            file_out = directory + File.separator + "extracted." + file_in.substring(file_in.lastIndexOf(File.separator) + 1).replace(".hc", "");
            decompressFile(file_in, file_out);
            System.out.println("Decompressed file successfully! (" + file_out + ")");
        }

        long out_filesize = getFileSize(file_out);

        // Print Time and Ratio
        long endTime = System.currentTimeMillis();
        System.out.println("\n• ANALYSIS:");
        System.out.println("Time: " + (endTime - startTime) + "ms");
        System.out.println("Ratio: " + calculateReductionRate(originalFileSize, out_filesize) + "%");
    }
}