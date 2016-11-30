package com.mygdx.game;



import java.util.ArrayList;

public class TriangleCounter {
	static ArrayList<Triangle> foundTriangles = new ArrayList<Triangle>();
	
	static ArrayList<Triangle> count(ArrayList<Point> points){
		for (Point p: points){
			ArrayList<Point> temp = new ArrayList<Point>();
			groupTriangles(p, temp, 1);
		}
		return foundTriangles;
	}
	
	static void groupTriangles(Point point, ArrayList<Point> build, int count){
		if (build.contains(point))
			return;
		build.add(point);
		if (count == 3){
			if (checkTriangle(build))
			{
				Triangle tri = new Triangle(build.get(0), build.get(1), build.get(2));
				//System.out.println(tri);
				if (unique(tri)){
					foundTriangles.add(tri);
					System.out.println("Found " + tri);
				}
			}
			return;
		}
		for (Point np : point.getAdjacentPoints()){
			groupTriangles(np, (ArrayList<Point>) build.clone(), count + 1);
		}
		
	}
	
	static boolean checkTriangle(ArrayList<Point> points){
		if (points.size() != 3)
			return false;
		if (!points.get(2).getAdjacentPoints().contains(points.get(0)))
			return false;
		if (!checkUniqueLine(points))
			return false;
		return true;
	}
	
	static boolean checkUniqueLine(ArrayList<Point> points){
		for (Integer l: points.get(0).getLines()){
			if (points.get(1).getLines().contains(l) && points.get(2).getLines().contains(l)){
				return false;
			}
		}
		return true;
	}
	
	static boolean unique(Triangle tri){
		for (Triangle o : foundTriangles){
			if (o.equals(tri))
				return false;
		}
		return true;
	}
}
