package geometry;


public class Line {

	private Point p1;
	private Point p2;
	// the other half of this line, connected to the other end point
	private Line halfEdge;
	// booleans to describe if this line has end points at Voronoi vertices. Initialised to false.
	private boolean complete;
	private Point endPoint;
	
	
	public Line(Point p1, Point p2) {
		this.setP1(p1);
		this.setP2(p2);
		this.complete = false;
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete, Point endPoint) {
		this.complete = complete;
		this.endPoint = endPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Line getHalfEdge() {
		return halfEdge;
	}

	public void setHalfEdge(Line halfEdge) {
		this.halfEdge = halfEdge;
	}
	
}
