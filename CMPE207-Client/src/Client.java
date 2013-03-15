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
		
		try{
			sock = new Socket(serverHostname, serverPort);
			System.out.println(sock);
			sockOutput = sock.getOutputStream();
			sockInput = sock.getInputStream();
			sockOutput.write(uname.getBytes());
			byte[] buffer = new byte[100];
			sockInput.read(buffer, 0, buffer.length);
			if(new String(buffer).trim().equals("ACK")){
				connected = true;
				System.out.println("Connection have been established.\r\nPlease type in your command.");
			}
			else {
				connected = false;
				System.out.println("Error when connecting, please check your username and try again.");
			}
		}
		catch (IOException e) {
			e.printStackTrace(System.err);
			return;
		}
		
		
		
	}
	
	public void listAll(){
		
	}
	
	public void post(String message) {
		postOn(this.uname, message);
	}
	
	public void postOn(String name, String message) {
		if(connected) {

			String request = String.format("Action: Post\r\nUser-Name: %s\r\nContent-Length: %d\r\n\r\n%s", name, message.length(), message);
			System.err.println(request);
			
			try {
				sockOutput.write(request.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

		}
		else {
			System.out.println("Connection required.");
		}
	}
	
	public void show() {
		showFrom(this.uname);
	}
	
	public void showFrom(String name) {
		
	}
	
	public void quit() {
		try {
			sockOutput.write("CLOSE".getBytes());
            sock.close();
        }
        catch (IOException e){
            System.err.println("Exception closing socket.");
            e.printStackTrace(System.err);
        }
	}
	
	public boolean connected() {
		return connected;
	}
}
