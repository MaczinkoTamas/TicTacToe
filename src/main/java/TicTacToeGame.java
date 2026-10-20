import java.util.Scanner;

public class TicTacToeGame {
    private final char[] board = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

    public boolean makeMove(int position) {
        return makeMove(position, 'X');
    }

    public boolean makeMove(int position, char player) {
        if (position < 0 || position > 8) {
            throw new IllegalArgumentException("A mező sorszáma 1 és 9 között lehet.");
        }
        if (player != 'X' && player != 'O') {
            throw new IllegalArgumentException("A játékos jele csak X vagy O lehet.");
        }
        if (board[position] != ' ') {
            return false;
        }
        board[position] = player;
        return true;
    }

    public char getWinner() {
        int[][] winningLines = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] line : winningLines) {
            char player = board[line[0]];
            if (player != ' ' && player == board[line[1]] && player == board[line[2]]) {
                return player;
            }
        }
        return ' ';
    }

    public int makeComputerMove() {
        int bestPosition = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int position = 0; position < board.length; position++) {
            if (board[position] == ' ') {
                board[position] = 'O';
                int score = minimax('X', 1);
                board[position] = ' ';
                if (score > bestScore) {
                    bestScore = score;
                    bestPosition = position;
                }
            }
        }
        if (bestPosition >= 0) {
            board[bestPosition] = 'O';
        }
        return bestPosition;
    }

    private int minimax(char player, int depth) {
        char winner = getWinner();
        if (winner == 'O') {
            return 10 - depth;
        }
        if (winner == 'X') {
            return depth - 10;
        }
        if (isBoardFull()) {
            return 0;
        }

        int bestScore = player == 'O' ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int position = 0; position < board.length; position++) {
            if (board[position] == ' ') {
                board[position] = player;
                int score = minimax(player == 'O' ? 'X' : 'O', depth + 1);
                board[position] = ' ';
                bestScore = player == 'O'
                        ? Math.max(bestScore, score)
                        : Math.min(bestScore, score);
            }
        }
        return bestScore;
    }

    private boolean isBoardFull() {
        for (char cell : board) {
            if (cell == ' ') {
                return false;
            }
        }
        return true;
    }

    public char[] getBoard() {
        return board.clone();
    }

    private void printBoard(java.io.PrintStream out) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                int position = row * 3 + column;
                char cell = board[position] == ' ' ? (char) ('1' + position) : board[position];
                out.print(" " + cell + " ");
                if (column < 2) {
                    out.print("|");
                }
            }
            out.println();
            if (row < 2) {
                out.println("---+---+---");
            }
        }
    }

    private void play(Scanner input, java.io.PrintStream out) {
        while (getWinner() == ' ' && !isBoardFull()) {
            printBoard(out);
            out.println("Te következel X-szel. Válassz egy mezőt (1-9):");
            if (!input.hasNextInt()) {
                if (!input.hasNext()) {
                    return;
                }
                input.next();
                out.println("Kérlek, 1 és 9 közötti számot adj meg.");
                continue;
            }

            int position = input.nextInt() - 1;
            if (position < 0 || position > 8) {
                out.println("A mező sorszáma 1 és 9 között lehet.");
                continue;
            }
            if (!makeMove(position, 'X')) {
                out.println("Ez a mező már foglalt.");
                continue;
            }
            if (getWinner() == ' ' && !isBoardFull()) {
                int computerPosition = makeComputerMove();
                out.println("A gép az " + (computerPosition + 1) + ". mezőt választotta.");
            }
        }

        printBoard(out);
        if (getWinner() != ' ') {
            out.println(getWinner() + " játékos nyert!");
        } else {
            out.println("Döntetlen!");
        }
    }

    public static void main(String[] args) {
        new TicTacToeGame().play(new Scanner(System.in), System.out);
    }
}
