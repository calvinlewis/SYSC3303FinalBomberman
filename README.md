SYSC3303FinalBomberman
======================

To start game:

1. Open terminal
2. Change directory to bin folder
3. Type in 'java Server' (will allocate default port number to 9000)
4. Server will now be running and will allow for PlayerClient's to connect
5. Make another terminal tab
6. Type in 'java PlayerClient' (will allocate PlayerClient to default port/ip address of 9000 and localhost)
7. Enter in user name, press Enter
8. Then type in 'START_GAME' to start a game, or 'JOIN_GAME' to join an already created game
9. Then type in 'PLAY' to deploy the player and play the game
10. Move player as you wish (u = UP, d = DOWN, l = LEFT, r = RIGHT, b = BOMB)
11. Type in 'END_GAME' to end the game, or find door hidden in boxes


Test Plan
======================
Functional tests:

Full game states:
1. Start the game
2. Show enemies as 'e' characters
3. Show boxes as 'b' characters
4. Show power-ups as 'P' characters once revealed after bombing boxes
5. Once door is found, game ends and displays which player found the door in the server terminal window


Game behaviours:
1. Start the server/game
2. Make another PlayerClient in new terminal tab
3. Move player as you wish (u = UP, d = DOWN, l = LEFT, r = RIGHT, b = BOMB)
4. Attempt to move PlayerClient's on top of each other (one goes right, one goes left), will not work and game will display "Invalid move!"
    -  Same case for enemies
5. Find power-ups; will be displayed if power-up is hidden after bombing a box
6. Bombs will deploy after 3 seconds of the BombFactory thread sleeping

Concurrency tests:

Buffer full:
1. Start the server
2. Create 4 PlayerClients to join the same game
3. Create a 5th PlayerClient to attempt to join the game.  Will receive message "Server too busy, try again later"
4. Type in 'END_GAME' to make one of the PlayerClient's leave the game
5. Now you can successfuly add that 5th player as there are now 1 more available client socket

Buffer empty:
1. DO NOT start server
2. Run ConcurrencyTestCase (should just run our one test case.. sorry about this)

Player-commands after player is dead:
1. Start a game with two PlayerClients
2. Let one player place a bomb near other player (run away!!)
3. Player will die and a message will be displayed in the server terminal window
4. That client will not be able to make any more moves, but the alive player can still move at will
