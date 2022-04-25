
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NPC extends Player{
    public NPC(GameLine gameLine, String PlayerName, List<Domino> dominoes) {
        super(gameLine, PlayerName, dominoes);
    }


    public void play() {
        if(!canPlay()) return;

        LinkedList<Domino> playableDominoes = new LinkedList<>();

        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(domino.canConnect(corner))
                    playableDominoes.add(domino);

        Collections.shuffle(playableDominoes);
        Domino playedDomino = playableDominoes.remove();
        dominoes.removeIf(x -> x.isEqual(playedDomino));

        Domino playedCorner = null;

        for(Domino corner : gameLine.getCorners())
            if(corner.canConnect(playedDomino)) {
                playedCorner = corner;
                break;
            }

        gameLine.insertDomino(playedDomino,playedCorner);
    }
}
