public class StateAndReward {

	
	/* State discretization function for the angle controller */
	public static String getStateAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */

		int nrOfValues = 8;

		// check if north
		int north = discretize(angle, nrOfValues, -0.75, 0.75);
		int west = discretize(angle, nrOfValues, -2, -0.75);
		int south1 = discretize(angle, nrOfValues, -Math.PI, -2);
		int south2 = discretize(angle, nrOfValues, 2, Math.PI);
		//int east = discretize(angle, nrOfValues, 0.75, 2);
		
		if (nrOfValues-1 > north && north > 0)
			return "North";
		else if (nrOfValues-1 > west && west > 0)
			return "West";
		else if ((nrOfValues-1 > south1 && south1 > 0) ||
			(nrOfValues-1 > south2 && south2 > 0))
			return "South";
		else
			return "East";
	}

	/* Reward function for the angle controller */
	public static double getRewardAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */

		int nrOfValues = 8;

		// check if north
		int north = discretize(angle, nrOfValues, -0.75, 0.75);
		int west = discretize(angle, nrOfValues, -2, -0.75);
		int south1 = discretize(angle, nrOfValues, -Math.PI, -2);
		int south2 = discretize(angle, nrOfValues, 2, Math.PI);
		//int east = discretize(angle, nrOfValues, 0.75, 2);
		
		if (nrOfValues-1 > north && north > 0)
			return 20;
		else if (nrOfValues-1 > west && west > 0)
			return 10;
		else if ((nrOfValues-1 > south1 && south1 > 0) ||
			(nrOfValues-1 > south2 && south2 > 0))
			return 0;
		else
			return 10;
	}

	/* State discretization function for the full hover controller */
	public static String getStateHover(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */

		String state = "OneStateToRuleThemAll2";
		
		return state;
	}

	/* Reward function for the full hover controller */
	public static double getRewardHover(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		
		double reward = 0;

		return reward;
	}

	// ///////////////////////////////////////////////////////////
	// discretize() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 1 and nrValues-2 is returned.
	//
	// Use discretize2() if you want a discretization method that does
	// not handle values lower than min and higher than max.
	// ///////////////////////////////////////////////////////////
	public static int discretize(double value, int nrValues, double min,
			double max) {
		if (nrValues < 2) {
			return 0;
		}

		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * (nrValues - 2)) + 1;
	}

	// ///////////////////////////////////////////////////////////
	// discretize2() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 0 and nrValues-1 is returned.
	// ///////////////////////////////////////////////////////////
	public static int discretize2(double value, int nrValues, double min,
			double max) {
		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * nrValues);
	}

}
