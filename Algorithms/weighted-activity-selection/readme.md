# Weighted Activity Selection

### Problem Statement
In class, we showed how the activity selection problem can be solved by a greedy algorithm. Assume that we change the problem such that each activity ai has a weight wi, and the goal is to find a subset of mutually compatible activities with maximum total weight. Show that choosing the activity with the highest weight first will definitely not work. Then, provide a dynamic programming solution to the problem.

Note: The DP formulation in the greedy algorithms lecture can be used, but it's not the simplest one and there could be simpler approaches to formulate the problem, which can help you develop more efficient solutions.

___

### Pseudocode

```
function weightedActivitySelection(activities):
    sort activities by end times in ascending order
    n = length of activities
    dp = new array of size n + 1 initialized to 0
    prev = new array of size n + 1

    for i = 1 to n:
        j = binarySearch(activities, i)
        prev[i] = j
        dp[i] = maximum of (activities[i - 1].weight + dp[prev[i] + 1]) and dp[i - 1]

    return dp[n]
```

### Recursion Formula
```
dp[i] = maximum of (activities[i - 1].weight + dp[prev[i] + 1]) and dp[i - 1]
```

### Solution Analysis
Binary search helps optimize the part of the algorithm where the algorithm searches for the latest non-overlapping activity. Instead of using a linear search, you can use binary search to find this activity more efficiently. This improves the time complexity of that part from `O(n)` to `O(log n)`.

The overall time complexity is `O(n log n)` due to the sorting step dominating the complexity analysis. The rest of the operations contribute to a linear term, which is less significant in big-O notation.

### Usage Command
`java -jar activity_<id>.jar absolute_path_to_input_file`