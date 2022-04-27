final public class HorizontalCornerLine extends CornerLine {
   HorizontalCornerLine(Coordinate coordinate,Domino domino,Direction direction,GameBoard board) {
      super(coordinate, domino, board);

      switch(direction) {
         case RIGHT, UpRIGHT, DownRIGHT -> super.nextDirection = Direction.RIGHT;
         case LEFT, UpLEFT, DownLEFT -> super.nextDirection = Direction.LEFT;
      }
      updateDirections();
   }

   public void updateDirections() {
      if(nextDirection == null || !isDirectionBlocked(nextDirection)) return;
      switch(nextDirection){
         case LEFT : nextDirection = Direction.UpLEFT; updateDirections(); return;
         case RIGHT : nextDirection = Direction.UpRIGHT; updateDirections(); return;
         case UpLEFT: nextDirection = Direction.DownLEFT; updateDirections(); return;
         case UpRIGHT: nextDirection = Direction.DownRIGHT;updateDirections(); return;
         case DownLEFT:
         case DownRIGHT: nextDirection = null;
      }
   }

   @Override
   //pre direction.ordinal > 3 && direction == (LEFT || RIGHT)
   protected boolean isGeneralDirectionBlocked(Domino dummy, Coordinate dummyCoordinate, Direction direction) {
      if(direction.ordinal() > 3 || direction == Direction.UP || direction == Direction.DOWN)
         throw new IllegalArgumentException("general block direction must be up or down (pre condition broken)");
      if(dummyCoordinate.y() +1 >= board.getLines() || dummyCoordinate.y() -1 < 0) super.doubleIsBlocked = true;
      if(direction == Direction.LEFT && (dummyCoordinate.x()+1  < 0 || dummyCoordinate.x()+1 >= board.getLines())) super.doubleIsBlocked = true;
      else if(dummyCoordinate.x() < 0 || dummyCoordinate.x() >= board.getLines()) super.doubleIsBlocked = true;

      if(dummyCoordinate.x() + 1 >= board.getColumns() || dummyCoordinate.x() < 0) super.lineIsBlocked = true;
      if(super.lineIsBlocked || super.doubleIsBlocked) return true;

      //Gets the dominoes on a rectangle that includes both cases, and if there is more than one piece(the piece that connects to the dummy) then it returns true.
      var nDominoes =  board.getDominoesInThisRectangle(dummyCoordinate.x()-3, dummyCoordinate.y()-3,dummyCoordinate.x()+3,dummyCoordinate.y()+3).iterator();
      nDominoes.next();
      return nDominoes.hasNext();
   }

   @Override
   //pre direction.ordinal > 3
   protected boolean isDoubleDirectionBlocked(Direction direction) {
      int x = domino.getUnconnected();
      var dummy = new Domino(x,x);
      Coordinate dummyCoordinate = getAvailableCoordinate(direction,dummy);
      if(direction == Direction.LEFT)
         return board.isThisRectangleOccupied(dummyCoordinate.x()-2,dummyCoordinate.y()-3,dummyCoordinate.x()-1,dummyCoordinate.y()+1);
      else if(direction == Direction.RIGHT)
         return board.isThisRectangleOccupied(dummyCoordinate.x()+1,dummyCoordinate.y()-3,dummyCoordinate.x()+2,dummyCoordinate.y()+1);
      else throw new IllegalArgumentException("(horizontal corners) Doubles must be placed either left or right");
   }

   @Override
   protected boolean isLineDirectionBlocked(Domino dummy,Coordinate dummyCoordinate,Direction direction){
      switch(direction){
         case LEFT ->{return board.isThisRectangleOccupied(dummyCoordinate.x()-3,dummyCoordinate.y()-3,dummyCoordinate.x(),dummyCoordinate.y()+3);}
         case RIGHT ->{return board.isThisRectangleOccupied(dummyCoordinate.x()+1,dummyCoordinate.y()-3,dummyCoordinate.x()+4,dummyCoordinate.y()+3);}
         case UpLEFT ,UpRIGHT ->{return board.isThisRectangleOccupied(dummyCoordinate.x()-2, dummyCoordinate.y()-1,dummyCoordinate.x() +2,dummyCoordinate.y()+2);}
         case DownLEFT ,DownRIGHT ->{return board.isThisRectangleOccupied(dummyCoordinate.x() -2,dummyCoordinate.y()-4,dummyCoordinate.x()+2,dummyCoordinate.y()-1);}
         }

      throw new IllegalStateException("This corner should not exist the nextDirection is null or UP or DOWN");
   }
}
