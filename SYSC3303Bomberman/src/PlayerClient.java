
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


public class PlayerClient implements Runnable {

	private static Socket clientSocket = null;
	public static PrintStream os = null;
	public static DataInputStream is = null;
        public static JLabel label;
        public static JButton bomb = new JButton("bomb");
        public static JButton left = new JButton("left");
        public static JButton right = new JButton("right");
        public static JButton up = new JButton("up");
        public static JButton down = new JButton("down");
	public static BufferedReader input = null;
	private static boolean closed = false;
	public static JFrame frame = new JFrame("Client console");
        public static clientbuttons buttonlistener = new clientbuttons ();
	
	public static void main(String[] args) {
                label = new JLabel();
                
                label.setFont(new Font("Courier", Font.PLAIN, 12));
                frame.setMinimumSize(new Dimension(500, 500));
                frame.setBounds(100,100,450,300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                bomb.addActionListener(buttonlistener);
                up.addActionListener(buttonlistener);
                down.addActionListener(buttonlistener);
                left.addActionListener(buttonlistener);
                right.addActionListener(buttonlistener);
                
                GridBagLayout gridBag = new GridBagLayout();
                GridBagConstraints cons = new GridBagConstraints();
        
                cons.fill = GridBagConstraints.BOTH;
                
                
                frame.setLayout(gridBag);
                
                cons.gridx = 2;
                cons.gridy = 1;
                gridBag.setConstraints(label, cons);
                frame.add(label);
                
                cons.gridx = 2;
                cons.gridy = 6;
                gridBag.setConstraints(down, cons);
                frame.add(down);
                
                cons.gridx = 2;
                cons.gridy = 4;
                gridBag.setConstraints(up, cons);
                frame.add(up);
                
                cons.gridx = 3;
                cons.gridy = 5;
                gridBag.setConstraints(right, cons);
                frame.add(right);
                
                cons.gridx = 2;
                cons.gridy = 5;
                gridBag.setConstraints(bomb, cons);
                frame.add(bomb);
                
                cons.gridx = 1;
                cons.gridy = 5;
                gridBag.setConstraints(left, cons);
                frame.add(left);
                
		int portNumber = 9000;
		String host = "localhost";
		
		if (args.length < 2) {
			System.out.println("host= " + host + ", portNumber= " + portNumber);
		} else {
			host = args[0];
			portNumber = Integer.valueOf(args[1]).intValue();
		}
		
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
			//System.err.println("Don't know about host " + host);
			return false;
		} catch (IOException e) {
			//System.err.println("Couldn't get I/O for the connection to the host "
			//		+ host);
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

    static void input(InputStreamReader inputStreamReader) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
	@Override
	public void run() {
		
		String responseLine;
                String[] boardarray = {"TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY","TEMPORARY"};
                String temp;
                int i = 0;
                int j = 0;
		try {
			
			while ((responseLine = is.readLine()) != null) {
                            
                            if(i==14){
                                while(i>0){
                                    if(j==0){
                                        label.setText("");
                                    }
                                    temp = label.getText().replaceAll("</html>", "");
                                    temp = temp.replaceAll("</p>", "");
                                    temp = temp.replaceAll("<pre>", "");
                                    temp = temp.replaceAll("</pre>", "");
                                    temp = temp.replaceAll("<html>", "");
                                    temp = temp.replaceAll("<p>", "");
                                    label.setText("<html>" + "<pre>" + "<p>"+ temp + boardarray[j] + " <br>" +"</p>"+ "</pre>" + "</html>");
                                    i--;
                                    j++;
                                }
                                j = 0;
                                
                            }
                            if(responseLine.length()==15){
                                boardarray[i]= responseLine;
                                
                                i++;
                            }
                            else{
                                i=0;
                                //label.setText("<html>" + "<p>" + responseLine + "</p>" + "</html>");
                            }
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

}
