package assignment2;

import java.net.*;

public class Router extends Node {
	
	Terminal terminal;
	InetSocketAddress dstAddress;

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
			DatagramPacket response;
			switch (data[TYPE_POS]) {
			case TYPE_ACK:
				terminal.println("Connected to Controller");
				break;
			case USER:
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				terminal.println("Packet received from user");
				data = new byte[HEADER_LENGTH];
				data[TYPE_POS] = TYPE_ACK;
				data[ACKCODE_POS] = ACK_ALLOK;
				response = new DatagramPacket(data, data.length);
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
				this.notify();
//				SocketAddress next = getNextRouterAdd(this);
//				if (next != null)
//					forwardPacket(content, next);
//				else {
//					InetSocketAddress endUser = new InetSocketAddress(USER2_PORT);
//					forwardPacket(content, endUser);
//				}
				InetSocketAddress endUser = new InetSocketAddress(USER2_PORT);
				forwardPacket(content, endUser);
				break;
			case ROUTER:
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				terminal.println("Packet received");
				forwardPacket(content, getNextRouterAdd(this));
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
	
	public SocketAddress getNextRouterAdd(Router current) {
		for (int i = 0; i < Controller.connectedRouters.size(); i++) {
			if (Controller.connectedRouters.get(i) == dstAddress) {
				return Controller.connectedRouters.get(i + 1);
			}
		}
		return null;
	}
	
	public void forwardPacket(String contentString, SocketAddress add) throws Exception {
		byte[] data = null;
		byte[] content = contentString.getBytes();
		DatagramPacket packet = null;

		data = new byte[HEADER_LENGTH + content.length];
		data[TYPE_POS] = ROUTER;
		data[LENGTH_POS] = (byte) content.length;
		System.arraycopy(content, 0, data, HEADER_LENGTH, content.length);
		
		terminal.println("Forwarding packet...");
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(add);
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