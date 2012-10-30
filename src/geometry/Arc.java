package geometry;

/**
 * 
 * 
 * @author Daniel Hunt
 *
 */
public class Arc implements Comparable<Arc> {

	/* The point around which the arc is based, which completely defines the arc.
	 */
	private Point point;
	/* The two half edges corresponding to this arc, which span between the two intersections
	 * of this arc and the neighbouring arcs. The edges this arc defines in the final Voronoi
	 * diagram.
	 */
	// edge at the right boundary
	private Line plusEdge;
	// edge at the left boundary
	private Line minusEdge;
	/* The left and right borders of the region defined by this arc. These parameters are
	 * defined separately to 'edge' so that they can be updated frequently when they are
	 * required without the y value.
	 * 		-if the arc is the leftmost arc, left should be Double.MIN_VALUE
	 * 		-if the arc is the rightmost arc, right should be Double.MAX_VALUE
	 */ 
	private double left;
	private double right;
	// not sure if these will be used - pointers to neighbouring arcs. If they are unique,
	// this arc will be part of a circle event.
	private Arc leftNeighbour;
	private Arc rightNeighbour;

	public Arc(Point point, Line edge) {
		this.setPoint(point);
		this.setPlusEdge(edge);
		this.setMinusEdge(cloneLine(edge));
		minusEdge.setHalfEdge(plusEdge);
		plusEdge.setHalfEdge(minusEdge);
		this.setLeft(edge.getP1().getX());
		this.setRight(edge.getP2().getX());
	}

	/**
	 * Create a dummy Arc to compare with the beachLine. Used to find the Arc above an Event, and
	 * can then later be modified to update other parameters.
	 * @param e the event to create an arc at
	 */
	public Arc(Event e) {
		this.setPoint(e.getPoint());
		this.setLeft(e.getPoint().getX());
		this.setRight(e.getPoint().getX());
	}

	/**
	 * Create a new Arc, given the point and its two neighbours. The neighbours MUST be up to date,
	 * as their values will be used to calculate the left and right boundaries for this Arc.
	 * @param point the point about which this arc is based
	 * @param left the arc that is the left neighbour of this arc
	 * @param right the arc that is the right neighbour of this arc
	 */
	public Arc(Point point, Arc left, Arc right, Line edge) {
		if(left!=null) {
			this.setLeftNeighbour(left);
			this.setLeft(left.getRight());
		} else {
			this.setLeft(-Double.MAX_VALUE);
		}
		if(right!=null) {
			this.setRightNeighbour(right);
			this.setRight(right.getLeft());
		} else {
			this.setRight(-Double.MAX_VALUE);
		}
		if(edge!=null) {
			this.setPlusEdge(edge);
			this.setMinusEdge(cloneLine(edge));
			minusEdge.setHalfEdge(plusEdge);
			plusEdge.setHalfEdge(minusEdge);
		}

		this.setPoint(point);
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public double getLeft() {
		return this.left;
	}

	public double getRight() {
		return this.right;
	}

	/**
	 * Update the left edge of the bounding region for this arc.
	 * @param l the new x value for the left edge
	 */
	public void setLeft(double left) {
		this.left = left;
	}

	/**
	 * Update the right edge of the bounding region for this arc.
	 * @param l the new x value for the right edge
	 */
	public void setRight(double right) {
		this.right = right;
	}

	/**
	 * A method to compare arcs in the order they appear in the beach line from left to right,
	 * i.e. in order of increasing x value.
	 * @param a the arc to be compared to
	 * @return -1, 0, or 1 if this arc is before, equal to, or after a
	 */
	public int compareTo(Arc a) {
		if(this.left < a.getLeft()) {
			return -1;
		} else if (this.left == a.getLeft() ) {
			return 0;
		}
		return 1;
	}

	public Arc getLeftNeighbour() {
		return leftNeighbour;
	}

	public void setLeftNeighbour(Arc leftNeighbour) {
		this.leftNeighbour = leftNeighbour;
	}

	public Arc getRightNeighbour() {
		return rightNeighbour;
	}

	public void setRightNeighbour(Arc rightNeighbour) {
		this.rightNeighbour = rightNeighbour;
	}

	public Line getPlusEdge() {
		return plusEdge;
	}

	public void setPlusEdge(Line plusEdge) {
		this.plusEdge = plusEdge;
	}

	public Line getMinusEdge() {
		return minusEdge;
	}

	public void setMinusEdge(Line minusEdge) {
		this.minusEdge = minusEdge;
	}

	private Line cloneLine(Line line) {
		// make a new line so the two edges don't reference the same line
		Point p1,p2;
		p1 = line.getP1();
		p2 = line.getP2();
		return new Line(p1,p2);
	}

	public void setEdges(Line line) {
		this.setMinusEdge(line);
		this.setPlusEdge(cloneLine(line));
		// set the half edges of each edge to point to each other
		minusEdge.setHalfEdge(plusEdge);
		plusEdge.setHalfEdge(minusEdge);
	}

}
