package connectfour

const val player1Symbol = "o"
const val player2Symbol = "*"
var player1 = ""
var player2 = ""
var board = mutableListOf<MutableList<String>>()
var row: Int = 0
var col: Int = 0
var winCondition = false
var fullBoard = false
var scorePlayer1 = 0
var scorePlayer2 = 0
var numberOfGames = 0
var endGame = false

fun main() {
    println("Connect Four\nFirst player's name: ")
    player1 = readln()
    println("Second player's name: ")
    player2 = readln()
    val settings = setter()
    row = settings[0]
    col = settings[1]
    numberOfGames = numberer()
    println("$player1 VS $player2")
    println("$row X $col board")
    if (numberOfGames > 1){
        println("Total $numberOfGames games")
    }
    turner()
}

fun turner() {
    var counter = 1
    var counter2 = numberOfGames
    while (counter2 > 0) {
        winCondition = false
        fullBoard = false
        if (numberOfGames ==1) {
            println("Single game")
        } else {
            println("Game #$counter")
        }
        gameMap2(row,col)
        printer()
        if (counter % 2 == 1) {
            turns(player1, row, col, player1Symbol)
        } else {
            turns(player2, row, col, player2Symbol)
        }
        if (endGame) {
            println("Game over!")
            break
        }
        println("Score\n"+"$player1: $scorePlayer1 $player2: $scorePlayer2")
        if (counter2 == 1){
            println("Game over!")
        }
        counter++
        counter2--
    }
}
fun numberer(): Int {
    println("Do you want to play single or multiple games?\n" +
            "For a single game, input 1 or press Enter\n" +
            "Input a number of games:")
    val number = readln()
    val number2 = number.toIntOrNull()
    if (number == "") {
        return 1
    } else {
        if (number2 != null) {
            if (number2 >= 1) {
                return number2
            } else {
                println("Invalid input")
                return numberer()
            }
        } else {
            println("Invalid input")
            return numberer()
        }
    }
}

fun setter(): List<Int> {
    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")
    var boardSize = readln()
    boardSize=boardSize.replace(" ","").replace("\b","").replace("\t","")
    boardSize=boardSize.lowercase()
    val boardFinalSize=boardSize.split('x')

    val row: Int
    val col: Int
    if (boardFinalSize.size == 2 && (boardFinalSize[0].toIntOrNull() != null) && (boardFinalSize[1].toIntOrNull() != null)){
        if (boardFinalSize[0].toInt() in 5..9) row = boardFinalSize[0].toInt()
        else {
            println("Board rows should be from 5 to 9")
            return setter()
        }
        if (boardFinalSize[1].toInt() in 5..9) col = boardFinalSize[1].toInt()
        else {
            println("Board columns should be from 5 to 9")
            return setter()
        }
        return listOf(row,col)
    } else if (boardSize == "") {
        return listOf(6,7)
    } else {
        println("Invalid input")
        return setter()
    }
}

fun gameMap2(row: Int, col: Int) {
    board = mutableListOf<MutableList<String>>()
    for (r in 1..row){
        board.add(mutableListOf())
    }
    for (r in 0 until row) {
        for (c in 0 until 2*col+1) {
            if (c % 2 == 0) board[r].add("|")
            else board[r].add(" ")
        }
    }
}

fun printer(){
    for (c in 1..col) print(" $c")
    print("\n")
    for (r in 0 until board.size) {
        for (c in 0 until board[0].size){
            print(board[r][c])
        }
        print("\n")
    }
    repeat(board[0].size) {
        print("=")}
    print("\n")
}

fun turns(player: String, row: Int, col: Int, symbol: String){
    println("$player's turn:")
    val answer = readln()
    val column = answer.toIntOrNull()
    var completed = false
    if (column != null && column in 1..col) {
        for (space in row downTo 1){
            if (board[space - 1][2*column - 1] == " ") {
                board[space - 1][2*column - 1] = symbol
                completed = true
                printer()
                revision(space - 1, 2*column -1, symbol)
                break
            }
        }
        if (!completed){
            println("Column $column is full")
            turns(player, row, col, symbol)
        } else if (!winCondition && !fullBoard){
            if (player == player1){
                turns(player2,row,col, player2Symbol)
            } else {
                turns(player1,row,col, player1Symbol)
            }
        } else if (!winCondition && fullBoard) {
            scorePlayer1+=1
            scorePlayer2+=1
            println("It is a draw")
        } else if (winCondition){
            if (player == player1){
                scorePlayer1+=2
            } else {
                scorePlayer2+=2
            }
            println("Player $player won")
        }

    } else if (column!= null){
        println("The column number is out of range (1 - $col)")
        turns(player, row, col, symbol)
    } else if (answer == "end"){
        endGame = true
    } else {
        println("Incorrect column number")
        turns(player, row, col, symbol)
    }

}

fun revision(row: Int, col: Int, symbol: String) {
    fullBoard = true
    var counter = 0
    for (r in 0 until board.size) {
        for (c in 1 until board[0].size - 1 step 2) {
            if (board[r][c] == " ") {
                fullBoard = false
                break
            }
        }
    }
    //vertical
    while (counter < 4 && row + counter < board.size) {
        if (board[row + counter][col] == symbol) {
            counter++
            if (counter == 4) {
                winCondition = true
                break
            }
        } else {
            counter = 0
            break
        }
    }
    //horizontal
    loop1@ for (r in 0 until board.size) {
        for (c in 1 until board[0].size - 1 step 2) {
            if (board[r][c] == symbol) {
                counter++
                if (counter == 4) {
                    winCondition = true
                    break@loop1
                }
            } else counter = 0
        }
    }

    //diagonal1
    loop2@ for (r in 0 until board.size) {
        for (c in 1 until board[0].size - 1 step 2) {
            counter = 0
            var fila = r
            var columna = c
            while (fila in 0 until board.size && columna in 0 until board[0].size) {
                if (board[fila][columna] == symbol) {
                    counter++
                    if (counter == 4) {
                        winCondition = true
                        break@loop2
                    }
                } else counter = 0
                fila++
                columna += 2
            }
        }
    }

    //diagonal2
    loop3@for (r in 0 until board.size) {
        for (c in 1 until board[0].size - 1 step 2) {
            counter = 0
            var fila = r
            var columna = c
            while(fila in 0 until board.size && columna in 0 until board[0].size) {
                if (board[fila][columna] == symbol) {
                    counter++
                    if (counter==4) {
                        winCondition = true
                        break@loop3
                    }
                } else counter = 0
                fila--
                columna+=2
            }
        }
    }

}
