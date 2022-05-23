
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class NPC extends Player{
   public NPC(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
      super(gameLine, PlayerName, dominoes, board);
   }


   //Inserts Double six into the middle of the board
   public void firstPlay(){
      gameLine.firstPlay(getDoubleSix(),board.getColumns()/2, board.getLines()/2);
   }

   public void play() {

     // printPlayablePieces();

      //Store Playable Dominos in a list
      LinkedList<Domino> playableDominoes = new LinkedList<>();
      for(Domino domino : dominoes)
         for(Domino corner : gameLine.getCorners())
            if(gameLine.canPlay(domino,corner))
               playableDominoes.add(domino);

      //Play Domino with Highest Value
      Collections.sort(playableDominoes, Comparator.comparingInt(Domino::getValue));
      Domino playedDomino = playableDominoes.removeLast();
      dominoes.remove(playedDomino);

      //System.out.println("Played Domino:");
      //playedDomino.print();
      Domino playedCorner = null;

      for(Domino corner : gameLine.getCorners())
         if(gameLine.canPlay(playedDomino,corner)) {
            playedCorner = corner;
            break;
         }
      //System.out.println(", corner played:"+playedCorner); //(debugging).
      //ironed
      gameLine.insertDomino(playedDomino,playedCorner);
   }
}
