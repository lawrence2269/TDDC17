public class TutorialController extends Controller {

    public SpringObject object;

    ComposedSpringObject cso;

    /* These are the agents senses (inputs) */
	DoubleFeature x; /* Positions */
	DoubleFeature y;
	DoubleFeature vx; /* Velocities */
	DoubleFeature vy;
	DoubleFeature angle; /* Angle */

    /* Example:
     * x.getValue() returns the vertical position of the rocket 
     */

	/* These are the agents actuators (outputs)*/
	RocketEngine leftRocket;
	RocketEngine middleRocket;
	RocketEngine rightRocket;

    /* Example:
     * leftRocket.setBursting(true) turns on the left rocket 
     */
	
	public void init() {
		cso = (ComposedSpringObject) object;
		x = (DoubleFeature) cso.getObjectById("x");
		y = (DoubleFeature) cso.getObjectById("y");
		vx = (DoubleFeature) cso.getObjectById("vx");
		vy = (DoubleFeature) cso.getObjectById("vy");
		angle = (DoubleFeature) cso.getObjectById("angle");

		leftRocket = (RocketEngine) cso.getObjectById("rocket_engine_left");
		rightRocket = (RocketEngine) cso.getObjectById("rocket_engine_right");
		middleRocket = (RocketEngine) cso.getObjectById("rocket_engine_middle");
	}

    public void tick(int currentTime) {
		System.out.println("currentTime :" + 
		"x = " + Math.round(x.getValue()) + ", y = " + Math.round(y.getValue()) + 
		"\n vx = " + Math.round(vx.getValue()) + ", vy = " + Math.round(vy.getValue()) + 
		"\n angle = " + Math.round(angle.getValue()));

		
		if (vy.getValue() > 0)
		{
			rightRocket.setBursting(true);
			leftRocket.setBursting(true);
			middleRocket.setBursting(true);
			
		} else {
			rightRocket.setBursting(false);
			leftRocket.setBursting(false);
			middleRocket.setBursting(false);
		}
    }

}
