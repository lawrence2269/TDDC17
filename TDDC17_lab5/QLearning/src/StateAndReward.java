public class StateAndReward {
    private static final double ANGLE_MAX_VALUE = Math.PI;
    private static final double ANGLE_MIN_VALUE = -Math.PI;
    private static final int ANGLE_NR_VALUES = 10;

    private static final int VY_MAX_VALUE = 2;
    private static final int VY_MIN_VALUE = -2;
    private static final int VY_NR_VALUES = 2;

    private static final int VX_MAX_VALUE = 10;
    private static final int VX_MIN_VALUE = -10;
    private static final int VX_NR_VALUES = 10;

    private static final double HOVER_MAX_VALUE = Math.PI;
    private static final double HOVER_MIN_VALUE = -Math.PI;
    private static final int HOVER_NR_VALUES = 100;

    /* State discretization function for the angle controller */
    public static String getStateAngle(double angle, double vx, double vy)
    {
	    String state = String.valueOf(discretize2(angle,
						 ANGLE_NR_VALUES,
						 ANGLE_MIN_VALUE,
						 ANGLE_MAX_VALUE));
	    return state;
    }

    /* Reward function for the angle controller */
    public static double getRewardAngle(double angle, double vx, double vy)
    {
	    return Math.PI/Math.abs(angle);
    }

    /* State discretization function for the full hover controller */
    public static String getStateHover(double angle, double vx, double vy)
    {
        /* TODO: IMPLEMENT THIS FUNCTION */
        String angle_state = "";
        String vx_state = "";
        String vy_state = "";

        angle_state = String.valueOf(discretize2(angle, HOVER_NR_VALUES, HOVER_MIN_VALUE, HOVER_MAX_VALUE));
        vx_state = String.valueOf(discretize(vx, VX_NR_VALUES, VX_MIN_VALUE, VX_MAX_VALUE));
        vy_state = String.valueOf(discretize(vy, VY_NR_VALUES, VY_MIN_VALUE, VY_MAX_VALUE));          

        return angle_state + vx_state + vy_state;
    }

    /* Reward function for the full hover controller */
    public static double getRewardHover(double angle, double vx, double vy)
    {
        /* TODO: IMPLEMENT THIS FUNCTION */
        return Math.PI/Math.abs(angle) + VX_MAX_VALUE/Math.abs(vx) + VY_MAX_VALUE/Math.abs(vy);
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
				 double max)
    {
        if (nrValues < 2)
        {
            return 0;
        }

        double diff = max - min;

        if (value < min)
        {
            return 0;
        }
        if (value > max)
        {
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
				  double max)
    {
        double diff = max - min;

        if (value < min)
        {
            return 0;
        }
        if (value > max)
        {
            return nrValues - 1;
        }

        double tempValue = value - min;
        double ratio = tempValue / diff;

        return (int) (ratio * nrValues);
    }
}
