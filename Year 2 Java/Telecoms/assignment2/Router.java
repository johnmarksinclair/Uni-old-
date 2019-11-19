package assignment2;

import java.net.*;
import java.util.*;

public class Router extends Node {
	
	static final String DEFAULT_DST_NODE = "localhost"; // Name of the host for the server
	
	Terminal terminal;
	InetSocketAddress dstAddress;
	static ArrayList<Router> routers = new ArrayList<Router>();

	Router(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal = terminal;
			dstAddress = new InetSocketAddress(dstHost, dstPort);
			socket = new DatagramSocket(srcPort);
			listener.go();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			String content;
			byte[] data;
			byte[] buffer;
			data = packet.getData();
			buffer = new byte[data[LENGTH_POS]];
			System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
			content = new String(buffer);
			terminal.println("Message received: " + content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connectToController() throws Exception {
		byte[] data = null;
		String input = "Connect me";
		byte[] message = input.getBytes();
		DatagramPacket packet = null;
		
		data = new byte[HEADER_LENGTH + message.length];
		data[TYPE_POS] = ROUTER;
		data[LENGTH_POS] = (byte) message.length;
		System.arraycopy(message, 0, data, HEADER_LENGTH, message.length);

		terminal.println("Connecting to Controller...");
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(dstAddress);
		socket.send(packet);
	}

	public static void main(String[] args) {
		try {
			for (int i = 0; i < NO_OF_ROUTERS; i++) {
				Terminal terminal = new Terminal("Router " + (i + 1));
				Router router = new Router(terminal, DEFAULT_DST_NODE, CONTROLLER_PORT, FIRST_ROUTER_PORT + i);
				router.connectToController();
				routers.add(router);
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}