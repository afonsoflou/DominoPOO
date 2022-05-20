/**
 * @inv coordinate != null
 * @inv domino != null
 * @inv board != null
 * @inv domino must not change.
 * @inv board must not change.
 */
public abstract class Corner {
   protected Coordinate coordinate;
   final protected Domino domino;
   final protected GameBoard board;

   Corner(Coordinate coordinate,Domino domino,GameBoard board){
      if(coordinate == null || domino == null || board == null) throw new IllegalArgumentException("There cannot be null fields");
      this.coordinate = coordinate;
      this.domino = domino;
      this.board = board;
   }

   /** It returns true if the corner has available direction, false otherwise.
    * @pre true
    * @post state = oldState
    * @return true if the corner has available directions, false otherwise.
    */
   abstract public boolean isCorner();

   /** It returns the corresponding coordinate to the direction played in relation to the corner domino.
    * @param direction
    * @param other
    * @pre isCorner() == true
    * @post state = oldState
    * @return The upper left coordinate of the next domino to be inserted according to the direction.
    */
   abstract public Coordinate getAvailableCoordinate(Direction direction,Domino other);

   /** It returns the direction in relation to the corner where a domino can be played.
    * @param other
    * @pre isCorner() == true
    * @post state = oldState
    * @return The direction in relation to the corner where a domino can be played.
    */
   abstract public Direction getAvailableDirection(Domino other);

   /**
    * It updates the direction and shift needs according to the current state of the board.
    * @pre true
    * @post updates the direction if blocked to the next non-blocked direction.
    */
   abstract public void updateDirections();

   /** It returns true if the corner's domino and the other domino are equal.
    * @param other
    * @pre true
    * @post state = oldState
    * @return domino.equals(other)
    */
   public boolean isEqual(Domino other) {return domino.equals(other);}

   /** Returns true if the domino can be played on the current corner, false otherwise.
    * @param other
    * @pre isCorner == true
    * @post state = oldState
    * @return true if the domino can be played on the current corner, false otherwise.
    */
   abstract public boolean canPlay(Domino other);

   /** returns true if to connect a domino, the board must shift, false otherwise.
    * @param other
    * @pre isCorner == true
    * @post state = oldState
    * @return true if to connect a domino, the board must shift, false otherwise.
    */
   abstract public boolean isShiftNeeded(Domino other);

   /** returns the number of shifts the board must do(in order to connect the next domino) in the direction correspondent to the directions
    * returned by getShiftDirection, with the indexes matching. ex: int[0] is the amount of Direction[0] and so on.
    * @param other
    * @pre isCorner == true && canPlay == true && isShiftNeeded == true
    * @post state = oldState
    * @return the number of shift the board must do in the direction correspondent to the directions returned by
    * getShiftDirection with the indexes matching.
    */
   abstract public int[] getShiftNTimes(Domino other);

   /** It returns the direction the board must shift in order to connect the next domino.
    * @param other
    * @pre isCorner == true && canPlay == true && isShiftNeeded == true
    * @post state = oldState
    * @return The direction the board must shift in order to connect the next domino.
    */
   abstract public Direction[] getShiftDirection(Domino other);

   /** It changes the coordinates of the corner accordingly to the amount of shift up.
    * @param n
    * @pre isCorner == true && canPlay == true && isShiftNeeded == true
    * @post updates the coordinates to match the amount of shift up.(presumably to match the changes on the board).
    */
   public void shiftUp(int n){coordinate = new Coordinate(coordinate.x(),coordinate.y() + n);}

   /** It changes the coordinates of the corner accordingly to the amount of shift down.
    * @param n
    * @pre isCorner == true && canPlay == true && isShiftNeeded == true
    * @post updates the coordinates to match the amount of shift down.(presumably to match the changes on the board).
    */
   public void shiftDown(int n){coordinate = new Coordinate(coordinate.x(),coordinate.y() - n);}

   /** It changes the coordinates of the corner accordingly to the amount of shift right.
    * @param n
    * @pre isCorner == true && canPlay == true && isShiftNeeded == true
    * @post updates the coordinates to match the amount of shift right.(presumably to match the changes on the board).
    */
   public void shiftRight(int n){coordinate = new Coordinate(coordinate.x()+n,coordinate.y());}

   /** It changes the coordinates of the corner accordingly to the amount of shift left.
    * @param n
    * @pre isCorner == true && canPlay == true && isShiftNeeded == true
    * @post updates the coordinates to match the amount of shift left.(presumably to match the changes on the board).
    */
   public void shiftLeft(int n){coordinate = new Coordinate(coordinate.x()-n,coordinate.y());}

  //note: Probably I need to explain why do I use up left corner when the domino can only be placed vertically or
  //horizontally which means that y will be the same or x will be same therefore either the vertical direction or the
   //horizontal direction is redundant on the explanation, but this is good to generalize for both cases.

   //this 4 functions give you the y coordinate from the x coordinate, because x will always be placed in the up left
   //on the board, then the y will always be placed on the down right of the board.

   //this might be going elsewhere because of design patterns ,so I will abstain from doing the javaDoc for now.
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
