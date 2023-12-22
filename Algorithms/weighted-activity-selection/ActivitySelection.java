import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.IOException;

public class ActivitySelection {
    
    static class Activity {
        int start;
        int end;
        int weight;
    
        public Activity(int start, int end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        // I acknowledge that I am aware of the academic integrity guidelines of this course, and that I worked on this assignment independently without any unauthorized help
        // Mamhoud Tarek Mahmoud Embaby - 20011800

        if (args.length < 1) {
            System.out.println("Please provide the input file path as an argument");
            return;
        }
        String filePath = args[0];
        Activity[] activities = getActivities(filePath);
        int n = activities.length;
        Arrays.sort(activities, Comparator.comparingInt(a -> a.end));

        int[] dp = new int[n + 1];
        int[] prev = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            int j = binarySearch(activities, i);
            prev[i] = j;
            dp[i] = Math.max(activities[i - 1].weight + dp[prev[i] + 1], dp[i - 1]);
        }
        
        String filePathOut = filePath.split("\\.")[0] + "_20011800.out";
        try {
            FileWriter writer = new FileWriter(filePathOut);
            writer.write(dp[n] + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Activity[] getActivities(String filePath) {
        Activity[] activities = null;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            int n = Integer.parseInt(scanner.nextLine().trim());
            activities = new Activity[n];

            for (int i=0; i<n; i++){
                String[] line = scanner.nextLine().split(" ");
                int start = Integer.parseInt(line[0]);
                int end = Integer.parseInt(line[1]);
                int weight = Integer.parseInt(line[2]);
                activities[i] = new Activity(start, end, weight);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return activities;
    }

    private static int binarySearch(Activity[] activities, int index) {
        int low = 0, high = index - 1;
        
        while (low <= high) {
            int mid = (low + high) / 2;

            if (activities[mid].end <= activities[index - 1].start) {
                if (activities[mid + 1].end <= activities[index - 1].start) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}