import matplotlib as plt
import csv
from rich import print

csv_file = 'performance_data.csv'
reader = csv.reader(open(csv_file, 'r'), delimiter=',')

data = {}

for line in reader:
    line[1] = int(line[1])
    line[2] = int(line[2])
    if line[0] not in data:
        data[line[0]] = {}
    if not line[2] in data[line[0]]:
        data[line[0]][line[2]] = []
    data[line[0]][line[2]].append(line[1])

for algorithm in data:
    for n in data[algorithm]:
        data[algorithm][n] = sum(data[algorithm][n]) / len(data[algorithm][n])

print(data)

import matplotlib.pyplot as plt

# Set up the plot
plt.figure(figsize=(10, 5))

# Plot each set of points
for algorithm, points in data.items():
    sorted_points = sorted(points.items())  # Sort the points by the x-axis (keys)
    x, y = zip(*sorted_points)  # Unzip into x and y coordinates
    plt.plot(x, y, marker='o', label=algorithm)

# Set the scale of the x and y axes to logarithmic if required
# plt.xscale('log')
# plt.yscale('log')

# Title and labels
plt.title('Algorithm Performance Comparison')
plt.xlabel('Input Size')
plt.ylabel('Time')

# Add a legend to explain which lines correspond to which algorithm
plt.legend()

# Show grid
plt.grid(True)

# Display the plot
plt.show()
