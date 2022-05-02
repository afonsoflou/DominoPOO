import java.util.*;

public abstract class Player {
    protected GameLine gameLine;
    private final String playerName;
    protected LinkedList<Domino> dominoes;
    protected GameBoard board;

    public Player(GameLine gameLine,String PlayerName,List<Domino> dominoes, GameBoard board){
        if(gameLine == null || PlayerName == null || dominoes == null || board == null)
            throw new IllegalArgumentException("Player cannot have null instances");
        if(dominoes.size() != 7)
            throw new IllegalArgumentException("Each player must start with 7 dominoes");

        this.dominoes = new LinkedList<>(dominoes);
        this.gameLine = gameLine;
        this.playerName = PlayerName;
        this.board = board;
    }


    public String getName(){ return playerName;}

    public boolean isFirst(){
        for(Domino domino : dominoes)
            if(domino.isStarter())
                return true;
        return false;
    }

    public boolean canPlay(){
        if(gameLine == null) return true;
        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(gameLine.canPlay(domino,corner))
                    return true;
        return false;
    }


    protected void printPieces(){
        for(Domino domino : dominoes)
            domino.print();
    }

    protected void printPlayablePieces(){
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

    protected void removeDomino(Domino domino){dominoes.removeIf(x -> x.equals(domino));}
    public Domino getDoubleSix(){ for(Domino domino : dominoes ) if(domino.isStarter()) {removeDomino(domino); return domino;} return null;}
    public abstract void play();
    public int getPoints(){ return dominoes.stream().mapToInt(Domino::getValue).sum();}
    public boolean isWinner(){return getNumDominoes() == 0;}
    private int getNumDominoes(){return dominoes.size();}


}
