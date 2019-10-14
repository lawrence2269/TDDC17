public class StateAndReward {
    private static final double ANGLE_MAX_VALUE = 2;
    private static final int ANGLE_NR_VALUES = 20;

    private static final int VY_MAX_VALUE = 2;
    private static final int VY_NR_VALUES = 2;

    private static final int VX_MAX_VALUE = 1;
    private static final int VX_NR_VALUES = 4;


    /* State discretization function for the angle controller */
    public static String getStateAngle(double angle, double vx, double vy)
    {
	    String state = String.valueOf(discretize(angle,
						 ANGLE_NR_VALUES,
						 -ANGLE_MAX_VALUE,
						 ANGLE_MAX_VALUE));
	    return state;
    }

    /* Reward function for the angle controller */
    public static double getRewardAngle(double angle, double vx, double vy)
    {
	    return getReward(angle, ANGLE_MAX_VALUE);
    }

    /* State discretization function for the full hover controller */
    public static String getStateHover(double angle, double vx, double vy)
    {
        String angle_state, vx_state, vy_state = "";

        angle_state = String.valueOf(discretize(angle, ANGLE_NR_VALUES, -ANGLE_MAX_VALUE, ANGLE_MAX_VALUE));
        vx_state = String.valueOf(discretize(vx, VX_NR_VALUES, -VX_MAX_VALUE, VX_MAX_VALUE));
        vy_state = String.valueOf(discretize(vy, VY_NR_VALUES, -VY_MAX_VALUE, VY_MAX_VALUE));          

        return angle_state + vx_state + vy_state;
    }

    /* Reward function for the full hover controller */
    public static double getRewardHover(double angle, double vx, double vy)
    {
        double angle_reward, vx_reward, vy_reward = 0;

        angle_reward = getReward(angle, ANGLE_MAX_VALUE);
        vx_reward = getReward(vx, VX_MAX_VALUE);
        vy_reward = getReward(vy, VY_MAX_VALUE);
        
        return angle_reward + vx_reward + vy_reward;
    }

    private static double getReward(double value, double max_value)
    {
        if (Math.abs(value) > max_value)
            return 0;
        return Math.pow(max_value - Math.abs(value), 2) * 20;
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
