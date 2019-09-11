package tddc17;

import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
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
				world[i][j] = UNKNOWN;
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
	public MyAgentState state = new MyAgentState();
	private Queue<Integer> actions = new LinkedList<Integer>();
	private ArrayList<Coordinates> path = new ArrayList<Coordinates>();
	private boolean is_going_home = false;
	public int iterationCounter = state.world.length * state.world[0].length * 2;

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

	private void generate_actions()
	{
		Coordinates current_node = this.path.remove(0);

		if (current_node.x > state.agent_x_position)
		{
			move_east();
		}
		else if (current_node.x < state.agent_x_position)
		{
			move_west();
		}
		else if (current_node.y < state.agent_y_position)
		{
			move_north();
		}
		else if (current_node.y > state.agent_y_position)
		{
			move_south();
		}
		this.actions.add(state.ACTION_MOVE_FORWARD);
	}

	private void move_east()
	{
		switch(state.agent_direction)
		{
			case MyAgentState.SOUTH:
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
			case MyAgentState.NORTH:
				this.actions.add(state.ACTION_TURN_RIGHT);
				break;
			case MyAgentState.WEST:
				this.actions.add(state.ACTION_TURN_LEFT);
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
		}
	}

	private void move_west()
	{
		switch(state.agent_direction)
		{
			case MyAgentState.SOUTH:
				this.actions.add(state.ACTION_TURN_RIGHT);
				break;
			case MyAgentState.NORTH:
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
			case MyAgentState.EAST:
				this.actions.add(state.ACTION_TURN_LEFT);
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
		}
	}

	private void move_north()
	{
		switch(state.agent_direction)
		{
			case MyAgentState.SOUTH:
				this.actions.add(state.ACTION_TURN_LEFT);
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
			case MyAgentState.EAST:
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
			case MyAgentState.WEST:
				this.actions.add(state.ACTION_TURN_RIGHT);
				break;
		}
	}

	private void move_south()
	{
		switch(state.agent_direction)
		{
			case MyAgentState.NORTH:
				this.actions.add(state.ACTION_TURN_LEFT);
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
			case MyAgentState.EAST:
				this.actions.add(state.ACTION_TURN_RIGHT);
				break;
			case MyAgentState.WEST:
				this.actions.add(state.ACTION_TURN_LEFT);
				break;
		}
	}

	// BFS
	private Boolean find_closest_position(int goal)
	{
		Queue<Coordinates> frontier = new LinkedList<Coordinates>();
		HashMap<Coordinates, Coordinates> explored = new HashMap<Coordinates, Coordinates>();
		Coordinates start_node = new Coordinates(state.agent_x_position, state.agent_y_position);
		frontier.add(start_node);
		explored.put(start_node, null);

		while (!frontier.isEmpty())
		{
			Coordinates current_node = frontier.remove();

			if (state.world[current_node.x][current_node.y] == goal)
				return generate_path(current_node, explored);

			for (int dx = -1; dx <= 1; dx++)
			{
				for (int dy = -1; dy <= 1; dy++)
				{
					Coordinates neighbor = new Coordinates(current_node.x + dx, current_node.y + dy);
					if ((Math.abs(dx) != Math.abs(dy) && !explored.containsKey(neighbor))
							&& state.world[neighbor.x][neighbor.y] != state.WALL)
					{
						explored.put(neighbor, current_node);
						frontier.add(neighbor);
					}
				}
			}
		}
		return false;
	}

	private Boolean generate_path(Coordinates end_node, HashMap<Coordinates, Coordinates> explored)
	{
		this.path.add(0, end_node);
		Coordinates current_node = explored.get(end_node);
		while (current_node != null)
		{
			this.path.add(0, current_node);
			current_node = explored.get(current_node);
		}
		this.path.remove(0);
		return !this.path.isEmpty();
	}

	private Action perform_action()
	{
		// pickup an action from the list and perform it
		int action = this.actions.remove();
		if (action == state.ACTION_MOVE_FORWARD)
			return move_forward();
		else if (action == state.ACTION_TURN_RIGHT)
			return turn_right();
		else if (action == state.ACTION_TURN_LEFT)
			return turn_left();
		else
			System.out.println("Doing nothing");
			return NoOpAction.NO_OP;
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

	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept)
	{
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
	public Action execute(Percept percept)
	{
		// DO NOT REMOVE this if condition!!!
    	if (initnialRandomActions>0)
    		return moveToRandomStartPosition((DynamicPercept) percept);
		else if (initnialRandomActions==0) 
		{
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
	    if (bump)
		{
			switch (state.agent_direction)
			{
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

		// verify that we do not overwrite the home position
		if (state.world[state.agent_x_position][state.agent_y_position] != state.HOME)
		{
			if (dirt)
				state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
			else
				state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
		}

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
			if (is_going_home && home)
			{
				return NoOpAction.NO_OP;
			}

	    	if(this.actions.isEmpty() && this.path.isEmpty())
			{
				if (!find_closest_position(state.UNKNOWN))
				{
					System.out.println("Going home");
					this.is_going_home = true;
					find_closest_position(state.HOME);
				}
				System.out.println("Generated new path");
				for (int i = 0; i < this.path.size(); i++)
					System.out.println("path_" + i + "=(" + this.path.get(i).x + "," + this.path.get(i).y + ")");
			}

			if (this.actions.isEmpty())
				generate_actions();

			return perform_action();
	    }
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}
