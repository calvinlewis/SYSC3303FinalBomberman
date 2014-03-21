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
9. Move player as you wish (u = UP, d = DOWN, l = LEFT, r = RIGHT, b = BOMB)
10. Type in 'END_GAME' to end the game, or find door hidden in boxes


Test Cases
======================
Full game states:
1. Start the game
2. Show enemies as 'e' characters
3. Show boxes as 'b' characters
4. Show power-ups as 'P' characters once revealed after bombing boxes


Game behaviours:
1. Start the game
2. Make another PlayerClient 
3. Attempt to move PlayerClient's on top of each, will not work and game will display "Invalid move!"
4. Find power-ups will be displayed if power-up is hidden after bombing a box
5. Bombs will deploy after 3 seconds of the BombFactory thread sleeping

