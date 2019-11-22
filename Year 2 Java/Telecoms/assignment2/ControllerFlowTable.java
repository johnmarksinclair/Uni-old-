package assignment2;

import java.util.ArrayList;

public class ControllerFlowTable {

	ArrayList<Route> routes;

	ControllerFlowTable() {
		routes = new ArrayList<Route>();
	}
	
	public void addRoute(int src, int dest) {
		routes.add(new Route(src, dest));
	}

	public class Route {

		ArrayList<Hop> hops = new ArrayList<Hop>();
		int src;
		int dest;

		Route(int srcPort, int destPort) {
			this.src = srcPort;
			this.dest = destPort;
			Hop one, two, three;
			if (srcPort == Node.USER1_PORT) { // U1 -> U2
				one = new Hop(Node.FIRST_ROUTER_PORT, srcPort, Node.FIRST_ROUTER_PORT + 1);
				two = new Hop(Node.FIRST_ROUTER_PORT + 1, Node.FIRST_ROUTER_PORT, Node.FIRST_ROUTER_PORT + 2);
				three = new Hop(Node.FIRST_ROUTER_PORT + 2, Node.FIRST_ROUTER_PORT + 1, destPort);
			} else { // U2 -> U1
				one = new Hop(Node.LAST_ROUTER_PORT, srcPort, Node.LAST_ROUTER_PORT - 1);
				two = new Hop(Node.LAST_ROUTER_PORT - 1, Node.LAST_ROUTER_PORT, Node.FIRST_ROUTER_PORT);
				three = new Hop(Node.FIRST_ROUTER_PORT, Node.LAST_ROUTER_PORT - 1, destPort);
			}
			hops.add(one);
			hops.add(two);
			hops.add(three);
		}
	}

	public class Hop {

		int routerPort;
		int in;
		int out;

		Hop(int routerPort, int in, int out) {
			this.routerPort = routerPort;
			this.in = in;
			this.out = out;
		}
	}
}
