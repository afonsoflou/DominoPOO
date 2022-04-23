import java.util.*;

public class GameBoard {
   final private int nColumns;
   final private int nLines;
   private RedBlackTree<Coordinate, Domino> board;  //responsible for printing the board and placing the dominoes on the correct place.
   private LinkedList<Corner> corners; //responsible for updating corner and inserting the dominoes.

   public GameBoard(int nColumns, int nLines, Domino firstDomino, int x, int y) {
      this.nColumns = nColumns;
      this.nLines = nLines;
      var board = new RedBlackTree<Coordinate, Domino>();
      var corners = new LinkedList<Corner>();

      if(x < 0 || y < 1 || x > nLines || y > nColumns)
         throw new IllegalArgumentException("Must place on the board");

      var firstCoordinate = new Coordinate(x, y);
      board.put(firstCoordinate, firstDomino);
      board.put(new Coordinate(x, y+1), firstDomino);
      board.put(new Coordinate(x, y+2), firstDomino);

      corners.add(new Intersection(firstCoordinate, firstDomino, null));
   }

   public void insertDomino(Domino dominoPlayed, Domino corner) {
      Corner corner1 = getCorner(corner);
      //insertDomino();

   }

   private Corner getCorner(Domino domino){
      for(Corner corner: corners)
         if(corner.equals(domino))
            return corner;
      throw new IllegalArgumentException("Corner does not exist");
   }

   //given the left up corner, the direction and the domino it inserts the domino into the board
   private void insertDomino(Coordinate coordinate,Direction direction,Domino domino) {
      if(domino.isVertical()) { //vertical
         board.put(coordinate, domino);
         board.put(new Coordinate(coordinate.x, coordinate.y - 1), domino);
         board.put(new Coordinate(coordinate.x, coordinate.y - 2), domino);
      } else { //horizontal
         board.put(coordinate, domino);
         board.put(new Coordinate(coordinate.x + 1, coordinate.y), domino);
         if(domino.isDouble())
            board.put(new Coordinate(coordinate.x + 2, coordinate.y), domino);
      }
   }

   public Iterable<Domino> getCorners() {
      return corners.stream().map(x -> x.domino).toList();
   }

   public void print() {
   }

   public void printDominoSegment(int segment,Domino Domino){
      if(segment == 0) System.out.println(Domino.getX());
      else if(segment == 1 && Domino.isDouble()) System.out.println("-");
      else System.out.println(Domino.getY());
   }

   //It has the coordinates to a corresponding segment of the domino.
   private record Coordinate(int x, int y) implements Comparable<Coordinate>{
      public int compareTo(Coordinate other) {
         int result = y - other.y;
         if(result == 0)
            result = other.x - x;
         return result;
      }
   }


   private abstract class Corner {
      protected Coordinate coordinate;
      protected Domino domino;

      abstract public boolean isCorner();
      abstract public void blockedDirection(Direction direction);
      abstract public Coordinate getAvailableCoordinate(Domino other);

      public boolean isEqual(Domino domino) {
         return domino.isEqual(domino);
      } //must be a reference to the same object
   }

   //aka double corners ///////////////////////// INTERSECTION /////////////////////////////
   private class Intersection extends Corner{
      private boolean[] unavailableDirections = new boolean[4];

      Intersection(Coordinate coordinate,Domino domino,Direction blockedDirection){
         super.coordinate = coordinate;
         super.domino = domino;
         if(blockedDirection != null) blockedDirection(blockedDirection);
      }

      public void blockedDirection(Direction direction) { unavailableDirections[direction.ordinal()] = true;}

      public boolean isCorner() {
         for(boolean x: unavailableDirections)
            if(!x) return false;
         return true;
      }

      public Coordinate getAvailableCoordinate(Domino other) {
         int N = unavailableDirections.length;
         for(int i = 0; i < N; i++) {
            if(!unavailableDirections[i])
               if(domino.isVertical())
                  return verticalAvailableCoordinate(i);
               else
                  return horizontalAvailableCoordinate(i);
         }
         throw new IllegalCallerException("There aren't any available coordinates this corner should not exist");
      }

      private Coordinate verticalAvailableCoordinate(int x){
         switch(x){
            case 0:return new Coordinate(coordinate.x -4,coordinate.y-1);
            case 1:return new Coordinate(coordinate.x,coordinate.y +4);
            case 2:return new Coordinate(coordinate.x +3,coordinate.y -1);
            case 3:return new Coordinate(coordinate.x,coordinate.y -5);
         }
         throw new IllegalArgumentException("Is the boolean array higher than 4?");
      }

      private Coordinate horizontalAvailableCoordinate(int x){
         switch(x){
            case 0:return new Coordinate(coordinate.x-3,coordinate.y);
            case 1:return new Coordinate(coordinate.x+1,coordinate.y+4);
            case 2:return new Coordinate(coordinate.x+4,coordinate.y);
            case 3:return new Coordinate(coordinate.x+1,coordinate.y-2);
         }
         throw new IllegalArgumentException("Is the boolean array higher than 4?");
      }

      private Direction convBooleanToDirection(int x) {
         switch(x) {
            case 0:
               return Direction.LEFT;
            case 1:
               return Direction.UP;
            case 2:
               return Direction.RIGHT;
            case 3:
               return Direction.DOWN;
         }
         throw new IllegalArgumentException("Must be a integer between 0 and 3");
      }
   } ////////////////////////////// END OF INTERSECTION //////////////////////////////////////////

   //aka non-double domino //////////////// LINE //////////////////////
   private class Line extends Corner{
      private Direction nextDirection;

      Line(Coordinate coordinate,Domino domino,Direction blockedDirection){
         super.coordinate = coordinate;
         super.domino = domino;
         blockedDirection(blockedDirection);
      }

      public void blockedDirection(Direction direction) { if(nextDirection == direction)  nextDirection = null; }

      public boolean isCorner() { return nextDirection != null ;}

      public Coordinate getAvailableCoordinate(Domino other){
         return domino.isVertical() ? getVerticalAvailableCoordinate(other) : getHorizontalAvailableCoordinate(other);
      }

      private Coordinate getVerticalAvailableCoordinate(Domino other){
         if(other.isDouble()) {
            if     (nextDirection == Direction.UP)         return new Coordinate(coordinate.x - 1, coordinate.y + 1);
            else if(nextDirection == Direction.DOWN)       return new Coordinate(coordinate.x - 1, coordinate.y - 1);
            else                                           throw new IllegalArgumentException("A horizontal double must be placed up or down");
         }
         else
         {
            if(nextDirection == Direction.UP) return new Coordinate(coordinate.x, coordinate.y + 4);
            else if(nextDirection == Direction.DOWN)      return new Coordinate(coordinate.x,coordinate.y -4);
            else if(nextDirection == Direction.UpLEFT)    return new Coordinate(coordinate.x -3,coordinate.y);
            else if(nextDirection == Direction.UpRIGHT)   return new Coordinate(coordinate.x +2,coordinate.y);
            else if(nextDirection == Direction.DownLEFT)  return new Coordinate(coordinate.x -3,coordinate.y-2);
            else if(nextDirection == Direction.DownRIGHT) return new Coordinate(coordinate.x +2,coordinate.y-2);
            else throw new IllegalArgumentException("This Corner should not exist");
            }
         }

      private Coordinate getHorizontalAvailableCoordinate(Domino other){
         if(other.isDouble()){
            if     (nextDirection == Direction.LEFT)         return new Coordinate(coordinate.x - 2, coordinate.y + 1);
            else if(nextDirection == Direction.RIGHT)        return new Coordinate(coordinate.x + 2, coordinate.y + 1);
            else                                             throw new IllegalArgumentException("A Vertical double must be placed right or left");
         }
         else
         {
            if(nextDirection == Direction.LEFT) return new Coordinate(coordinate.x-3, coordinate.y);
            else if(nextDirection == Direction.RIGHT)     return new Coordinate(coordinate.x+3,coordinate.y);
            else if(nextDirection == Direction.UpLEFT)    return new Coordinate(coordinate.x ,coordinate.y +4);
            else if(nextDirection == Direction.UpRIGHT)   return new Coordinate(coordinate.x +1,coordinate.y+4);
            else if(nextDirection == Direction.DownLEFT)  return new Coordinate(coordinate.x ,coordinate.y-2);
            else if(nextDirection == Direction.DownRIGHT) return new Coordinate(coordinate.x +1,coordinate.y-2);
            else throw new IllegalArgumentException("This Corner should not exist");
         }
      }

   } /////////////////// END OF LINE ///////////////////////////////////////////////////

}

/*      //direction is the variable that holds the direction of the domino before the corner.
      Corner(Coordinate coordinate, Domino domino, Direction direction) {
         this.direction = direction;
         if(domino.isDouble()) {
            for(boolean dir : availableCornerDirections)
               dir = true;

            if(direction != null) // aka isn't starter
               availableCornerDirections[direction.ordinal()] = false;
         } else
            availableCornerDirections[(direction.ordinal() + 2) % 4] = true; //if isn't double then the opposite direction is available
      }*


      /
 */

