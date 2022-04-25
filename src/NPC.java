
import java.util.List;

public class NPC extends Player{
    public NPC(GameLine gameLine, String PlayerName, List<Domino> dominoes) {
        super(gameLine, PlayerName, dominoes);
    }


    public void play() {
        if(!canPlay()) return;
    }
}
