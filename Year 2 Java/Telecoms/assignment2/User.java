package assignment2;

import java.net.*;

public class User extends Node {

	Terminal terminal;
	InetSocketAddress dstAddress;
	InetSocketAddress myAdd;
	byte myType;

	User(Terminal terminal, String dstHost, int dstPort, int srcPort, byte type) throws Exception {
		this.terminal = terminal;
		dstAddress = new InetSocketAddress(dstHost, dstPort);
		socket = new DatagramSocket(srcPort);
		this.myAdd = new InetSocketAddress(dstHost, srcPort);
		this.myType = type;
		listener.go();
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) throws Exception {
		byte[] data = packet.getData();
		switch (data[TYPE_POS]) {
		case TYPE_ACK:
			terminal.println("Message sent");
			break;
		case ROUTER:
			socket.send(createPacket(packet, TYPE_USER_ACK, null, null));
			terminal.println("Message received: " + getStringContent(packet));
			break;
		default:
			terminal.println("Unexpected packet" + packet.toString());
		}
	}

	public void sendMessage() throws Exception {
		String input = terminal.read("Message: ");
		byte[] buffer = input.getBytes();
		if (!new String(buffer).equals("")) {
			terminal.println("Sending message...");
			socket.send(createPacket(null, this.myType, buffer, dstAddress));
		}
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		this.wait();
	}

	public static void main(String[] args) throws Exception {
		Terminal terminal1 = new Terminal("User 1");
		Terminal terminal2 = new Terminal("User 2");
		User user1 = new User(terminal1, DEFAULT_DST_NODE, FIRST_ROUTER_PORT, USER1_PORT, USER1);
		User user2 = new User(terminal2, DEFAULT_DST_NODE, LAST_ROUTER_PORT, USER2_PORT, USER2);
		while (true) {
			user1.sendMessage();
			user2.sendMessage();
		}
	}
}
