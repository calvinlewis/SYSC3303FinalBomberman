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

        private static int  PlayerNumber;
        private static char[] BombermanList = {'o','O','0','@'};
        private static int[][] BombermanPos = {{-1,-1}, {-1,-1}, {-1,-1}, {-1,-1}};
        private static int[] Bombermanlives = {2, 2, 2, 2}; //decrement when lives==0
	private static char DOOR = 'D'; 
	private static char BOX = 'b';
        public static char POWER = 'P';
        private static int[][] Powers = {{-1,-1},{-1,-1},{-1,-1},{-1,-1}};   //list of the powers
        private static int numPower;    //index for powers
        
	public static int Xwin;
	public static int Ywin;
	boolean doorset = false;
	private char[][] gameBoard;
	private static int bombID;
	private int xSize, ySize, levels;

	//TestDriver  driver = new TestDriver();

	GameBoard(int x, int y, int l) {
		this.xSize = x;
		this.ySize = y;
		this.levels = l;
		this.bombID = 0;
                this.numPower = 0;
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
							Xwin = i;
							Ywin = j;
						}
                                                else{
                                                    if(/*rand%2 ==0 &&*/ numPower<4){
                                                        Powers[numPower][0]= i;
                                                        Powers[numPower][1]= j;
                                                        numPower++;
                                                    }
                                                }
					}
					else{
						gameBoard[i][j] = ' ';
					}
				}

			}
		}

		//gameBoard[Xwin][Ywin] = DOOR;
                

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
				direction.equals("PLAY") || 
				direction.equals("BOMB");
	}


	// Method to move the bomberman to valid spaces
	public boolean move(int x, int y, String direction, int playernumber) {
                

		int newX=x;
		int newY=y;
		if (validMove(direction)) {
                    switch (direction) {
                        
			case "BOMB":

				if(x == getBombermanX(playernumber) && y == getBombermanY(playernumber)){
					gameBoard[x][y] = 'Q';
				}
				else if(move(x,y,"DOWN", playernumber)!=false){
					move(x,y,"DOWN", playernumber);
				}
				else if(move(x,y,"RIGHT", playernumber)!=false){
					move(x,y,"RIGHT", playernumber);
				}
				else{
					gameBoard[x][y] = '*';
				}
				BombFactory bomb = new BombFactory(gameBoard, x, y, Xwin, Ywin, DOOR);
				bomb.start();
				return true;
                        
                        case "PLAY":
                            if(gameBoard[0][0] == ' '){
                                gameBoard[0][0] = BombermanList[playernumber];
                                updatebombermanpos(0, 0, playernumber);
                            }
                            else if(gameBoard[0][1] == ' '){
                                gameBoard[0][1] = BombermanList[playernumber];
                                updatebombermanpos(0, 1, playernumber);
                            }
                            else if(gameBoard[0][2] == ' '){
                                gameBoard[0][2] = BombermanList[playernumber];
                                updatebombermanpos(0, 2, playernumber);
                            }
                            else if(gameBoard[0][3] == ' '){
                                gameBoard[0][3] = BombermanList[playernumber];
                                updatebombermanpos(0, 3, playernumber);
                            }
                            else if(gameBoard[0][4] == ' '){
                                gameBoard[0][4] = BombermanList[playernumber];
                                updatebombermanpos(0, 4, playernumber);
                            }
                            else if(gameBoard[1][0] == ' '){
                                gameBoard[1][0] = BombermanList[playernumber];
                                updatebombermanpos(1, 0, playernumber);
                            }
                            
                            return true;

			case "DOWN": 
                            newX = x+1;
                            if(getBombermanX(playernumber)==-1){
                                return false;
                            }
                            else if (newX < xSize && newX >= 0 && isEmptySpace(newX,y)) {
                                updatebombermanpos(newX, y, playernumber);
                                gameBoard[newX][y] = BombermanList[playernumber];
                                if(gameBoard[x][y]=='Q'){
                                    gameBoard[x][y] = '*';
                                }
                                else{
                                    gameBoard[x][y] = ' ';
                                }
                                return true;
                            }
                            else if(newX==Xwin && newY==Ywin && gameBoard[newX][newY]!= 'b'){
                                
                                if(gameBoard[x][y]=='Q'){
                                    gameBoard[x][y] = '*';
                                }
                                else{
                                    gameBoard[x][y] = ' ';
                                }
                                updatebombermanpos(-1,-1, playernumber);
                                System.out.printf("Player %d found door!!\n", playernumber);
                                //driver.addLog("Player found door!!");
                                //driver.writeLog();


                                return true;
                            }
                            else if(Powercheck(newX, newY)>-1 && gameBoard[newX][newY]!= 'b'){
                                Bombermanlives[playernumber]++;
                                System.out.printf("Player %d took power up. now has %d lives!\n",playernumber,Bombermanlives[playernumber]);
                                if(gameBoard[x][y]=='Q'){
                                    gameBoard[x][y] = '*';
                                }
                                else{
                                    gameBoard[x][y] = ' ';
                                }
                                gameBoard[newX][newY]=BombermanList[playernumber];
                                updatebombermanpos(newX,newY, playernumber);
                                return true;
                            }
                            break;

			case "UP": 
                            newX = x-1;
                            if(getBombermanX(playernumber)==-1){
                                return false;
                            }
                            else if (newX < xSize && newX >= 0 && isEmptySpace(newX,y)) {
                                gameBoard[newX][y] = BombermanList[playernumber];
                                updatebombermanpos(newX, y, playernumber);
                                if(gameBoard[x][y]=='Q'){
                                        gameBoard[x][y] = '*';
                                    }
                                    else{
                                        gameBoard[x][y] = ' ';
                                    }
                                    return true;
                            }
                            else if(newX==Xwin && newY==Ywin && gameBoard[newX][newY]!= 'b'){
                                if(gameBoard[x][y]=='Q'){
                                        gameBoard[x][y] = '*';
                                    }
                                    else{
                                        gameBoard[x][y] = ' ';
                                    }
                                    updatebombermanpos(-1,-1, playernumber);
                                    System.out.printf("Player %d found door!!\n", playernumber);
                                    //driver.addLog("Player found door!!");
                                    //driver.writeLog();


                                    return true;
                            }
                            else if(Powercheck(newX, newY)>-1 && gameBoard[newX][newY]!= 'b'){
                                Bombermanlives[playernumber]++;
                                System.out.printf("Player %d took power up. now has %d lives!\n",playernumber,Bombermanlives[playernumber]);
                                if(gameBoard[x][y]=='Q'){
                                    gameBoard[x][y] = '*';
                                }
                                else{
                                    gameBoard[x][y] = ' ';
                                }
                                gameBoard[newX][newY]=BombermanList[playernumber];
                                updatebombermanpos(newX,newY, playernumber);
                                return true;
                            }
                            
                            break;

			case "LEFT": 
                            newY = y-1;
                            if(getBombermanX(playernumber)==-1){
                                return false;
                            }
                            else if (newY < ySize && newY >= 0 && isEmptySpace(x,newY)) {
                                gameBoard[x][newY] = BombermanList[playernumber];
                                updatebombermanpos(x, newY, playernumber);
                                if(gameBoard[x][y]=='Q'){
                                        gameBoard[x][y] = '*';
                                    }
                                    else{
                                        gameBoard[x][y] = ' ';
                                    }
                                    return true;
                            }
                            else if(newX==Xwin && newY==Ywin && gameBoard[newX][newY]!= 'b'){
                                if(gameBoard[x][y]=='Q'){
                                    gameBoard[x][y] = '*';
                                }
                                else{
                                    gameBoard[x][y] = ' ';
                                }
                                updatebombermanpos(-1,-1, playernumber);
                                System.out.printf("Player %d found door!!\n", playernumber);
                                //driver.addLog("Player found door!!");
                                //driver.writeLog();


                                return true;
                            }
                            else if(Powercheck(newX, newY)>-1 && gameBoard[newX][newY]!= 'b'){
                                Bombermanlives[playernumber]++;
                                System.out.printf("Player %d took power up. now has %d lives!\n",playernumber,Bombermanlives[playernumber]);
                                if(gameBoard[x][y]=='Q'){
                                    gameBoard[x][y] = '*';
                                }
                                else{
                                    gameBoard[x][y] = ' ';
                                }
                                gameBoard[newX][newY]=BombermanList[playernumber];
                                updatebombermanpos(newX,newY, playernumber);
                                return true;
                            }
                            
                            break;

			case "RIGHT": 
                            newY = y+1;
                            if(getBombermanX(playernumber)==-1){
                                return false;
                            }
                            else if (newY < ySize && newY >= 0 && isEmptySpace(x,newY)) {
                                gameBoard[x][newY] = BombermanList[playernumber];
                                updatebombermanpos(x, newY, playernumber);
                                
                                if(gameBoard[x][y]=='Q'){
                                        gameBoard[x][y] = '*';
                                    }
                                else{
                                        gameBoard[x][y] = ' ';
                                }
                                return true;
                            }
                            else if(newX==Xwin && newY==Ywin && gameBoard[newX][newY]!= 'b'){
                                if(gameBoard[x][y]=='Q'){
                                        gameBoard[x][y] = '*';
                                    }
                                    else{
                                        gameBoard[x][y] = ' ';
                                    }
                                    updatebombermanpos(-1,-1, playernumber);
                                    System.out.printf("Player %d found door!!\n", playernumber);
                                    //driver.addLog("Player found door!!");
                                    //driver.writeLog();


                                    return true;
                            }
                            else if(Powercheck(newX, newY)>-1 && gameBoard[newX][newY]!= 'b'){
                                Bombermanlives[playernumber]++;
                                System.out.printf("Player %d took power up. now has %d lives!\n",playernumber,Bombermanlives[playernumber]);
                                if(gameBoard[x][y]=='Q'){
                                    gameBoard[x][y] = '*';
                                }
                                else{
                                    gameBoard[x][y] = ' ';
                                }
                                gameBoard[newX][newY]=BombermanList[playernumber];
                                updatebombermanpos(newX,newY, playernumber);
                                return true;
                            }
                            break;
			}
                }
		return false;
	}


	// Get current bomberman x position
	public int getBombermanX(int playernumber) {
            return BombermanPos[playernumber][0];
        }


	// Gets current bomberman y position
	public int getBombermanY(int playernumber) {
            return BombermanPos[playernumber][1];
	}


	// Generates random number for boxes
	public static int randInt(int min, int max) {

		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static void updatebombermanpos(int x, int y, int playernumber){
		BombermanPos[playernumber][0]= x;
                BombermanPos[playernumber][1]= y;
        }
        
        public static int Powercheck(int newX, int newY){
            int i = 0;
            int j = 0;
            while(i<4){
                if(Powers[i][0]==newX && Powers[i][1]==newY){
                    return i;
                }
                i++;
            }
            return -1;
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

			sleep(6000);
			gameBoard[x][y]=' ';

			// Check for boxes
			int newX, newY;

			newX = x+1;
			if (newX < gameBoard.length)
				if (gameBoard[newX][y] == 'b'){
					if(newX==Xwin && y==Ywin){
						gameBoard[Xwin][Ywin] = DOOR;
					}
                                        else if(GameBoard.Powercheck(newX, y)>-1){
                                            gameBoard[newX][y] = GameBoard.POWER;
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
                                        else if(GameBoard.Powercheck(newX, y)>-1){
                                            gameBoard[newX][y] = GameBoard.POWER;
                                        }
					else{
						gameBoard[newX][y] = ' ';
					}
				}
			}

			newY = y+1;
			if (newY < gameBoard.length){
				if (gameBoard[x][newY] == 'b'){
					if(x==Xwin && newY==Ywin){
						gameBoard[Xwin][Ywin] = DOOR;
					}
                                        else if(GameBoard.Powercheck(x, newY)>-1){
                                            gameBoard[x][newY] = GameBoard.POWER;
                                        }
					else{
						gameBoard[x][newY] = ' ';
					}
				}
			}
			
			newY = y-1;
			if (newY >= 0){
				if (gameBoard[x][newY] == 'b'){
					if(x==Xwin && newY==Ywin){
						gameBoard[Xwin][Ywin] = DOOR;
					}
                                        else if(GameBoard.Powercheck(x, newY)>-1){
                                            gameBoard[x][newY] = GameBoard.POWER;
                                        }
					else{
						gameBoard[x][newY] = ' ';
					}
				}
			}			

		} catch (InterruptedException ex) {
			Logger.getLogger(BombFactory.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
