package assignment2;

import java.util.*;

public class RouterFlowTable {
	
	ArrayList<Hop> hops;
	int dest;
	int in;
	int out;
	
	RouterFlowTable(int dest, int in, int out) {
		this.dest = dest;
		this.in = in;
		this.out = out;
	}
	
	public class Hop {

		int dest;
		int in;
		int out;

		Hop(int dest, int in, int out) {
			this.dest = dest;
			this.in = in;
			this.out = out;
		}
	}
}