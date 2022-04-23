import java.util.List;
import java.util.Scanner;

public class Human extends Player{

    public Human(GameBoard board, String PlayerName, List<Domino> dominoes) {
        super(board, PlayerName, dominoes);
    }

    private void printPieces(){
        for(Domino domino : dominoes)
            domino.print();
    }

    private void printPlayablePieces(){
        for(Domino domino : dominoes)
            for(Domino corner : board.getCorners())
                if(domino.isEqual(corner))
                   domino.print();
    }

    public void play() {
        if(!canPlay()) return;
        Scanner sc = new Scanner(System.in);
        printPlayablePieces();
        //board.insertDomino(domino,corner);
    }
}
