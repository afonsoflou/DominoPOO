import java.util.*;

public class DominoesGame {

    private int nLines;
    private int nColumns;
    private Player[] players;
    private GameBoard board;

    public DominoesGame(int nLines, int nColumns) {
        this.nLines = nLines;
        this.nColumns = nColumns;
        this.players = new Player[4];

    }

    public void startGame() {
        Scanner sc = new Scanner(System.in);
        LinkedList<Domino> Dominoes = new LinkedList<>();
        for (int i = 0; i < 7; i++)
            for (int j = i; j < 7; j++)
                Dominoes.add(new Domino(i, j));
        Collections.shuffle(Dominoes);
        this.board = new GameBoard(nColumns, nLines, new Domino(6, 6), 1, 1); // I'll think about this dog later.
        this.players[0] = new Player.Human(board, sc.nextLine(), Dominoes.subList(0, 7));
        for (int i = 1; i <= 3; i++) this.players[i] = new Player.NPC(board, "NPC " + i, Dominoes.subList(i*7, (i+1)*7));
    }

}

//