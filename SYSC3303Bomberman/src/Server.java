
import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Dimension;


public class Server {

	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;

	private static final int maxClients = 4;
	private static final ClientThread[] threads = new ClientThread[maxClients];
	public static boolean Playing = true;
	private static GameBoard gameBoard = new GameBoard(15,15,1);
        public static JLabel label;
        public static EndGame endgame = new EndGame();
        public static JButton end = new JButton("End game");
        public static JFrame frame = new JFrame("Server console");
        
        public void initialize(){
            
        }
	public static void main(String args[]) {
                       
                label = new JLabel();
                frame.setMinimumSize(new Dimension(500, 200));
                frame.setBounds(100,100,450,300);
                label.setText("Clients can be run now to play the game. To quit, press end game.");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                frame.setLayout(new BorderLayout());
                end.addActionListener(endgame);
                frame.add(label, BorderLayout.EAST);
                frame.add(end, BorderLayout.WEST);
                
                
		int portNumber = 9000;
		
		//Optional portNumber add feature
		if (args.length < 1) {
			System.out.println("portNumber= " + portNumber);
		} else {
			portNumber = Integer.valueOf(args[0]).intValue();
		}
		
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
		QuitCheck check = new QuitCheck();
                check.start();
                
		// Accepts max number of clients (4) to server
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxClients; i++) {
					if (threads[i] == null) {
						(threads[i] = new ClientThread(clientSocket, threads, gameBoard, i)).start();
						break;
					}
				}
				if (i == maxClients) {
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println("IOException" + e);
			}
                        
		}
                
	}
        
}


class ClientThread extends Thread {
	
	private String clientName = null;
	private Socket clientSocket = null;
        private int playernumber;

	private DataInputStream is = null;
	private PrintStream os = null;
	
	private final ClientThread[] threads;
	private int maxClients;
	private GameBoard gameBoard;
	public ClientThread(Socket clientSocket, ClientThread[] threads, GameBoard board,int i) {
        this.playernumber = i;
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClients = threads.length;
		this.gameBoard = board;
	}
	
	public int getMaxClients() {
		return maxClients;
	}

	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}

	public ClientThread[] getThreads() {
		return threads;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {

		int maxClient = getMaxClients();
		ClientThread[] threads = getThreads();
		
		try {

			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			
			//Get PlayerClient's name
			String name;
			os.println("Enter your name: ");
			name = is.readLine().trim();
			setClientName(name);
			
			os.println("Welcome " + getClientName() + " to Bomberman.\nTo start a game type <START_GAME> or <JOIN_GAME> to join a game.\n");
			

			
			
			// Start game
			String line = is.readLine();
			
			if (line.startsWith("START_GAME") || line.startsWith("JOIN_GAME")) {
				
				while (true) {
					
					line = is.readLine();
					
					if (line.startsWith("END_GAME")) {
						break;
					}
					
					// Prints board to all clients
					synchronized (this) {
						for (int i = 0; i < maxClients; i++) {
							if (threads[i] != null && threads[i].clientName != null) {
								threads[i].os.println(gameBoard.printBoard());
							}
						}
					}
					
					// Gets player move
					synchronized (this) {
						for (int i = 0; i < maxClients; i++) {
							if (threads[i] != null && threads[i] == this) {
								
								//int x = gameBoard.getBombermanX();
								//int y = gameBoard.getBombermanY();

								int x = gameBoard.getBombermanX(i);
								int y = gameBoard.getBombermanY(i);

								
								if (x == gameBoard.Xwin && y == gameBoard.Ywin) {
									this.os.println("Player " + getClientName() + " has found the door!");
                                                                        
									break;
								}
								
								if (line.startsWith("U") || line.startsWith("u")) {
									if (gameBoard.move(x, y, "UP",i)) {
										String move = "Moved: UP";
										threads[i].os.println(move);
				
									}
									else {
										String invalid = "\nInvalid Move!\n";
										threads[i].os.println(invalid);
									}
								}
								else if (line.startsWith("D") || line.startsWith("d")) {
									if (gameBoard.move(x, y, "DOWN",i)) {
										String move = "Moved: DOWN";
										threads[i].os.println(move);
				
									}
									else {
										String invalid = "\nInvalid Move!\n";
										threads[i].os.println(invalid);
									}
								}
								else if (line.startsWith("L") || line.startsWith("l")) {
									if (gameBoard.move(x, y, "LEFT",i)) {
										String move = "Moved: LEFT";
										threads[i].os.println(move);
				
									}
									else {
										String invalid = "\nInvalid Move!\n";
										threads[i].os.println(invalid);
									}
								}
								else if (line.startsWith("R") || line.startsWith("r")) {
									if (gameBoard.move(x, y, "RIGHT",i)) {
										String move = "Moved: RIGHT";
										threads[i].os.println(move);
				
									}
									else {
										String invalid = "\nInvalid Move!\n";
										threads[i].os.println(invalid);
									}
								}
								else if (line.startsWith("B") || line.startsWith("b")) {
									if (gameBoard.move(x, y, "BOMB", i)) {
										String bomb = "Placed: BOMB";
										threads[i].os.println(bomb);
				
									}
									else {
										String invalid = "\nInvalid Move!\n";
										threads[i].os.println(invalid);
									}
								}
                                                                
								else if (line.startsWith("P") || line.startsWith("p")) {
									if (gameBoard.move(x, y, "PLAY", i)) {
										String play = "Player deployed";
										threads[i].os.println(play);
				
									}
									else {
										String invalid = "\nInvalid Move!\n";
										threads[i].os.println(invalid);
									}
								}
								break;
							}
						}
					}
					
					// Prints board again to all clients to show movement
					synchronized (this) {
						for (int i = 0; i < maxClients; i++) {
							if (threads[i] != null && threads[i].clientName != null) {
								threads[i].os.println(gameBoard.printBoard());
							}
						}
					}
					

				}
			}
			
				
//				//Determines new user joined the game
//				synchronized (this) {
//					
//					for (int i = 0; i < maxClients; i++) {
//						if (threads[i] != null && threads[i] != this) {
//							threads[i].os.println("\nA new player " + getClientName() + " has joined the game!");
//						}
//					}
//				}
//			
//			// Client has left game
//			synchronized (this) {
//				for (int i = 0; i < maxClients; i++) {
//					if (threads[i] != null && threads[i] != this
//							&& threads[i].clientName != null) {
//						threads[i].os.println("The player " + getClientName() + " has left the game");
//					}
//				}
//			}
				
			os.println("Game ended...");
			// Set left player to null so new client can join
			synchronized (this) {
				for (int i = 0; i < maxClients; i++) {
					if (threads[i] == this) {
						threads[i] = null;
						
					}
				}
			}

			is.close();
			os.close();
			clientSocket.close();
			
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		}
		
	}


}

class QuitCheck extends Thread {
    
    QuitCheck(){
    }
    @Override
    public  void run(){
        while(true){
            //System.out.println("Playing is now: "+Playing);
            if(Server.Playing==false){
                System.exit(0);
            }
        }
    }
}
