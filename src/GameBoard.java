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

      if(x < 0 || y < 0 || x > nLines || y > nColumns)
         throw new IllegalArgumentException("Must place on the board");

      var firstCoordinate = new Coordinate(x, y);
      board.put(firstCoordinate, firstDomino);
      board.put(new Coordinate(x, y), firstDomino);
      board.put(new Coordinate(x, y), firstDomino);

   //   corners.add(new Corner(firstCoordinate, firstDomino, null));
   }

   public void insertDomino(Domino dominoPlayed, Domino corner) {
      //var coordinate = corners.get();
      if(!corner.isDouble()) {
         if(corner.isVertical()) {
            if(true) ;
         }
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
            result = x - other.x;
         return result;
      }
   }


   private abstract class Corner {
      protected Coordinate coordinate;
      protected Domino domino;

      abstract public boolean isCorner();
      abstract public void blockedDirection(Direction direction);
      abstract public Coordinate getAvailableCoordinate();

      public boolean isEqual(Domino domino) {
         return domino.isEqual(domino);
      } //must be a reference to the same object
   }

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
            if(!x)
               return false;
         return true;
      }

      public Coordinate getAvailableCoordinate() {
         int N = unavailableDirections.length;
         for(int i = 0; i < N; i++) {
            if(!unavailableDirections[i])
               if(domino.isVertical())
                  return verticalAvailableCoordinate(i);
               else
                  return horizontalAvailableCoordinate(i);
         }


         return null;
      }

      private Coordinate verticalAvailableCoordinate(int x){
         switch(x){
            case 0:return new Coordinate(coordinate.x -4,coordinate.y-1);
            case 1:return new Coordinate(coordinate.x,coordinate.y +4);
            case 2:return new Coordinate(coordinate.x +3,coordinate.y -1);
            case 3:return new Coordinate(coordinate.x,coordinate.y -5);
         }
         throw new IllegalArgumentException("is the boolean array higher than 4?");
      }

      private Coordinate horizontalAvailableCoordinate(int x){
         switch(x){
            case 0:return new Coordinate(coordinate.x-3,coordinate.y);
            case 1:return new Coordinate(coordinate.x+1,coordinate.y+4);
            case 2:return new Coordinate(coordinate.x+4,coordinate.y);
            case 3:return new Coordinate(coordinate.x+1,coordinate.y-2);
         }
         throw new IllegalArgumentException("is the boolean array higher than 4?");
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
         throw new IllegalArgumentException("must be a integer between 0 and 3");
      }
   }

   private class Line extends Corner{
      private Direction nextDirection;

      Line(Coordinate coordinate,Domino domino,Direction blockedDirection){
         super.coordinate = coordinate;
         super.domino = domino;
         blockedDirection(blockedDirection);
      }

      public void blockedDirection(Direction direction) { if(nextDirection == direction)  nextDirection = null; }

      public boolean isCorner() { return nextDirection != null ;}

      public Coordinate getAvailableCoordinate(){
         return null;
      }
   }

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

