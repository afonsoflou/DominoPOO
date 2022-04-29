import java.util.*;

public class GameLine {
   private HashSet<Corner> corners = new HashSet<>(); //responsible for updating corner and inserting the dominoes.
   private GameBoard board;

   GameLine(GameBoard board){ this.board = board;}

   public void firstPlay(Domino firstDomino,int x,int y){
      if(firstDomino.getValue() != 12) throw new IllegalArgumentException("Cannot call firstPlay with 6|6");
      firstDomino.beVertical();
      board.insertDomino(x,y,firstDomino);
      corners.add(new CornerIntersection(new Coordinate(x,y),firstDomino,null,board));
      updateCornersBlockedBy(firstDomino,x,y);
   }

   public boolean canPlay(Domino dominoToPlay,Domino corner){
      Corner anActualCorner = getCorner(corner);
      if(anActualCorner == null) return false;
      return anActualCorner.canPlay(dominoToPlay);
   }

   // gameLine responsibility
   public void insertDomino(Domino dominoPlayed, Domino corner) {
      Corner anActualCorner = getCorner(corner);//gets the Corner type from Domino corner
      if(anActualCorner == null) throw new IllegalArgumentException("domino Played:"+dominoPlayed+",corner"+corner+" , The corner does not exist");
      if(!canPlay(dominoPlayed,corner)) throw new IllegalArgumentException("This move is illegal");
      Direction direction = anActualCorner.getAvailableDirection();                            //gets the available direction in relation to the corner where the domino will be placed on the corner.
      Coordinate coordinate = anActualCorner.getAvailableCoordinate(direction,dominoPlayed);   //gets the coordinate of the left upmost corner of the domino vertical or horizontal.
      anActualCorner.connectWith(dominoPlayed,direction);                                       //connects the domino to the corner.
      board.insertDomino(coordinate.x(),coordinate.y(),dominoPlayed);                          //inserts the domino on the board.
      generateCorner(coordinate,anActualCorner,dominoPlayed,direction);                        //generates a new corner and removes useless ones from the newly formed corner after the placement of this domino.
      updateCornersBlockedBy(dominoPlayed,coordinate.x(),coordinate.y());
   }

   //responsibility of the game line
   private void generateCorner(Coordinate coordinate,Corner corner,Domino domino,Direction direction){
      if(domino.isDouble()) //generate intersection
         corners.add(new CornerIntersection(coordinate, domino, oppositeDirection(direction,domino),board)); //needs blocked direction
      else { //generate line
         var aFutureCorner = (domino.isVertical()) ? new VerticalCornerLine(coordinate,domino,direction,board) :
                 new HorizontalCornerLine(coordinate,domino,direction,board);
         corners.add(aFutureCorner);
      }
      if(corner instanceof CornerLine) corners.remove(corner);
   }

   private void updateCornersBlockedBy(Domino other, int x,int y){
      Iterable<Domino> cornersToBeUpdated;

      if(other.isVertical()) //a bit generous, because if there isn't any problem then the corner won't be removed because of the check
         cornersToBeUpdated = board.getDominoesInThisRectangle(x-6,y-4,x+6,y+4);
      else
         cornersToBeUpdated = board.getDominoesInThisRectangle(x-4,y-4,x+4,y+4);

      for(Domino maybeCorner : cornersToBeUpdated){
         Corner corner = getCorner(maybeCorner);
         if(corner != null){
            corner.updateDirections();
            if(!corner.isCorner()) corners.remove(corner);
         }
      }

   }

   //game line responsibility
   public Iterable<Domino> getCorners() {
      return corners.stream().map(x -> x.domino).toList();
   }

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
