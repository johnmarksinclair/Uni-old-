package assignment2;

import java.net.*;

public class Controller extends Node {

	Terminal terminal;
	public static InetSocketAddress dstAddress;
	InetSocketAddress myAdd;
	ControllerFlowTable flowTable;

	Controller(Terminal terminal, int port) throws Exception {
		this.terminal = terminal;
		this.socket = new DatagramSocket(port);
		this.myAdd = new InetSocketAddress(DEFAULT_DST_NODE, port);
		this.listener.go();
		this.flowTable = new ControllerFlowTable();
		this.flowTable.addRoute(USER1_PORT, USER2_PORT);
		this.flowTable.addRoute(USER2_PORT, USER1_PORT);
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) throws Exception {
		byte[] data = packet.getData();
		switch (data[TYPE_POS]) {
		case ROUTER_CON:
			terminal.println("Connect request from Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
			socket.send(createPacket(packet, TYPE_CONNECT_ACK, null, null));
			break;
		case FEA_REQ:
			terminal.println("Feature request from Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
			String tailoredTable = tailorTable(packet.getPort()).toString();
			socket.send(createPacket(packet, CONTROLLER, tailoredTable.getBytes(), packet.getSocketAddress()));
			break;
		default:
			terminal.println("Unexpected packet" + packet.toString());
		}
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
