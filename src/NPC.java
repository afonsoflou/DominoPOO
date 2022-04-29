
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class NPC extends Player{
    public NPC(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
        super(gameLine, PlayerName, dominoes, board);
    }

    private void printPieces(){
        for(Domino domino : dominoes)
            domino.print();
    }

    private void printPlayablePieces(){
        System.out.println("Player's Dominoes");
        printPieces();
        System.out.println();
        System.out.println("Select Domino");
        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(gameLine.canPlay(domino,corner)){
                    domino.print();
                    break;}

    }

    public void play() {

        printPieces();

        if(isFirst()){
            System.out.println("First Player");
            gameLine.firstPlay(getDoubleSix(),board.getColumns()/2, board.getLines()/2);
            return;
        }

        printPlayablePieces();
        LinkedList<Domino> playableDominoes = new LinkedList<>();

        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(gameLine.canPlay(domino,corner))
                    playableDominoes.add(domino);

        //Play Domino with Highest Value
        Collections.sort(playableDominoes, Comparator.comparingInt(Domino::getValue));
        Domino playedDomino = playableDominoes.remove();
        removeDomino(playedDomino);

        System.out.println("Played Domino:");
        playedDomino.print();
        Domino playedCorner = null;

        for(Domino corner : gameLine.getCorners())
            if(gameLine.canPlay(playedDomino,corner)) {
                playedCorner = corner;
                break;
            }

        gameLine.insertDomino(playedDomino,playedCorner);
    }
}
