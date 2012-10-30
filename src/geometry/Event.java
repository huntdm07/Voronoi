package geometry;

/**
 * A class to store events for Fortune's algorithm. Events can be one of two things:
 * 		1) a point event, when the sweepline crosses a new point; or
 * 		2) a circle event, when the sweepline crosses a point at which a new vertex in
 * 		   the Voronoi diagram would be added.
 * This class implements Comparable, as an ordering over all events is required in order for
 * the events to be stored in a sorted structure.
 * 
 * @author Dan
 *
 */
public class Event implements Comparable<Event> {

	// is this event a circle event
	private boolean circle;
	// is this circle event valid (or has it been deleted?). For use when dequeuing delelted events from PQ
	private boolean valid;
	// the point at which this event occurs
	private Point point;
	// circle event only variables
	private Arc arc;
	private double radius;

	/**
	 * Regular event constructor, for sites.
	 * @param point
	 * @param circle should always be false here
	 */
	public Event(Point point, boolean circle) {
		this.circle = circle;
		this.setPoint(point);
	}
	
	/**
	 * Circle event constructor, which links the circle event to its constituent arcs.
	 * @param point
	 * @param l the left arc
	 * @param c the centre arc
	 * @param r the right arc
	 */
	public Event(Point point, Arc a, double radius) {
		this.circle = true;
		this.setValid(true);
		this.setPoint(point);
		this.setArc(a);
		this.setRadius(radius);	
	}

	public boolean isCircle() {
		return circle;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * Comparator for checking which event should be processed first based on coordinates.
	 * @param e the event to be compared to
	 * @return true if the current should be processed before e
	 */
	public boolean isBefore(Event e) {
		// if the y values are equal, check for the event with the smaller x-value
		if( this.getPoint().getY() == e.getPoint().getY() )
			return this.getPoint().getX() < e.getPoint().getX();
		// otherwise check for the event with the larger y-value
		return this.getPoint().getY() > e.getPoint().getY();
	}

	/**
	 * Comparator for checking which event should be processed first based on coordinates. The
	 * event with the largest y coordinate, and then the largest smallest x coordinate, comes
	 * first (i.e. a top-left to bottom-right ordering).
	 * @param e the event to be compared to
	 * @return -1,0, or 1 if this event comes before, is equal to, or comes after e
	 */
	public int compareTo(Event e) throws NullPointerException {
		// can't compare to a null object
		if(e == null) {
			throw new NullPointerException();
		}
		// if the y values are equal, check for the event with the smaller x-value
		if( this.getPoint().getY() == e.getPoint().getY() ) {
			if(this.getPoint().getX() < e.getPoint().getX()) {
				return -1;
			// if the x values are the same, then the two events are equal
			} else if(this.getPoint().getX() == e.getPoint().getX()) {
				return 0;
			}
			return 1;
		}
		// otherwise check for the event with the larger y-value
		if(this.getPoint().getY() > e.getPoint().getY())
			return -1;
		return 1;
	}

	public Arc getArc() {
		return arc;
	}

	public void setArc(Arc arc) {
		this.arc = arc;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

}
