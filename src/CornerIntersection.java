public class CornerIntersection extends Corner{
      private final boolean[] unavailableDirections = new boolean[4];

      public CornerIntersection(Coordinate coordinate,Domino domino,Direction blockedDirection,GameBoard board){
         super.coordinate = coordinate;
         super.domino = domino;
         super.board = board;
         if(blockedDirection != null) blockedDirection(blockedDirection);
      }

      public void blockedDirection(Direction direction) { unavailableDirections[direction.ordinal()] = true;}

      public boolean isCorner() {
         for(boolean x: unavailableDirections)
            if(!x) return true;
         return false;
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

      public void updateDirections(){

      }

      private Coordinate verticalAvailableCoordinate(int x){
         switch(x){
            case 0: blockedDirection(Direction.LEFT); return new Coordinate(coordinate.x() -4,coordinate.y() -1);
            case 1: blockedDirection(Direction.UP);   return new Coordinate(coordinate.x(),coordinate.y() +4);
            case 2: blockedDirection(Direction.RIGHT);return new Coordinate(coordinate.x() +3,coordinate.y() -1);
            case 3: blockedDirection(Direction.DOWN); return new Coordinate(coordinate.x(),coordinate.y() -4);
         }
         throw new IllegalArgumentException("Is the boolean array higher than 4?");
      }

      private Coordinate horizontalAvailableCoordinate(int x){
         switch(x){
            case 0:blockedDirection(Direction.LEFT);  return new Coordinate(coordinate.x()-3,coordinate.y());
            case 1:blockedDirection(Direction.UP);    return new Coordinate(coordinate.x()+1,coordinate.y()+4);
            case 2:blockedDirection(Direction.RIGHT); return new Coordinate(coordinate.x()+4,coordinate.y());
            case 3:blockedDirection(Direction.DOWN);  return new Coordinate(coordinate.x()+1,coordinate.y()-2);
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
