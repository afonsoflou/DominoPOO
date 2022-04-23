import java.util.*;

public class GameBoard {
   final private int nColumns;
   final private int nLines;
   private RedBlackTree<Coordinate, Domino> board;  //responsible for printing the board and placing the dominoes on the correct place.
   private LinkedList<Corner> corners; //responsible for updating corner and inserting the dominoes.

   public static void main(String[] args){
      var domino66 = new Domino(6,6);
      var domino61 = new Domino(6,1);
      var domino62 = new Domino(6,2);
      var domino63 = new Domino(6,3);
      var domino64 = new Domino(6,4);
      var domino65 = new Domino(6,5);
      var domino55 = new Domino(5,5);
      var domino22 = new Domino(4,4);
      var domino33 = new Domino(3,3);
      var domino44 = new Domino(2,2);
      var domino11 = new Domino(1,1);

      var game = new GameBoard(80,11,domino66,16,7);
      game.print();
      var corner = game.getCorner(new Domino(6,6));
      game.insertDomino(domino61,domino66);
      game.insertDomino(domino11,domino61); //FUCK
      game.print();
   }

   public GameBoard(int nColumns, int nLines, Domino firstDomino, int x, int y) {
      this.nColumns = nColumns;
      this.nLines = nLines;
      board = new RedBlackTree<Coordinate, Domino>();
      corners = new LinkedList<Corner>();

      if(x < 0 || y < 2 || x > nColumns || y > nLines)
         throw new IllegalArgumentException("Must place on the board");
      if(!firstDomino.isStarter())
         throw new IllegalArgumentException("The first domino must be 6|6");

      firstDomino.beVertical();
      var firstCoordinate = new Coordinate(x-1, y-1);
      board.put(firstCoordinate, firstDomino);
      board.put(new Coordinate(x-1, y-2), firstDomino);
      board.put(new Coordinate(x-1, y-3), firstDomino);

      corners.add(new Intersection(firstCoordinate, firstDomino, null));
   }

   public void insertDomino(Domino dominoPlayed, Domino corner) {
      Corner anActualCorner = getCorner(corner);
      Direction direction = anActualCorner.getAvailableDirection();
      Coordinate coordinate = anActualCorner.getAvailableCoordinate(direction,dominoPlayed);
      connectDomino(dominoPlayed,corner,direction);
      insertDominoOnBoard(coordinate,dominoPlayed); //inserts domino
      generateCorner(coordinate,anActualCorner,dominoPlayed,direction);
   }

   private void connectDomino(Domino domino,Domino corner,Direction direction){
      if(domino.isDouble()){
         if(direction == Direction.UP || direction == Direction.DOWN) domino.beHorizontal();
         else if(direction == Direction.RIGHT || direction == Direction.LEFT)  domino.beVertical();
         else throw new IllegalArgumentException("A double can't be placed on a blocked line");
      }
      else
      {
         switch(direction) {
            case DOWN -> { domino.beVertical();   connectToX(domino, corner); }
            case UP ->   { domino.beVertical();   connectToY(domino, corner); }
            case RIGHT ->{ domino.beHorizontal(); connectToX(domino, corner); }
            case LEFT -> { domino.beHorizontal(); connectToY(domino, corner); }
            default -> {// I LISTEN TO THE VOICES AND THE VOICES LISTEN TO ME
               if(corner.isVertical()) {
                  domino.beHorizontal();
                  if(direction == Direction.UpLEFT || direction == Direction.DownLEFT) connectToY(domino, corner);
                  else connectToX(domino, corner);
               } else {
                  domino.beVertical();
                  if(direction == Direction.UpLEFT || direction == Direction.UpRIGHT) connectToY(domino, corner);
                  else connectToX(domino, corner);
               }
            }
         }
      }
   }

   private void connectToX(Domino domino,Domino corner){  domino.connectToX(); if(domino.getX() != corner.getUnconnected()) domino.flip();}
   private void connectToY(Domino domino,Domino corner){  domino.connectToY(); if(domino.getY() != corner.getUnconnected()) domino.flip();}

   private void generateCorner(Coordinate coordinate,Corner corner,Domino domino,Direction direction){
      if(corner instanceof Line) {
         corners.remove(corner);
         if(domino.isDouble()) {
            var aFutureCorner = new Intersection(coordinate, domino, oppositeDirection(direction,domino));
            if(aFutureCorner.isCorner()) corners.add(aFutureCorner);
         }
         else{
            var aFutureCorner = new Line(coordinate,domino,oppositeDirection(direction,domino));
            if(aFutureCorner.isCorner()) corners.add(aFutureCorner);
         }

      }
      else if(!corner.isCorner())
         corners.remove(corner);

   }

   private Corner getCorner(Domino domino){
      for(Corner corner: corners)
         if(corner.isEqual(domino))
            return corner;
      throw new IllegalArgumentException("Corner does not exist");
   }

   //given the left up corner, the direction and the domino it inserts the domino into the board
   private void insertDominoOnBoard(Coordinate coordinate,Domino domino) {
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
      System.out.println("#".repeat(nColumns+2));
      HashMap<Domino,Integer> segments = new HashMap<>();
      int prev = 0;// contains the amount of white space previously used
      var coordinates = this.board.getKeysInorder().iterator();
      var dominoes = board.getValuesInorder().iterator();
      Coordinate current = coordinates.next();

      for(int i = nLines -1; i >= 0 ; i--){
         if(current != null && current.y < i) System.out.println("#" + " ".repeat(nColumns) + "#");
         else{
            prev = 0;
            System.out.print("#");
            while(current != null && current.y == i) {
               System.out.print(" ".repeat(current.x - prev));
               var domino = dominoes.next();
               if(!segments.containsKey(domino)) segments.put(domino,0);
               else segments.put(domino,segments.get(domino)+1);
               printDominoSegment(segments.get(domino),domino);
               prev = current.x + 1; // +1 from the printDominoSegment
               if(coordinates.hasNext()) current = coordinates.next();
               else current = null;
            }
            System.out.println(" ".repeat(nColumns - prev) + "#");
         }
      }

      System.out.println("#".repeat(nColumns+2));
   }

   public void printDominoSegment(int segment,Domino Domino){
      if(segment == 0) System.out.print(Domino.getX());
      else if(segment == 1 && Domino.isDouble()) System.out.print("-");
      else System.out.print(Domino.getY());
   }

   //It has the coordinates to a corresponding segment of the domino.
   private record Coordinate(int x, int y) implements Comparable<Coordinate>{
      public int compareTo(Coordinate other) {
         int result = other.y -y;
         if(result == 0)
            result = x - other.x;
         return result;
      }
   }

   private Direction oppositeDirection(Direction direction,Domino domino){
      switch(direction){
         case LEFT : return Direction.RIGHT;
         case RIGHT: return Direction.LEFT;
         case UP   : return Direction.DOWN;
         case DOWN : return Direction.UP;
      }
      
      if(domino.isVertical())
         switch(direction) {
            case UpRIGHT: 
            case UpLEFT : return Direction.DOWN;
            case DownLEFT: 
            case DownRIGHT: return Direction.UP;
         }
      else
         switch(direction) {
            case UpRIGHT : 
            case DownRIGHT: return Direction.LEFT;
            case UpLEFT:
            case DownLEFT: return Direction.RIGHT;
         }
      throw new IllegalArgumentException("direction cannot be null");
   }


   private Direction convIntToDirection(int x) {
      switch(x) {
         case 0: return Direction.LEFT;
         case 1: return Direction.UP;
         case 2: return Direction.RIGHT;
         case 3: return Direction.DOWN;
      }
      throw new IllegalArgumentException("must be a integer between 0 and 3");
   }


   static private abstract class Corner {
      protected Coordinate coordinate;
      protected Domino domino;

      abstract public boolean isCorner();
      abstract public void blockedDirection(Direction direction);
      abstract public Coordinate getAvailableCoordinate(Direction direction,Domino other);
      abstract public Direction getAvailableDirection();
      public boolean isEqual(Domino domino) {
         return domino.isEqual(domino);
      } //must be a reference to the same object
   }

   //aka double corners ///////////////////////// INTERSECTION /////////////////////////////
   private class Intersection extends Corner{
      private final boolean[] unavailableDirections = new boolean[4];

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

      public Direction getAvailableDirection() {
         int N = unavailableDirections.length;
         for(int i = 0; i < N; i++) 
            if(!unavailableDirections[i])
               return convIntToDirection(i);
         throw new IllegalCallerException("The corner should not exist there aren't any available directions");
      }

      public Coordinate getAvailableCoordinate(Direction direction,Domino other) { 
         return (domino.isVertical()) ? verticalAvailableCoordinate(direction.ordinal()) : horizontalAvailableCoordinate(direction.ordinal());
      }

      private Coordinate verticalAvailableCoordinate(int x){
         switch(x){
            case 0: blockedDirection(Direction.LEFT); return new Coordinate(coordinate.x -4,coordinate.y-1);
            case 1: blockedDirection(Direction.UP);   return new Coordinate(coordinate.x,coordinate.y +4);
            case 2: blockedDirection(Direction.RIGHT);return new Coordinate(coordinate.x +3,coordinate.y -1);
            case 3: blockedDirection(Direction.DOWN); return new Coordinate(coordinate.x,coordinate.y -5);
         }
         throw new IllegalArgumentException("is the boolean array higher than 4?");
      }

      private Coordinate horizontalAvailableCoordinate(int x){
         switch(x){
            case 0:blockedDirection(Direction.LEFT);  return new Coordinate(coordinate.x-3,coordinate.y);
            case 1:blockedDirection(Direction.UP);    return new Coordinate(coordinate.x+1,coordinate.y+4);
            case 2:blockedDirection(Direction.RIGHT); return new Coordinate(coordinate.x+4,coordinate.y);
            case 3:blockedDirection(Direction.DOWN);  return new Coordinate(coordinate.x+1,coordinate.y-2);
         }
         throw new IllegalArgumentException("is the boolean array higher than 4?");
      }

   } ////////////////////////////// END OF INTERSECTION //////////////////////////////////////////

   //aka non-double domino //////////////// LINE //////////////////////
   static private class Line extends Corner{
      private Direction nextDirection;

      Line(Coordinate coordinate,Domino domino,Direction blockedDirection){
         super.coordinate = coordinate;
         super.domino = domino;
         blockedDirection(blockedDirection);
      }

      public void blockedDirection(Direction direction) { if(nextDirection == direction)  nextDirection = null; }

      public boolean isCorner() { return nextDirection != null ;}
      
      public Direction getAvailableDirection(){return nextDirection;}

      public Coordinate getAvailableCoordinate(Direction direction,Domino other){
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
            else if(nextDirection == Direction.RIGHT)      return new Coordinate(coordinate.x+3,coordinate.y);
            else if(nextDirection == Direction.UpLEFT)    return new Coordinate(coordinate.x ,coordinate.y +4);
            else if(nextDirection == Direction.UpRIGHT)   return new Coordinate(coordinate.x +1,coordinate.y+4);
            else if(nextDirection == Direction.DownLEFT)  return new Coordinate(coordinate.x ,coordinate.y-2);
            else if(nextDirection == Direction.DownRIGHT) return new Coordinate(coordinate.x +1,coordinate.y-2);
            else throw new IllegalArgumentException("This Corner should not exist");
         }
      }

   } /////////////////// END OF LINE ///////////////////////////////////////////////////

}

