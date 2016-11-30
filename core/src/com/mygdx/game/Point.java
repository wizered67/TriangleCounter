package com.mygdx.game;
import java.util.ArrayList;
import java.util.Arrays;


public class Point {
	private ArrayList<Point> adjacentPoints = new ArrayList<Point>();
	private ArrayList<Integer> lines = new ArrayList<Integer>();
	private char id;
	private int x, y = 0;
	
	public Point(char name){
		id = name;
	}
	
	public Point(char name, int x, int y){
		id = name;
		this.x = x;
		this.y = y;
	}
	
	
	public void addAdjacent(ArrayList<Point> points){
		for (Point p: points){
			if (!adjacentPoints.contains(p))
			adjacentPoints.add(p);
		}
	}
	
	public void addAdjacent(Point[] points){
		addAdjacent((ArrayList<Point>)Arrays.asList(points));
	}
	
	public ArrayList<Point> getAdjacentPoints(){
		return adjacentPoints;
	}
	
	public void setLines(int[] l){
		for (Integer num : l){
			lines.add(num);
		}
	}
	
	public void addLine(int l){
		lines.add(l);
	}
	
	public ArrayList<Integer> getLines(){
		return lines;
	}
	
	public char getId(){
		return id;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Point p){
		return (p.id == id);
	}
	
	public String toString(){
		return Character.toString(id);
	}
}
