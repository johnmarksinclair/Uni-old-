package assignment2;

import java.net.*;

public class Router extends Node {
	
	public static Terminal terminal;
	public static InetSocketAddress dstAddress;
	public static InetSocketAddress port;
	public static InetSocketAddress nextPort;

	Router(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			Router.terminal = terminal;
			Router.dstAddress = new InetSocketAddress(dstHost, dstPort);
			Router.port = new InetSocketAddress(dstHost, srcPort);
			if (srcPort == LAST_ROUTER_PORT)
				Router.nextPort = new InetSocketAddress(dstHost, USER2_PORT);
			else
				Router.nextPort = new InetSocketAddress(dstHost, srcPort+1);
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
			DatagramPacket response;
			switch (data[TYPE_POS]) {
			case CONNECT_ACK:
				terminal.println("Connected to Controller");
				break;
			case TYPE_ACK:
				terminal.println("Received by router " + packet.getPort());
				break;
			case USER:
				terminal.println("Packet received from user");
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				data = new byte[HEADER_LENGTH];
				data[TYPE_POS] = TYPE_ACK;
				data[ACKCODE_POS] = ACK_ALLOK;
				response = new DatagramPacket(data, data.length);
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
				this.notify();
				forwardPacket(content);
				break;
			case ROUTER:
				terminal.println("Packet received from router");
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				data = new byte[HEADER_LENGTH];
				data[TYPE_POS] = TYPE_ACK;
				data[ACKCODE_POS] = ACK_ALLOK;
				response = new DatagramPacket(data, data.length);
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
				this.notify();
				forwardPacket(content);
				break;
			default:
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				terminal.println("Message received: " + content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void forwardPacket(String contentString) throws Exception {
		byte[] data = null;
		byte[] content = contentString.getBytes();
		DatagramPacket packet = null;

		data = new byte[HEADER_LENGTH + content.length];
		data[TYPE_POS] = ROUTER;
		data[LENGTH_POS] = (byte) content.length;
		System.arraycopy(content, 0, data, HEADER_LENGTH, content.length);
		
		terminal.println("Forwarding packet...");
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(nextPort);
		socket.send(packet);
	}
	
	public void contactController() throws Exception {
		byte[] data = null;
		String input = "Hello";
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
				router.contactController();
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
