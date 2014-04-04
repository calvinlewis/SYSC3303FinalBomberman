
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class PlayerClient implements Runnable {

	private static Socket clientSocket = null;
	private static PrintStream os = null;
	private static DataInputStream is = null;

	private static BufferedReader input = null;
	private static boolean closed = false;
	
	static File file = new File("/Users/Calvin/Documents/SYSC3303FinalBomberman/SYSC3303Bomberman/scalability.txt");
	

	public static void main(String[] args) {

		int portNumber = 9000;
		String host = "localhost";
		
		if (args.length < 2) {
			System.out.println("host= " + host + ", portNumber= " + portNumber);
		} else {
			host = args[0];
			portNumber = Integer.valueOf(args[1]).intValue();
		}
		makeTestFile();
		createSocket(host, portNumber);
	}
	
	public static boolean createSocket(String host, int port) {
		
		// Open socket on given host and port number
		try {
			
			clientSocket = new Socket(host, port);
			input = new BufferedReader(new InputStreamReader(System.in));
			os = new PrintStream(clientSocket.getOutputStream());
			is = new DataInputStream(clientSocket.getInputStream());
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
			return false;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to the host" + host);
			return false;
		}
		
		// Create PlayerClient thread to read from Server
		if (clientSocket != null && os != null && is != null) {
			try {

				new Thread(new PlayerClient()).start();
				while (!closed) {
					
					os.println(input.readLine().trim());

				}
				os.close();
				is.close();
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
				return false;
			}
			return true;
		}
		return true;	
	}
	
	@Override
	public void run() {
		
		String responseLine;
		try {
			
			while ((responseLine = is.readLine()) != null) {
				System.out.println(responseLine);

					
				if (responseLine.indexOf("END_GAME") != -1)
					break;
			}
			closed = true;
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
	
	public boolean testEmptyBuffer() {

		return createSocket("localhost", 5555);
	}
	
	public static void makeTestFile() {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("Calvin");
			output.write("START_GAME");
			for (int i=0; i<5000; i++) {
				output.write("UP");
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
