
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NPC extends Player{
    public NPC(GameLine gameLine, String PlayerName, List<Domino> dominoes) {
        super(gameLine, PlayerName, dominoes);
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
                if(domino.canConnect(corner)){
                    domino.print();
                    break;}

    }

    public void play() {
        if(cantPlay()) return;

        printPlayablePieces();
        LinkedList<Domino> playableDominoes = new LinkedList<>();

        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(domino.canConnect(corner))
                    playableDominoes.add(domino);

        Collections.shuffle(playableDominoes);
        Domino playedDomino = playableDominoes.remove();
        removeDomino(playedDomino);

        System.out.println("Played Domino:");
        playedDomino.print();
        Domino playedCorner = null;

        for(Domino corner : gameLine.getCorners())
            if(corner.canConnect(playedDomino)) {
                playedCorner = corner;
                break;
            }

        gameLine.insertDomino(playedDomino,playedCorner);
    }
}
