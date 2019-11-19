package assignment2;

import java.net.*;
import java.util.*;

public class Controller extends Node {
	
	Terminal terminal;
	InetSocketAddress dstAddress;
	static ArrayList<SocketAddress> connectedRouters = new ArrayList<SocketAddress>();
	
	Controller(Terminal terminal, int port) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(port);
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
			case ROUTER:
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				if (!content.contentEquals("")) terminal.println("Message received: " + content);
				data = new byte[HEADER_LENGTH];
				data[TYPE_POS] = TYPE_ACK;
				data[ACKCODE_POS] = ACK_ALLOK;
				response = new DatagramPacket(data, data.length);
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
				SocketAddress address = packet.getSocketAddress();
				if (!checkRouters(address)) {
					connectedRouters.add(packet.getSocketAddress());
					System.out.println("Current router addresses:\n" + connectedRouters.toString());
				}
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
		terminal.println("Waiting for contact");
		this.wait();
	}
	
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Controller");
			Controller controller = new Controller(terminal, CONTROLLER_PORT);
			controller.start();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
