package assignment2;

import java.net.*;
import java.util.*;

public class Controller extends Node {

	Terminal terminal;
	public static InetSocketAddress dstAddress;
	public static ArrayList<InetSocketAddress> connectedRouters = new ArrayList<InetSocketAddress>();
	public static ArrayList<Integer> feaReqs = new ArrayList<Integer>();
	InetSocketAddress myAdd;
	ControllerFlowTable flowTable;

	Controller(Terminal terminal, int port) throws Exception {
		this.terminal = terminal;
		this.socket = new DatagramSocket(port);
		this.myAdd = new InetSocketAddress(DEFAULT_DST_NODE, port);
		this.listener.go();
		// terminal.println("My Socket Address: " + myAdd);
		this.flowTable = new ControllerFlowTable();
		this.flowTable.addRoute(USER1_PORT, USER2_PORT); // U1 -> U2
		this.flowTable.addRoute(USER2_PORT, USER1_PORT); // U2 -> U1
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			byte[] data = packet.getData();
			switch (data[TYPE_POS]) {
			case ROUTER_CON:
				terminal.println("Connect request from Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
				socket.send(createPacket(packet, TYPE_CONNECT_ACK, null, null));
				InetSocketAddress address = new InetSocketAddress(DEFAULT_DST_NODE, packet.getPort());
				if (!checkRouters(address)) {
					connectedRouters.add(address);
					System.out.println("Current router addresses:\n" + connectedRouters.toString());
				}
				break;
			case FEA_REQ:
				if (!checkFeaReq(packet.getPort())) {
					terminal.println("Feature request from Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
					feaReqs.add(packet.getPort());
					RouterFlowTable routerTable = tailorTable(packet.getPort());
					String tailoredTable = routerTable.toString();
					socket.send(createPacket(packet, CONTROLLER, tailoredTable.getBytes(), packet.getSocketAddress()));
				}
				break;
			default:
				terminal.println("Unexpected packet" + packet.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkFeaReq(int port) {
		for (int i = 0; i < feaReqs.size(); i++) {
			if (port == feaReqs.get(i)) {
				return true;
			}
		}
		return false;
	}

	public RouterFlowTable tailorTable(int port) {
		RouterFlowTable table = new RouterFlowTable();
		int dest, in, out;
		for (int index = 0; index < flowTable.routes.size(); index++) {
			ControllerFlowTable.Route route = flowTable.routes.get(index);
			for (int i = 0; i < route.hops.size(); i++) {
				if (route.hops.get(i).routerPort == port) {
					dest = route.dest;
					in = route.hops.get(i).in;
					out = route.hops.get(i).out;
					table.addHop(dest, in, out);
				}
			}
		}
		return table;
	}

	public static boolean checkRouters(InetSocketAddress add) {
		for (int i = 0; i < connectedRouters.size(); i++) {
			if (connectedRouters.get(i).equals(add)) {
				return true;
			}
		}
		return false;
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact...");
		this.wait();
	}

	public static void main(String[] args) throws Exception {
		Terminal terminal = new Terminal("Controller");
		Controller controller = new Controller(terminal, CONTROLLER_PORT);
		controller.start();
	}
}
