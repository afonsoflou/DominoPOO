import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class DominoesGame {

    private final int nLines;
    private final int nColumns;
    private final Player[] players;
    public static final PrintStream out = new PrintStream(new OutputStream() {
        @Override
        public void write(int i) throws IOException {
            return;
        }
    });


    //There is a bug were a domino will be put on top of another one, although this is so unlikely it happens on around 1/4 of the times.
    //Considering the main runs the games 1000 times. It is indeed unlikely. I will patch it on the future.
    public static void main(String[] argv){
       var game = new DominoesGame(40,15);
       int nTESTS = 1000;
       int[] result = new int[4];
        for(int i =0; i < nTESTS; i++)
           result[game.testAi()]++;
        System.out.println("1st place :" + result[0] + ", 2nd place :" + result[1] + ", 3rd place :" + result[2] + ", 4th place :" + result[3]);
    }



    public DominoesGame(int nLines, int nColumns) {
        this.nLines = nLines;
        this.nColumns = nColumns;
        this.players = new Player[4];
    }

    //true if it wins false otherwise.
    public int testAi() {

        var stdout = System.out;
       System.setOut(out);

        //Create Board
        GameBoard board = new GameBoard(nColumns, nLines);
        GameLine gameLine = new GameLine(board);

        //Create Dominoes and shuffle them
        LinkedList<Domino> Dominoes = new LinkedList<>();
        for(int i = 0; i < 7; i++)
            for(int j = i; j < 7; j++)
                Dominoes.add(new Domino(i, j));
        Collections.shuffle(Dominoes);

        //Create AI Player
        this.players[0] = new AI(gameLine, "AI", Dominoes.subList(0, 7), board);

        //Create NPC players
        for(int i = 1; i <= 3; i++)
            this.players[i] = new NPC(gameLine, "NPC " + i, Dominoes.subList(i * 7, (i + 1) * 7), board);

        //Find first player
        int currentPlayer = 0;
        for(Player player : players) {
            if(player.isFirst()) {
                //First Player plays
                System.out.println(players[currentPlayer % 4].getName() + "'s turn");
                players[currentPlayer % 4].firstPlay();
                //board.print();
                break;
            }
            currentPlayer++;
        }

        //Players play until there is a winner, didntPlay counts how many players can't play in the turn
        //If the counter reaches 4, the game ends since no one can play.
        int didntPlay = 0;
        while(didntPlay < 4) {
            if(players[currentPlayer % 4].isWinner()) break;
            currentPlayer++;
            System.out.println();
            System.out.println(players[currentPlayer % 4].getName() + "'s turn");
            System.out.println();
            if(players[currentPlayer % 4].canPlay()) {
                players[currentPlayer % 4].play();
                didntPlay = 0;
                //board.print();
            } else {
                System.out.println("Can't Play");
                didntPlay++;
            }
        }

        //Put winner at start of array
        Player swap = players[0];
        players[0] = players[currentPlayer % 4];
        players[currentPlayer % 4] = swap;

        //Sort players by points at the end;
        Arrays.sort(players, Comparator.comparingInt(Player::getPoints));
        System.setOut(stdout);
        for(int i = 0;i < 4 ;i++)
            if(players[i] instanceof AI)
                return i;
        throw new IllegalArgumentException("What happened");
    }

    public void startGame() {

        //Create Board
        GameBoard board = new GameBoard(nColumns,nLines);
        GameLine gameLine = new GameLine(board);

        //Create Dominoes and shuffle them
        LinkedList<Domino> Dominoes = new LinkedList<>();
        for (int i = 0; i < 7; i++)
            for (int j = i; j < 7; j++)
                Dominoes.add(new Domino(i, j));
        Collections.shuffle(Dominoes);

        //Create Human Player
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose Your Name");
        this.players[0] = new Human(gameLine, sc.nextLine(), Dominoes.subList(0, 7), board);

        //Create NPC players
        for (int i = 1; i <= 3; i++) this.players[i] = new NPC(gameLine, "NPC " + i, Dominoes.subList(i*7, (i+1)*7), board);

        //Find first player
        int currentPlayer = 0;
        for(Player player: players){
            if(player.isFirst()){
                //First Player plays
                System.out.println(players[currentPlayer%4].getName() + "'s turn");
                players[currentPlayer%4].firstPlay();
                board.print();
                break;
            }
            currentPlayer++;
        }

        //Players play until there is a winner, didntPlay counts how many players can't play in the turn
        //If the counter reaches 4, the game ends since no one can play.
        int didntPlay = 0;
        while(didntPlay<4){
            if(players[currentPlayer%4].isWinner()) break;
            currentPlayer++;
            System.out.println("\n" + players[currentPlayer%4].getName() + "'s turn\n");
            if(players[currentPlayer%4].canPlay()) {
                players[currentPlayer%4].play();
                didntPlay = 0;
                board.print();
            }
            else{
                System.out.println("Can't Play");
                didntPlay++;
            }
        }

        //Put winner at start of array
        Player swap = players[0];
        players[0] = players[currentPlayer%4];
        players[currentPlayer%4] = swap;

        //Sort players by points at the end;
        Arrays.sort(players, Comparator.comparingInt(Player::getPoints));

        //Print results
        for(int i = 0; i<4; i++) System.out.println(i+1 + " Place : "+ players[i].getName() + "- " + players[i].getPoints() + " Points");
    }

}

