import java.util.*;

public class DominoesGame {

    private final int nLines;
    private final int nColumns;
    private final Player[] players;
    private GameLine gameLine;

    public DominoesGame(int nLines, int nColumns) {
        this.nLines = nLines;
        this.nColumns = nColumns;
        this.players = new Player[4];
    }

    private boolean hasWinner(){
        for(Player player : players ) if(player.isWinner()) return true;return false;
    }

    public void startGame() {

        //Create Board
        GameBoard board = new GameBoard(nColumns,nLines);

        //Create Dominoes and shuffle them
        LinkedList<Domino> Dominoes = new LinkedList<>();
        for (int i = 0; i < 7; i++)
            for (int j = i; j < 7; j++)
                Dominoes.add(new Domino(i, j));
        Collections.shuffle(Dominoes);

        //Create Human Player
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose Your Name");
        this.players[0] = new Human(gameLine, sc.nextLine(), Dominoes.subList(0, 7));

        //Create NPC players
        for (int i = 1; i <= 3; i++) this.players[i] = new NPC(gameLine, "NPC " + i, Dominoes.subList(i*7, (i+1)*7));

        //Find first player and take their double six
        int currentPlayer = 0;
        Domino firstDomino = null;
        for(Player player: players){
            if(player.isFirst()){
                firstDomino = player.getDoubleSix();
                break;
            }
            currentPlayer++;
        }

        //Create gameLine, if first player is Human start in coordinates given by input
        if(currentPlayer == 0) {
            System.out.println("Input starting coordinates");
            gameLine = new GameLine(Objects.requireNonNull(firstDomino),sc.nextInt() ,sc.nextInt() , board);
        }
        else gameLine = new GameLine(Objects.requireNonNull(firstDomino),nColumns/2,nLines/2, board);
        for(Player player : players) player.joinGame(gameLine);

        //Players play until there is a winner
        while(!hasWinner()){
            currentPlayer++;
            System.out.println();
            System.out.println(players[currentPlayer%4].getName() + "'s turn");
            System.out.println();
            players[currentPlayer%4].play();
            board.print();
        }

        //Put winner at start of array
        Player swap = players[0];
        players[0] = players[currentPlayer%4];
        players[currentPlayer%4] = swap;

        //Sort players by points at the end;
        for (int i = 1; i < 4; i++) {
            Player temp = players[i];
            int j = i - 1;
            while (j >= 0 && players[i].getPoints() < players[j].getPoints()) {
                players[j + 1] = players[j];
                --j;
            }
            players[j + 1] = temp;
        }

        //Print results
        for(int i = 0; i<4; i++) System.out.println(i+1 + " Place : "+ players[i].getName() + "- " + players[i].getPoints() + " Points");

    }

}

