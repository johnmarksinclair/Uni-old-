package assignment2;

import java.net.*;

public class Router extends Node {
	
	Terminal terminal;
	InetSocketAddress controlAdd;
	InetSocketAddress prevAdd;
	InetSocketAddress myAdd;
	InetSocketAddress nextAdd;

	Router(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal = terminal;
			this.controlAdd = new InetSocketAddress(dstHost, dstPort);
			this.myAdd = new InetSocketAddress(dstHost, srcPort);
			if (srcPort == FIRST_ROUTER_PORT) {
				this.prevAdd = new InetSocketAddress(dstHost, USER1_PORT);
				this.nextAdd = new InetSocketAddress(dstHost, srcPort+1);
			} else if (srcPort == LAST_ROUTER_PORT) {
				this.prevAdd = new InetSocketAddress(dstHost, srcPort-1);
				this.nextAdd = new InetSocketAddress(dstHost, USER2_PORT);
			} else {
				this.prevAdd = new InetSocketAddress(dstHost, srcPort-1);
				this.nextAdd = new InetSocketAddress(dstHost, srcPort+1);
			}
			//terminal.println("My Socket Address: " + this.myAdd);
			socket = new DatagramSocket(srcPort);
			listener.go();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
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
				terminal.println("Received packet from User " + (packet.getPort() - USER1_PORT + 1));
				terminal.println("Forwarding...");
				socket.send(createPacket(packet, TYPE_ACK, null, null));
				socket.send(createPacket(packet, ROUTER, getByteContent(packet), nextAdd));
				break;
			case ROUTER:
				terminal.println("Received packet from Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
				terminal.println("Forwarding...");
				socket.send(createPacket(packet, TYPE_ACK, null, null));
				socket.send(createPacket(packet, ROUTER, getByteContent(packet), nextAdd));
				break;
			case CONTROLLER:
				break;
			default:
				terminal.println("Message received: " + getStringContent(packet));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void contactController() throws Exception {
		byte[] data = null;
		String input = "Connect me";
		byte[] message = input.getBytes();
		DatagramPacket packet = null;
		data = new byte[HEADER_LENGTH + message.length];
		data[TYPE_POS] = ROUTER_CON;
		data[LENGTH_POS] = (byte) message.length;
		System.arraycopy(message, 0, data, HEADER_LENGTH, message.length);
		terminal.println("Connecting to Controller...");
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(controlAdd);
		socket.send(packet);
	}

	public static void main(String[] args) {
		try {
			for (int i = 0; i < NO_OF_ROUTERS; i++) {
				Terminal terminal = new Terminal("Router " + (i + 1));
				Router router = new Router(terminal, DEFAULT_DST_NODE, CONTROLLER_PORT, FIRST_ROUTER_PORT + i);
				router.contactController();
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
