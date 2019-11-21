package assignment2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

public abstract class Node {
	
	static final int PACKETSIZE = 65536;

	static final int TYPE_POS = 0;
	static final int LENGTH_POS = 1;
	static final int HEADER_LENGTH = 2;
	
	static final String DEFAULT_DST_NODE = "localhost"; // Name of the host for the server
	static final int USER1_PORT = 50000;
	static final int USER2_PORT = 50001;
	static final int CONTROLLER_PORT = 50005;
	
	static final int NO_OF_ROUTERS = 3; // Can change no. of routers here
	static final int FIRST_ROUTER_PORT = 50010;
	static final int LAST_ROUTER_PORT = FIRST_ROUTER_PORT + NO_OF_ROUTERS - 1;
	
	static final byte TYPE_ACK = 4;
	static final byte CONNECT_ACK = 5;
	static final byte USER_ACK = 6;
	static final int ACKCODE_POS = 1;
	static final byte ACK_ALLOK = 10;
	
	static final byte USER1 = 15;
	static final byte USER2 = 16;
	static final byte ROUTER = 17;
	static final byte ROUTER_CON = 17;
	

	DatagramSocket socket;
	Listener listener;
	CountDownLatch latch;
	
	Node() {
		latch= new CountDownLatch(1);
		listener= new Listener();
		listener.setDaemon(true);
		listener.start();
	}
	
	
	public abstract void onReceipt(DatagramPacket packet);
	
	/**
	 *
	 * Listener thread
	 * 
	 * Listens for incoming packets on a datagram socket and informs registered receivers about incoming packets.
	 */
	class Listener extends Thread {
		
		/*
		 *  Telling the listener that the socket has been initialized 
		 */
		public void go() {
			latch.countDown();
		}
		
		/*
		 * Listen for incoming packets and inform receivers
		 */
		public void run() {
			try {
				latch.await();
				// Endless loop: attempt to receive packet, notify receivers, etc
				while(true) {
					DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
					socket.receive(packet);
					
					onReceipt(packet);
				}
			} catch (Exception e) {if (!(e instanceof SocketException)) e.printStackTrace();}
		}
	}
}