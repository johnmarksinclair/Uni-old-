package assignment2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

public abstract class Node {

	static final int PACKETSIZE = 65536;
	// positions
	static final int TYPE_POS = 0;
	static final int ACKCODE_POS = 1;
	static final int LENGTH_POS = 1;
	static final int HEADER_LENGTH = 2;
	// ports
	static final String DEFAULT_DST_NODE = "localhost"; // Name of the host for the server
	static final int USER1_PORT = 50000;
	static final int USER2_PORT = 50001;
	static final int CONTROLLER_PORT = 50005;
	// router info and ports
	static final int NO_OF_ROUTERS = 3; // Can change no. of routers here
	static final int FIRST_ROUTER_PORT = 50010;
	static final int LAST_ROUTER_PORT = FIRST_ROUTER_PORT + NO_OF_ROUTERS - 1;
	// response types
	static final byte TYPE_ACK = 4;
	static final byte TYPE_CONNECT_ACK = 5;
	static final byte TYPE_USER_ACK = 6;
	static final byte ACK_ALLOK = 10;
	// packet types
	static final byte USER1 = 15;
	static final byte USER2 = 16;
	static final byte ROUTER = 17;
	static final byte ROUTER_CON = 18;
	static final byte CONTROLLER = 19;

	DatagramSocket socket;
	Listener listener;
	CountDownLatch latch;

	Node() {
		latch = new CountDownLatch(1);
		listener = new Listener();
		listener.setDaemon(true);
		listener.start();
	}

	public abstract void onReceipt(DatagramPacket packet);

	class Listener extends Thread {

		public void go() {
			latch.countDown();
		}

		public void run() {
			try {
				latch.await();
				while (true) {
					DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
					socket.receive(packet);

					onReceipt(packet);
				}
			} catch (Exception e) {
				if (!(e instanceof SocketException))
					e.printStackTrace();
			}
		}
	}
	
	public DatagramPacket createPacket(DatagramPacket packet, byte type, byte[] content) {
		byte[] data = packet.getData();
		DatagramPacket response;
		byte[] buffer = new byte[data[LENGTH_POS]];
		System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
		data = new byte[HEADER_LENGTH];
		switch (type) {
		case TYPE_ACK:
			data[TYPE_POS] = TYPE_ACK;
			break;
		case TYPE_CONNECT_ACK:
			data[TYPE_POS] = TYPE_CONNECT_ACK;
			break;
		case TYPE_USER_ACK:
			data[TYPE_POS] = TYPE_USER_ACK;
			break;
		}
		data[ACKCODE_POS] = ACK_ALLOK;
		response = new DatagramPacket(data, data.length);
		response.setSocketAddress(packet.getSocketAddress());
		return response;
	}
	
	public String getContent(DatagramPacket packet) {
		String content;
		byte[] data;
		byte[] buffer;
		data = packet.getData();
		buffer = new byte[data[LENGTH_POS]];
		System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
		content = new String(buffer);
		return content;
	}
}
