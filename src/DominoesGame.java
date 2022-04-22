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

    private boolean hasWinner(){
        for(Player player : players ) if(player.isWinner()) return true; return false;
    }

    public void startGame() {
        Scanner sc = new Scanner(System.in);
        LinkedList<Domino> Dominoes = new LinkedList<>();
        //Create Dominoes
        for (int i = 0; i < 7; i++)
            for (int j = i; j < 7; j++)
                Dominoes.add(new Domino(i, j));
        Collections.shuffle(Dominoes);
        //Create Human Player
        this.players[0] = new Player.Human(board, sc.nextLine(), Dominoes.subList(0, 7));
        sc.next();
        //Create NPC players
        for (int i = 1; i <= 3; i++) this.players[i] = new Player.NPC(board, "NPC " + i, Dominoes.subList(i*7, (i+1)*7));
        //Find first player
        int p = 0;
        Domino firstDomino = null;
        for(Player player: players){
            if(player.isFirst()){
                //get the first domino in this line, still not done.
                break;
            }

            p++;
        }
        //Create board, if first player is Human start in coordinates given by input
        if(p == 0) board = new GameBoard(nColumns,nLines,firstDomino, sc.nextInt(), sc.nextInt());
        else board = new GameBoard(nColumns,nLines,firstDomino,nLines/2,nColumns/2);
        //Players play until there is a winner
        while(!hasWinner())players[p++%4].play();
        //Put winner at start of array

        //Sort players by points at the end;



    }

}

//