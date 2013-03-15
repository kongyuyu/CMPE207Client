import java.io.*;


public class CPME207client {

	public CPME207client() {
	}

	public static void main(String[] args) {

//		System.out.println("Enter something here : ");
//
//		try{
//			
//
//			System.out.println(s);
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
		

		Client client = new Client("localhost", 7612);
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		while(!client.connected()) {
			try {
				s = bufferRead.readLine();
				if(s.equals("quit")){
					client.quit();
					return;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			client.connect(s);
		}
		
		while(client.connected()) {
			 try {
				s = bufferRead.readLine();
				if(s.equals("quit")){
					client.quit();
					return;
				}
				System.out.println(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
}
