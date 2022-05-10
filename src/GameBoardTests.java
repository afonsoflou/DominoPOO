import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTests {
    Domino verticalDomino = new Domino(2,6);
    Domino verticalDoubleDomino = new Domino(6,6);
    Domino horizontalDomino = new Domino(1,6);
    Domino horizontalDoubleDomino = new Domino(2,2);

    {
        verticalDomino.beVertical();
        verticalDoubleDomino.beVertical();
    }

    /*vertical domino and vertical double domino board representation
                  x
                  -
                  x
     horizontal domino board representation
                  xx
     horizontal double domino board representation
                  x-x
     */


    //lab 3 unit test code.
    // Redirect STDIN and STDOUT for Mooshak like black box tests
    static private ByteArrayOutputStream setIOStreams(String input) {
        //set stdin
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        //set stdout
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        return os;
    }

    //the board must at least ensure that 100 spaces are available to play on the board at the time of creation.
    @Test
    public void testGameBoard(){
       assertThrows(IllegalArgumentException.class,() -> new GameBoard(0,0));
       assertThrows(IllegalArgumentException.class,() -> new GameBoard(10,9));
       assertDoesNotThrow(() -> new GameBoard(10,10));
    }

    //acceptable range for a board (25,25), for horizontal domino (0 to 23,0 to24), for vertical domino (0 to 24,2 to 24)
    //for horizontal doubles (0 to 22,0 to 24) and for the vertical doubles it should be identical to the vertical domino.
    @Test
    public void testInsertDomino(){
        GameBoard gameBoard = new GameBoard(25,25);

        //vertical domino check
        assertThrows(IllegalArgumentException.class,() -> gameBoard.insertDomino(0,1, verticalDomino));
        assertDoesNotThrow(() -> gameBoard.insertDomino(0,2, verticalDomino));
        assertDoesNotThrow(() -> gameBoard.insertDomino(24,24,verticalDomino));


        GameBoard gameBoard1 = new GameBoard(25,25);
        //horizontal domino check
        assertThrows(IllegalArgumentException.class,() -> gameBoard1.insertDomino(24,0,horizontalDomino));
        assertDoesNotThrow(() -> gameBoard1.insertDomino(23,0,horizontalDomino));
        assertDoesNotThrow(() -> gameBoard1.insertDomino(23,24,horizontalDomino));

        GameBoard gameBoard2 = new GameBoard(25,25);
        //vertical double domino check.
        assertThrows(IllegalArgumentException.class,() -> gameBoard2.insertDomino(0,1, verticalDoubleDomino));
        assertDoesNotThrow(() -> gameBoard2.insertDomino(0,2, verticalDoubleDomino));
        assertDoesNotThrow(() -> gameBoard2.insertDomino(24,24,verticalDoubleDomino));

        GameBoard gameBoard3 = new GameBoard(25,25);
        //horizontal double domino check
        assertThrows(IllegalArgumentException.class,() -> gameBoard3.insertDomino(23,0,horizontalDoubleDomino));
        assertDoesNotThrow(() -> gameBoard3.insertDomino(22,0,horizontalDoubleDomino));
        assertDoesNotThrow(() -> gameBoard3.insertDomino(22,24,horizontalDoubleDomino));

        //cannot insert pieces on top of another pieces.
        GameBoard gameBoard4 = new GameBoard(25,25);
        gameBoard4.insertDomino(10,10,verticalDomino);

        assertThrows(IllegalArgumentException.class,() -> gameBoard4.insertDomino(10,10,verticalDomino));
        assertThrows(IllegalArgumentException.class,() -> gameBoard4.insertDomino(9,8,horizontalDomino));
        assertThrows(IllegalArgumentException.class,() -> gameBoard4.insertDomino(8,8,horizontalDoubleDomino));

        gameBoard4.insertDomino(10,2,verticalDomino);
        assertThrows(IllegalArgumentException.class,() -> gameBoard4.insertDomino(10,4,verticalDomino));
        assertThrows(IllegalArgumentException.class,() -> gameBoard4.insertDomino(10,4,verticalDoubleDomino));
    }

    @Test
    void printTest() throws IOException {

        var gameBoard = new GameBoard(30,10);

        //insert double vertical 66 on coordinates 10,4.
        String expected = new String(
                "#".repeat(32) + "\n" +
                        ("#" + " ".repeat(30) + "#\n").repeat(5) +
                        "#" + " ".repeat(10) + "6" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(10) + "-" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(10) + "6" + " ".repeat(19) + "#\n" +
                        ("#" + " ".repeat(30) + "#\n").repeat(2) +
                        "#".repeat(32) + "\n"
        );

        PrintStream stdout = System.out;
        ByteArrayOutputStream os = setIOStreams("");
        gameBoard.insertDomino(10,4,verticalDoubleDomino);
        gameBoard.print();

/* in case you want to see a comparison between the output and the expected output put this after gameBoard.print()
        System.setOut(stdout);
        System.out.println("expected\n" + expected);
        System.out.println("received\n" + os.toString());
*/

        assertEquals(expected,os.toString());
        os.close();


        expected = new String(
                "#".repeat(32) + "\n" +
                        ("#" + " ".repeat(30) + "#\n").repeat(5) +
                        "#" + " ".repeat(7) + "   6" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(7) + "16 -" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(7) + "   6" + " ".repeat(19) + "#\n" +
                        ("#" + " ".repeat(30) + "#\n").repeat(2) +
                        "#".repeat(32) + "\n"
        );

        os = setIOStreams("");
        gameBoard.insertDomino(7,3,horizontalDomino);
        gameBoard.print();

        assertEquals(expected,os.toString());
        os.close();

        expected = new String(
                "#".repeat(32) + "\n" +
                        "#" + " ".repeat(7) + "  2-2" + " ".repeat(18) + "#\n" +
                        "#" + " ".repeat(7) + "   2" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(7) + "   -" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(7) + "   6" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(30) + "#\n" +
                        "#" + " ".repeat(7) + "   6" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(7) + "16 -" + " ".repeat(19) + "#\n" +
                        "#" + " ".repeat(7) + "   6" + " ".repeat(19) + "#\n" +
                        ("#" + " ".repeat(30) + "#\n").repeat(2) +
                        "#".repeat(32) + "\n"
        );

        os = setIOStreams("");
        gameBoard.insertDomino(10,8,verticalDomino);
        gameBoard.insertDomino(9,9,horizontalDoubleDomino);
        gameBoard.print();


        assertEquals(expected,os.toString());
        os.close();
    }
}
