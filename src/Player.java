import java.util.*;

/**
 * @inv gameLine != null
 * @inv playerName != null
 * @inv dominoes.length must start at 7
 * @inv 0 <= dominoes.length && dominoes.length <= 7
 * @inv board != null
 */
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

    /** returns the player's name.
     * @pre true
     * @post state = oldState.
     * @return player name.
     */
    public String getName(){ return playerName;}
//
    /** returns true if the player is first e.i It has the domino 6|6, otherwise it returns false.
     * @pre true
     * @post state = oldState
     * @return true if the player is first e.i It has the domino 6|6, otherwise it returns false.
     */
    public boolean isFirst(){
        for(Domino domino : dominoes)
            if(domino.isStarter())
                return true;
        return false;
    }

    /** Returns true if the player can play, false otherwise.
     * @pre true
     * @post state = oldState
     * @return true if the player can play, false otherwise.
     */
    public boolean canPlay(){
        if(gameLine == null) return true;
        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(gameLine.canPlay(domino,corner))
                    return true;
        return false;
    }

    //to be put just on the human on the future.
    protected void printPieces(){
        for(Domino domino : dominoes)
            domino.print();
    }

    //to be put just on the human on the future.
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

    /** returns the domino 6|6 and removes it from the player.
     * @pre isFirst == true
     * @post dominoes = dominoes without the domino 6|6
     * @return domino 6|6
     */
    public Domino getDoubleSix(){ for(Domino domino : dominoes ) if(domino.isStarter()) {
        dominoes.remove(domino);
        return domino;} return null;}

    /** Makes the player play a domino on the board, removing it from its poll of dominoes.
     * @pre canPlay() == true
     * @post a domino is removed from dominoes.
     */
    public abstract void play();

    /** It returns the total amount of points the player has currently.
     * @pre true
     * @post state = oldState
     * @return the total amount of points the player has currently.
     */
    public int getPoints(){ return dominoes.stream().mapToInt(Domino::getValue).sum();}

    /** It returns true if the player is the winner at the moment, false otherwise.
     * @pre true
     * @post state = oldState
     * @return true if the player is the winner at the moment, false otherwise.
     */
    public boolean isWinner(){return dominoes.size() == 0;}

    /** Makes the first player play their double six on the board, removing it from its poll of dominoes.
     * @pre canPlay() == true && isFirst() == true
     * @post the double six is removed from dominoes.
     */
    public abstract void firstPlay();
}
