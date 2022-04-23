import java.util.List;

public class NPC extends Player{
    public NPC(GameBoard board, String PlayerName, List<Domino> dominoes) {
        super(board, PlayerName, dominoes);
    }

    public void play() {
        if(!canPlay()) return;
    }
}
