import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.*;

public class GameLine {
   private HashSet<Corner> corners = new HashSet<>(); //responsible for updating corner and inserting the dominoes.
   private HashSet<Domino> playedDominoes = new HashSet<>(); //responsible for maintaining the class invariant
   private GameBoard board;

   GameLine(GameBoard board){
      if(board == null) throw new IllegalArgumentException("class invariant broken: board is null");
      this.board = board;}

   public void firstPlay(Domino firstDomino,int x,int y){
      if(firstDomino.getValue() != 12) throw new IllegalArgumentException("Cannot call firstPlay with 6|6");
      firstDomino.beVertical();
      board.insertDomino(x,y,firstDomino);
      corners.add(new CornerIntersection(new Coordinate(x,y),firstDomino,null,board));
      updateCornersBlockedBy(firstDomino,x,y);
      playedDominoes.add(firstDomino);
   }

   //pre dominoToPlay.isEqual(corner) == false
   public boolean canPlay(Domino dominoToPlay,Domino corner){
      if(corners.isEmpty()) throw new IllegalArgumentException("There aren't any corner available");
      if(dominoToPlay.equals(corner) || playedDominoes.contains(dominoToPlay))
         throw new IllegalArgumentException("There can't be duplicate pieces on gameLine");
      Corner anActualCorner = getCorner(corner);
      if(anActualCorner == null) throw new IllegalArgumentException("This corner does not exist");
      return anActualCorner.canPlay(dominoToPlay);
   }

   // gameLine responsibility
   public void insertDomino(Domino dominoPlayed, Domino corner) {
      Corner anActualCorner = getCorner(corner);//gets the Corner type from Domino corner
      if(anActualCorner == null) throw new IllegalArgumentException("domino Played:"+dominoPlayed+",corner"+corner+" , The corner does not exist");
      if(!canPlay(dominoPlayed,corner)) throw new IllegalArgumentException("This move is illegal");
      shiftTheBoard(anActualCorner,corner,dominoPlayed);                                              //shift the board as necessary.
      Direction direction = anActualCorner.getAvailableDirection(dominoPlayed);                            //gets the available direction in relation to the corner where the domino will be placed on the corner.
      Coordinate coordinate = anActualCorner.getAvailableCoordinate(direction,dominoPlayed);   //gets the coordinate of the left upmost corner of the domino vertical or horizontal.
      connectWith(corner,dominoPlayed,direction);                                              //connects the domino to the corner.
      board.insertDomino(coordinate.x(),coordinate.y(),dominoPlayed);                          //inserts the domino on the board.
      generateCorner(coordinate,anActualCorner,dominoPlayed,direction);                        //generates a new corner and removes useless ones from the newly formed corner after the placement of this domino.
      updateCornersInTheEdges();
      updateCornersBlockedBy(dominoPlayed,coordinate.x(),coordinate.y());
      playedDominoes.add(dominoPlayed);
   }

   private void shiftTheBoard(Corner anActualCorner,Domino corner,Domino other){
      if(!anActualCorner.isShiftNeeded(other)) return;
      var directions = anActualCorner.getShiftDirection(other);
      var nShift = anActualCorner.getShiftNTimes(other);
      for(int i = 0; i < directions.length ; i++){
         if(directions[i] == null) continue;
         switch(directions[i]){
            case UP -> {board.shiftUp(nShift[i]); updateCornerCoordinates(directions[i],nShift[i]) ; }
            case DOWN -> {board.shiftDown(nShift[i]); updateCornerCoordinates(directions[i],nShift[i]);}
            case LEFT -> {board.shiftLeft(nShift[i]); updateCornerCoordinates(directions[i],nShift[i]); }
            case RIGHT -> {board.shiftRight(nShift[i]); updateCornerCoordinates(directions[i],nShift[i]);}
         }
      }
   }

   //responsibility of the game line
   private void generateCorner(Coordinate coordinate,Corner corner,Domino domino,Direction direction){
      if(domino.isDouble()) //generate intersection
         corners.add(new CornerIntersection(coordinate, domino, oppositeDirection(direction,domino),board)); //needs blocked direction
      else { //generate line
         var aFutureCorner = new CornerLine(coordinate,domino,direction,board) ;
         corners.add(aFutureCorner);
      }
      if(corner instanceof CornerLine) corners.remove(corner);
   }

   //updates corner accordingly to the shift on the board.
   private void updateCornerCoordinates(Direction direction,int n){
     switch(direction){
        case UP -> corners.forEach(x -> x.shiftUp(n));
        case DOWN-> corners.forEach(x -> x.shiftDown(n));
        case LEFT-> corners.forEach(x -> x.shiftLeft(n));
        case RIGHT-> corners.forEach(x -> x.shiftRight(n));
     }
   }

   //pre-condition, updateCornerCoordinates must have been executed before this function is called
   private void updateCornersInTheEdges(){
      HashSet<Domino> cornersToBeUpdated;

         cornersToBeUpdated = board.getDominoesInThisRectangle(0, 0, board.getColumns() - 1, 3);
         cornersToBeUpdated.addAll(board.getDominoesInThisRectangle(0, board.getLines() - 5, board.getColumns() - 1, board.getLines()));
         cornersToBeUpdated.addAll(board.getDominoesInThisRectangle(0,0,2,board.getLines()-1));
         cornersToBeUpdated.addAll(board.getDominoesInThisRectangle(board.getColumns()-4,0,board.getColumns()-1, board.getLines()));

      for(Domino maybeCorner : cornersToBeUpdated){
         Corner anActualCorner = getCorner(maybeCorner);
         if(anActualCorner != null){
            anActualCorner.updateDirections();
            if(!anActualCorner.isCorner()) corners.remove(anActualCorner);
         }
      }


   }

   private void updateCornersBlockedBy(Domino other, int x,int y){
      Iterable<Domino> cornersToBeUpdated;

      if(other.isVertical()) //a bit generous, because if there isn't any problem then the corner won't be removed because of the check
         cornersToBeUpdated = board.getDominoesInThisRectangle(x-4,y-6,x+6,y+4);
      else
         cornersToBeUpdated = board.getDominoesInThisRectangle(x-4,y-5,x+4,y+5);

      for(Domino maybeCorner : cornersToBeUpdated){
         Corner corner = getCorner(maybeCorner);
         if(corner != null){
            corner.updateDirections();
            if(!corner.isCorner()) corners.remove(corner);
         }
      }
   }

   //gameLine responsibility, future removal
   public void connectWith(Domino corner,Domino other,Direction direction){
      if(other.isDouble()){
         if(direction == Direction.UP || direction == Direction.DOWN) other.beHorizontal();
         else if(direction == Direction.RIGHT || direction == Direction.LEFT)  other.beVertical();
         else throw new IllegalArgumentException("A double can't be placed on a blocked line");
      }
      else
      {
         switch(direction) {
            case DOWN -> { other.beVertical();   connectToX(corner,other); }
            case UP ->   { other.beVertical();   connectToY(corner,other); }
            case RIGHT ->{ other.beHorizontal(); connectToX(corner,other); }
            case LEFT -> { other.beHorizontal(); connectToY(corner,other); }
            default -> {// I LISTEN TO THE VOICES AND THE VOICES LISTEN TO ME
               if(corner.isVertical()) {
                  other.beHorizontal();
                  if(direction == Direction.UpLEFT || direction == Direction.DownLEFT) connectToY(corner,other);
                  else connectToX(corner,other);
               } else {
                  other.beVertical();
                  if(direction == Direction.UpLEFT || direction == Direction.UpRIGHT) connectToY(corner,other);
                  else connectToX(corner,other);
               }
            }
         }
      }
   }

   //gameLine responsibility, future removal
   private void connectToX(Domino corner,Domino other){  other.connectToX(); if(other.getX() != corner.getUnconnected()) other.flip();}

   //gameLine responsibility, future removal
   private void connectToY(Domino corner,Domino other){  other.connectToY(); if(other.getY() != corner.getUnconnected()) other.flip();}

   //game line responsibility
   public Iterable<Domino> getCorners() {return corners.stream().map(x -> x.domino).toList();}

   //responsibility of game line
   private Corner getCorner(Domino domino){
      for(Corner corner: corners)
         if(corner.isEqual(domino))
            return corner;
      return null;
   }

   private Direction oppositeDirection(Direction direction,Domino domino){
      switch(direction){
         case LEFT : return Direction.RIGHT;
         case RIGHT: return Direction.LEFT;
         case UP   : return Direction.DOWN;
         case DOWN : return Direction.UP;
      }
      throw new IllegalArgumentException("direction cannot be null");
   }
}
