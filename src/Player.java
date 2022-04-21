import java.util.*;

public abstract class Player {
    private GameBoard board;
    private String playerName;
    private LinkedList<Domino> dominoes;

    public Player(GameBoard board,String PlayerName,List<Domino> dominoes){
        this.dominoes = new LinkedList<>(dominoes);
        this.board = board;
        this.playerName = PlayerName;
    }

    public String getName(){ return playerName;};
    public boolean isFirst(){
        for(Domino domino : dominoes)
            if(domino.getValue() == 12)
                return true;
        return false;
    }

    public boolean canPlay(){return false;};
    public abstract void play();
    public int getPoints(){ return dominoes.stream().mapToInt(Domino::getValue).sum(); }
    public void printPieces(){};
    public boolean isWinner(){return getNumDominoes() == 0;}
    private int getNumDominoes(){return dominoes.size();}
}
