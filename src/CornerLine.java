public class CornerLine extends Corner{
   private Direction nextDirection;

   CornerLine(Coordinate coordinate,Domino domino,Direction direction,GameBoard board){
      super.coordinate = coordinate;
      super.domino = domino;
      super.board = board;

      if(direction.ordinal() > 3)
         if(domino.isVertical())
            switch (direction) {
               case UpRIGHT, UpLEFT -> nextDirection = Direction.UP;
               case DownLEFT, DownRIGHT -> nextDirection = Direction.DOWN;
            }
         else
            switch (direction) {
               case UpRIGHT, DownRIGHT -> nextDirection = Direction.RIGHT;
               case UpLEFT, DownLEFT -> nextDirection = Direction.LEFT;
            }
      else
         nextDirection = direction;

      updateDirections();
   }

   public void blockedDirection(Direction direction) { if(nextDirection == direction)  nextDirection = null; }

   public boolean isCorner() { return nextDirection != null ;}

   public void updateDirections(){ //THE WALLS ARE STARING AT YOU, DON'T BLINK.
      if(nextDirection == null || !isDirectionBlocked(nextDirection)) return;
      if(domino.isVertical()){
         switch(nextDirection){
            case UP:   nextDirection = Direction.UpLEFT ; updateDirections(); return;
            case DOWN: nextDirection = Direction.DownLEFT; updateDirections(); return;
            case UpLEFT: nextDirection = Direction.UpRIGHT; updateDirections(); return;
            case DownLEFT: nextDirection = Direction.DownRIGHT; updateDirections(); return;
            case UpRIGHT:
            case DownRIGHT: nextDirection = null;
         }
      } else {
         switch(nextDirection){
            case LEFT : nextDirection = Direction.UpLEFT; updateDirections(); return;
            case RIGHT : nextDirection = Direction.UpRIGHT; updateDirections(); return;
            case UpLEFT: nextDirection = Direction.DownLEFT; updateDirections(); return;
            case UpRIGHT: nextDirection = Direction.DownRIGHT;updateDirections(); return;
            case DownLEFT:
            case DownRIGHT: nextDirection = null;
         }
      }
   }

   private boolean isDirectionBlocked(Direction direction) {
      if(direction == null)
         throw new IllegalArgumentException("null direction can't be blocked, maybe this corner shouldn't exist");
      var dummy = new Domino(domino.getX(), domino.getY());
      var dummyCoordinate = getAvailableCoordinate(direction, dummy);
      this.connectWith(dummy, direction); //to make the domino have the right rotation (horizontal or vertical)
      return (dummy.isVertical()) ? isVerticalDirectionBlocked(dummyCoordinate, direction) :
                                    isHorizontalDirectionBlocked(dummyCoordinate,direction);
   }

   private boolean isVerticalDirectionBlocked(Coordinate dummyCoordinate, Direction direction){
      if(dummyCoordinate.y() >= board.getLines() || dummyCoordinate.y() < 2) return true;

      if(direction == Direction.UP || direction == Direction.UpRIGHT || direction == Direction.UpLEFT)
         return board.isThisRectangleOccupied(dummyCoordinate.x()-2, dummyCoordinate.y()-1,dummyCoordinate.x() +2,dummyCoordinate.y()+2);
      else if(direction == Direction.DOWN || direction == Direction.DownRIGHT || direction == Direction.DownLEFT)
         return board.isThisRectangleOccupied(dummyCoordinate.x() -2,dummyCoordinate.y()-4,dummyCoordinate.x()+2,dummyCoordinate.y()-1);
      else throw new IllegalStateException("Vertical pieces can't have horizontal directions ex: left or right");

   }

   private boolean isHorizontalDirectionBlocked(Coordinate dummyCoordinate, Direction direction){
         if(dummyCoordinate.x() >= board.getColumns()-1 || dummyCoordinate.x() < 0) return true;

         if(direction == Direction.LEFT || direction == Direction.UpLEFT || direction == Direction.DownLEFT)
            return board.isThisRectangleOccupied(dummyCoordinate.x()-3,dummyCoordinate.y()-3,dummyCoordinate.x(),dummyCoordinate.y()+3);
         else if(direction == Direction.RIGHT || direction == Direction.UpRIGHT || direction == Direction.DownRIGHT)
            return board.isThisRectangleOccupied(dummyCoordinate.x()+1,dummyCoordinate.y()-3,dummyCoordinate.x()+4,dummyCoordinate.y()+3);
         else throw new IllegalStateException("Horizontal pieces can't have vertical directions ex: up and down");
      }

   public Direction getAvailableDirection(){return nextDirection;}

   public Coordinate getAvailableCoordinate(Direction direction,Domino other){
      return domino.isVertical() ? getVerticalAvailableCoordinate(other) : getHorizontalAvailableCoordinate(other);
   }

   private Coordinate getVerticalAvailableCoordinate(Domino other){
      if(other.isDouble()) {
         if     (nextDirection == Direction.UP)         return new Coordinate(coordinate.x() - 1, coordinate.y() + 1);
         else if(nextDirection == Direction.DOWN)       return new Coordinate(coordinate.x() - 1, coordinate.y() - 1);
         else                                           throw new IllegalArgumentException("A horizontal double must be placed up or down");
      }
      else
      {
         if(nextDirection == Direction.UP) return new Coordinate(coordinate.x(), coordinate.y() + 4);
         else if(nextDirection == Direction.DOWN)      return new Coordinate(coordinate.x(),coordinate.y() -4);
         else if(nextDirection == Direction.UpLEFT)    return new Coordinate(coordinate.x() -3,coordinate.y());
         else if(nextDirection == Direction.UpRIGHT)   return new Coordinate(coordinate.x() +2,coordinate.y());
         else if(nextDirection == Direction.DownLEFT)  return new Coordinate(coordinate.x() -3,coordinate.y()-2);
         else if(nextDirection == Direction.DownRIGHT) return new Coordinate(coordinate.x() +2,coordinate.y()-2);
         else throw new IllegalArgumentException("This Corner should not exist");
      }
   }

   private Coordinate getHorizontalAvailableCoordinate(Domino other){
      if(other.isDouble()){
         if     (nextDirection == Direction.LEFT)         return new Coordinate(coordinate.x() - 2, coordinate.y() + 1);
         else if(nextDirection == Direction.RIGHT)        return new Coordinate(coordinate.x() + 2, coordinate.y() + 1);
         else                                             throw new IllegalArgumentException("A Vertical double must be placed right or left");
      }
      else
      {
         if(nextDirection == Direction.LEFT) return new Coordinate(coordinate.x()-3, coordinate.y());
         else if(nextDirection == Direction.RIGHT)      return new Coordinate(coordinate.x()+3,coordinate.y());
         else if(nextDirection == Direction.UpLEFT)    return new Coordinate(coordinate.x() ,coordinate.y() +4);
         else if(nextDirection == Direction.UpRIGHT)   return new Coordinate(coordinate.x() +1,coordinate.y()+4);
         else if(nextDirection == Direction.DownLEFT)  return new Coordinate(coordinate.x() ,coordinate.y()-2);
         else if(nextDirection == Direction.DownRIGHT) return new Coordinate(coordinate.x() +1,coordinate.y()-2);
         else throw new IllegalArgumentException("This Corner should not exist");
      }
   }

}

