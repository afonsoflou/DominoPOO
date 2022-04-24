public abstract class Corner {
   protected Coordinate coordinate;
   protected Domino domino;
   protected GameBoard board;

   abstract public boolean isCorner();
   abstract public void blockedDirection(Direction direction);
   abstract public Coordinate getAvailableCoordinate(Direction direction,Domino other);
   abstract public Direction getAvailableDirection();
   abstract public void updateDirections();
   public boolean isEqual(Domino other) {return domino.isEqual(other);}


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

}
