import geometry.*;

public class VoronoiTest {

	public static PriorityQ generateRandomInput(int num, int min, int max)  {
		
		PriorityQ pq = new PriorityQ(num);
		int range = max - min;
		
		System.out.println("RANDOM POINTS>>>>>>>>>>>>>>>>>>>");
		for (int i=0; i<num; i++)  {
			double x = (range*java.lang.Math.random());
			double y = (range*java.lang.Math.random());
			Event p = new Event(new Point(x,y),false);

			//System.out.println(x + "," + y);
			pq.insert( p );
		}
		System.out.println("End of Random points>>>>>>>>>>>>>>>");
		System.out.println();
		
		return pq;	
	}
	
	/**
	 * If the input is greater than length 5, it is presumed to be a file name.
	 * If it is less than or equal to length 5, it is presumed to be an integer.
	 * @param args
	 */
	public static void main(String[] args) {
//		if ( args[0].length() > 5 ) {
//			System.out.println("Reading points in from: " + args[0]);
//			// this will read in the points from the file in the argument and put them into the PQ
//			Voronoi v = new Voronoi(args[0]);
//			v.printPoints();
//		} else {
			int numPoints = Integer.parseInt(args[0]);
			PriorityQ pq = generateRandomInput(numPoints,0,750);
			Voronoi v = new Voronoi(pq,750.0,750.0);
			Fortune f = new Fortune(v);
		//}
		
		
		
	}

}
