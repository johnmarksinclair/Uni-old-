package assignment2;

import java.net.*;

public class Router extends Node {
	
	Terminal terminal;
	InetSocketAddress dstAddress;
	InetSocketAddress prevPort;
	InetSocketAddress port;
	InetSocketAddress nextPort;
	//hi

	Router(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal = terminal;
			this.dstAddress = new InetSocketAddress(dstHost, dstPort);
			this.port = new InetSocketAddress(dstHost, srcPort);
			if (srcPort == FIRST_ROUTER_PORT) {
				this.prevPort = new InetSocketAddress(dstHost, USER1_PORT);
				this.nextPort = new InetSocketAddress(dstHost, srcPort+1);
				terminal.println("My port: " + this.port);
			} else if (srcPort == LAST_ROUTER_PORT) {
				this.prevPort = new InetSocketAddress(dstHost, srcPort-1);
				this.nextPort = new InetSocketAddress(dstHost, USER2_PORT);
				terminal.println("My port: " + this.port);
			} else {
				this.prevPort = new InetSocketAddress(dstHost, srcPort-1);
				this.nextPort = new InetSocketAddress(dstHost, srcPort+1);
				terminal.println("My port: " + this.port);
			}
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
				terminal.println("Received by Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
				break;
			case USER_ACK:
				terminal.println("Received by User " + (packet.getPort() - USER1_PORT + 1));
				break;
			case USER1:
			case USER2:
				terminal.println("Received packet from User " + (packet.getPort() - USER1_PORT + 1));
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
				terminal.println("Received packet from Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
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
	
	public synchronized void forwardPacket(String contentString) throws Exception {
		byte[] data = null;
		byte[] content = contentString.getBytes();
		DatagramPacket packet = null;

		data = new byte[HEADER_LENGTH + content.length];
		data[TYPE_POS] = ROUTER;
		data[LENGTH_POS] = (byte) content.length;
		System.arraycopy(content, 0, data, HEADER_LENGTH, content.length);
		
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(nextPort);
		if (packet.getPort() == USER1_PORT)
			terminal.println("Forwarding packet to User " + (packet.getPort() - USER2_PORT + 1) + "...");
		else if (packet.getPort() == USER2_PORT)
			terminal.println("Forwarding packet to User " + (packet.getPort() - USER1_PORT + 1) + "...");
		else
			terminal.println("Forwarding packet to Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1) + "...");
		socket.send(packet);
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
