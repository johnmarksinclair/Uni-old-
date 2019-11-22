package assignment2;

import java.util.*;

public class RouterFlowTable {
	
	ArrayList<Hop> hops;
	
	RouterFlowTable() {
		hops = new ArrayList<Hop>();
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
		
		public int getOut() {
			return this.out;
		}
	}
	
	public void addHop(int dest, int in, int out) {
		hops.add(new Hop(dest, in, out));
	}
	
	public static String toString(RouterFlowTable table) {
		String message = "";
		for (int i = 0; i < table.hops.size(); i++) {
			message += table.hops.get(i).dest + "." + table.hops.get(i).in + "." + table.hops.get(i).out + ".";
		}
		return message;
	}
}
