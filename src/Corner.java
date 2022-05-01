public abstract class Corner {
   protected Coordinate coordinate;
   protected Domino domino;
   protected GameBoard board;
   protected Direction[] shiftDirection = new Direction[2]; //two shifts may happen, as in the case of a horizontal double,
   protected int[] shiftNTimes = new int[2];                //may break the horizontal boundary and the vertical boundary at the same time.

   Corner(Coordinate coordinate,Domino domino,GameBoard board){
      this.coordinate = coordinate;
      this.domino = domino;
      this.board = board;
   }

   abstract public boolean isCorner();
   abstract public Coordinate getAvailableCoordinate(Direction direction,Domino other);
   abstract public Direction getAvailableDirection();
   abstract public void updateDirections();
   public boolean isEqual(Domino other) {return domino.equals(other);}
   abstract public boolean canPlay(Domino other);


   public void connectWith(Domino other,Direction direction){
      if(other.isDouble()){
         if(direction == Direction.UP || direction == Direction.DOWN) other.beHorizontal();
         else if(direction == Direction.RIGHT || direction == Direction.LEFT)  other.beVertical();
         else throw new IllegalArgumentException("A double can't be placed on a blocked line");
      }
      else
      {
         switch(direction) {
            case DOWN -> { other.beVertical();   connectToX(other); }
            case UP ->   { other.beVertical();   connectToY(other); }
            case RIGHT ->{ other.beHorizontal(); connectToX(other); }
            case LEFT -> { other.beHorizontal(); connectToY(other); }
            default -> {// I LISTEN TO THE VOICES AND THE VOICES LISTEN TO ME
               if(this.domino.isVertical()) {
                  other.beHorizontal();
                  if(direction == Direction.UpLEFT || direction == Direction.DownLEFT) connectToY(other);
                  else connectToX(other);
               } else {
                  other.beVertical();
                  if(direction == Direction.UpLEFT || direction == Direction.UpRIGHT) connectToY(other);
                  else connectToX(other);
               }
            }
         }
      }
   }

   private void connectToX(Domino other){  other.connectToX(); if(other.getX() != this.domino.getUnconnected()) other.flip();}
   private void connectToY(Domino other){  other.connectToY(); if(other.getY() != this.domino.getUnconnected()) other.flip();}

   public int getShiftNTimes(){return shiftNTimes;}
   public Direction getShiftDirection(){return shiftDirection;}
}
