import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;


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
        String filePath = "/Users/mac/Documents/Education/Educational-Projects/courses-assignments/Algorithms/weighted-activity-selection/input.txt";
        Activity[] activities = getActivities(filePath);
        int n = activities.length;
        Arrays.sort(activities, Comparator.comparingInt(a -> a.end));

        int[] dp = new int[n + 1];
        int[] prev = new int[n + 1];

        for (int i = 1; i <= n; i++) {

            // Binary Search O(nlogn)
            // int low = 0, high = i - 1;
            // while (low <= high) {
            //     int mid = (low + high) / 2;
            //     if (activities[mid].end <= activities[i - 1].start) {
            //         if (activities[mid + 1].end <= activities[i - 1].start) {
            //             low = mid + 1;
            //         } else {
            //             prev[i] = mid;
            //             break;
            //         }
            //     } else {
            //         high = mid - 1;
            //     }
            // }
            
            // Without Binary Search O(n^2)
            int j = i - 1;
            while (j >= 0 && activities[j].end > activities[i - 1].start) {
                j--;
            }
            prev[i] = j;


            dp[i] = Math.max(activities[i - 1].weight + dp[prev[i] + 1], dp[i - 1]);
        }
        
        System.out.println(dp[n]);
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
}