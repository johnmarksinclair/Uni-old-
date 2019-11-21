package assignment2;

import java.net.*;

import assignment1.Terminal;

public class User extends Node {

	Terminal terminal;
	InetSocketAddress dstAddress;
	int port;

	User(Terminal terminal, String dstHost, int dstPort, int srcPort) throws Exception {
		this.terminal = terminal;
		dstAddress = new InetSocketAddress(dstHost, dstPort);
		socket = new DatagramSocket(srcPort);
		this.port = srcPort;
		terminal.println("My port: " + this.port);
		listener.go();
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		String content;
		byte[] data;
		byte[] buffer;

		data = packet.getData();
		switch (data[TYPE_POS]) {
		case TYPE_ACK:
			terminal.println("Packet received by router: " + packet.getPort());
			break;
		case ROUTER:
			buffer = new byte[data[LENGTH_POS]];
			System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
			content = new String(buffer);
			terminal.println("Packet received: " + content);
			break;
		default:
			terminal.println("Unexpected packet" + packet.toString());
		}
	}

	public void sendMessage() throws Exception {
		byte[] data = null;
		byte[] buffer = null;
		DatagramPacket packet = null;
		String input;

		input = terminal.read("Payload: ");
		buffer = input.getBytes();
		if (!new String(buffer).equals("")) {
			data = new byte[HEADER_LENGTH + buffer.length];
			if (this.port == USER1_PORT)
				data[TYPE_POS] = USER1;
			else 
				data[TYPE_POS] = USER2;
			data[LENGTH_POS] = (byte) buffer.length;
			System.arraycopy(buffer, 0, data, HEADER_LENGTH, buffer.length);
			terminal.println("Sending packet...");
			packet = new DatagramPacket(data, data.length);
			packet.setSocketAddress(dstAddress);
			socket.send(packet);
		}
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		this.wait();
	}

	public static void main(String[] args) throws Exception {
		Terminal terminal1 = new Terminal("User 1");
		Terminal terminal2 = new Terminal("User 2");
		User user1 = new User(terminal1, DEFAULT_DST_NODE, FIRST_ROUTER_PORT, USER1_PORT);
		User user2 = new User(terminal2, DEFAULT_DST_NODE, FIRST_ROUTER_PORT, USER2_PORT);
		while (true) {
			user1.sendMessage();
			user2.sendMessage();
		}
	}
}
