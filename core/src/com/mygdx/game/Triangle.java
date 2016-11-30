package com.mygdx.game;

import java.util.Arrays;


public class Triangle {
	Point[] points = new Point[3];
	public Triangle(Point a, Point b, Point c){
		points[0] = a;
		points[1] = b;
		points[2] = c;
	}
	
	public boolean equals(Triangle t){
		for (Point p : points){
			if (!Arrays.asList(t.points).contains(p)){
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		return Arrays.asList(points).toString();
	}
}
