

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TicTacToeGameTest {
    private TicTacToeGame game;
    @BeforeEach
    void setUp() {
        game = new TicTacToeGame();
    }
    //Érvényes lépések
    @Test
    void testMakeMoveShouldReturnTrueWhenPositionIsValid(){
        boolean result = game.makeMove(0);
        assertTrue(result);
    }
//Foglalt pozíció
    @Test
    void testMakeMoveShouldReturnFalseWhenPositionIsAlreadyTaken(){
        game.makeMove(4);
        boolean result = game.makeMove(4);
        assertFalse(result);
    }
    //Alacsony szám
    @Test
    void testMakeMoveShouldReturnExceptionWhenPositionIsTooLow(){
        assertThrows(IllegalArgumentException.class, ()-> {
            game.makeMove(-1);
        });

    }
    //Magas szám
    @Test
    void testMakeMoveShouldThrowExceptionWhenPositionIsTooHigh(){
        assertThrows(IllegalArgumentException.class, ()-> {
            game.makeMove(9);
        });
    }
    //A tábla mezői
    @Test
    void testGetBoardShouldReturnBoardArray(){
        char[] board = game.getBoard();
        assertNotNull(board);
        assertEquals(9, board.length);
    }
//Az eltárolt játékos
    @Test
    void testMakeMoveStoresTheSelectedPlayer() {
        assertTrue(game.makeMove(0, 'O'));
        assertEquals('O', game.getBoard()[0]);
    }
//Nyertes játékos
    @Test
    void testGetWinnerReturnsWinningPlayer() {
        game.makeMove(0, 'X');
        game.makeMove(1, 'X');
        game.makeMove(2, 'X');

        assertEquals('X', game.getWinner());
    }
//A bot nyerő lépései
    @Test
    void computerTakesWinningMoveWhenAvailable() {
        game.makeMove(0, 'O');
        game.makeMove(1, 'O');
        game.makeMove(3, 'X');
        game.makeMove(4, 'X');

        assertEquals(2, game.makeComputerMove());
        assertEquals('O', game.getWinner());
    }
//A bot akadályozó lépései
    @Test
    void computerBlocksHumanWinningMove() {
        game.makeMove(0, 'X');
        game.makeMove(1, 'X');
        game.makeMove(4, 'O');

        assertEquals(2, game.makeComputerMove());
        assertEquals(' ', game.getWinner());
    }
//Tábla és nyertes
    @Test
    void testMainDisplaysBoardAndWinner() {
        java.io.InputStream originalIn = System.in;
        java.io.PrintStream originalOut = System.out;
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        try {
            System.setIn(new java.io.ByteArrayInputStream("1".getBytes()));
            System.setOut(new java.io.PrintStream(output));
            TicTacToeGame.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        assertTrue(output.toString().contains("A gép az "));
    }
//Érvénytelen, foglalt és döntetlen kezelése
    @Test
    void testMainHandlesInvalidAndTakenPositionsAndDraws() {
        java.io.InputStream originalIn = System.in;
        java.io.PrintStream originalOut = System.out;
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        try {
            System.setIn(new java.io.ByteArrayInputStream("nope 10 1 1".getBytes()));
            System.setOut(new java.io.PrintStream(output));
            TicTacToeGame.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        assertTrue(output.toString().contains("Kérlek, 1 és 9 közötti számot adj meg."));
        assertTrue(output.toString().contains("A mező sorszáma 1 és 9 között lehet."));
        assertTrue(output.toString().contains("Ez a mező már foglalt."));
    }
}
