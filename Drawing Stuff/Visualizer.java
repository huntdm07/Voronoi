import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.lang.Math.*;

/**
 *	Class for drawing voronoi diagrams.
 */
public class Visualizer extends JFrame  {
	
	static final int WINDOW_WIDTH = 500;
	static final int WINDOW_HEIGHT = 500;
	private VCanvas canvas;
	private Container window;
	private Graphics gfx;
	
	/**
	 * Default constructor. 500x500 window.
	 */
	public Visualizer()  {
		super("Voronoi Diagram");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create and intialize the pane
		window = this.getContentPane();
		window.setBackground(Color.white);
		window.setVisible(true);	
		
		// create and add canvas to the pane
		initCanvas();
		
		setVisible(true);
		
		
		
		
	}
	
	private void initCanvas()  {
		canvas = new VCanvas(10);
		canvas.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		canvas.setBackground(Color.RED);
		window.add(canvas);
		canvas.setVisible(true);
		
	}
	
	
	public void redraw()  {
		canvas.repaint();
	}
	
	
	
	public static void main(String[] args)  {
		Visualizer v = new Visualizer();
		
	}
	
	/**
	 * The actual canvas object.
	 */
	public class VCanvas extends JPanel  {

		double[] points;
		int parabolas;
		int beachLine;
		int[] origin;
		
		double SCALE_FACTOR;
	
		public VCanvas(int max)  {
			SCALE_FACTOR = WINDOW_WIDTH / max;
			points = new double[(int)(SCALE_FACTOR*max + 1)];
			beachLine = 0;
			origin = new int[2];
			origin[0] = 0; origin[1] = WINDOW_HEIGHT; // bottom left corner
		}
		
		
		public void drawParabola(double a, double b, double c, double startX, double endX)  {
		
			for (int i=(int)(startX*SCALE_FACTOR); i<(int)(endX*SCALE_FACTOR); i++)  {
				if ( i == points.length ) break;
				double x = (double)(i/SCALE_FACTOR);
				if ( i == 250)  {
					System.out.println("a is " + a);
					System.out.println("a double is " + (double)a);
					System.out.println("ax2 " + Math.pow(x, 2)	);
				System.out.println("bx " + b*x);			
				}

				double px = a*Math.pow(x, 2) + b*x + c;
				if ( i == 250 ) System.out.println( "parabola is at " + px + " for x = " + x);
				points[i] = px;
				
			}
		}
		
		public void updateBeachLine(int l)  {
			beachLine = l;
		}
		
		public void paint(Graphics g)  {
			g = (Graphics2D)g;
			g.clearRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
			
			double a = 1.0/10;
			double b =-1;
			double c = 5;
			drawParabola(a, b, c, 0, 10);
			
			
			for (int i=0; i<points.length; i++)  {
				double yd = (double) SCALE_FACTOR * points[i];
				int y = (int)yd; // convert back to canvas coords before plotting
				//g.drawLine( i, origin[1] - y -1, i, origin[1] - y - 1); // thicker line
				if ( i == 0 ) System.out.println("0, " + yd);
				if ( i == points.length - 2) System.out.println(i + ", " + yd);
				g.drawLine( i, origin[1] - y , i, origin[1] - y );
				//g.drawLine( i, origin[1] - y +1, i, origin[1] - y + 1); // thicker line
			}
			
			g.drawLine(0, beachLine, WINDOW_WIDTH, beachLine);
		}
		
	}
}

