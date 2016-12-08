# TriangleCounter

##Overview
They say necessity is the mother of invention, and that certainly proved to be the case for this project. When I was playing Professor Layton one day, I encountered a challenging puzzle. A triangle was shown with lines passing through it. The goal was to answer how many triangles total were in the picture. This proved to be much tougher than I originally anticipated and only by using all of the available hints and some trial and error was I able to solve the puzzle. I was so enraged by the experience that I vowed never again to succumb to such a puzzle. And so I began writing this program.

TriangleCounter takes an input of points and connecting lines (essentially vertices and edges) and uses a recursive algorithm to calculate how many triangles there are. The original version relied on providing data in a text file, but this was clunky, error prone, and tedious, so I switched to using [LibGDX] (https://libgdx.badlogicgames.com/) for this project in order to provide a user interface for constructing triangles.

The code for most of the program, including the GUI and triangle counting algorithm can be seen [in the core package] (https://github.com/wizered67/TriangleCounter/tree/master/core/src/com/mygdx/game).

##Demonstration
Here is the original puzzle I struggled with (the answer is 17):

![puzzle](https://1.bp.blogspot.com/_qozgm0W_-kE/R8j1u_6WzPI/AAAAAAAAEaA/47nwundYma8/s320/123B.gif)

And here is a video of my program in action. By inputting a triangle similar to the one in the puzzle, it is able to calculate the same result. The shape does not have to match the original exactly as long as the structure is the same.

![program](https://dl.dropboxusercontent.com/u/25507891/triangledemonstration2.gif)
##The Interface
In order to calculate a result, the user must first recreate a similar triangle using the GUI. There are several actions the user can take, such as adding a point, adding a line between points, adding a point to an existing line, and deleting a point. The ability to add a point to an existing line is important because the program must know when points are colinear. Otherwise, it would incorrectly assume that 3 colinear points form a triangle. It is also possible to add a point to multiple lines, as is necessary when multiple lines intersect. Holding shift while adding a point to a line automatically adds it to all lines in close proximity.

##The Algorithm
The [triangle counting algorithm](https://github.com/wizered67/TriangleCounter/blob/master/core/src/com/mygdx/game/TriangleCounter.java) works by iterating through all points and attempting to build all triangles containing that point by calling the [groupTriangles] (https://github.com/wizered67/TriangleCounter/blob/master/core/src/com/mygdx/game/TriangleCounter.java#L18) method. The groupTriangles function takes as arguments the new point to add, a list of points already in the triangle, and the number of points in the triangle. 

If the new point is not already part of the list of points in the triangle, it is added. Then, if a total of 3 points have been found, the [checkTriangle] (https://github.com/wizered67/TriangleCounter/blob/master/core/src/com/mygdx/game/TriangleCounter.java#L40) method is called to determine if the 3 points form a valid triangle based on the following criteria: there must be exactly 3 points, the last point must be connected to the first point, and the 3 points must not all be colinear. If the triangle meets these criteria, and has not already been found, it is added to a list of all triangles found. 

If the number of points in the list passed into groupTriangles is less than 3, it iterates through all points connected to the new one and recursively calls groupTriangles, with each call adding a different adjacent point to the list. After processing all triangles containing one point, groupTriangles is called again for the rest of the points. Once the algorithm finishes, the number of triangles can be found by checking the size of the list of all triangles found.

If I were to return to this algorithm now, years after originally making it, I believe I could improve it by not considering connected points that have already been processed. 

