package assignment2;

import java.util.ArrayList;

public class ControllerFlowTable {

	ArrayList<Route> routes = new ArrayList<Route>();
	int src;
	int dest;

	ControllerFlowTable(int src, int dest) {
		this.src = src;
		this.dest = dest;
		Route route = new Route(src, dest);
		routes.add(route);
	}

	public class Route {

		ArrayList<Hop> hops = new ArrayList<Hop>();
		int src;
		int dest;

		Route(int srcPort, int destPort) {
			this.src = srcPort;
			this.dest = destPort;
			Hop one = new Hop(Node.FIRST_ROUTER_PORT, srcPort, Node.FIRST_ROUTER_PORT + 1);
			Hop two = new Hop(Node.FIRST_ROUTER_PORT + 1, Node.FIRST_ROUTER_PORT, Node.FIRST_ROUTER_PORT + 2);
			Hop three = new Hop(Node.FIRST_ROUTER_PORT + 2, Node.FIRST_ROUTER_PORT + 1, destPort);
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
