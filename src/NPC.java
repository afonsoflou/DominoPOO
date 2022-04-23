import java.util.List;

public class NPC extends Player{
    public NPC(GameBoard board, String PlayerName, List<Domino> dominoes) {
        super(board, PlayerName, dominoes);
    }

    private void printPlayablePieces(){
        for(Domino domino : dominoes)
            for(Domino corner : board.getCorners())
                if(domino.canConnect(corner))
                    domino.print();
    }


    public void play() {
        if(!canPlay()) return;
        printPlayablePieces();
    }
}
