# TriangleCounter
They say necessity is the mother of invention, and that certainly proved to be the case for this project. When I was playing Professor Layton one day, I encountered a challenging puzzle. A triangle was shown with lines passing through it. The goal was to answer how many triangles total were in the picture. This proved to be much tougher than I originally anticipated and only by using all of the available hints and some trial and error was I able to solve the puzzle. I was so enraged by the experience that I vowed never again to succumb to such a puzzle. And so I began writing this program.

TriangleCounter takes an input of points and connecting lines (essentially a graph, although I hadn't yet studied graph theory at that point) and uses a recursive algorithm to calculate how many triangles there are. The original version relied on providing data in a text file, but this was clunky, error prone, and tedious, so I switched to using [LibGDX] (https://libgdx.badlogicgames.com/) for this project in order to provide a user interface for constructing triangles.

Here is the original puzzle I struggled with (the answer is 17):

![puzzle](https://1.bp.blogspot.com/_qozgm0W_-kE/R8j1u_6WzPI/AAAAAAAAEaA/47nwundYma8/s320/123B.gif)

And here is a video of my program in action. The shape does not have to match the original exactly as long as the structure is the same.

[![program](https://img.youtube.com/vi/9caxprgu0Wo/0.jpg)](https://www.youtube.com/watch?v=9caxprgu0Wo)
