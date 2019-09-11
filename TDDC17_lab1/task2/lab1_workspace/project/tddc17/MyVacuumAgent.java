package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.ArrayList;
import java.util.Random;

class MyAgentState
{
	public int[][] world = new int[30][30];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final int ACTION_NONE 			= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;
	
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;
	
	MyAgentState()
	{
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
			{
				if (i == 0 || j == 0)
					world[i][j] = WALL;
				else
					world[i][j] = UNKNOWN;
			}
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p)
	{
		Boolean bump = (Boolean)p.getAttribute("bump");

		if (agent_last_action==ACTION_MOVE_FORWARD && !bump)
	    {
			switch (agent_direction) {
			case MyAgentState.NORTH:
				agent_y_position--;
				break;
			case MyAgentState.EAST:
				agent_x_position++;
				break;
			case MyAgentState.SOUTH:
				agent_y_position++;
				break;
			case MyAgentState.WEST:
				agent_x_position--;
				break;
			}
	    }
		
	}
	
	public void updateWorld(int x_position, int y_position, int info)
	{
		world[x_position][y_position] = info;
	}
	
	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++)
		{
			for (int j=0; j < world[i].length ; j++)
			{
				if (world[j][i]==UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" . ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private int initnialRandomActions = 10;
	private Random random_generator = new Random();
	
	// Here you can define your variables!
	public int iterationCounter = 20 * 20 * 2;
	public MyAgentState state = new MyAgentState();
	private int max_x = 0;
	private int max_y = 0;
	private boolean is_going_home = false;
	ArrayList<Integer> action_list = new ArrayList<Integer>();
	
	private class Coordinates {
		// A class that is used to be able to return the x
		// and y coordinates of a specific position
		private int x;
		private int y;
		
		Coordinates(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initnialRandomActions--;
		state.updatePosition(percept);
		if(action==0) {
		    state.agent_direction = ((state.agent_direction-1) % 4);
		    if (state.agent_direction<0) 
		    	state.agent_direction +=4;
		    state.agent_last_action = state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		} else if (action==1) {
			state.agent_direction = ((state.agent_direction+1) % 4);
		    state.agent_last_action = state.ACTION_TURN_RIGHT;
		    return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		} 
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}
	
	
	@Override
	public Action execute(Percept percept) {
		
		// DO NOT REMOVE this if condition!!!
    	if (initnialRandomActions>0) {
    		return moveToRandomStartPosition((DynamicPercept) percept);
    	} else if (initnialRandomActions==0) {
    		// process percept for the last step of the initial random actions
    		initnialRandomActions--;
    		state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
    	}
		
    	// This example agent program will update the internal agent state while only moving forward.
    	// START HERE - code below should be modified!
    	    	
    	System.out.println("x=" + state.agent_x_position);
    	System.out.println("y=" + state.agent_y_position);
    	System.out.println("dir=" + state.agent_direction);
    	
		
	    iterationCounter--;
	    
	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    System.out.println("percept: " + p);
	    
	    // State update based on the percept value and the last action
	    state.updatePosition((DynamicPercept)percept);
	    if (bump) {
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				break;
			case MyAgentState.EAST:
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				break;
			}
	    }
	    if (dirt)
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    // verify that we do not overwrite the home position
	    else if (state.world[state.agent_x_position][state.agent_y_position] != state.HOME)
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	    
	    state.printWorldDebug();
	    
	    
	    // Next action selection based on the percept value
	    if (dirt)
	    {
	    	System.out.println("DIRT -> choosing SUCK action!");
	    	state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
	    } 
	    else
	    {
	    	// stop executing if we do not have any actions to execute
	    	// and we know that we are done
	    	if (action_list.size() == 0 && is_going_home)
	    	{
	    		return NoOpAction.NO_OP;
	    	}
	    	else 
	    	{
	    		if (bump)
	    		{
	    			// if we bumped and are facing east then we must have found 
	    			// the east wall, so mark all positions beyond that wall as
	    			// walls too
	    			if (state.agent_direction == MyAgentState.EAST)
	    			{
	    				for (int i = state.agent_x_position + 1; i < state.world.length; i++) 
	    				{
	    					for (int j = 0; j < state.world.length; j++)
	    						state.world[i][j] = state.WALL;
	    				}
	    			}
	    			// if we bumped and are facing south then we must have found 
	    			// the south wall, so mark all positions beyond that wall as
	    			// walls too
	    			if (state.agent_direction == MyAgentState.SOUTH)
	    			{
	    				for (int i = 0; i < state.world.length; i++) 
	    				{
	    					for (int j = state.agent_y_position + 1; j < state.world.length; j++)
	    						state.world[i][j] = state.WALL;
	    				}
	    			}	
		    	}
	    		// if we do not have any actions to perform,
	    		// then generate new ones
	    		if (action_list.size() == 0)
	    		{
	    			generate_actions();
	    		}
	    		// perform one of the actions in the action list
	    		return perform_action();
	    	}
	    }
	}
	
	private void generate_actions() 
	{
		Coordinates closest_pos = find_closest_unknown_position();
		// since the default is (1, 1) we can use that to know when
		// we are supposed to go home
		if (closest_pos.x == 1 && closest_pos.y == 1)
			is_going_home = true;
		// generate actions for getting to the position in both
		// the y-axis and x-axis
		int direction = generate_y_actions(closest_pos);
		generate_x_actions(direction, closest_pos);
	}
	
	private int generate_y_actions(Coordinates closest_pos)
	{
		// the direction is used to tell generate_x_actions
		// which direction we are facing after running this
		// function, the default is current direction
		int direction = state.agent_direction;
		// if the destination's y-coordinate is greater than
		// the agent's, then we must move south
		if (closest_pos.y > state.agent_y_position)
		{
			direction = MyAgentState.SOUTH;
			if (state.agent_direction == MyAgentState.EAST) 
				action_list.add(state.ACTION_TURN_RIGHT);
			else if (state.agent_direction == MyAgentState.WEST) 
				action_list.add(state.ACTION_TURN_LEFT);
			else if (state.agent_direction == MyAgentState.NORTH) 
			{
				for (int i = 0; i < 2; i++)
				{
					action_list.add(state.ACTION_TURN_LEFT);
				}
				
			}
		}
		// if the destination's y-coordinate is less than
		// the agent's, then we must move north
		else if (closest_pos.y < state.agent_y_position)
		{
			direction = MyAgentState.NORTH;
			if (state.agent_direction == MyAgentState.EAST) 
				action_list.add(state.ACTION_TURN_LEFT);
			else if (state.agent_direction == MyAgentState.WEST) 
				action_list.add(state.ACTION_TURN_RIGHT);
			else if (state.agent_direction == MyAgentState.SOUTH) 
			{
				for (int i = 0; i < 2; i++)
					action_list.add(state.ACTION_TURN_LEFT);
			}
		}
		
		// add the actions to move forward until we reach the destination's
		// y position
		for (int i = 0; i < Math.abs(closest_pos.y - state.agent_y_position); i++)
			action_list.add(state.ACTION_MOVE_FORWARD);
		
		return direction;
	}
	
	private void generate_x_actions(int direction, Coordinates closest_pos)
	{
		// if the destination's x-coordinate is greater than
		// the agent's, then we must move east
		if (closest_pos.x > state.agent_x_position)
		{
			if (direction == MyAgentState.NORTH)
				action_list.add(state.ACTION_TURN_RIGHT);
			else if (direction == MyAgentState.SOUTH)
				action_list.add(state.ACTION_TURN_LEFT);
			else if (direction == MyAgentState.WEST) 
			{
				for (int i = 0; i < 2; i++)
					action_list.add(state.ACTION_TURN_LEFT);
			}
		}
		// if the destination's x-coordinate is greater than
		// the agent's, then we must move west
		else if (closest_pos.x < state.agent_x_position) 
		{
			if (direction == MyAgentState.NORTH)
				action_list.add(state.ACTION_TURN_LEFT);
			else if (direction == MyAgentState.SOUTH)
				action_list.add(state.ACTION_TURN_RIGHT);
			else if (direction == MyAgentState.EAST) 
			{
				for (int i = 0; i < 2; i++)
					action_list.add(state.ACTION_TURN_LEFT);
			}
		}
		
		// add the actions to move forward until we reach the destination's
		// x position
		for (int i = 0; i < Math.abs(closest_pos.x - state.agent_x_position); i++)
			action_list.add(state.ACTION_MOVE_FORWARD);
	}
	
	private Coordinates find_closest_unknown_position() 
	{
		// min distance is set to the maximum distance that
		// any of the worlds allow which is 20 in the x-axis
		// and 20 in the y-axis
		int min_distance = 20 * 2;
		// set x and y as defaults to the home position
		int x = 1;
		int y = 1;
		for (int i = 0; i < state.world.length; i++)
		{
			for (int j = 0; j < state.world.length; j++)
			{
				if (state.world[i][j] == state.UNKNOWN)
				{
					int x_diff = Math.abs(i - state.agent_x_position);
					int y_diff = Math.abs(j - state.agent_y_position);
					
					// check if the sum of the distance in the y-direction
					// and x-direction is less than the currently known
					// minimum distance
					if (x_diff + y_diff < min_distance)
					{
						min_distance = x_diff + y_diff;
						x = i;
						y = j;
					}
				}
			}
		}
		return new Coordinates(x, y);
	}
	
	private Action perform_action() 
	{
		// pickup an action from the list and perform it
		int action = action_list.get(0);
		action_list.remove(0);
		if (action == state.ACTION_MOVE_FORWARD)
			return move_forward();
		else if (action == state.ACTION_TURN_RIGHT)
			return turn_right();
		else
			return turn_left();
	}
	
	private Action move_forward()
	{
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}
	
	private Action turn_left()
	{
		state.agent_direction = ((state.agent_direction+3) % 4);
		state.agent_last_action=state.ACTION_TURN_LEFT;
		return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	}
	
	private Action turn_right()
	{
		state.agent_direction = ((state.agent_direction+1) % 4);
		state.agent_last_action=state.ACTION_TURN_RIGHT;
		return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}