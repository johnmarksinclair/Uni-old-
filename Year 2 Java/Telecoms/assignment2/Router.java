package assignment2;

import java.net.*;
import java.util.*;

import assignment1.Terminal;

public class Router extends Node {
	
	static final int LENGTH_POS = 1;
	static final int HEADER_LENGTH = 2;
	static final int NO_OF_ROUTERS = 3;
	static final int FIRST_ROUTER_PORT = 50010;
	
	Terminal terminal;
	InetSocketAddress dstAddress;
	static ArrayList<Router> routers = new ArrayList<Router>();

	Router(Terminal terminal, int port) {
		try {
			this.terminal = terminal;
//			dstAddress = new InetSocketAddress(dstHost, dstPort);
//			socket = new DatagramSocket(srcPort);
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
	
	public void connectToController() {
		
	}

	public static void main(String[] args) {
		try {
			for (int i = 0; i < NO_OF_ROUTERS; i++) {
				Terminal terminal = new Terminal("Router " + (i + 1));
				Router router = new Router(terminal, FIRST_ROUTER_PORT + i);
				router.connectToController();
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}