import matplotlib.pyplot as plt
import matplotlib.patches as patches

# Define the square size
square_size = int(input("Enter the square size: "))
# Define the points
print("Enter the points separated by a space: (ENTER to finish)")
points = []
while True:
    user_input = input()
    if user_input == '':
        break
    else:
        points.append(tuple(map(int, user_input.split())))

# Setup the plot
fig, ax = plt.subplots()

# Plot each point and draw a square around it
for point in points:
    # Plot the point
    ax.plot(point[0], point[1], 'ro')

    # Calculate the bottom left corner of the square
    bottom_left_corner = (point[0] - square_size/2, point[1] - square_size/2)

    # Create a square patch
    square = patches.Rectangle(bottom_left_corner, square_size, square_size, fill=False)

    # Add the square patch to the plot
    ax.add_patch(square)

# Set equal scaling by changing the axis limits
ax.set_aspect('equal', adjustable='box')

# Set the axis labels
plt.xlabel('X coordinate')
plt.ylabel('Y coordinate')

# WRITE SIZE OF SQUARE
# Set the title
plt.title(f'Square size: {square_size}')

# Set a grid
plt.grid(True)

# Display the plot
plt.show()
