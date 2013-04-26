import java.io.*;
import java.net.*;

public class Client {

	private String serverHostname = null;
	private int serverPort = 0;
	private Socket sock = null;
	private InputStream sockInput = null;
	private OutputStream sockOutput = null;
	private String uname = null;
	private boolean connected = false;

	public Client(String serverHostname, int serverPort) {
		this.serverHostname = serverHostname;
		this.serverPort = serverPort;
	}

	public void connect(String uname) {
		this.uname = uname;

		try {
			sock = new Socket(serverHostname, serverPort);
			System.out.println(sock);
			sockOutput = sock.getOutputStream();
			sockInput = sock.getInputStream();
			sockOutput.write(uname.getBytes());
			byte[] buffer = new byte[100];
			sockInput.read(buffer, 0, buffer.length);
			if (new String(buffer).trim().equals("ACK")) {
				connected = true;
				System.out.println("Connection have been established.");
			} else if (new String(buffer).trim().equals("NACK WAIT")) {
				connected = false;
				System.out.println("Server is busy, please try later.");
			} else {
				connected = false;
				System.out
						.println("Error when connecting, please check your username and try again.");
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return;
		}

	}

	public void listAll() {
		byte[] byte_uname = null;
		String uname;
		if (connected) {
			try {
				sockOutput.write("LIST".getBytes());
				sockInput.read(byte_uname);
				uname = new String(byte_uname).trim();
				System.out.println(uname);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void post(String message) {
		postOn(this.uname, message);
	}

	public void postOn(String name, String message) {
		if (connected) {
			try {
				sockOutput.write("MSG".getBytes());
				sockOutput.write(name.getBytes());
				sockOutput.write(message.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else {
			System.out.println("Connection required.");
		}
	}

	public void show() {
		showFrom(this.uname);
	}

	public void showFrom(String name) {
		byte[] byte_to = null;
		byte[] byte_from = null;
		byte[] byte_msg = null;
		String to, from, msg;
		if (connected) {
			try {
				sockOutput.write("SHOW".getBytes());
				sockOutput.write(name.getBytes());
				sockInput.read(byte_to);
				to = new String(byte_to).trim();
				sockInput.read(byte_from);
				from = new String(byte_from).trim();
				sockInput.read(byte_msg);
				msg = new String(byte_msg).trim();
				String fullmsg = String.format("From %0, To %1\r\n%s", from, to, msg);
				System.out.println(fullmsg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void quit() {
		try {
			sockOutput.write("CLOSE".getBytes());
			sock.close();
		} catch (IOException e) {
			System.err.println("Exception closing socket.");
			e.printStackTrace(System.err);
		}
	}

	public void runClient() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String s = null;
		String[] command = null;
		while (!connected) {
			try {
				System.out
						.println("Please enter your username here or \"quit\" to quit: ");
				s = bufferRead.readLine();
				if (s.equals("quit")) {
					return;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connect(s);
		}

		while (connected) {
			try {
				System.out
						.println("Please enter your commands here or \"quit\" to quit: ");
				s = bufferRead.readLine();
				command = s.split("\\s+");
				if (command.length == 1) {
					switch (command[0]) {
					case "listall":
						listAll();
						break;
					case "show":
						show();
						break;
					case "quit":
						quit();
						return;
					}
				} else if (command.length == 2) {
					switch (command[0]) {
					case "post":
						post(command[1]);
						break;
					case "show":
						showFrom(command[1]);
						break;
					}
				} else if (command.length == 3) {
					switch (command[0]) {
					case "post":
						postOn(command[1], command[2]);
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
