import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import static java.lang.System.exit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// GameBoard class
class GameBoard extends Thread{

	private static char BOMBERMAN1 = 'o'; 	// 1 player
	private static char BOMBERMAN2 = 'O';	// 2 player
	private static char DOOR = 'D';
	private static char BOX = 'b';
	private static char BOMBERMAN = 'o';


	public static int Xwin;
	public static int Ywin;
	boolean doorset = false;
	private char[][] gameBoard;
	private static int[] bombermanpos1 = {-1,-1};
	private static int[] bombermanpos2 = {-1,-1};
	private static int bombID;
	private int xSize, ySize, levels;

	//TestDriver  driver = new TestDriver();

	GameBoard(int x, int y, int l) {
		this.xSize = x;
		this.ySize = y;
		this.levels = l;
		this.bombID = 0;
		createBoard();
	}

	public char[][] getBoard() {
		return gameBoard;
	}

	public void createBoard() {

		gameBoard = new char[xSize][ySize];

		for (int i=0; i<xSize; i++) {
			for (int j=0; j<ySize; j++) {

				// Creation of walls
				if (!(j % 2 == 0) && !(i % 2 == 0)) {
					gameBoard[i][j] = '+';
				}

				// If odd number from 1-10, place box
				else if ((j % 1 == 0) && (i % 1 == 0)) {
					int rand = randInt(0,100);

					if (rand % 10 == 0){ 
						gameBoard[i][j] = BOX;
						if(!doorset){
							doorset = true;
							gameBoard[i][j] = BOX;
							Xwin = i;
							Ywin = j;
						}
					}
					else{
						gameBoard[i][j] = ' ';
					}
				}

			}
		}

		//gameBoard[Xwin][Ywin] = DOOR;

		gameBoard[0][0] = BOMBERMAN1;
		updatebombermanpos(0,0);

	}

	// Prints current game board
	public String printBoard() {

		String output = "\n";

		for (int i=0; i<xSize; i++) {
			for (int j=0; j<ySize; j++) {
				output += gameBoard[i][j];
			}
			output += "\n";
		}

		return output;
	}

	// Checks for an empty space for the bomberman to move
	public boolean isEmptySpace(int x, int y) {
		return gameBoard[x][y] == ' '; 
	}


	// Makes sure there is a valid command line argument
	public boolean validMove(String direction) {

		return direction.equals("UP") ||
				direction.equals("DOWN") ||
				direction.equals("LEFT") ||
				direction.equals("RIGHT")|| 
				direction.equals("O")    || 
				direction.equals("o")    || 
				direction.equals("BOMB");
	}


	// Method to move the bomberman to valid spaces
	public boolean move(int x, int y, String direction){


		int newX=x;
		int newY=y;

		if (validMove(direction)) {
			switch (direction) {

			case "BOMB":

				if(x == getBombermanX() && y == getBombermanY()){
					gameBoard[x][y] = 'Q';
				}
				else if(move(x,y,"DOWN")!=false){
					move(x,y,"DOWN");
				}
				else if(move(x,y,"RIGHT")!=false){
					move(x,y,"RIGHT");
				}
				else{
					gameBoard[x][y] = '*';
				}
				BombFactory bomb = new BombFactory(gameBoard, x, y, Xwin, Ywin, DOOR);
				bomb.start();
				return true;

			case "o":
				BOMBERMAN = 'o';
				if(getBombermanX()==-1){

					if(gameBoard[0][0]=='O'|| gameBoard[0][0]=='Q'){
						gameBoard[1][0]= 'o';
						updatebombermanpos(1,0);
					}
					else if(gameBoard[0][0]=='*'){
						gameBoard[0][0]='Q';
					}
					else{
						gameBoard[0][0]= 'o';
						updatebombermanpos(0,0);
					}
					return true;
				}
				else{
					return true;
				}

			case "O":
				BOMBERMAN = 'O';
				if(getBombermanX()==-1){
					if(gameBoard[0][0]=='o' || gameBoard[0][0]=='Q'){
						gameBoard[1][0]= 'O';
						updatebombermanpos(1,0);
					}
					else if(gameBoard[0][0]=='*'){
						gameBoard[0][0]='Q';
					}
					else{
						gameBoard[0][0]= 'O';
						updatebombermanpos(0,0);
					}
					return true;
				}
				else{
					return true;
				}


			case "DOWN": 
				newX = x+1;
				if (newX < xSize && newX >= 0 && isEmptySpace(newX,y)) {
					gameBoard[newX][y] = BOMBERMAN;
					updatebombermanpos(newX, y);
					gameBoard[x][y] = ' ';
					return true;
				}
				if(newX==Xwin && newY==Ywin){

					gameBoard[x][y] = ' ';
					updatebombermanpos(-1,-1);
					System.out.println("Player found door!!");
					//driver.addLog("Player found door!!");
					//driver.writeLog();


					return true;
				}
				break;

			case "UP": 
				newX = x-1;
				if (newX < xSize && newX >= 0 && isEmptySpace(newX,y)) {
					gameBoard[newX][y] = BOMBERMAN;
					updatebombermanpos(newX, y);
					gameBoard[x][y] = ' ';
					return true;
				}
				if(newX==Xwin && newY==Ywin){

					gameBoard[x][y] = ' ';
					updatebombermanpos(-1,-1);
					System.out.println("Player found door!!");
					//driver.addLog("Player found door!!");
					//driver.writeLog();


					return true;
				}
				break;

			case "LEFT": 
				newY = y-1;
				if (newY < ySize && newY >= 0 && isEmptySpace(x,newY)) {
					gameBoard[x][newY] = BOMBERMAN;
					updatebombermanpos(x, newY);
					gameBoard[x][y] = ' ';
					return true;
				}
				if(newX==Xwin && newY==Ywin){

					gameBoard[x][y] = ' ';
					updatebombermanpos(-1,-1);
					System.out.println("Player found door!!");
					//driver.addLog("Player found door!!");
					//driver.writeLog();

					return true;
				}
				break;

			case "RIGHT": 
				newY = y+1;
				if (newY < ySize && newY >= 0 && isEmptySpace(x,newY)) {
					gameBoard[x][newY] = BOMBERMAN;
					updatebombermanpos(x, newY);
					gameBoard[x][y] = ' ';
					return true;
				}
				if(newX==Xwin && newY==Ywin){

					gameBoard[x][y] = ' ';
					updatebombermanpos(-1,-1);
					System.out.println("Player found door!!");
					//driver.addLog("Player found door!!");
					//driver.writeLog();

					return true;
				}
				break;


			}

		}
		return false;
	}


	// Get current bomberman x position
	public int getBombermanX() {

		/*for (int i=0; i<xSize; i++) {
			for (int j=0; j<ySize; j++) {
				if (gameBoard[i][j] == BOMBERMAN)
					return i;
			}*/

		if(BOMBERMAN=='o'){
			if(bombermanpos1[0]!=-1){
				return bombermanpos1[0];
			}

		}
		else{
			if(bombermanpos2[0]!=-1){
				return bombermanpos2[0];
			}
		}
		return -1;
	}


	// Gets current bomberman y position
	public int getBombermanY() {

		/*for (int i=0; i<xSize; i++) {
			for (int j=0; j<ySize; j++) {
				if (gameBoard[i][j] == BOMBERMAN)
					return j;
			}

		}*/

		if(BOMBERMAN=='o'){
			if(bombermanpos1[1]!=-1){
				return bombermanpos1[1];
			}

		}
		else{
			if(bombermanpos2[1]!=-1){
				return bombermanpos2[1];
			}
		}
		return -1;
	}


	// Generates random number for boxes
	public static int randInt(int min, int max) {

		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static void updatebombermanpos(int x, int y){
		if(BOMBERMAN=='o'){
			bombermanpos1[0]=x;
			bombermanpos1[1]=y;

		}
		else{
			bombermanpos2[0]=x;
			bombermanpos2[1]=y;
		}
	}

}

class BombFactory extends Thread {

	char[][] gameBoard;
	int x;
	int y;
	int Xwin;
	int Ywin;
	char DOOR;
	GameBoard gb;
	
	BombFactory(char[][] game, int x, int y, int Xwin, int Ywin, char DOOR){
		gameBoard = game;
		this.x = x;
		this.y = y;
		this.Xwin = Xwin;
		this.Ywin = Ywin;
		this.DOOR = DOOR;
	}


	@Override
	public void run() {
		try {

			sleep(2000);
			gameBoard[x][y]=' ';

			// Check for boxes
			int newX, newY;

					newX = x+1;
					if (newX < gameBoard.length)
						if (gameBoard[newX][y] == 'b'){
							if(newX==Xwin && y==Ywin){
								gameBoard[Xwin][Ywin] = DOOR;
							}
							else{
								gameBoard[newX][y] = ' ';
							}
						}


					newX = x-1;
					if (newX >= 0){
						if (gameBoard[newX][y] == 'b'){
							if(newX==Xwin && y==Ywin){
								gameBoard[Xwin][Ywin] = DOOR;
							}
							else{
								gameBoard[newX][y] = ' ';
							}
						}
					}

					newY = y+1;
					if (newY < gameBoard.length){
						if (gameBoard[newX][y] == 'b'){
							if(newX==Xwin && y==Ywin){
								gameBoard[Xwin][Ywin] = DOOR;
							}
							else{
								gameBoard[newX][y] = ' ';
							}
						}
					}
					
					newY = y-1;
					if (newY >= 0){
						if (gameBoard[newX][y] == 'b'){
							if(newX==Xwin && y==Ywin){
								gameBoard[Xwin][Ywin] = DOOR;
							}
							else{
								gameBoard[newX][y] = ' ';
							}
						}
					}			

		} catch (InterruptedException ex) {
			Logger.getLogger(BombFactory.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
