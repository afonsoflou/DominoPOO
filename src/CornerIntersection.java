public class CornerIntersection extends Corner{
   private final boolean[] unavailableDirections = new boolean[4];
   private Direction nextDirection;

   public CornerIntersection(Coordinate coordinate,Domino domino,Direction blockedDirection,GameBoard board){
      super(coordinate,domino,board);
      if(blockedDirection != null) blockedDirection(blockedDirection);
      nextDirection = getAvailableDirection();
   }

   public boolean canPlay(Domino other) {return domino.canConnect(other);}

   private void blockedDirection(Direction direction) { unavailableDirections[direction.ordinal()] = true;}

   public boolean isCorner() {
      return nextDirection != null;
   }

   public Direction getAvailableDirection() {
      int N = unavailableDirections.length;
      for(int i = 0; i < N; i++)
         if(!unavailableDirections[i])
            return convIntToDirection(i);
      return null;
   }

   public Coordinate getAvailableCoordinate(Direction direction,Domino other) {
      return getAvailableCoordinate(direction);
   }

   private Coordinate getAvailableCoordinate(Direction direction){
      return (domino.isVertical()) ? verticalAvailableCoordinate(direction.ordinal()) : horizontalAvailableCoordinate(direction.ordinal());
   }

   public void updateDirections(){
      nextDirection = getAvailableDirection();
      if(nextDirection == null || !isDirectionBlocked(nextDirection)) return;
      else blockedDirection(nextDirection);
      updateDirections();
   }

   private boolean isDirectionBlocked(Direction direction){
      Coordinate dummyCoordinate = getAvailableCoordinate(direction);

      if((direction == Direction.UP || direction == Direction.DOWN)
              && (dummyCoordinate.y() >= board.getLines() || dummyCoordinate.y() < 2)) return true;
      if((direction == Direction.LEFT || direction == Direction.RIGHT)
              && (dummyCoordinate.x() +1 >= board.getColumns() || dummyCoordinate.x() < 0)) return true;

      switch(direction){
         case LEFT -> {return board.isThisRectangleOccupied(dummyCoordinate.x()-2,dummyCoordinate.y()-3,dummyCoordinate.x(),dummyCoordinate.y()+3);}
         case UP ->   {return board.isThisRectangleOccupied(dummyCoordinate.x()-2,dummyCoordinate.y()-1,dummyCoordinate.x() +2,dummyCoordinate.y()+2);}
         case RIGHT ->{return board.isThisRectangleOccupied(dummyCoordinate.x()+1,dummyCoordinate.y()-3,dummyCoordinate.x()+3,dummyCoordinate.y()+3);}
         case DOWN -> {return board.isThisRectangleOccupied(dummyCoordinate.x()-2,dummyCoordinate.y()-4,dummyCoordinate.x()+2,dummyCoordinate.y()-1);}
      }
      throw new IllegalStateException("This Intersection should not exist, nextDirection is null or direction.ordinal > 3 (class invariant is broken)");
   }

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


   private Direction convIntToDirection(int x) {
      switch(x) {
         case 0: return Direction.LEFT;
         case 1: return Direction.UP;
         case 2: return Direction.RIGHT;
         case 3: return Direction.DOWN;
      }
      throw new IllegalArgumentException("must be a integer between 0 and 3");
   }
}
