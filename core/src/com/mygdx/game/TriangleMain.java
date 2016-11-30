package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.javafx.geom.Line2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TriangleMain extends ApplicationAdapter {
	ShapeRenderer drawer;
	ArrayList<Point> points;
	ArrayList<Line> lines;
	ArrayList<Line> multiLines;
	Map<Integer, ArrayList<Point>> linePoints;
	Point selectedPoint;
	Point connectPoint;
	Line selectedLine;
	char pointId = 'A';
	int lineId = 0;
	OrthographicCamera camera;
	SpriteBatch batch;
	BitmapFont font;
	String[] tools = new String[]{"ADD", "DELETE", "CONNECT", "ADD_TO_LINE"};
	int currentTool;
	Vector2 perp;
	boolean multiAdd;
	@Override
	public void create () {
		drawer = new ShapeRenderer();
		points = new ArrayList<Point>();
		lines = new ArrayList<Line>();
		multiLines = new ArrayList<Line>();
		linePoints = new HashMap<Integer, ArrayList<Point>>();
		selectedPoint = null;
		connectPoint = null;
		selectedLine = null;
		currentTool = 0;
		multiAdd = false;
		camera = new OrthographicCamera();
		camera.position.set(new Vector3(0, 0, 0));
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		MyInputProcessor inputProcessor = new MyInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);
		perp = new Vector2();
		//System.out.println(getPerpendicularCoordinates(169, 290, 341, -.54));
		//Point pa = new Point('A', 147, 341);
		//Point pb = new Point('B', 477, 162);
		//points.add(pa);
		//points.add(pb);
		//lines.add(new Line(0, pa, pb));
		
		//img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		camera.update();
		Vector3 temp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(temp);
		int unprojX = (int) temp.x;
		int unprojY = (int) temp.y;
		Point near = nearestPoint(unprojX, unprojY);
		if (!tools[currentTool].equals("ADD_TO_LINE") && near != null && distance(near.getX(), near.getY(), unprojX, unprojY ) < 12){
			selectedPoint = near;
		}
		else
			selectedPoint = null;
		if (tools[currentTool].equals("ADD_TO_LINE") && multiAdd){
			multiLines = getMultiLines(unprojX, unprojY);
		}
		if (tools[currentTool].equals("ADD_TO_LINE")){
			Line ne = nearestLine(unprojX, unprojY);
			if (ne != null){
				double slope = ne.getSlope();
				perp = getPerpendicularCoordinates(unprojX, unprojY, ne.getFirst().getX(), ne.getFirst().getY(), slope);
				int lowerBoundX = Math.min(ne.getFirst().getX(), ne.getSecond().getX());
				int upperBoundX = Math.max(ne.getFirst().getX(), ne.getSecond().getX());
				int lowerBoundY = Math.min(ne.getFirst().getY(), ne.getSecond().getY());
				int upperBoundY = Math.max(ne.getFirst().getY(), ne.getSecond().getY());
				if (perp.x > lowerBoundX && perp.x < upperBoundX && perp.y > lowerBoundY && perp.y < upperBoundY)
					selectedLine = ne;
				else
					selectedLine = null;
			}
			else
				selectedLine = null;
			
		}
		else
		{
			selectedLine = null;
		}
		
		drawer.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		drawer.begin(ShapeType.Filled);
		
		for (Point p: points){
			if (selectedPoint == p)
				drawer.setColor(Color.RED);
			else if (connectPoint == p)
				drawer.setColor(Color.GREEN);
			else
				drawer.setColor(Color.BLACK);
			drawer.circle(p.getX(), p.getY(), 12);
		}
		drawer.end();
		
		batch.begin();
		for (Point p: points){
			font.draw(batch, p.getId() + "", p.getX(), p.getY());
		}
		batch.end();
		
		drawer.begin(ShapeType.Line);
		for (Line l: lines){
			if (!multiAdd && selectedLine == l)
				drawer.setColor(Color.RED);
			else if (multiAdd && multiLines.contains(l)) //&& (selectedLine == l || linesIntersect(l, selectedLine)))
				drawer.setColor(Color.RED);
			else
				drawer.setColor(Color.YELLOW);
			drawer.line(l.getFirst().getX(), l.getFirst().getY(), l.getSecond().getX(), l.getSecond().getY());
		}
		drawer.end();
		if (tools[currentTool].equals("ADD_TO_LINE") && selectedLine != null){
			drawer.begin(ShapeType.Line);
			drawer.setColor(Color.RED);
			if (!multiAdd || multiLines.size() <= 1)
				drawer.circle(perp.x, perp.y, 12);
			else
			{
				Vector2 vp = intersectionPoint(multiLines.get(0), multiLines.get(1));
				//System.out.println(vp);
				drawer.circle(vp.x, vp.y, 12);
			}
			drawer.end();
		}
		
		batch.begin();
		for (Line l: lines){
			font.draw(batch, l.getId() + "", l.getX(), l.getY());
		}
		batch.end();
		
		//batch.begin();
		//if (!points.isEmpty())
		//font.draw(batch, points.get(0).getId() + "", points.get(0).getX(), points.get(0).getY());
		//batch.end();
		batch.begin();
		font.draw(batch, "Points Made: " + points.size(), 0, 40);
		font.draw(batch, "Lines Made: " + lines.size(), 0, 60);
		font.draw(batch, "Current Char: " + pointId, 0, 20);
		font.draw(batch, "Multi Lines: " + multiLines, 0, 80);
		if (multiAdd && tools[currentTool].equals("ADD_TO_LINE")){
			font.draw(batch, "Tool: " + tools[currentTool] + " [Multi Add]", 0, 460);
		}
		else
			font.draw(batch, "Tool: " + tools[currentTool], 0, 460);
		font.draw(batch, "X: " + unprojX + ", Y: " + unprojY, 500, 460);
		if (selectedPoint != null)
			font.draw(batch, "Lines: " + selectedPoint.getLines(), 250, 460);
		batch.end();
	}
	
	public void dispose(){
		batch.dispose();
		drawer.dispose();
		font.dispose();
	}
	
	public void leftClick(int x, int y){
		Vector3 pos = new Vector3(x, y, 0);
		camera.unproject(pos);
		if (tools[currentTool].equals("ADD")){
			addPoint(pos);	
		}
		else if (tools[currentTool].equals("DELETE")){
			deletePoint(pos);
		}
		else if (tools[currentTool].equals("CONNECT")){
			connectPoint(pos);
		}
		else if (tools[currentTool].equals("ADD_TO_LINE")){
			lineAddPoint(pos);
		}
	}
	
	public void addPoint(Vector3 coords){
		points.add(new Point(pointId, (int)coords.x ,(int)coords.y));
		pointId = (char)((int)pointId + 1);
	}
	
	public void deletePoint(Vector3 coords){
		if (selectedPoint != null){
			points.remove(selectedPoint);
			if (selectedPoint == connectPoint){
				connectPoint = null;
			}
			selectedPoint = null;
		}
	}
	
	public void connectPoint(Vector3 coords){
		if (connectPoint == selectedPoint)
			connectPoint = null;
		else if (selectedPoint != null){
			if (connectPoint == null)
				connectPoint = selectedPoint;
			else
			{
				lines.add(new Line(lineId, connectPoint, selectedPoint));
				connectPoint.addLine(lineId);
				selectedPoint.addLine(lineId);
				ArrayList<Point> ps = new ArrayList<Point>();
				ps.add(selectedPoint);
				ps.add(connectPoint);
				linePoints.put(lineId, ps);
				
				lineId++;
				connectPoint = null;
			}
		}
		else
			connectPoint = null;
	}
	
	public void lineAddPoint(Vector3 coords){
		if (selectedLine != null){
			Point p;
			if (!multiAdd || multiLines.size() <= 1)
				p = new Point(pointId, (int)perp.x ,(int)perp.y);
			else{
				Vector2 tp = intersectionPoint(multiLines.get(0), multiLines.get(1));
				p = new Point(pointId, (int)tp.x, (int)tp.y);
			}
			if (multiAdd){
				for (Line l: multiLines){
					if (!p.getLines().contains(l))
						p.addLine(l.getId());
					if (!linePoints.get(l.getId()).contains(p))
						linePoints.get(l.getId()).add(p);
				}
			}
			else
			{
			p.addLine(selectedLine.getId());
			linePoints.get(selectedLine.getId()).add(p);
			}
			points.add(p);
			pointId = (char)((int)pointId + 1);
		}
	}
	
	public void scroll(int amount){
		currentTool -= amount;
		if (currentTool < 0){
			currentTool = tools.length - 1;
		}
		else if (currentTool >= tools.length){
			currentTool = 0;
		}
	}
	
	public void process(){
		for (Point p: points){
			for (int line: p.getLines()){
				ArrayList<Point> adj = linePoints.get(line);
				p.addAdjacent(adj);
			}
		}
		ArrayList<Triangle> found = TriangleCounter.count(points);
		System.out.println(found);
		System.out.println("Found " + found.size());
	}
	
	public void setMulti(boolean state){
		multiAdd = state;
	}
	
	 /**
	   * Computes the intersection between two lines. The calculated point is approximate, 
		 * since integers are used. If you need a more precise result, use doubles
		 * everywhere. 
		 * (c) 2007 Alexander Hristov. Use Freely (LGPL license). http://www.ahristov.com
		 *
	   * @return Point where the segments intersect, or null if they don't
	   */
	  public Vector2 intersectionPoint(Line a, Line b) {
		int x1 = a.getFirst().getX();
		int y1 = a.getFirst().getY();
		int x2 = a.getSecond().getX();
		int y2 = a.getSecond().getY();
		int x3 = b.getFirst().getX();
		int y3 = b.getFirst().getY();
		int x4 = b.getSecond().getX();
		int y4 = b.getSecond().getY();
	    int d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
	    if (d == 0) return null;
	    
	    int xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
	    int yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
	    
	    return new Vector2((float)xi,(float)yi);
	  }
	
	/*
	private Vector2 intersectionPoint(Line a, Line b){
		int x1 = a.getFirst().getX();
		int y1 = a.getFirst().getY();
		int x2 = b.getFirst().getX();
		int y2 = b.getFirst().getY();
		double m1 = a.getSlope();
		double m2 = b.getSlope();
		double xc = ((double)(m2 * x2) + y2 + (double) (m1 * x1) - y1) / (m1 - m2);
		double yc = m1 * (xc - x1) + y1;
		return new Vector2((float)xc, (float)yc);
	}
	*/
	private Vector2 getPerpendicularCoordinates(int mouseX, int mouseY, int xOffset, int yOffset, double slope){
		yOffset -= slope * xOffset;
		//System.out.println(slope);
		double denom = ((slope + (1 / slope)));
		//System.out.println(denom);
		double xc = ((((1 / slope) * (double)mouseX) + (mouseY - yOffset)) / denom);
		double yc =  ((slope * xc) + yOffset);
		return new Vector2((float)xc, (float)yc);
	}
	
	private double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	private ArrayList<Line> getMultiLines(int x, int y){
		ArrayList<Line> multi = new ArrayList<Line>();
		for (Line l: lines){
			Vector2 perp = getPerpendicularCoordinates(x, y, l.getFirst().getX(), l.getFirst().getY(), l.getSlope());
			double dist = distance((int)perp.x, (int)perp.y, x, y);
			//System.out.println(dist);
			if (dist < 10){
				multi.add(l);
			}
		}
		return multi;
	}
	
	private Line nearestLine(int x, int y){
		double leastDistance = 10000;
		Line nearest = null;
		for (Line l: lines){
			Vector2 perp = getPerpendicularCoordinates(x, y, l.getFirst().getX(), l.getFirst().getY(), l.getSlope());
			if (nearest == null)
				nearest = l;
			double dist = distance((int)perp.x, (int)perp.y, x, y);
			if (dist < leastDistance){
				leastDistance = dist;
				nearest = l;
			}
		}
		return nearest;
		
	}
	
	private Point nearestPoint(int x, int y){
		double leastDistance = 10000;
		Point nearest = null;
		for (Point p: points){
			if (nearest == null)
				nearest = p;
			double dist = distance(p.getX(), p.getY(), x, y);
			if (dist < leastDistance){
				leastDistance = dist;
				nearest = p;
			}
		}
		return nearest;
	}
	
}
