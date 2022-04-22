import java.util.List;

public class Human extends Player{

    public Human(GameBoard board, String PlayerName, List<Domino> dominoes) {
        super(board, PlayerName, dominoes);
    }

    private void printPieces(){
        for(Domino domino : dominoes)
            domino.print();
    }

    public void play() {

    }
}
