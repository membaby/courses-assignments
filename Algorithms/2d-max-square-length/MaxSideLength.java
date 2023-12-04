import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.lang.Math;

public class MaxSideLength {
    
    private static class Point {
        public long x;
        public long y;

        Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public static long getMinSquareLength(Point p1, Point p2) {
            return (long) Math.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
        }
    }

    public long solve(String inputFile) {
        File input_file = new File(inputFile);
        Point[] points = readPoints(input_file);
        Arrays.sort(points, (p1, p2) -> Long.compare(p1.x, p2.x));
        return findMaxSides(points, 0, points.length);
    }

    private long findMaxSides(Point[] points, int start, int end) {
        if (end - start <= 3) {
            return getMinDistance(points, start, end);
        }

        int mid = start + (end - start) / 2;
        long leftSides = findMaxSides(points, start, mid);
        long rightSides = findMaxSides(points, mid, end);
        long minSides = Math.min(leftSides, rightSides);

        // // Combine
        Point[] strip = new Point[end - start + 1];
        int i = 0;
        for (int j = start; j < end; j++) {
            if (Math.abs(points[j].x - points[mid].x) < minSides) {
                strip[i] = points[j];
                i++;
            }
        }
        
        return Math.min(minSides, getStripClosest(strip, i, minSides));
    }

    public static long getMinDistance(Point[] points, int start, int end) {
        long minDistance = Long.MAX_VALUE;
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                long distance = Point.getMinSquareLength(points[i], points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        return minDistance;
    }

    public static long getStripClosest(Point[] strip, int size, long d) {
        long min_distance = d;
        Arrays.sort(strip, 0, size, (p1, p2) -> Long.compare(p1.y, p2.y));
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size && (strip[j].y - strip[i].y) < min_distance; j++) {
                long distance = Point.getMinSquareLength(strip[i], strip[j]);
                if (distance < min_distance) {
                    min_distance = distance;
                }
            }
        }
        return min_distance;
    }
 
    private Point[] readPoints(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int n = scanner.nextInt();
            Point[] points = new Point[n];
            for (int i=0; i<n; i++){
                long x = scanner.nextLong();
                long y = scanner.nextLong();
                points[i] = new Point(x, y);
            }
            return points;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Point[0];
        }
    }

    public static void main(String[] args) {
        MaxSideLength maxSideLength = new MaxSideLength();
        System.out.println(maxSideLength.solve("input.txt"));
    }
}
