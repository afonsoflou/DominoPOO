public class CornerIntersection extends Corner{
   private final Direction[] nextDirection;
   private int currentNextDirection = 0;
   private Direction shiftDirection;
   private int shiftNTimes = 0;


   public CornerIntersection(Coordinate coordinate,Domino domino,Direction blockedDirection,GameBoard board){
      super(coordinate,domino,board);
      if(blockedDirection == null)
         nextDirection = new Direction[]{Direction.LEFT,Direction.UP,Direction.RIGHT,Direction.DOWN};
      else
         switch(blockedDirection){
         case LEFT -> nextDirection = new Direction[]{Direction.UP,Direction.RIGHT,Direction.DOWN};
         case UP ->   nextDirection = new Direction[]{Direction.LEFT,Direction.RIGHT,Direction.DOWN};
         case RIGHT ->nextDirection = new Direction[]{Direction.LEFT,Direction.UP,Direction.DOWN};
         case DOWN -> nextDirection = new Direction[]{Direction.LEFT,Direction.UP,Direction.RIGHT};
         default -> throw new IllegalArgumentException("Illegal direction");
      }
   }

   public boolean canPlay(Domino other) {return domino.canConnect(other);}

   public boolean isShiftNeeded(Domino other) { return shiftNTimes != 0; }

   public boolean isCorner() { return currentNextDirection < nextDirection.length;}

   public Direction getAvailableDirection(Domino other) {
      return nextDirection[currentNextDirection];
   }

   public Coordinate getAvailableCoordinate(Direction direction,Domino other) {
      return getAvailableCoordinate(direction);
   }

   private Coordinate getAvailableCoordinate(Direction direction){
      return (domino.isVertical()) ? verticalAvailableCoordinate(direction.ordinal()) : horizontalAvailableCoordinate(direction.ordinal());
   }

   public void updateDirections() {
      while(currentNextDirection < nextDirection.length) {
         if(!isDirectionBlocked(nextDirection[currentNextDirection])) break;
         currentNextDirection++;
      }
   }

   private boolean isDirectionBlocked(Direction direction){
      var dummy = new Domino(domino.getX(), domino.getY());
      var dummyCoordinate = getAvailableCoordinate(direction, dummy);
      var dummyCoordinateY = (direction == Direction.DOWN || direction == Direction.UP ) ?
              getVerticalYCoordinate(dummyCoordinate) : getHorizontalYCoordinate(dummyCoordinate);

      if(isOutsideTheBoard(dummyCoordinate,dummyCoordinateY,direction)) return true;

      //there is the domino before the dummy that is the corner domino.If there is more than 1 then it's blocked
      return 1 != board.getNDominoesOnThisRectangle(dummyCoordinate.x()-2, dummyCoordinateY.y()-2, dummyCoordinateY.x()+2,dummyCoordinate.y()+2);
   }

   private boolean isOutsideTheBoard(Coordinate dummyCoordinate,Coordinate dummyCoordinateY,Direction direction){
         switch(direction) {
            case UP ->   {
               shiftDirection = Direction.DOWN;
               shiftNTimes = amountOfDownShift(dummyCoordinate.y());
               return !board.canShiftDown(shiftNTimes);
            }
            case DOWN -> {
               shiftDirection = Direction.UP;
               shiftNTimes = amountOfUpShift(dummyCoordinateY.y());
               return !board.canShiftUp(shiftNTimes);
            }
            case RIGHT-> {
               shiftDirection = Direction.LEFT;
               shiftNTimes = amountOfLeftShift(dummyCoordinateY.x());
               return !board.canShiftLeft(shiftNTimes);
            }
            case LEFT->  {
               shiftDirection = Direction.RIGHT;
               shiftNTimes = amountOfRightShift(dummyCoordinate.x());
               return !board.canShiftRight(shiftNTimes);
            }
         }
      throw new IllegalArgumentException("There are illegal direction for a double corner ex:UpLeft");
   }

   @Override
   public int[] getShiftNTimes(Domino other) { return new int[]{shiftNTimes}; }

   @Override
   public Direction[] getShiftDirection(Domino other) { return new Direction[]{shiftDirection}; }

   private Coordinate verticalAvailableCoordinate(int x){
      switch(x){
         case 0: return new Coordinate(coordinate.x() -3,coordinate.y() -1);
         case 1: return new Coordinate(coordinate.x(),coordinate.y() +4);
         case 2: return new Coordinate(coordinate.x() +2,coordinate.y() -1);
         case 3: return new Coordinate(coordinate.x(),coordinate.y() -4);
      }
      throw new IllegalArgumentException("Is the boolean array higher than 4?");
   }

   private Coordinate horizontalAvailableCoordinate(int x){
      switch(x){
         case 0: return new Coordinate(coordinate.x()-3,coordinate.y());
         case 1: return new Coordinate(coordinate.x()+1,coordinate.y()+4);
         case 2: return new Coordinate(coordinate.x()+4,coordinate.y());
         case 3: return new Coordinate(coordinate.x()+1,coordinate.y()-2);
      }
      throw new IllegalArgumentException("Is the boolean array higher than 4?");
   }

}
