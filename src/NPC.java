
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class NPC extends Player{
    public NPC(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
        super(gameLine, PlayerName, dominoes, board);
    }


    public void play() {

        if(isFirst()){
            System.out.println("First Player");
            gameLine.firstPlay(getDoubleSix(),board.getColumns()/2, board.getLines()/2);
            return;
        }

        printPlayablePieces();

        //Store Playable Dominos in a list
        LinkedList<Domino> playableDominoes = new LinkedList<>();
        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(gameLine.canPlay(domino,corner))
                    playableDominoes.add(domino);

        //Play Domino with Highest Value
        Collections.sort(playableDominoes, Comparator.comparingInt(Domino::getValue));
        Domino playedDomino = playableDominoes.removeLast();
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
