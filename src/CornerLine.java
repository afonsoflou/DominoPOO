public abstract class CornerLine extends Corner{
   protected Direction nextDirection;
   protected boolean lineIsBlocked;
   protected boolean doubleIsBlocked;

   CornerLine(Coordinate coordinate,Domino domino,GameBoard board){ super(coordinate,domino,board); }

   public void blockedDirection(Direction direction) { if(nextDirection == direction)  nextDirection = null; }

   public boolean canPlay(Domino other) {
      if(other.isDouble()){
         if(doubleIsBlocked) return false;
         return other.canConnect(domino);
      }

      if(lineIsBlocked) return false;
      return domino.canConnect(other);
   }

   public boolean isCorner() { return nextDirection != null ;}

   abstract public void updateDirections();

   protected boolean isDirectionBlocked(Direction direction) {
      lineIsBlocked = false;
      doubleIsBlocked = false; //reset availability for each direction.

      if(direction == null)
         throw new IllegalArgumentException("null direction can't be blocked, maybe this corner shouldn't exist");
      var dummy = new Domino(domino.getX(), domino.getY());
      var dummyCoordinate = getAvailableCoordinate(direction, dummy);
      this.connectWith(dummy, direction); //to make the domino have the right rotation (horizontal or vertical)
      if(direction.ordinal() <= 3)
      {
         if(!isGeneralDirectionBlocked(dummy, dummyCoordinate, direction)) return false;
      }
      else doubleIsBlocked = true;

      if(!doubleIsBlocked) doubleIsBlocked = isDoubleDirectionBlocked(direction) ;
      if(!lineIsBlocked) lineIsBlocked = isLineDirectionBlocked(dummy,dummyCoordinate,direction);
      return doubleIsBlocked && lineIsBlocked;
   }

   abstract protected boolean isDoubleDirectionBlocked(Direction direction);
   abstract protected boolean isLineDirectionBlocked(Domino dummy,Coordinate dummyCoordinate,Direction direction);
   abstract protected boolean isGeneralDirectionBlocked(Domino dummy,Coordinate dummyCoordinate,Direction direction);




   public Direction getAvailableDirection(){return nextDirection;}

   public Coordinate getAvailableCoordinate(Direction direction,Domino other){
      return domino.isVertical() ? getVerticalAvailableCoordinate(other) : getHorizontalAvailableCoordinate(other);
   }

   private Coordinate getVerticalAvailableCoordinate(Domino other){
      if(other.isDouble()) {
         if     (nextDirection == Direction.UP)         return new Coordinate(coordinate.x() - 1, coordinate.y() + 1);
         else if(nextDirection == Direction.DOWN)       return new Coordinate(coordinate.x() - 1, coordinate.y() - 4);
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
         else if(nextDirection == Direction.RIGHT)        return new Coordinate(coordinate.x() + 3, coordinate.y() + 1);
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

