import java.util.*;

public class GameBoard {
   final private int nColumns;
   final private int nLines;
   private RedBlackTree<CoordinateOfDominoSegment, Domino> board;  //responsible for printing the board and placing the dominoes on the correct place.
   private LinkedList<Corner> corners; //responsible for updating corner and inserting the dominoes.

   public GameBoard(int nColumns, int nLines, Domino firstDomino, int x, int y) {
      this.nColumns = nColumns;
      this.nLines = nLines;
      var board = new RedBlackTree<CoordinateOfDominoSegment, Domino>();
      var corners = new LinkedList<Corner>();

      if(x < 0 || y < 0 || x > nLines || y > nColumns)
         throw new IllegalArgumentException("Must place on the board");

      var firstCoordinate = new CoordinateOfDominoSegment(x, y, 0);
      board.put(firstCoordinate, firstDomino);
      board.put(new CoordinateOfDominoSegment(x, y, 1), firstDomino);
      board.put(new CoordinateOfDominoSegment(x, y, 2), firstDomino);

      corners.add(new Corner(firstCoordinate, firstDomino, null));
   }

   public void insertDomino(Domino dominoPlayed, Domino corner) {
      //var coordinate = corners.get();
      if(!corner.isDouble()) {
         if(corner.isVertical()) {
            if(true) ;
         }
      }
   }

   private boolean isBlocked(int x, int y, Direction direction) {
      return false;
   }

   public Iterable<Domino> getCorners() {
      return corners.stream().map(x -> x.domino).toList();
   }

   public void print() {
   }


   private class Corner {
      final boolean[] availableCornerDirections = new boolean[4];
      CoordinateOfDominoSegment coordinate;
      Domino domino;
      Direction direction;

      //direction is the variable that holds the direction of the domino before the corner.
      Corner(CoordinateOfDominoSegment coordinate, Domino domino, Direction direction) {
         this.direction = direction;
         if(domino.isDouble()) {
            for(boolean dir : availableCornerDirections)
               dir = true;

            if(direction != null) // aka isn't starter
               availableCornerDirections[direction.ordinal()] = false;
         } else
            availableCornerDirections[(direction.ordinal() + 2) % 4] = true; //if isn't double then the opposite direction is available
      }

      private void updateCorner(Direction direction, CoordinateOfDominoSegment pointOfInterest) {
         // X|X   x = x
         if(domino.isVertical()) {

         } else {

         }

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

      public void blockCorner(Direction direction) {
         availableCornerDirections[direction.ordinal()] = false;
      }

      public Direction availableDirection() {
         for(int i = 0; i < 4; i++)
         if(availableCornerDirections[i])
            return convBooleanToDirection(i);
         throw new IllegalArgumentException("There aren't any available direction this corner should not exist");
   }

      public boolean isEqual(Domino domino) {
         return domino.isEqual(domino);
      } //must be a reference to the same object
   }


   //It has the coordinates to a corresponding segment of the domino. WARNING SEGMENT WILL DISAPPEAR IN A FUTURE ITERATION
   private record CoordinateOfDominoSegment(int x, int y,int segment) implements Comparable<CoordinateOfDominoSegment>{
      public int compareTo(CoordinateOfDominoSegment other) {
         int result = y - other.y;
         if(result == 0)
            result = x - other.x;
         return result;
      }
   }
}

