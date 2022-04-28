final public class VerticalCornerLine extends CornerLine{
   public VerticalCornerLine(Coordinate coordinate,Domino domino,Direction direction,GameBoard board){
      super(coordinate,domino,board);

      switch(direction) {
         case UP, UpRIGHT, UpLEFT -> super.nextDirection = Direction.UP;
         case DOWN, DownLEFT, DownRIGHT -> super.nextDirection = Direction.DOWN;
      }
   }
   
   public void updateDirections() {
      if(nextDirection == null || !isDirectionBlocked(nextDirection)) return;
      switch(nextDirection){
         case UP:   nextDirection = Direction.UpLEFT ; updateDirections(); return;
         case DOWN: nextDirection = Direction.DownLEFT; updateDirections(); return;
         case UpLEFT: nextDirection = Direction.UpRIGHT; updateDirections(); return;
         case DownLEFT: nextDirection = Direction.DownRIGHT; updateDirections(); return;
         case UpRIGHT:
         case DownRIGHT: nextDirection = null;
         }
   }

   @Override
   protected boolean isGeneralDirectionBlocked(Domino dummy, Coordinate dummyCoordinate, Direction direction) {
      if(direction.ordinal() > 3 || direction == Direction.LEFT || direction == Direction.RIGHT)
         throw new IllegalArgumentException("general block direction must be up or left (pre condition broken)");
      //double available space check.
      if(dummyCoordinate.x()-1 < 0 || dummyCoordinate.x()+1 >= board.getColumns()) super.doubleIsBlocked = true;
      if(direction == Direction.UP && (dummyCoordinate.y()-2 >= board.getLines() || dummyCoordinate.y()-2 < 0)) super.doubleIsBlocked = true;
      else if(dummyCoordinate.y() >= board.getLines() || dummyCoordinate.y() < 0) super.doubleIsBlocked = true;

      //single available space check.
      if(dummyCoordinate.y() >= board.getLines() || dummyCoordinate.y() < 2) super.lineIsBlocked = true;
      if(super.lineIsBlocked || super.doubleIsBlocked) return true; //The general case fails because there is at least one of them without available space.

      //Gets the dominoes on a rectangle that includes both cases, and if there is more than one piece(the piece that connects to the dummy) then it returns true.
      var nDominoes =  board.getDominoesInThisRectangle(dummyCoordinate.x()-3, dummyCoordinate.y()-4,dummyCoordinate.x()+3,dummyCoordinate.y()+2).iterator();
      nDominoes.next();
      return nDominoes.hasNext();
   }

   @Override
   //pre direction.ordinal > 3
   protected boolean isDoubleDirectionBlocked(Direction direction) {
      int x = domino.getUnconnected();
      var dummy = new Domino(x,x);
      Coordinate dummyCoordinate = getAvailableCoordinate(direction,dummy);
      if(direction == Direction.UP)
         return board.isThisRectangleOccupied(dummyCoordinate.x()-2,dummyCoordinate.y()+1,dummyCoordinate.x()+4,dummyCoordinate.y()+2);
      else if(direction == Direction.DOWN)
         return board.isThisRectangleOccupied(dummyCoordinate.x()-2,dummyCoordinate.y()-2,dummyCoordinate.x()+4,dummyCoordinate.y()-1);
      else throw new IllegalArgumentException("(vertical corners) Doubles must be placed either up or down");
   }

   @Override
   protected boolean isLineDirectionBlocked(Domino dummy,Coordinate dummyCoordinate,Direction direction){
      switch(direction){
         case UP ->{return board.isThisRectangleOccupied(dummyCoordinate.x()-2, dummyCoordinate.y()-1,dummyCoordinate.x() +2,dummyCoordinate.y()+2);}
         case DOWN ->{return board.isThisRectangleOccupied(dummyCoordinate.x() -2,dummyCoordinate.y()-4,dummyCoordinate.x()+2,dummyCoordinate.y()-1);}
         case UpLEFT , DownLEFT->{
            if(dummyCoordinate.x() < 0 || dummyCoordinate.x() +1 >= board.getColumns()) return true; //checks if it falls outside the board.
            return board.isThisRectangleOccupied(dummyCoordinate.x()-2,dummyCoordinate.y()-3,dummyCoordinate.x(),dummyCoordinate.y()+3);
         }
         case UpRIGHT ,DownRIGHT-> {
            if(dummyCoordinate.x() < 0 || dummyCoordinate.x() +1 >= board.getLines()) return true; //checks if it falls outside the board.
            return board.isThisRectangleOccupied(dummyCoordinate.x()+1,dummyCoordinate.y()-3,dummyCoordinate.x()+3,dummyCoordinate.y()+3);
         }
      }
      throw new IllegalStateException("This corner should not exist the nextDirection is null or left or right");
   }
}
