package assignment2;

import java.util.ArrayList;

public class ControllerFlowTable {
	
	ArrayList<Route> routes = new ArrayList<Route>();
	int src;
	int dest;
	
	ControllerFlowTable() {
		
	}
	
	public class Route {
		
		ArrayList<Hop> hops = new ArrayList<Hop>();
		int src;
		int dest;
		
		Route() {
			
		}
	}
	
	public class Hop {
		
		int routerPort;
		int in;
		int out;
		
		Hop() {
			
		}
	}
}
