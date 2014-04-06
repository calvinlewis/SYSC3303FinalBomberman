import static org.junit.Assert.*;

import org.junit.Test;


public class ScalabilityTestCase {

	GameBoard gameboard, gameboard2;
	@Test
	public void testFourPlayerHeavyLoad() {
		System.out.println("** SCALABILITY TEST **\n");

		gameboard = new GameBoard(11,11,1);
		System.out.println(gameboard.printBoard());
		GameBoard.move(0, 0, "PLAY", 0);
		GameBoard.move(0, 0, "PLAY", 1);
		GameBoard.move(0, 0, "PLAY", 2);
		GameBoard.move(0, 0, "PLAY", 3);

		System.out.println(gameboard.printBoard());

		for (int i=0; i<100; i++) {
			int x0 = GameBoard.getBombermanX(0);
			int y0 = GameBoard.getBombermanY(0);
			int x1 = GameBoard.getBombermanX(1);
			int y1 = GameBoard.getBombermanY(1);
			int x2 = GameBoard.getBombermanX(2);
			int y2 = GameBoard.getBombermanY(2);
			int x3 = GameBoard.getBombermanX(3);
			int y3 = GameBoard.getBombermanY(3);
			
			if (GameBoard.move(x0, y0, "UP", 0) &&
					GameBoard.move(x1, y1, "UP", 1) &&
					GameBoard.move(x2, y2, "UP", 2) &&
					GameBoard.move(x3, y3, "UP", 3)) { }
			else
				System.out.println("Invalid move!");
				
			System.out.println(gameboard.printBoard());
		}
		
		gameboard = null;
	}
	
	@Test
	public void testOnePlayerLightLoad() {
		System.out.println("** SCALABILITY TEST **\n");

		gameboard2 = new GameBoard(11,11,1);
		System.out.println(gameboard2.printBoard());
		gameboard2.move(0, 0, "PLAY", 0);
		System.out.println(gameboard2.printBoard());
		
		for (int i=0; i<10; i++) {
			int x0 = gameboard2.getBombermanX(0);
			int y0 = gameboard2.getBombermanY(0);
			
			if (gameboard2.move(x0, y0, "UP", 0)) { }
			else
				System.out.println("Invalid Move!");
			
			System.out.println(gameboard2.printBoard());

		}
	}

}
