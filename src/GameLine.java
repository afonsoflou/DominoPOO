import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.*;

/**
 * @inv board != null
 * @inv It cannot be played the same domino twice.
 */
public class GameLine {
   private HashSet<Corner> corners = new HashSet<>(); //responsible for updating corner and inserting the dominoes.
   private HashSet<Domino> playedDominoes = new HashSet<>(); //responsible for maintaining the class invariant.
   private GameBoard board;

   GameLine(GameBoard board){
      if(board == null) throw new IllegalArgumentException("class invariant broken: board is null");
      this.board = board;}

   /**It inserts the first domino 6|6 in the board.
    * @param firstDomino
    * @param x
    * @param y
    * @pre firstDomino.getValue == 12 && The coordinates x,y must be inside the board.
    * @post The domino must have been inserted on the board and must be a corner that the player can access.
    */
   public void firstPlay(Domino firstDomino,int x,int y){
      if(firstDomino.getValue() != 12) throw new IllegalArgumentException("Cannot call firstPlay with 6|6");
      firstDomino.beVertical();
      board.insertDomino(x,y,firstDomino);
      corners.add(new CornerIntersection(new Coordinate(x,y),firstDomino,null,board));
      updateCorners();
      playedDominoes.add(firstDomino);
   }

   /** It returns true if the domino can be played on corner,false otherwise.
    * @param dominoToPlay
    * @param corner
    * @pre dominoToPlay must have not been played before on gameLine and corner must be a valid corner on the gameLine.
    * @post  state = oldState
    * @return returns true if the domino can be played on corner,false otherwise.
    */
   public boolean canPlay(Domino dominoToPlay,Domino corner){
      if(corners.isEmpty()) throw new IllegalArgumentException("There aren't any corner available");
      if(dominoToPlay.equals(corner) || playedDominoes.contains(dominoToPlay))
         throw new IllegalArgumentException("There can't be duplicate pieces on gameLine");
      Corner anActualCorner = getCorner(corner);
      if(anActualCorner == null) throw new IllegalArgumentException("This corner does not exist");
      return anActualCorner.canPlay(dominoToPlay);
   }

   /** It inserts the domino on the gameBoard and updates the corner accordingly.
    * @param dominoPlayed
    * @param corner
    * @pre canPlay(dominoPlayed,corner) == true && getCorner(corner) != null && playedDominoes.contains(dominoToPlay) == false;
    * @post it must create a new corner and remove the corner if needed, and update his corners accordingly.
    */
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
      updateCorners();                                                                         //It updates the corners to take in account the new domino played.
      playedDominoes.add(dominoPlayed);                                                        //Adds the domino to the list of played dominoes.
   }

   //shifts the board to make space for other to be played in accord with the specification from corner.
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

   //it updates all gameLines corners.
   private void updateCorners(){
      for(Corner corner: corners.toArray(Corner[]::new)){
         corner.updateDirections();
         if(!corner.isCorner()) corners.remove(corner);
      }
   }

   //it creates a new corner after the domino has been played, and removes the corner to which it connects if it is a cornerLine
   private void generateCorner(Coordinate coordinate,Corner corner,Domino domino,Direction direction){
      if(domino.isDouble()) //generate intersection
         corners.add(new CornerIntersection(coordinate, domino, oppositeDirection(direction,domino),board)); //needs blocked direction
      else { //generate line
         var aFutureCorner = new CornerLine(coordinate,domino,direction,board) ;
         corners.add(aFutureCorner);
      }
      if(corner instanceof CornerLine) corners.remove(corner);
      else if(corner.getAvailableDirection(null) == Direction.DOWN) corners.remove(corner);
   }

   //updates corner accordingly to the shift on the board.
   //because all the coordinates on the board will be changed therefore it is needed to update the
   //corner coordinates as well as the gameBoard and the corners aren't connected.
   private void updateCornerCoordinates(Direction direction,int n){
     switch(direction){
        case UP -> corners.forEach(x -> x.shiftUp(n));
        case DOWN-> corners.forEach(x -> x.shiftDown(n));
        case LEFT-> corners.forEach(x -> x.shiftLeft(n));
        case RIGHT-> corners.forEach(x -> x.shiftRight(n));
     }
   }

   //it connects the domino played with corner, attention this does change the state of the domino itself
   //so if the domino that needs to connect becomes vertical to connect it will do so.
   private void connectWith(Domino corner,Domino other,Direction direction){
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

   /**
    * @pre true
    * @post true
    * @return an hashSet with all the dominoes previously played.
    */
   @SuppressWarnings("unchecked cast")
   public HashSet<Domino> getPlayedDominoes(){
      return (HashSet<Domino>) playedDominoes.clone();
   }

   //it connects other to the x side of the domino.
   private void connectToX(Domino corner,Domino other){  other.connectToX(); if(other.getX() != corner.getUnconnected()) other.flip();}

   //it connects other to the y side of the domino.
   private void connectToY(Domino corner,Domino other){  other.connectToY(); if(other.getY() != corner.getUnconnected()) other.flip();}

   /** It returns the corner available to be played.
    * @pre true
    * @post true
    * @return an iterable with the corners.
    */
   public Iterable<Domino> getCorners() {return corners.stream().map(x -> x.domino).toList();}

   //responsibility of game line
   private Corner getCorner(Domino domino){
      for(Corner corner: corners)
         if(corner.isEqual(domino))
            return corner;
      return null;
   }

   //it returns the opposite direction.
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
