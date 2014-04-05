

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FunctionalTestCase {

	GameBoard gameboard;
	
	@Before
    public void setUp() {
		gameboard = new GameBoard();
    }
	 
	@After
	public void tearDown() {
		gameboard = null;
	}
	
	@Test
	public void testFindDoor() {
		
		gameboard = gameboard.testGameBoard();
		System.out.println("** PLAYER FINDS DOOR TEST **\n");
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "PLAY", 0);
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "RIGHT", 0);
		System.out.println(gameboard.printBoard());

	}
	
	@Test
	public void testPlayersTouch() {
		gameboard = new GameBoard(11,11,1);
		System.out.println("** PLAYERS TOUCH TEST **\n");
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "PLAY", 1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "PLAY", 2);
		System.out.println(gameboard.printBoard());
		for (int i=0; i<10; i++) {
			if(GameBoard.move(0, 0, "RIGHT", 1) && GameBoard.move(0, 0, "LEFT", 2)) { }
			else
				System.out.println("Invalid move!");
			
			System.out.println(gameboard.printBoard());
		}

	}
	
	@Test
	public void testDeployBomb() {
		gameboard = new GameBoard().testGameBoard();
		System.out.println("** BOMB DEPLOYMENT TEST **\n");
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "PLAY", 1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(1), GameBoard.getBombermanY(1), "BOMB", 1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(1),GameBoard.getBombermanY(1), "DOWN", 1);
		System.out.println(gameboard.printBoard());

	}
	
	@Test
	public void testFindPowerUp() {
		gameboard = new GameBoard().testGameBoardPower();
		System.out.println("** FIND POWERUP TEST **\n");
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "PLAY", 3);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(3),GameBoard.getBombermanY(3), "RIGHT", 3);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(3),GameBoard.getBombermanY(3), "DOWN", 3);


	}
	
	@Test
	public void testMultipleDeployBomb() {
		gameboard = new GameBoard().testGameBoard();
		System.out.println("** BOMB DEPLOYMENT TEST **\n");
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "PLAY", 1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(1), GameBoard.getBombermanY(1), "BOMB", 1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(1),GameBoard.getBombermanY(1), "DOWN", 1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(1), GameBoard.getBombermanY(1), "BOMB", 1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(GameBoard.getBombermanX(1),GameBoard.getBombermanY(1), "DOWN", 1);
		System.out.println(gameboard.printBoard());
	}

}
