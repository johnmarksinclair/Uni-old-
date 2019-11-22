package assignment2;

import java.net.*;
import java.util.*;

public class Controller extends Node {

	Terminal terminal;
	public static InetSocketAddress dstAddress;
	public static ArrayList<SocketAddress> connectedRouters = new ArrayList<SocketAddress>();
	InetSocketAddress myAdd;
	ControllerFlowTable flowTable;

	Controller(Terminal terminal, int port) {
		try {
			this.terminal = terminal;
			this.socket = new DatagramSocket(port);
			this.myAdd = new InetSocketAddress(DEFAULT_DST_NODE, port);
			//terminal.println("My Socket Address: " + myAdd);
			this.flowTable = new ControllerFlowTable(USER1, USER2);
			this.listener.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			byte[] data = packet.getData();
			switch (data[TYPE_POS]) {
			case ROUTER_CON:
				terminal.println("Connect request from Router");
				socket.send(createPacket(packet, TYPE_CONNECT_ACK, null, null));
				SocketAddress address = packet.getSocketAddress();
				if (!checkRouters(address)) {
					connectedRouters.add(packet.getSocketAddress());
					System.out.println("Current router addresses:\n" + connectedRouters.toString());
				}
				break;
			case ROUTER:
				break;
			default:
				terminal.println("Unexpected packet" + packet.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checkRouters(SocketAddress add) {
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
