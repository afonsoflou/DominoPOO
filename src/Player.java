import java.util.*;

public abstract class Player {
    private GameBoard board;
    private String playerName;
    private static LinkedList<Domino> dominoes;

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

    public boolean canPlay(){
        for(Domino domino : dominoes)
            for(Domino corner : board.getCorners())
                if(domino.isEqual(corner))
                    return true;
        return false;
    }



    public abstract void play();
    public int getPoints(){ return dominoes.stream().mapToInt(Domino::getValue).sum();}
    public boolean isWinner(){return getNumDominoes() == 0;}
    private int getNumDominoes(){return dominoes.size();}

   public static class Human extends Player{


       public Human(GameBoard board, String PlayerName, List<Domino> dominoes) {
           super(board, PlayerName, dominoes);
       }

       private void printPieces(){
           for(Domino domino : dominoes)
               domino.print();
       }

       public void play() {

       }

   }

    public static class NPC extends Player{

        public NPC(GameBoard board, String PlayerName, List<Domino> dominoes) {
            super(board, PlayerName, dominoes);
        }

        public void play() {

        }

    }

}
