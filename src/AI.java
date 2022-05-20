import java.util.*;

public class AI extends NPC{

   public AI(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
      super(gameLine, PlayerName, dominoes, board);
   }



   @Override
   public void play(){

      if(isFirst()){
 //        System.out.println("First Player");
         gameLine.firstPlay(getDoubleSix(),board.getColumns()/2, board.getLines()/2);
         return;
      }
   //    printPlayablePieces();

      //Store Playable Dominoes in a list
      LinkedList<Pair> playableDominoes = new LinkedList<>();
      for(Domino domino : dominoes)
         for(Domino corner : gameLine.getCorners())
            if(gameLine.canPlay(domino,corner))
               playableDominoes.add(new Pair(corner.getUnconnected(),domino));


      var playedDominoes = gameLine.getPlayedDominoes();

      //all dominoes
      LinkedList<Domino> otherPlayerDominoes = new LinkedList<>();
      for (int i = 0; i < 7; i++)
         for (int j = i; j < 7; j++) {
            var domino = new Domino(i,j);
            if(!playedDominoes.contains(domino) || !dominoes.contains(domino))
               otherPlayerDominoes.add(domino);
         }

      //playableDominoes.sort(Comparator.comparingInt(x -> x.domino.getValue()));
   //  Play domino with the highest value
      playableDominoes.sort((x,y) -> {
         int xConnected = -1;
         int yConnected = -1;
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
         if(result != 0 ) return result;
         result =   x.domino.getValue() - y.domino.getValue() ;
         if(Math.abs(result) == 0) return result;
         xConnected = 0; yConnected = 0;
         for(Domino domino : dominoes) {
            if(domino.canConnect(xDummy))
               xConnected++;
            if(domino.canConnect(yDummy))
               yConnected++;
            }
            result = xConnected - yConnected;
         return result;
      });
      Domino playedDomino = playableDominoes.remove().domino;
	   dominoes.remove(playedDomino);

	   //System.out.println("Played Domino:");
      //playedDomino.print();
      Domino playedCorner = null;

      for(Domino corner : gameLine.getCorners())
         if(gameLine.canPlay(playedDomino,corner)) {
            playedCorner = corner;
            break;
         }
      //System.out.println(", corner played:"+playedCorner);// (debugging).
      //ironed
      gameLine.insertDomino(playedDomino,playedCorner);
   }

   static class Pair{
      int x;
      Domino domino;
      Pair(int x,Domino domino){
         this.x = x; this.domino = domino;
      }
   }
}
