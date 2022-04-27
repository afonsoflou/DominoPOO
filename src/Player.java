import java.util.*;

public abstract class Player {
    protected GameLine gameLine;
    private String playerName;
    protected LinkedList<Domino> dominoes;

    public Player(GameLine gameLine,String PlayerName,List<Domino> dominoes){
        this.dominoes = new LinkedList<>(dominoes);
        this.gameLine = gameLine;
        this.playerName = PlayerName;
    }

    public void joinGame(GameLine gameLine){
        this.gameLine = gameLine;
    }

    public String getName(){ return playerName;}

    public boolean isFirst(){
        for(Domino domino : dominoes)
            if(domino.isStarter())
                return true;
        return false;
    }

    public boolean canPlay(){
        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(domino.canConnect(corner))
                    return true;
        return false;
    }

    protected void removeDomino(Domino domino){dominoes.removeIf(x -> x.isEqual(domino));}
    public Domino getDoubleSix(){ for(Domino domino : dominoes ) if(domino.isStarter()) {removeDomino(domino); return domino;} return null;}
    public abstract void play();
    public int getPoints(){ return dominoes.stream().mapToInt(Domino::getValue).sum();}
    public boolean isWinner(){return getNumDominoes() == 0;}
    private int getNumDominoes(){return dominoes.size();}


}
