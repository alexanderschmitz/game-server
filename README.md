# game-server
 Bachelor Assignment TU Berlin Alexander Schmitz

## Installation

import the project into your IDE as maven project

`maven clean install`

For testing you can use the existing TicTacToeExample

Start the server by executing App.java

Connect to the websocket at:
`ws://localhost:8025/websockets/game`

send messages as JSON. messageType defines the purpose of the message and can be a number from:
- 0: Login
- 1: GetGames
- 2: CreateGame
- 3: JoinGame
- 4: Move
- 5: DB Query

General structure:
{
    "type": _messageType_,
    "username" : _name_,
    "playerID" : _providedAfterLogin_,
    "move": _any string_,
    "gameType" : _for 2 or 5_
}

Better documentation in the Paper.

If you want to play against the Bot, try the PlayTheBotTest  in src/test/java/de/tu_berlin/pjki_server

## Contact

For any questions:
- alexander.schmitz98@outlook.com
