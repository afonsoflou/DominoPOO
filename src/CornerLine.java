import java.util.Arrays;

public class CornerLine extends Corner {
   private Direction[] nextDirection;
   private int currentNextDirection;// holds the index of the current nextDirection
   private boolean lineIsBlocked;
   private boolean doubleIsBlocked;
   private Direction shiftDirection;//non-double shift-direction
   private final Direction[] doubleShiftDirection = new Direction[2];
   private int shiftNTimes = 0;
   private final int[] doubleShiftNTimes = new int[2];

   CornerLine(Coordinate coordinate, Domino domino, Direction direction, GameBoard board) {
      super(coordinate, domino, board);

      if(domino.isVertical())
         switch(direction) {
            case UP, UpRIGHT, UpLEFT ->       nextDirection = new Direction[]{Direction.UP, Direction.UpLEFT, Direction.UpRIGHT};
            case DOWN, DownLEFT, DownRIGHT -> nextDirection = new Direction[]{Direction.DOWN, Direction.DownLEFT, Direction.DownRIGHT};
         }
      else
         switch(direction) {
            case RIGHT, UpRIGHT, DownRIGHT -> nextDirection = new Direction[]{Direction.RIGHT, Direction.UpRIGHT, Direction.DownRIGHT};
            case LEFT, UpLEFT, DownLEFT ->    nextDirection = new Direction[]{Direction.LEFT, Direction.UpLEFT, Direction.DownLEFT};
         }
   }

   public boolean canPlay(Domino other) {
      if(other.isDouble()) {
         if(doubleIsBlocked) return false;
         return domino.canConnect(other);
      }

      if(lineIsBlocked) return false;
      return domino.canConnect(other);
   }


   public boolean isCorner() {return !doubleIsBlocked || !lineIsBlocked;}

   public void updateDirections() {
      doubleIsBlocked = isDoubleDirectionBlocked(nextDirection[0]);
      while(currentNextDirection < nextDirection.length) {
         lineIsBlocked = isLineDirectionBlocked(nextDirection[currentNextDirection]);
         if(!lineIsBlocked) break;
         currentNextDirection++;
      }
   }


   //pre direction.ordinal > 3
   private boolean isDoubleDirectionBlocked(Direction direction) {
      int x = domino.getUnconnected();
      var dummy = new Domino(x, x);
      Coordinate dummyCoordinate = getAvailableCoordinate(direction, dummy);
      var dummyCoordinateY = domino.isVertical() ? getHorizontalDoubleYCoordinate(dummyCoordinate) : getVerticalDoubleYCoordinate(dummyCoordinate);

      if(isDoubleOutsideTheBoard(dummyCoordinate,dummyCoordinateY)) return true;

      //there is the domino before the dummy that is the corner domino.If there is more than 1 then it's blocked
      return 1 != board.getNDominoesOnThisRectangle(dummyCoordinate.x()-2, dummyCoordinateY.y()-2, dummyCoordinateY.x()+2,dummyCoordinate.y()+2);
   }

   private boolean isDoubleOutsideTheBoard(Coordinate dummyCoordinate, Coordinate dummyCoordinateY){
      doubleShiftDirection[0] = Direction.DOWN;
      doubleShiftNTimes[0] = amountOfDownShift(dummyCoordinate.y());
      if(!board.canShiftDown(doubleShiftNTimes[0])) return true;

      if(doubleShiftNTimes[0] == 0){
         doubleShiftDirection[0] = Direction.UP;
         doubleShiftNTimes[0] = amountOfUpShift(dummyCoordinateY.y());
         if(!board.canShiftUp(doubleShiftNTimes[0])) return true;
      }

      doubleShiftDirection[1] = Direction.LEFT;
      doubleShiftNTimes[1] = amountOfLeftShift(dummyCoordinateY.x());
      if(!board.canShiftLeft(doubleShiftNTimes[1])) return true;

      if(doubleShiftNTimes[1] == 0){
         doubleShiftDirection[1] = Direction.RIGHT;
         doubleShiftNTimes[1] = amountOfRightShift(dummyCoordinate.x());
         if(!board.canShiftRight(doubleShiftNTimes[1])) return true;
      }

      return false;
   }


   private boolean isLineDirectionBlocked(Direction direction) {
      var dummy = new Domino(domino.getX(), domino.getY());
      var dummyCoordinate = getAvailableCoordinate(direction, dummy);
      Coordinate dummyCoordinateY = (domino.isVertical()) ?
              (currentNextDirection == 0) ? getVerticalDoubleYCoordinate(dummyCoordinate) : getHorizontalDoubleYCoordinate(dummyCoordinate) :
              (currentNextDirection == 0) ? getHorizontalYCoordinate(dummyCoordinate)     : getVerticalYCoordinate(dummyCoordinate);

      if(isLineOutsideTheBoard(dummyCoordinate,dummyCoordinateY,direction)) return true;

      //there is the domino before the dummy that is the corner domino.If there is more than 1 then it's blocked
      return 1 != board.getNDominoesOnThisRectangle(dummyCoordinate.x()-2, dummyCoordinateY.y()-2, dummyCoordinateY.x()+2,dummyCoordinate.y()+2);
   }

   private boolean isLineOutsideTheBoard(Coordinate dummyCoordinate,Coordinate dummyCoordinateY,Direction direction){
      //vertical direction
      if(dummyCoordinate.y() != dummyCoordinateY.y())
            switch(direction) {
               case UP, UpRIGHT, UpLEFT ->      {
                 shiftDirection = Direction.DOWN;
                 shiftNTimes = amountOfDownShift(dummyCoordinate.y());
                 return !board.canShiftDown(shiftNTimes);
               }
               case DOWN, DownLEFT, DownRIGHT -> {
                  shiftDirection = Direction.UP;
                  shiftNTimes = amountOfUpShift(dummyCoordinateY.y());
                  return !board.canShiftUp(shiftNTimes);
               }
            }
         else
            switch(direction) {
               case RIGHT, UpRIGHT, DownRIGHT -> {
                  shiftDirection = Direction.LEFT;
                  shiftNTimes = amountOfLeftShift(dummyCoordinateY.x());
                  return !board.canShiftLeft(shiftNTimes);
               }
               case LEFT, UpLEFT, DownLEFT ->  {
                  shiftDirection = Direction.RIGHT;
                  shiftNTimes = amountOfRightShift(dummyCoordinate.x());
                  return !board.canShiftRight(shiftNTimes);
               }
            }
         throw new IllegalArgumentException("Either a vertical dummy piece being put left or right, or a horizontal dummy piece being put up or down, both of those direction are illegal");
   }

   public Direction[] getShiftDirection(Domino other){
      return (other.isDouble()) ? Arrays.copyOf(doubleShiftDirection,2) : new Direction[]{shiftDirection};
   }

   @Override
   public int[] getShiftNTimes(Domino other) {
      return (other.isDouble()) ? Arrays.copyOf(doubleShiftNTimes,2) : new int[]{shiftNTimes};
   }

   public boolean isShiftNeeded(Domino other){
      return (other.isDouble()) ? doubleShiftNTimes[0] + doubleShiftNTimes[1] != 0 : shiftNTimes != 0;
   }
   public Direction getAvailableDirection(Domino other){return (other.isDouble()) ? nextDirection[0] : nextDirection[currentNextDirection];}

   public Coordinate getAvailableCoordinate(Direction direction,Domino other){
      return domino.isVertical() ? getVerticalAvailableCoordinate(other) : getHorizontalAvailableCoordinate(other);
   }

   //this gives you the available coordinate for the x on the gameBoard, note that the x will always be the up left corner.
   private Coordinate getVerticalAvailableCoordinate(Domino other){
      Direction direction = (other.isDouble()) ? nextDirection[0] : nextDirection[currentNextDirection];
      if(other.isDouble()) {
         if     (direction == Direction.UP)         return new Coordinate(coordinate.x() - 1, coordinate.y() + 1);
         else if(direction == Direction.DOWN)       return new Coordinate(coordinate.x() - 1, coordinate.y() - 3);
         else                                           throw new IllegalArgumentException("A horizontal double must be placed up or down");
      }
      else
      {
         if(direction == Direction.UP) return new Coordinate(coordinate.x(), coordinate.y() + 4);
         else if(direction == Direction.DOWN)      return new Coordinate(coordinate.x(),coordinate.y() -4);
         else if(direction == Direction.UpLEFT)    return new Coordinate(coordinate.x() -3,coordinate.y());
         else if(direction == Direction.UpRIGHT)   return new Coordinate(coordinate.x() +2,coordinate.y());
         else if(direction == Direction.DownLEFT)  return new Coordinate(coordinate.x() -3,coordinate.y()-2);
         else if(direction == Direction.DownRIGHT) return new Coordinate(coordinate.x() +2,coordinate.y()-2);
         else throw new IllegalArgumentException("This Corner should not exist");
      }
   }

   private Coordinate getHorizontalAvailableCoordinate(Domino other){
      Direction direction = (other.isDouble()) ? nextDirection[0] : nextDirection[currentNextDirection];
      if(other.isDouble()){
         if     (direction == Direction.LEFT)         return new Coordinate(coordinate.x() - 2, coordinate.y() + 1);
         else if(direction == Direction.RIGHT)        return new Coordinate(coordinate.x() + 3, coordinate.y() + 1);
         else                                             throw new IllegalArgumentException("A Vertical double must be placed right or left");
      }
      else
      {
         if(direction == Direction.LEFT) return new Coordinate(coordinate.x()-3, coordinate.y());
         else if(direction == Direction.RIGHT)      return new Coordinate(coordinate.x()+3,coordinate.y());
         else if(direction == Direction.UpLEFT)    return new Coordinate(coordinate.x() ,coordinate.y() +3);
         else if(direction == Direction.UpRIGHT)   return new Coordinate(coordinate.x() +1,coordinate.y()+3);
         else if(direction == Direction.DownLEFT)  return new Coordinate(coordinate.x() ,coordinate.y()-1);
         else if(direction == Direction.DownRIGHT) return new Coordinate(coordinate.x() +1,coordinate.y()-1);
         else throw new IllegalArgumentException("This Corner should not exist");
      }
   }

}


