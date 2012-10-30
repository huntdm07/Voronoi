import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.lang.Math.*;
import java.util.*;

import geometry.*;
import geometry.Event;
import geometry.Point;

/**
 *	Class for drawing voronoi diagrams.
 */
public class Visualizer extends JFrame  {

	int WINDOW_WIDTH = 750; // constants
	int WINDOW_HEIGHT = 750;
	private VCanvas canvas;
	private Container window;
	public Graphics gfx;

	/**
	 * Default constructor. Constructs a square window of size 750x750.
	 * @param max The maximum x value of the input vertices. Used for scaling the canvas appropriately.
	 */
	public Visualizer(int max, Event[] e)  {
		super("Voronoi Diagram");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create and intialize the pane
		window = this.getContentPane();
		window.setBackground(Color.white);
		window.setVisible(true);	
		setVisible(true);

		initCanvas(max);

		canvas.inputVertices = e;
	}

	/**
	 * Constructs a square window with the specified width.
	 * @param max The maximum x value of the input vertices. Used for scaling the canvas appropriately.
	 * @param width The required width of the window.
	 */
	public Visualizer(int max, int width)  {
		super("Voronoi Diagram");
		WINDOW_WIDTH = width;
		WINDOW_HEIGHT = width;
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create and intialize the pane
		window = this.getContentPane();
		window.setBackground(Color.white);
		window.setVisible(true);	
		setVisible(true);

		initCanvas(max);

	}

	private void initCanvas(int max)  {
		canvas = new VCanvas(max);
		canvas.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		canvas.setBackground(Color.RED);
		window.add(canvas);
		canvas.setVisible(true);

	}


	//	public void storeInputVertices(PriorityQ pq)  {
	//		canvas.storeInputVertices(pq);
	//	}

	public void redraw()  {
		canvas.repaint();
	}

	public void updateBeachline(double sweep, TreeSet<Arc> beachLine) {
		canvas.updateBeachLine2(sweep, beachLine);
	}
	
	public void updateSweep(double sweep) {
		canvas.sweepLine = sweep;
	}

	public void updateEdges(HashMap<Point, LinkedList<Line>> voronoi) {
		canvas.edges = voronoi;
		canvas.repaint();
	}

	//	public static void main(String[] args)  {
	//		Visualizer v = new Visualizer(10);
	//		
	//	}

	/*************************************************************************************************
	 * The actual canvas object.
	 *************************************************************************************************/
	public class VCanvas extends JPanel  {

		double[] points; // set of canvas coords to plot for the parabola
		Event[] inputVertices;
		Point[] voronoiPts; 
		double sweepLine;
		TreeSet<Arc> beachLine;
		int[] origin; // might implement moving origin. currently (0,WINDOW_HEIGHT) in canvas coords.
		double SCALE_FACTOR;
		HashMap<Point,LinkedList<Line>> edges;

		/**
		 * Constructor.
		 */
		public VCanvas(int max)  {
			SCALE_FACTOR = ((double) WINDOW_WIDTH) / max;
			points = new double[(int)(SCALE_FACTOR*max + 1)];
			sweepLine = 0;
			origin = new int[2];
			origin[0] = 0; origin[1] = WINDOW_HEIGHT; // bottom left corner
		}
		/************ Scaling/translation functions to fit diagram onto canvas***********************************/
		private double scaleUp(double i)  {
			return ((double)SCALE_FACTOR) * i;
		}

		private double scaleDown(double i)  {
			return ((double)i)/SCALE_FACTOR;
		}

		private int translate(int i)  {
			return origin[1] - i;
		}
		/*********************************************************************************************/

		/**
		 * Generates the raw points to plot for the given parabola. Doesn't actually do any drawing.
		 */
		public void drawParabola(double a, double b, double c, double startX, double endX)  {
			if(startX < 0) {
				startX = 0;
			}
			if(endX > 750) {
				endX = 750;
			}
			
			for (int i=(int)(startX*SCALE_FACTOR); i<(int)(endX*SCALE_FACTOR); i++)  {
				if ( i == points.length ) break;
				double x = scaleDown(i);
				double px = a*Math.pow(x, 2) + b*x + c;
				points[i] = px;
			}
		}

		/**
		 * Calculates a,b,c coefficients of a parabola from the given point. Requires an array
		 * to be passed to it to store the coefficients.
		 */
		private void calcCoefficients(geometry.Point p, double[] parabola)  {
			double x = p.getX();
			double y = p.getY();

			// ax^2 + b*x + c
			parabola[0] = 1.0/(2*(y-sweepLine) );
			parabola[1] = (-2.0*x)/(2*(y-sweepLine) );
			parabola[2] = (x*x)/(2*(y-sweepLine) ) + (y+sweepLine)/2 + sweepLine;

		}

		/**
		 * Processes beachline.
		 */
		public void updateBeachLine(double sweep, TreeSet<Arc> beachLine)  {
			sweepLine = sweep;

			Iterator it = beachLine.iterator();

			Arc a;
			geometry.Point p;
			double[] coefficients = new double[3];
			double left;
			double right;

			while ( it.hasNext() )  {
				a = (Arc)(it.next());
				p = a.getPoint();
				left = a.getLeft();
				right = a.getRight();
				calcCoefficients(p, coefficients);
				drawParabola( coefficients[0], coefficients[1], coefficients[2], left, right );
			}

		}
		
		public void updateBeachLine2(double sweep, TreeSet<Arc> beachLine) {
			Iterator it = beachLine.iterator();
			
			Arc a; Point p; double left,right;
			while ( it.hasNext() )  {
				a = (Arc)(it.next());
				p = a.getPoint();
				left = a.getLeft();
				right = a.getRight();
				if(left < 0) {
					left = 0;
				}
				if(right > 750) {
					right = 750;
				}
				for (int i=(int)(left*SCALE_FACTOR); i<(int)(right*SCALE_FACTOR); i++)  {
					if ( i == points.length ) break;
					double x = scaleDown(i);
					double px = Fortune.evaluate(a,x,sweep);
					points[i] = px;
				}
			}
			
			
			
			
		}

		/**
		 * Processes input vertices.
		 *
		public void storeInputVertices(PriorityQ pq)  {
			geometry.Point p; 		
			inputVertices = new geometry.Point[pq.getNumElements()];
			System.out.println("number of points is : " + pq.getNumElements());

			int i=0;
			while ( !pq.isEmpty() ) {
				p = pq.extractMin().getPoint();
				System.out.println(" inserting point " + p.getX() + ", " + p.getY());
				inputVertices[i] = p;
				i++;
			}
		}*/

		/**
		 * Processes the edges from the given data structure.
		 */
		public void storeVoronoiEdges()  {

		}

		public void paint(Graphics g)  {
			g = (Graphics2D)g;
			g.clearRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);


			// draw sweep line
			for (int i=0; i<points.length; i++)  {
				double yd = scaleUp( points[i] );
				int y = (int)yd; // convert back to canvas coords before plotting

				g.drawLine( i, translate(y) , i, translate(y) );
			}		


			// draw the input vertices
			if ( inputVertices != null )  {
				//g.setColor(Color.BLUE);
				for ( int i=0; i<inputVertices.length; i++)  {
					if (inputVertices[i] == null )  {
						//System.out.println(i + " was null");
						continue;
					}
					double xd = scaleUp( inputVertices[i].getPoint().getX() );
					double yd = scaleUp( (inputVertices[i]).getPoint().getY() );
					int x = (int)xd;
					int y = (int)yd;

					//System.out.println("x: " + x + ", y: " + y);

					g.fillOval(x-1,translate(y-1),3,3);
				}		
			}


			// draw Voronoi edges
			if( edges != null ) {
				int x1,y1,x2,y2;
				g.setColor(Color.BLUE);
				for(LinkedList<Line> list: edges.values()) {
					for(Line l: list) {
						if(l != null) {
							x1 = (int) scaleUp( l.getP1().getX() );
							y1 = (int) scaleUp( l.getP1().getY() );
							x2 = (int) scaleUp( l.getP2().getX() );
							y2 = (int) scaleUp( l.getP2().getY() );
							//System.out.println("Point1: (" + x1 + "," + y1 + ") Point2: (" + x1 + "," + y1 +")");
							//					if(! (x1>500 || y1>750 ))
							g.drawLine(x1,translate(y1-4),x2,translate(y2-4));
						} else {
							System.out.println("Null line encountered.");
						}
					}
				}
				g.setColor(Color.RED);
				// draw voronoi vertices
				for(Point p: edges.keySet()) {
					x1 = (int) scaleUp( p.getX() );
					y1 = (int) scaleUp( p.getY() );
					g.fillOval(x1-1,translate(y1-1),4,4);
				}
			}
			
			g.setColor(Color.GREEN);
			// draw sweep line
			g.drawLine(0, translate((int)sweepLine), WINDOW_WIDTH, translate((int)sweepLine));
		}

	}



}

