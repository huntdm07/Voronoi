import java.io.*;
import java.util.*;

import geometry.*;

import java.lang.Math;

public class Fortune {


	// number of points
	private int numPoints;
	// the range between the largest and smallest x value
	private final double RANGE;
	// priority queue to store the events
	private PriorityQ pq;
	// treeset to store the beach line in sorted order to facilitate fast removal/addition of events
	private TreeSet<Arc> beachLine = new TreeSet<Arc>();
	// a hashmap to store the circle events based on the arcs they belong to
	private HashMap<Arc,Event> circleEvents = new HashMap<Arc,Event>();
	// the canvas to draw on
	private Visualizer visualiser;

	public Fortune(Voronoi v) {
		// clone the pq so that other implementations can use it as it was when initiliased (with just the points).
		// this is also the reason an instance variable PQ is maintained rather than just updating this one.
		this.pq = v.getPq().clone();
		this.RANGE = v.getRANGE();
		this.numPoints = pq.getNumElements();
		this.visualiser = new Visualizer((int)v.getMaxValue()+1,pq.getHeap().clone());
		HashMap<Point,LinkedList<Line>> voronoi = runAlgorithm();
		drawEdges(voronoi);
	}

	private void drawEdges(HashMap<Point, LinkedList<Line>> voronoi) {
		visualiser.updateEdges(voronoi);
		System.out.println("Size: " + voronoi.size());
	}

	/**
	 * A helper function to print the diagram at a particular point in time.
	 */
	private void drawVoronoi(double sweep) {
		visualiser.updateBeachline(sweep, beachLine);
		return;
	}

	private void printStatus(double sweep, Event e) {
		System.out.println("Sweep: " + sweep);
		System.out.println("Event: (" + e.getPoint().getX() + "," + e.getPoint().getY() + ")");
	}

	/**
	 * A function to run Fortune's algorithm. At this stage it is a non-functional skeleton of what
	 * the final function should do.
	 */
	private HashMap<Point,LinkedList<Line>> runAlgorithm() {
		// a map to store the vertices and edges in the final diagram
		HashMap<Point,LinkedList<Line>> voronoi = new HashMap<Point,LinkedList<Line>>();
		//HashMap<Point,LinkedList<Point>> cells = new HashMap<Point,LinkedList<Point>>();

		// a variable to ensure that the expensive updateArcs() function isn't run
		// multiple times when the sweep line hasn't moved
		double lastUpdate = Double.MAX_VALUE;
		// is it the first loop
		boolean first = true;

		/****************** DEBUGGING ********************************/
		int event = 0;
		int nullEdges = 0;
		/************** END DEBUGGING ****************/

		// as long as there are more events
		while( !pq.isEmpty() ) {
			Event e = pq.extractMin();
			// ignore invalid (i.e. removed) circle events

			/************** DEBUGGING ********************/
			System.out.println();
			System.out.println("******** EVENT " + event + ", CIRCLE: " + e.isCircle() + "*********"); event++;
			//printStatus(lastUpdate,e);
			/************** END DEBUGGING ****************/

			if(e.isCircle() && !e.isValid()) {
				continue;
			}
			double sweep = e.getPoint().getY();

			// if the sweep line has moved, updated the arcs
			// in the first iteration there will be no beach line, so don't try to update it
			if(lastUpdate != sweep && !first) {
				// update the arcs with the current value
				updateArcs(sweep);
				drawVoronoi(sweep);
			}
			if(first)
				first = false;

			if ( !e.isCircle() ) {
				addArc(e,sweep);
			} else {
				Arc current = e.getArc();

				// for cleaner code
				Arc left = current.getLeftNeighbour();
				Arc right = current.getRightNeighbour();
				Line leftEdge = left.getPlusEdge();
				Line rightEdge = right.getMinusEdge();

				// create a new Voronoi vertex corresponding to this circle event
				Point vVertex = new Point(e.getPoint().getX(),e.getPoint().getY() + e.getRadius());

				// update these halfedges as complete, since they now have an end point at this vertex
				if(rightEdge!=null)
					rightEdge.setComplete(true,vVertex);
				if(leftEdge!=null)
					leftEdge.setComplete(true,vVertex);

				// now need to add a new edge to the beach line corresp. to new break point
				Point newBreak;
				Line newEdge;
				double[] intersections = intersectArc(left, right, sweep + RANGE/numPoints/50.0);
				// find the intersection near the new voronoi vertex and use this to define the line
				if( (intersections[1]-vVertex.getX()) < (intersections[0]-vVertex.getX()) ) {
					newBreak = new Point(intersections[1],evaluate(current.getLeftNeighbour(),
							intersections[1],sweep + RANGE/50.0));
					newEdge = new Line(vVertex,newBreak);
					newEdge.setHalfEdge(rightEdge);
				} else {
					newBreak = new Point(intersections[0],evaluate(current.getLeftNeighbour(),
							intersections[0],sweep + RANGE/50.0));
					newEdge = new Line(vVertex,newBreak);
					newEdge.setHalfEdge(rightEdge);
				}

				// remove the arc associated with this circle event, and update its neighbours
				removeArc(current);
				// set other circle events associated with this arc to invalid so they will not be processed
				deleteCircle(current);

				if(leftEdge != null) {
					if(leftEdge.getHalfEdge().isComplete()) {
						Line l = new Line(leftEdge.getHalfEdge().getEndPoint(),vVertex);
						if(!voronoi.containsKey(vVertex)) {
							voronoi.put(vVertex, new LinkedList<Line>());
						}
						if(!voronoi.containsKey(leftEdge.getHalfEdge().getEndPoint())) {
							voronoi.put(leftEdge.getHalfEdge().getEndPoint(), new LinkedList<Line>());
						}
						voronoi.get(vVertex).add(l);
						voronoi.get(leftEdge.getHalfEdge().getEndPoint()).add(l);
						System.out.println("Edge added: (" + l.getP1().getX() +","+l.getP1().getY()+"), ("
								+ l.getP2().getX() +","+l.getP2().getY()+") ");
						drawEdges(voronoi);
					}}
				if(rightEdge != null) {
					if(rightEdge.getHalfEdge().isComplete()) {
						Line l = new Line(rightEdge.getHalfEdge().getEndPoint(),vVertex);
						if(!voronoi.containsKey(vVertex)) {
							voronoi.put(vVertex, new LinkedList<Line>());
						}
						if(!voronoi.containsKey(rightEdge.getHalfEdge().getEndPoint())) {
							voronoi.put(rightEdge.getHalfEdge().getEndPoint(), new LinkedList<Line>());
						}
						voronoi.get(vVertex).add(l);
						voronoi.get(rightEdge.getHalfEdge().getEndPoint()).add(l);
						System.out.println("Edge added: (" + l.getP1().getX() +","+l.getP1().getY()+"), ("
								+ l.getP2().getX() +","+l.getP2().getY()+") ");
						drawEdges(voronoi);
					}}

				// add the new edge to the two arcs
				left.setPlusEdge(newEdge);
				right.setMinusEdge(newEdge);

				/************************** END DEBUGGING ***************************************/

			}

			// update to take into account the event that was just processed
			lastUpdate = sweep;
			visualiser.updateSweep(sweep);
			visualiser.redraw();
			try {
				drawVoronoi(sweep);
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		return voronoi;
	}

	/**
	 * Remove an arc from the beach line, and reassign the neighbours of the arcs immediately to the
	 * left and to the right of this arc.
	 * @param current the arc to be removed
	 */
	private void removeArc(Arc current) {
		beachLine.remove(current);
		Arc left = current.getLeftNeighbour();
		Arc right = current.getRightNeighbour();
		left.setRightNeighbour(right);
		right.setLeftNeighbour(left);
	}

	/**
	 * Add a point event to the beach line, removing any negated arcs, and adding/removing any
	 * circle events as required.
	 * 
	 * @param e the event to be added to the beach line
	 */
	private void addArc(Event e, double sweep) {
		// the first time this is called, add the arc to the empty line
		if(beachLine.isEmpty()) {
			Arc a = new Arc(e);
			a.setLeft(-Double.MAX_VALUE);
			a.setRight(Double.MAX_VALUE);
			beachLine.add(a);
			return;
			// the second time this is called, add the second arc and don't look for circle events
		}

		boolean end;

		// increment sweep by a small amount so that two intersections can be found
		sweep = sweep - ((double)RANGE)/numPoints/1000000.0;
		updateArcs(sweep);
		// find the arc above this event in the beach line (in log(beachLine.size()) time)
		Arc current = new Arc(e);
		Arc above = beachLine.lower(current);

		/* check for cases at the end of the beach line, when a new edge must be added to the inner arc */
		if((above.getLeft() == -Double.MAX_VALUE || above.getRight() == Double.MAX_VALUE) 
				&& beachLine.size() >= 3) {
			end = true;
			System.out.println("Edge of beachLine");
		} else {
			end = false;
		}

		System.out.println("Above: " + above.getLeft() + "," + above.getRight());
		System.out.println("Event x: " + e.getPoint().getX());
		// find the intersections of the current arc with the arc above it (may be over beach line)
		double intersectionLeft, intersectionRight;
		double[] intersections = intersectArc(current,above,sweep);
		intersectionLeft = intersections[0];
		intersectionRight = intersections[1];

		// create a list for storing intermediate arcs (will later need to be deleted)
		LinkedList<Arc> arcList = new LinkedList<Arc>();
		arcList.add(above);

		// delete all intermediate arcs from the beach line and remove their circle events
		deleteArcsAndCircles(arcList);

		// update the boundaries of current
		current.setLeft(intersectionLeft);
		current.setRight(intersectionRight);
		// define the two intersection points and make a new edge using them
		Point l = new Point(intersectionLeft,evaluate(current,intersectionLeft,sweep));
		Point r = new Point(intersectionRight,evaluate(current,intersectionRight,sweep));
		current.setEdges(new Line(l,r));

		// create new arcs for left and right, to be added to the beach line
		Arc left,right;
		left = new Arc(above.getPoint(),above.getLeftNeighbour(),current,above.getMinusEdge());
		right = new Arc(above.getPoint(),current,above.getRightNeighbour(),above.getPlusEdge());
		// update the neighbours accordingly
		current.setLeftNeighbour(left);
		current.setRightNeighbour(right);

		// if this arc was added onto the left end of the beach line, add an edge to right
		if(end && left.getLeft() == -Double.MAX_VALUE ) {
			System.out.print("Edge was: " + right.getMinusEdge());
			l = new Point(right.getLeft(),evaluate(right,right.getLeft(),sweep));
			r = new Point(right.getRight(),evaluate(right,right.getRight(),sweep));
			right.setEdges(new Line(l,r));
			System.out.println(" and was updated to (" + l.getX() + "," + l.getY() + "), ("+ r.getX() +"," + r.getY() + ")");
			// if this arc was added onto the right end of the beach line, add an edge to left
		} else if ( end && right.getRight() == Double.MAX_VALUE ) {
			l = new Point(left.getLeft(),evaluate(left,left.getLeft(),sweep));
			r = new Point(left.getRight(),evaluate(left,left.getRight(),sweep));
			left.setEdges(new Line(l,r));
		}

		// add the new arcs in place of the one previous arc. Left/right can belong to same point
		beachLine.add(current);
		beachLine.add(left);
		beachLine.add(right);
		// when the second event is considered, add lines to the first arc
		//		if(beachLine.size() == 3) {
		//			beachLine.first().setEdges(current.g)
		//		}
		System.out.println(left.getLeft() + " " + current.getLeft() + " " + right.getLeft() );
		System.out.println(intersectionLeft + " " + intersectionRight );

		// check for new circle events on left, current and right, and add them
		addCircles(left,current,right);

	}

	/**
	 * Determine the value of an arc at a given x-coordinate. This simply calculates the
	 * corresponding y-coordinate of the provided arc and value of sweep.
	 * 
	 * Note: this method is public so that it can be used in the visualiser.
	 * @param a the arc to evaluate
	 * @param x the point at which to evaluate the arc
	 * @return the value of the arc
	 */
	public static double evaluate(Arc a, double x, double sweep) {
		double px = a.getPoint().getX();
		double py = a.getPoint().getY();
		// this will never happen, as a point already processed will not lie on the sweep line
		if(py == sweep) {
			return -1;
		}
		return Math.pow(x - px,2.0) / (2.0 * (py - sweep)) + (py + sweep)/2.0;
	}

	/**
	 * Check the three points, presumed to be neighbours in the implied order, and add circle
	 * events where they are relevant. Includes checks in case the neighbours of the left/right
	 * arcs are not defined (i.e. in case these arcs are at the end of the beach line).
	 * @param left
	 * @param centre
	 * @param right
	 */
	private void addCircles(Arc left, Arc centre, Arc right) {
		// check left neighbour of left is not null (left not at end of beach line)
		if(left.getLeftNeighbour()!=null && circle(left.getLeftNeighbour(),left, centre)) {
			Event c = circleEvent(left.getLeftNeighbour(),left, centre);
			if(c != null) {
				pq.insert(c);
				circleEvents.put(left, c);
			}
		}
		if(circle(left, centre, right)) {
			Event c = circleEvent(left,centre,right);
			if(c != null) {
				pq.insert(c);
				circleEvents.put(centre, c);
			}
		}
		// check right neighbour of right is not null (right not at end of beach line)
		if(right.getRightNeighbour()!=null && circle(centre, right, right.getRightNeighbour())) {
			Event c = circleEvent(centre, right, right.getRightNeighbour());
			if(c != null) {
				pq.insert(c);
				circleEvents.put(right, c);
			}
		}

	}

	/**
	 * Deletes all arcs and circle events between two arcs.
	 * @param left the arc bounding the region on the left
	 * @param right the arc bounding the region on the right
	 */
	private void deleteArcsAndCircles(List<Arc> arcList) {
		for(Arc a: arcList) {
			beachLine.remove(a);
			// if there is a circle event associated with this arc, delete it
			if(circleEvents.containsKey(a))
				deleteCircle(a);
		}
	}

	/**
	 * Set all circle events that this arc is a part of to not valid. Then they will not be processed
	 * when dequeued from the priority queue. Also remove the entry for this arc from the circleEvents
	 * map.
	 * @param a the arc to delete the circle events of
	 */
	private void deleteCircle(Arc a) {
		circleEvents.get(a).setValid(false);
		circleEvents.remove(a);
	}

	/**
	 * Check if the three points corresponding to the three arcs will form a valid circle event or not.
	 * This depends on the value of their cross-product as two difference vectors, i.e. (p1-p2), (p1-p3).
	 * This will also check if the points are collinear (cross-product = 0).
	 * @param left
	 * @param centre
	 * @param right
	 * @return true if a valid circle event would be formed by these points.
	 */
	private boolean circle(Arc left, Arc centre, Arc right) {
		// ignore case where circle event uses two arcs with the same point
		if(left.getPoint() == right.getPoint()) {
			return false;
		}

		double lx = left.getPoint().getX();
		double ly = left.getPoint().getY();
		double cx = centre.getPoint().getX();
		double cy = centre.getPoint().getY();
		double rx = right.getPoint().getX();
		double ry = right.getPoint().getY();

		double cross = (cx-lx)*(ry-ly) - (cy-ly)*(rx-lx);

		return cross > 0;
	}

	/**
	 * Create a circle event based on the three provided arcs.
	 * @param left the arc on the left
	 * @param current the central arc
	 * @param right the arc on the right
	 * @return a new circle event for these arcs
	 */
	private Event circleEvent(Arc left, Arc current, Arc right) {
		Line l1 = bisector(left.getPoint(), current.getPoint());
		Line l2 = bisector(current.getPoint(),right.getPoint());
		Point centre = intersectLines(l1,l2);

		System.out.println("Circle centre at: " + centre.getX() + ", " + centre.getY());
		System.out.print("\t used points: ");
		printPoint(left.getPoint());
		printPoint(current.getPoint());
		printPoint(right.getPoint());

		if(centre == null) {
			return null;
		}

		double radius = dist(left.getPoint(),centre);

		System.out.println();
		System.out.println("\t radius: " + radius);

		// move the centre down by the radius to get the lowest point on the circle.
		// this is where the circle event will occur.
		centre.setY(centre.getY()-radius);
		System.out.println("Circle event at: " + centre.getX() + ", " + centre.getY());

		Event e = new Event(centre,current, radius); 
		// update the circle event set for the middle arc
		circleEvents.put(current,e);

		return e;
	}

	private void printPoint(Point p) {
		System.out.print("(" + p.getX() + "," + p.getY() + ")");
	}

	/**
	 * Calculate the distance between two points.
	 * @param p1
	 * @param p2
	 * @return
	 */
	private double dist(Point p1, Point p2) {
		return Math.hypot(p1.getX()-p2.getX(),p1.getY()-p2.getY());
	}

	/**
	 * Create a perpendicular bisector between two points.
	 * @param p1 the first point
	 * @param p2 the second point
	 * @return the line bisecting p1 and p2
	 */
	private Line bisector(Point p1, Point p2) {
		Point first = new Point( (p1.getX()+p2.getX())/2.0, (p1.getY()+p2.getY())/2.0 );
		Point second = new Point( first.getX() + p2.getY()-p1.getY() , first.getY() + p1.getX()-p2.getX() );
		return new Line(first, second);
	}

	/**
	 * Determine the intersection of two lines.
	 * @param l1 the first line
	 * @param l2 the second line
	 * @return the point at which the two lines intersect, or null if they don't intersect
	 */
	private Point intersectLines(Line l1, Line l2) {
		double x1=l1.getP1().getX();
		double y1=l1.getP1().getY();
		double x2=l1.getP2().getX();
		double y2=l1.getP2().getY();
		double x3=l2.getP1().getX();
		double y3=l2.getP1().getY();
		double x4=l2.getP2().getX();
		double y4=l2.getP2().getY();

		double pxNumer = ((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4)) ;
		double pyNumer = ((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4)) ;
		double pointDenom = ((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4)) ;

		if(pointDenom == 0) {
			return null;
		} else {
			return new Point(pxNumer/pointDenom,pyNumer/pointDenom);
		}

	}

	/**
	 * Updates the 'edge' parameter on each arc based on the current position of the sweep
	 * line. This will then define the region covered by each arc. This should be used before
	 * adding a new Event to the pq, as the values should be up to date. It can also be used
	 * frequently if a visualisation function is being used, in order to update all of the
	 * regions and edges at any given point in the algorithm.
	 * 
	 * @param sweep the current y-value of the sweep line
	 */
	public void updateArcs(double sweep) {
		// a variable to hold the updated right point in the edge, which is also the value of the
		// updated left edge in the next arc.
		double leftPoint = -Double.MAX_VALUE;
		double rightPoint = -Double.MAX_VALUE;
		// the next arc - required for determining new intersection at boundary
		Arc previous = null;
		// clone the tree to avoid ConcurrentModificationException during iteration
		TreeSet<Arc> clone = (TreeSet<Arc>)beachLine.clone();
		beachLine = new TreeSet<Arc>();

		// get arcs from left to right from cloned beach line
		for(Arc a: clone) {
			// if first iteration, update previous variable and continue
			if(a.getLeft() == -Double.MAX_VALUE) {
				previous = a;
				continue;
			}

			// the right point set in the previous iteration is the left point of this arc. After
			// the first iteration rightPoint will still be -Double.MAX_VALUE, so no issue there.
			leftPoint = rightPoint;
			previous.setLeft(leftPoint);

			//System.out.println("Updated leftPoint to: " + leftPoint);

			// update the rightPoint for the previous section by finding the intersection with the
			// current arc
			double[] intersections = intersectArc(previous, a, sweep);
			//System.out.println("Found rightPoints: " + intersections[0] + " " + intersections[1]);
			// make sure the correct intersection is used (the one with previous left of a)
			if(intersections[0] < previous.getLeft()) {
				rightPoint = intersections[1];
			} else {
				rightPoint = intersections[0];
			}
			previous.setRight(rightPoint);
			//System.out.println("Updated rightPoint to: " + rightPoint);
			//System.out.println("Leftpoint: " + leftPoint);
			//System.out.println("Rightpoint: " + rightPoint);


			// add the previous segment to the new, updated beach line
			beachLine.add(previous);

			// update previous variable
			previous = a;
		}

		// if the 
		if(previous != null) {

			previous.setLeft(rightPoint);		// after the loop, we have updated all but the last arc, so update the last arc now
			previous.setRight(Double.MAX_VALUE);
			beachLine.add(previous);
		}
	}

	/**
	 * Find the x values of the intersection between two arcs. The intersections will be calculated
	 * with the current position of the sweep line.
	 * 
	 * @param arc1 the left arc
	 * @param arc2 the right arc
	 * @param sweep the y value of the sweep line
	 * @return the x values of the intersections between a1 and a2
	 */
	private double[] intersectArc(Arc arc1, Arc arc2, double sweep) {
		// define some parameters for clarity. These are the points about which a1/a2 are based
		double p1x = arc1.getPoint().getX();
		double p1y = arc1.getPoint().getY();
		double p2x = arc2.getPoint().getX();
		double p2y = arc2.getPoint().getY();

		// quadratic equation parameters, written out for clarity - see Mathematica doc for working
		double minusB = (p1y * p2x) - (p1x * p2y) + sweep * (p1x - p2x);
		double sqrtB2_4AC = Math.sqrt(  ( Math.pow((p1x - p2x),2) + Math.pow((p1y - p2y),2) ) 
				* (p1y - sweep) * (p2y - sweep)  );
		double A2 = p1y - p2y;

		double[] values = new double[2];
		// the left most solution from the quadratic eqn
		if( ((minusB - sqrtB2_4AC) / A2) < ((minusB + sqrtB2_4AC) / A2) ) {
			values[0] = (minusB - sqrtB2_4AC) / A2;
			values[1] = (minusB + sqrtB2_4AC) / A2;
		} else {
			values[1] = (minusB - sqrtB2_4AC) / A2;
			values[0] = (minusB + sqrtB2_4AC) / A2;
		}
		//System.out.println(values[0] + " " + values[1]);
		return values;
	}

}