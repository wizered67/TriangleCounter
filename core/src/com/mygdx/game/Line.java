package com.mygdx.game;

public class Line {
	Point first;
	Point second;
	int id;
	public Line(int id, Point p1, Point p2){
		this.id = id;
		first = p1;
		second = p2;
	}
	
	public Point getFirst(){
		return first;
	}
	
	public Point getSecond(){
		return second;
	}
	
	public int getId(){
		return id;
	}
	
	public int getX(){
		return (first.getX() + second.getX()) / 2;
	}
	
	public int getY(){
		return (first.getY() + second.getY()) / 2;
	}
	
	public double getSlope(){
		double dy = second.getY() - first.getY();
		double dx = second.getX() - first.getX();
		return (dy / dx);
	}
	
	public String toString(){
		return "" + id;
	}
}
