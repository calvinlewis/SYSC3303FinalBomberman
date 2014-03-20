

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ConcurrencyTestCase {

	PlayerClient client;
	Server server;
	PrintStream os;
	ByteArrayInputStream in;
	DataInputStream is;
	DataOutputStream oos;
	
    @Before
    public void setUp() {

    	client = new PlayerClient();
    }

    @After
    public void tearDown() {
        
    }
    
    
	@Test
	public void buferFull() {
		
		client.testFullBuffer();

	}
	
	@Test
	public void buferEmpty() {
		
		assertFalse("Buffer is empty", client.testEmptyBuffer());
	}

}
