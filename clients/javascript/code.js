const player = "O";
const computer = "X";

let board_full = false;
let play_board = ["", "", "", "", "", "", "", "", ""];
let ws = new WebSocket("ws://localhost:8025/websockets/game");
let username = "";
let playerID = "";
let gameID = "";

const board_container = document.querySelector(".play-area");

const winner_statement = document.getElementById("winner");

check_board_complete = () => {
    let flag = true;
    play_board.forEach(element => {
      if (element != player && element != computer) {
        flag = false;
      }
    });
    board_full = flag;
  };


const addPlayerMove = e => {
  if (!board_full && play_board[e] == "") {
    play_board[e] = player;
    game_loop();
    addComputerMove();
  }
};

const check_line = (a, b, c) => {
  return (
    play_board[a] == play_board[b] &&
    play_board[b] == play_board[c] &&
    (play_board[a] == player || play_board[a] == computer)
  );
};

const check_match = () => {
  for (i = 0; i < 9; i += 3) {
    if (check_line(i, i + 1, i + 2)) {
      return play_board[i];
    }
  }
  for (i = 0; i < 3; i++) {
    if (check_line(i, i + 3, i + 6)) {
      return play_board[i];
    }
  }
  if (check_line(0, 4, 8)) {
    return play_board[0];
  }
  if (check_line(2, 4, 6)) {
    return play_board[2];
  }
  return "";
};

const render_board = () => {
  board_container.innerHTML = ""
  play_board.forEach((e, i) => {
    board_container.innerHTML += `<div id="block_${i}" class="block" onclick="addPlayerMove(${i})">${play_board[i]}</div>`
    if (e == player || e == computer) {
      document.querySelector(`#block_${i}`).classList.add("occupied");
    }
  });
};

const game_loop = () => {
  render_board();
  check_board_complete();
  check_for_winner();
}




//WEBSOCKET stuff

const registerUser = () => {
  let username = document.getElementById("username").value;
 
  let message = JSON.stringify({
    "type": 0,
    "username": username
  });
  console.log(message);
  ws.send(message);
}



ws.onopen = function(){
}

ws.onmessage = e => {
    let message = e.data;
    let parsedMessage = JSON.parse(message);
    console.log("Message from server: \n" + message);
    username = parsedMessage.username;
    playerID = parsedMessage.playerID;
}

ws.onclose = () => {
    alert("disconnected");
}