package assignment2;

import java.util.ArrayList;

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
	
	public String toString() {
		String message = "";
		for (int i = 0; i < this.hops.size(); i++) {
			message += this.hops.get(i).dest + "." + this.hops.get(i).in + "." + this.hops.get(i).out + ".";
		}
		return message;
	}
}
