package assignment1;

import java.net.*;
import java.util.*;
import java.io.*;

// Modified Server class

public class Broker extends Node {
	static final int BROKER_PORT = 50001; // port of the broker

	static final int HEADER_LENGTH = 2;
	static final int TYPE_POS = 0;

	static final byte TYPE_UNKNOWN = 0;

	static final int LENGTH_POS = 1;

	static final byte TYPE_ACK = 4;
	static final byte CONNECT_ACK = 5;
	static final int ACKCODE_POS = 1;
	static final byte ACK_ALLOK = 10;

	static final byte CANDC = 1;
	static final byte BROKER = 2;
	static final byte WORKER = 3;

	private ArrayList<SocketAddress> workerAddresses = new ArrayList<SocketAddress>();

	Terminal terminal;
	InetSocketAddress dstAddress;
	SocketAddress commandAdd;
	public int declines = 0;
	public boolean beenDeclined = false;
	public boolean beenAccepted = false;

	Broker(Terminal terminal, int port) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(port);
			listener.go();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			String content;
			byte[] data;
			byte[] buffer;

			data = packet.getData();
			DatagramPacket response;
			switch (data[TYPE_POS]) {
			case TYPE_ACK:
				terminal.println("Packet received by Worker " + (packet.getPort() - 50001));
				break;
			case CANDC:
				beenAccepted = false;
				beenDeclined = false;
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				terminal.println("Packet received from C&C");
				data = new byte[HEADER_LENGTH];
				data[TYPE_POS] = TYPE_ACK;
				data[ACKCODE_POS] = ACK_ALLOK;
				response = new DatagramPacket(data, data.length);
				response.setSocketAddress(packet.getSocketAddress());
				commandAdd = packet.getSocketAddress();
				socket.send(response);
				declines = 0;
				this.notify();
				forwardMessage(content, "workers");
				break;
			case WORKER:
				buffer = new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content = new String(buffer);
				if (!content.contentEquals("")) {
					terminal.println("Worker " + (packet.getPort()- 50001) + " said: " + content);
				}
				data = new byte[HEADER_LENGTH];
				data[TYPE_POS] = TYPE_ACK;
				data[ACKCODE_POS] = ACK_ALLOK;
				response = new DatagramPacket(data, data.length);
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
				SocketAddress address = packet.getSocketAddress();
				if (!checkWorkerList(address)) {
					workerAddresses.add(packet.getSocketAddress());
					System.out.println("Current worker addresses:\n" + workerAddresses.toString());
				}
				if (content.equalsIgnoreCase("Accept") && !beenDeclined && !beenAccepted) {
					declines = workerAddresses.size()+1;
					beenAccepted = true;
					forwardMessage("Your work order has been accepted by Worker " + (packet.getPort()- 50001), "command");
				} else if (content.equalsIgnoreCase("Decline")) {
					declines++;
					if (declines == workerAddresses.size()) {
						forwardMessage("Your work order has been declined by all workers", "command");
						beenDeclined = true;
					}
				} else if (content.equalsIgnoreCase("Withdraw")) {
					packet.getSocketAddress();
					workerAddresses.remove(packet.getSocketAddress());
					System.out.println("Current worker addresses:\n" + workerAddresses.toString());
				}
				break;
			default:
				terminal.println("Unexpected packet" + packet.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkWorkerList(SocketAddress add) {
		for (int i = 0; i < workerAddresses.size(); i++) {
			if (workerAddresses.get(i).equals(add)) {
				return true;
			}
		}
		return false;
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		this.wait();
	}

	public void forwardMessage(String contentString, String dest) throws IOException, InterruptedException {
		byte[] data = null;
		byte[] content = contentString.getBytes();
		DatagramPacket packet = null;

		data = new byte[HEADER_LENGTH + content.length];
		data[TYPE_POS] = BROKER;
		data[LENGTH_POS] = (byte) content.length;
		System.arraycopy(content, 0, data, HEADER_LENGTH, content.length);
		
		if (dest.contentEquals("workers")) {
			terminal.println("Forwarding packet...");
			packet = new DatagramPacket(data, data.length);
			for (int i = 0; i < workerAddresses.size(); i++) {
				packet.setSocketAddress(workerAddresses.get(i));
				socket.send(packet);
			}
			terminal.println("Packet forwarded to workers");
		} else if (dest.contentEquals("command")) {
			terminal.println("Contacting command...");
			packet = new DatagramPacket(data, data.length);
			packet.setSocketAddress(commandAdd);
			socket.send(packet);
			terminal.println("Command notified");
		}
	}

	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Broker");
			Broker broker = new Broker(terminal, BROKER_PORT);
			broker.start();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}