import java.util.*;

public class SmartAI extends Player{

   public SmartAI(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
      super(gameLine, PlayerName, dominoes, board);
   }

   @Override
   public void play(){

      //Store Playable Dominoes in a list
      LinkedList<Pair> playableDominoes = new LinkedList<>();
      for(Domino domino : dominoes)
         for(Domino corner : gameLine.getCorners())
            if(gameLine.canPlay(domino,corner))
               playableDominoes.add(new Pair(corner.getUnconnected(),domino));

      var playedDominoes = gameLine.getPlayedDominoes(); //all dominoes
      LinkedList<Domino> otherPlayerDominoes = new LinkedList<>();
      for (int i = 0; i < 7; i++)
         for (int j = i; j < 7; j++) {
            var domino = new Domino(i,j);
            if(!playedDominoes.contains(domino) || !dominoes.contains(domino))
               otherPlayerDominoes.add(domino);
         }

      playableDominoes.sort((x,y) -> {
         int xConnected = 0;
         int yConnected = 0;
         var xDummy = new Domino(x.domino.getX(), x.domino.getY());
         var yDummy = new Domino(y.domino.getX(), y.domino.getY());

         if(!xDummy.isDouble())
            if(xDummy.getX() == x.x) xDummy.connectToX();
            else xDummy.connectToY();
         if(!yDummy.isDouble())
            if(yDummy.getX() == y.x) yDummy.connectToX();
            else yDummy.connectToY();
         for(Domino domino : otherPlayerDominoes) {
            if(domino.canConnect(xDummy))
               xConnected++;
            if(domino.canConnect(yDummy))
               yConnected++;
         }
         int result = xConnected - yConnected;
         return (result != 0 )? result : x.domino.getValue() - y.domino.getValue() ;
      });
      Domino playedDomino = playableDominoes.remove().domino;
	   dominoes.remove(playedDomino);

      Domino playedCorner = null;

      for(Domino corner : gameLine.getCorners())
         if(gameLine.canPlay(playedDomino,corner)) {
            playedCorner = corner;
            break;
         }
      gameLine.insertDomino(playedDomino,playedCorner);
   }

   @Override
   public void firstPlay() {
      gameLine.firstPlay(getDoubleSix(),board.getColumns()/2, board.getLines()/2);
   }

   static class Pair{
      int x;
      Domino domino;
      Pair(int x,Domino domino){
         this.x = x; this.domino = domino;
      }
   }
}
