import java.io.*;
import java.util.*;
import geometry.*;

public class Voronoi {

	// priority queue to store the points in order
	private static PriorityQ pq;
	private static double RANGE;
	private static double maxValue;

	public Voronoi(String path) {
		readPoints(path);
	}
	
	public Voronoi(PriorityQ pq, double range, double maxValue) {
		this.pq = pq;
		this.RANGE = range;
		this.maxValue = maxValue;
	}

	/**
	 * Prints the X and Y coordinates of each point in the PQ in the order they would be
	 * processed in.
	 */
	public void printPoints() {
		Event e;
		while(!pq.isEmpty()) {
			e = pq.extractMin();
			System.out.println("{" + e.getPoint().getX() + ", " + e.getPoint().getY()
					+ "}");
		}
	}

	/**
	 * Reads in a list of points from the file at the provided path. The file should be a text
	 * file in which the x-coordinate of a point and the y-coordinate of a point are listed on
	 * the same line separated by a single space, e.g. "10.0 5.5" -> {10.0,5.5}.
	 * @param path the path at which the file with the desired point data is located
	 */
	private static PriorityQ readPoints(String filePath) {
		// store all points in a list temporarily, will be moved to the PQ once (#points) is known
		LinkedList<Event> list = new LinkedList<Event>();

		// store min and max
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		try {
			File f = new File(filePath);
			BufferedReader input = new BufferedReader(new FileReader(f));

			try {
				String line = null;

				while((line = input.readLine()) != null ) {
					// remove extra spaces
					line = line.replaceAll("[ ]{2,}"," ");
					// 
					//line = line.replaceAll(" ","\\n");
					// split the line on the space to obtain the two doubles as strings
					String[] temp = line.split(" ");

					if(temp.length != 2) {
						System.out.println("Format incorrect");
					} else {
						// extract the double values from the strings
						double x = Double.parseDouble(temp[0]);
						double y = Double.parseDouble(temp[1]);
						if(x < min) {
							min = x;
						} else if (y < min) {
							min = y;
						} else if (x > max ) {
							max = x;
						} else if (y > max) {
							max = y;
						}
						
						// enqueue a new point with x and y coords as extracted
						list.add(new Event(new Point(x,y),false));
					}
				}
			}
			finally {
				input.close();
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}

		RANGE = max - min;
		setMaxValue(max);
		
		// knowing how many points there are, create a PQ of the appropriate size
		pq = new PriorityQ( 2 * list.size() );

		// enqueue all of the points on the PQ
		for(int i=0; i<list.size(); i++) {
			pq.insert(list.get(i));
		}

		return pq;
	}
	
	public static void generateRandomInput(int num, int min, int max)  {
	
		pq = new PriorityQ(num);
		
		int range = max - min;
		
		
		for (int i=0; i<num; i++)  {
			double x = (int)(range*java.lang.Math.random()) + min;
			double y = (int)(range*java.lang.Math.random()) + min;
			Point p = new Point( x, y);
			pq.insert( new Event(p, false) );
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// read in the points from a file specified as the argument
		Voronoi voronoi = new Voronoi(args[0]);
//		//pq = readPoints(args[0]);
//		//Fortune f = new Fortune(pq, RANGE);
//		generateRandomInput(100, 0, 100);
//		Visualizer v = new Visualizer(100);
//		//voronoi.printPoints();
//		PriorityQ pqClone = pq.clone();
//		v.storeInputVertices(pqClone);
//		v.redraw();
	}

	public PriorityQ getPq() {
		return pq;
	}

	public static void setPq(PriorityQ pq) {
		Voronoi.pq = pq;
	}

	public static double getRANGE() {
		return RANGE;
	}

	public static void setRANGE(double rANGE) {
		RANGE = rANGE;
	}

	public static double getMaxValue() {
		return maxValue;
	}

	public static void setMaxValue(double maxValue) {
		Voronoi.maxValue = maxValue;
	}
	
}
