

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ConcurrencyTestCase {

	PlayerClient client1, client0, client2, client3, client4, client5;
	ClientThread test;
	ClientThread[] threads = new ClientThread[4]; 
	Socket clientSocket;
	Server server;
	PrintStream os;
	ByteArrayInputStream in;
	DataInputStream is;
	DataOutputStream oos;
	
    @Before
    public void setUp() {

    	client0 = new PlayerClient();
    	client1 = new PlayerClient();
    	client2 = new PlayerClient();
    	client3 = new PlayerClient();
    	client4 = new PlayerClient();
    	client5 = new PlayerClient();
    	
    }

    @After
    public void tearDown() {
        
    	client0 = null;
    	client1 = null;
    	client2 = null;
    	client3 = null;
    	client4 = null;
    	client5 = null;

    }
    
    
	@Test
	public void testBufferFull() {
		
		while (true) {
			client0.testFullBuffer();
			client1.testFullBuffer();
			client2.testFullBuffer();
			client3.testFullBuffer();
			client4.testFullBuffer();
		}

	}
	
	@Test
	public void testBufferEmpty() {
		
		client0.testEmptyBuffer();
		client1.testEmptyBuffer();
		client2.testEmptyBuffer();
		client3.testEmptyBuffer();
	}

}
