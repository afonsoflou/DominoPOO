public abstract class Corner {
   protected Coordinate coordinate;
   protected Domino domino;
   protected GameBoard board;


   Corner(Coordinate coordinate,Domino domino,GameBoard board){
      this.coordinate = coordinate;
      this.domino = domino;
      this.board = board;
   }

   abstract public boolean isCorner();
   abstract public Coordinate getAvailableCoordinate(Direction direction,Domino other);
   abstract public Direction getAvailableDirection(Domino other);
   abstract public void updateDirections();
   public boolean isEqual(Domino other) {return domino.equals(other);}
   abstract public boolean canPlay(Domino other);

   abstract public boolean isShiftNeeded(Domino other);
   abstract public int[] getShiftNTimes(Domino other);
   abstract public Direction[] getShiftDirection(Domino other);

   public void shiftUp(int n){coordinate = new Coordinate(coordinate.x(),coordinate.y() + n);}
   public void shiftDown(int n){coordinate = new Coordinate(coordinate.x(),coordinate.y() - n);}
   public void shiftRight(int n){coordinate = new Coordinate(coordinate.x()+n,coordinate.y());}
   public void shiftLeft(int n){coordinate = new Coordinate(coordinate.x()-n,coordinate.y());}

  //note: Probably I need to explain why do I use up left corner when the domino can only be placed vertically or
  //horizontally which means that y will be the same or x will be same therefore either the vertical direction or the
   //horizontal direction is redundant on the explanation, but this is good to generalize for both cases.

   //this 4 functions give you the y coordinate from the x coordinate, because x will always be placed in the up left
   //on the board, then the y will always be placed on the down right of the board.
   protected Coordinate getVerticalYCoordinate(Coordinate x){
     return new Coordinate(x.x(),x.y()-2);
   }

   protected Coordinate getHorizontalYCoordinate(Coordinate x){
      return new Coordinate(x.x()+1,x.y());
   }

   protected Coordinate getVerticalDoubleYCoordinate(Coordinate x){
      return new Coordinate(x.x(),x.y()-2); //this is identical to the vertical coordinate;
   }

   protected Coordinate getHorizontalDoubleYCoordinate(Coordinate x){
      return new Coordinate(x.x()+2,x.y());
   }

   //Attention this might confuse you a bit, but if you want to put a piece on the right you need to check if the left
   //side as space in case of a blockage, but the arguments will be off course the piece rightmost segment and the right border,
   // because they are needed to know the amount you will need for the other side.

   //returns 0 if upperShift isn't necessary x > 0 otherwise. upperY is exclusive.
   protected int amountOfDownShift(int y){
      if(y > board.getLines() -1) return y - board.getLines() + 1;
      return 0;
   }

   //returns 0 if lowerShift isn't necessary x > 0 otherwise. lowerY is inclusive
   protected int amountOfUpShift(int y){
      if(y < 0) return -y;
      return 0;
   }

   //returns 0 if rightShift isn't necessary x > 0 otherwise. upperX is exclusive.
   protected int amountOfLeftShift(int x){
      if(x > board.getColumns() -1) return x-board.getColumns() +1;
      return 0;
   }

   //returns 0 if leftShift isn't necessary x > 0 otherwise. lowerX is inclusive
   protected int amountOfRightShift(int x){
      if(x < 0) return -x;
      return 0;
   }

}
