package assignment2;

import java.io.*;
import java.net.*;
import java.util.regex.Pattern;

public class Router extends Node {

	Terminal terminal;
	InetSocketAddress controlAdd;
	InetSocketAddress myAdd;
	InetSocketAddress nextAdd;
	RouterFlowTable myTable;
	byte[] content;
	static int from = 0;
	boolean hasReq = false;

	Router(Terminal terminal, String dstHost, int dstPort, int srcPort) throws Exception {
		this.terminal = terminal;
		this.controlAdd = new InetSocketAddress(dstHost, dstPort);
		this.myAdd = new InetSocketAddress(dstHost, srcPort);
		socket = new DatagramSocket(srcPort);
		listener.go();
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) throws Exception {
		byte[] data = packet.getData();
		switch (data[TYPE_POS]) {
		case TYPE_CONNECT_ACK:
			terminal.println("Connected to Controller");
			break;
		case TYPE_ACK:
			terminal.println("Successfully forwarded to Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
			break;
		case TYPE_USER_ACK:
			terminal.println("Successfully forwarded to User " + (packet.getPort() - USER1_PORT + 1));
			break;
		case USER1:
		case USER2:
		case ROUTER:
			if (data[TYPE_POS] == ROUTER) {
				terminal.println("Received packet from Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
			} else {
				terminal.println("Received packet from User " + (packet.getPort() - USER1_PORT + 1));
				if (data[TYPE_POS] == USER1)
					Router.from = 0;
				else
					Router.from = 1;
			}
			content = getByteContent(packet);
			socket.send(createPacket(packet, TYPE_ACK, null, null));
			if (!hasReq) {
				terminal.println("Requesting flow table...");
				socket.send(createPacket(packet, FEA_REQ, null, null));
				this.hasReq = true;
			} else {
				updateInfo();
				forwardPacket(packet);
			}
			break;
		case CONTROLLER:
			terminal.println("Received routing info");
			myTable = getTable(packet);
			updateInfo();
			forwardPacket(packet);
			break;
		default:
			terminal.println("Message received: " + getStringContent(packet));
		}
	}

	public void updateInfo() {
		this.nextAdd = new InetSocketAddress(DEFAULT_DST_NODE, myTable.hops.get(from).getOut());
	}

	public void forwardPacket(DatagramPacket packet) throws IOException {
		terminal.println("Forwarding...");
		socket.send(createPacket(packet, ROUTER, content, nextAdd));
	}

	public RouterFlowTable getTable(DatagramPacket packet) {
		RouterFlowTable table = new RouterFlowTable();
		String content = getStringContent(packet);
		System.out.println(terminal.name + " Flow Table: " + content);
		String[] partitioned = content.split(Pattern.quote("."));
		int dest, in, out;
		int i = 0;
		while (i < partitioned.length) {
			dest = Integer.valueOf(partitioned[i]);
			in = Integer.valueOf(partitioned[i + 1]);
			out = Integer.valueOf(partitioned[i + 2]);
			table.addHop(dest, in, out);
			i += 3;
		}
		return table;
	}

	public synchronized void contactController() throws Exception {
		socket.send(createPacket(null, ROUTER_CON, "Connect me".getBytes(), controlAdd));
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < NO_OF_ROUTERS; i++) {
			Terminal terminal = new Terminal("Router " + (i + 1));
			Router router = new Router(terminal, DEFAULT_DST_NODE, CONTROLLER_PORT, FIRST_ROUTER_PORT + i);
			router.contactController();
		}
	}
}
