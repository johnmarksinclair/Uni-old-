package assignment2;

import java.net.*;
import java.util.concurrent.CountDownLatch;

public abstract class Node {

	static final int PACKETSIZE = 65536;
	// positions
	static final int TYPE_POS = 0;
	static final int ACKCODE_POS = 1;
	static final int LENGTH_POS = 1;
	static final int HEADER_LENGTH = 2;
	// ports
	static final String DEFAULT_DST_NODE = "localhost";
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
	static final byte ACK_ALLOK = 7;
	// packet types
	static final byte USER1 = 15;
	static final byte USER2 = 16;
	static final byte ROUTER = 17;
	static final byte ROUTER_CON = 18;
	static final byte CONTROLLER = 19;
	static final byte FEA_REQ = 20;

	DatagramSocket socket;
	Listener listener;
	CountDownLatch latch;

	Node() {
		latch = new CountDownLatch(1);
		listener = new Listener();
		listener.setDaemon(true);
		listener.start();
	}

	public abstract void onReceipt(DatagramPacket packet) throws Exception;

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

	public DatagramPacket createPacket(DatagramPacket packet, byte type, byte[] content, SocketAddress add) {
		byte[] data = null;
		DatagramPacket response;
		DatagramPacket message;
		if (packet != null) {
			data = packet.getData();
			response = new DatagramPacket(data, data.length);
			byte[] buffer = new byte[data[LENGTH_POS]];
			System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
			data = new byte[HEADER_LENGTH];
		}
		switch (type) {
		case TYPE_ACK:
		case TYPE_CONNECT_ACK:
		case TYPE_USER_ACK:
		case FEA_REQ:
			data[TYPE_POS] = type;
			data[ACKCODE_POS] = ACK_ALLOK;
			response = new DatagramPacket(data, data.length);
			if (data[TYPE_POS] == FEA_REQ)
				response.setSocketAddress(new InetSocketAddress(DEFAULT_DST_NODE, CONTROLLER_PORT));
			else
				response.setSocketAddress(packet.getSocketAddress());
			return response;
		case ROUTER:
		case ROUTER_CON:
		case CONTROLLER:
		case USER1:
		case USER2:
			data = new byte[HEADER_LENGTH + content.length];
			data[TYPE_POS] = type;
			data[LENGTH_POS] = (byte) content.length;
			System.arraycopy(content, 0, data, HEADER_LENGTH, content.length);
			message = new DatagramPacket(data, data.length);
			message.setSocketAddress(add);
			return message;
		default:
			return null;
		}
	}

	public byte[] getByteContent(DatagramPacket packet) {
		byte[] data = packet.getData();
		byte[] buffer = new byte[data[LENGTH_POS]];
		System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
		return buffer;
	}

	public String getStringContent(DatagramPacket packet) {
		return new String(getByteContent(packet));
	}
}
