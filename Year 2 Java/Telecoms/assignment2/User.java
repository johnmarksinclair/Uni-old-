package assignment2;

import java.net.*;

import assignment1.Terminal;

public class User extends Node {

	Terminal terminal;
	InetSocketAddress dstAddress;
	InetSocketAddress myAdd;

	User(Terminal terminal, String dstHost, int dstPort, int srcPort) throws Exception {
		this.terminal = terminal;
		dstAddress = new InetSocketAddress(dstHost, dstPort);
		socket = new DatagramSocket(srcPort);
		this.myAdd = new InetSocketAddress(dstHost, srcPort);
		//terminal.println("My Socket Address: " + this.myAdd);
		listener.go();
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			byte[] data = packet.getData();
			switch (data[TYPE_POS]) {
			case TYPE_ACK:
				//terminal.println("Message received by Router " + (packet.getPort() - FIRST_ROUTER_PORT + 1));
				break;
			case ROUTER:
				socket.send(createPacket(packet, TYPE_USER_ACK, null, null));
				terminal.println("Message received: " + getStringContent(packet));
				break;
			default:
				terminal.println("Unexpected packet" + packet.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage() throws Exception {
		byte[] data = null;
		byte[] buffer = null;
		DatagramPacket packet = null;
		String input;

		input = terminal.read("Message: ");
		buffer = input.getBytes();
		if (!new String(buffer).equals("")) {
			data = new byte[HEADER_LENGTH + buffer.length];
			if (this.myAdd.equals(new InetSocketAddress("localhost", USER1_PORT)))
				data[TYPE_POS] = USER1;
			else
				data[TYPE_POS] = USER2;
			data[LENGTH_POS] = (byte) buffer.length;
			System.arraycopy(buffer, 0, data, HEADER_LENGTH, buffer.length);
			terminal.println("Sending message...");
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
		User user2 = new User(terminal2, DEFAULT_DST_NODE, LAST_ROUTER_PORT, USER2_PORT);
		while (true) {
			user1.sendMessage();
			user2.sendMessage();
		}
	}
}
