import java.util.*;

/**
 * @inv nLines*nColumns >= 100
 * @inv pieces must not overlap on the board.
 */
public class GameBoard {
   final private int nColumns;
   final private int nLines;
   final private HashTable[] board;

   public static void main(String[] args){
      //there mustn't be any changes to these dominoes during the tests.
      Domino domino00 = new Domino(0,0);
      Domino domino01 = new Domino(0,1);
      Domino domino02 = new Domino(0,2);
      Domino domino03 = new Domino(0,3);
      Domino domino04 = new Domino(0,4);
      Domino domino05 = new Domino(0,5);
      Domino domino06 = new Domino(0,6);
      Domino domino11 = new Domino(1,1);
      Domino domino12 = new Domino(1,2);
      Domino domino13 = new Domino(1,3);
      Domino domino14 = new Domino(1,4);
      Domino domino15 = new Domino(1,5);
      Domino domino16 = new Domino(1,6);
      Domino domino22 = new Domino(2,2);
      Domino domino23 = new Domino(2,3);
      Domino domino24 = new Domino(2,4);
      Domino domino25 = new Domino(2,5);
      Domino domino26 = new Domino(2,6);
      Domino domino33 = new Domino(3,3);
      Domino domino34 = new Domino(3,4);
      Domino domino35 = new Domino(3,5);
      Domino domino36 = new Domino(3,6);
      Domino domino44 = new Domino(4,4);
      Domino domino45 = new Domino(4,5);
      Domino domino46 = new Domino(4,6);
      Domino domino55 = new Domino(5,5);
      Domino domino56 = new Domino(5,6);
      Domino domino66 = new Domino(6,6);

      var gameBoard = new GameBoard(15,40);
      var game = new GameLine(gameBoard);
      game.firstPlay(domino66,7,20);
      gameBoard.print();
      game.insertDomino(domino56,domino66);
      gameBoard.print();
      game.insertDomino(domino55,domino56);
      gameBoard.print();
      game.insertDomino(domino25,domino55);
      gameBoard.print();
      game.insertDomino(domino45,domino55);
      gameBoard.print();
      game.insertDomino(domino46,domino66);
      gameBoard.print();
      game.insertDomino(domino36,domino66);
      gameBoard.print();
      game.insertDomino(domino44,domino45);
      gameBoard.print();
      game.insertDomino(domino26,domino25);
      gameBoard.print();
      game.insertDomino(domino24,domino44);
      gameBoard.print();
   }

   public GameBoard(int nColumns, int nLines) {
      if(nColumns * nLines < 100) throw new IllegalArgumentException("The board is to small to play the game");
      this.nColumns = nColumns;
      this.nLines = nLines;
      board = new HashTable[nLines];
      for(int i = 0; i < nLines; i++)
         board[i] = new HashTable();
   }

   /** Given the up left coordinates of the domino, and the domino it inserts the domino
    * on the corresponding tiles on the board.
    * @param x
    * @param y
    * @param domino
    * @pre the domino can't be placed outside the board,therefore the coordinates must be within the
    * boarder of the board taking in account the features of the domino to be placed.
    * @post inserts the domino on the board.
    */
   public void insertDomino(int x,int y,Domino domino) {
      if(domino.isVertical()) { //vertical
         if(x < 0 || y < 2 || x >= nColumns || y >= nLines)
            throw new IllegalArgumentException("It's outside the board");
         if(board[y].contains(x) || board[y-1].contains(x) || board[y-2].contains(x)) throw new IllegalArgumentException("Cannot place a domino on top of another domino." + board[y].get(x)+ board[y+1].get(x)+ board[y+2].get(x));
         board[y].put(x,domino);
         board[y-1].put(x,domino);
         board[y-2].put(x,domino);
      } else { //horizontal
         if(x >= nColumns -1) throw new IllegalArgumentException("It's outside the board");
         if(board[y].contains(x) || board[y].contains(x+1)) throw new IllegalArgumentException("Cannot place a domino on top of another domino." + board[y].get(x) + board[y].get(x+1));
         board[y].put(x, domino);
         board[y].put(x+1,domino);
         if(domino.isDouble()) {
            if(board[y].contains(x+2)) throw new IllegalArgumentException("Cannot place a domino on top of another domino." + board[y].get(x+2));
            if(x >= nColumns -2) {
               board[y].remove(x); board[y].remove(x+1); //avoids side effect if the client wants to catch the exception.
               throw new IllegalArgumentException("It's outside the board");
            }
            board[y].put(x + 2, domino);
         }
      }
   }

   /** Prints the board.
    * @pre true
    * @post prints the board
    */
   public void print() {
      System.out.println("#".repeat(nColumns+2));
      HashMap<Domino,Integer> segments = new HashMap<>();
      int prev;// contains the amount of white space previously used

      for(int i = nLines -1; i >= 0 ; i--){
         if(board[i].isEmpty()) System.out.println("#" + " ".repeat(nColumns) + "#");
         else{
            prev = 0;
            var orderedCoordinates = board[i].getKeys();
            Arrays.sort(orderedCoordinates); //sorts array
            System.out.print("#");
            for(int x :orderedCoordinates) {
               System.out.print(" ".repeat(x - prev));
               var domino = board[i].get(x);
               if(!segments.containsKey(domino)) segments.put(domino,0);
               else segments.put(domino,segments.get(domino)+1);
               printDominoSegment(segments.get(domino),domino);
               prev = x + 1; // +1 from the printDominoSegment
            }
            System.out.println(" ".repeat(nColumns - prev) + "#");
         }
      }
      System.out.println("#".repeat(nColumns+2));
   }

   /** It prints the corresponding segment of the domino, 0 for the upper left side of the domino
    *  and 1 for the bottom left side of the domino, if the domino is vertical or double
    *  than 1 becomes the middle piece of the domino aka "-", and the 2 the bottom right part of the domino.
     * @param segment
    * @param domino
    * @pre 0 <= segment && segment < 3 && domino != null
    * @post prints the corresponding segment of the domino.
    */
   private void printDominoSegment(int segment,Domino domino){
      if(segment == 0) System.out.print(domino.getX());
      else if(segment == 1 && (domino.isDouble() || domino.isVertical())) System.out.print("-");
      else System.out.print(domino.getY());
   }

   /** It returns the number of columns from the gameBoard.
    * @pre true
    * @return the number of columns
    */
   public int getColumns(){ return nColumns;}

   /**It returns the number of lines from the gameBoard.
    * @pre true
    * @return the number of lines
    */
   public int getLines(){ return nLines;}

   /** It returns the dominoes inside the piece of rectangle inside the board.
    * @param x1
    * @param y1
    * @param x2
    * @param y2
    * @pre (x1,y1) must be the bottom left corner of the rectangle and (x2,y2) the upper right corner.
    * @post true
    * @return a hashSet with the dominoes present inside the rectangle.
    */
   private HashSet<Domino> getDominoesInThisRectangle(int x1,int y1,int x2,int y2){
      var bX = Math.max(0,x1); //(bottomX)
      var bY = Math.max(0,y1);
      var uX = Math.min(nColumns-1,x2); //(upperX)
      var uY = Math.min(nLines-1,y2);

      HashSet<Domino> dominoesInside = new HashSet<>();

      for(int i = bY; i <= uY ; i++){
         if(board[i].isEmpty()) continue;
         for(var key :board[i].getKeys()){
            if(bX <= key && key <= uX )
               dominoesInside.add(Domino.equalsClone(board[i].get(key)));
         }
      }
      return dominoesInside;
   }

   /** Returns true if there are dominoes inside the rectangle, false otherwise.
    * @param x1
    * @param y1
    * @param x2
    * @param y2
    * @pre (x1,y1) must be the bottom left corner of the rectangle and (x2,y2) the upper right corner.
    * @post true
    * @return true if the rectangle has dominoes, false otherwise.
    */
   private boolean isThisRectangleOccupied(int x1,int y1,int x2,int y2){
      return getNDominoesOnThisRectangle(x1,y1,x2,y2) != 0;
   }

   /** Returns the number of dominoes present inside the rectangle.
    * @param x1
    * @param y1
    * @param x2
    * @param y2
    * @pre (x1,y1) must be the bottom left corner of the rectangle and (x2,y2) the upper right corner.
    * @post true
    * @return the number of dominoes on the rectangle.
    */
   public int getNDominoesOnThisRectangle(int x1,int y1,int x2,int y2){
      return getDominoesInThisRectangle(x1,y1,x2,y2).size();
   }

   /** Shifts the board up x times.
    * @param x
    * @pre canShiftUp(x) == true && x > 0
    * @post The board must have shifted up x times.
    */
   public void shiftUp(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative up shifting");
      for(int i = nLines -x-1; i >= 0; i--)
         board[i+x] = board[i];

      for(int i = x-1; i >= 0; i--){
         board[i] = new HashTable();
      }
   }

   /** Shifts the board Down x times.
    * @param x
    * @pre canShiftDown(x) == true && x > 0
    * @post The board must have shifted down x times.
    */
   public void shiftDown(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative down shifting");
      for(int i = x; i < nLines; i++)
         board[i-x] = board[i];

      for(int i = nLines -x; i < nLines; i++){
         board[i] = new HashTable();
      }
   }

   //it shifts the board right if x is positive or left if x is negative.
   private void horizontalShift(int x){
      for(int i = 0; i < nLines ; i++) {
         if(board[i].isEmpty()) continue;
         var keys = board[i].getKeys();
         var values = board[i].getValues();
         int N = keys.length;
         board[i] = new HashTable();
         for(int j = 0; j < N; j++)
            board[i].put((keys[j] + x)%nColumns, values[j]);
      }
   }

   /** Shifts the board right x times.
    * @param x
    * @pre canShiftRight(x) == true && x > 0
    * @post The board must have shifted right x times.
    */
   public void shiftRight(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative right shifting");
      horizontalShift(x);
   }


   /** Shifts the board Down x times.
    * @param x
    * @pre canShiftDown(x) == true && x > 0
    * @post The board must have shifted left x times.
    */
   public void shiftLeft(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative left shifting");
      horizontalShift(-x);
   }

   /** If the board has enough empty space to shift down n lines it returns true, false otherwise.
    * @param n
    * @pre n >= 0
    * @post true
    * @return true if the board has enough space to shift down, false otherwise.
    */
   public boolean canShiftDown(int n){
      if(n == 0) return true;
      if(n < 0 ) throw new IllegalArgumentException("n must be a natural integer(0 included)");
      return !isThisRectangleOccupied(0,0,nColumns -1,n-1);
   }

   /** If the board has enough empty space to shift up n lines it returns true, false otherwise.
    * @param n
    * @pre n >= 0
    * @post true
    * @return true if the board has enough space to shift up, false otherwise.
    */
   public boolean canShiftUp(int n){
      if(n == 0) return true;
      if(n <0 ) throw new IllegalArgumentException("n must be a natural integer(0 included)");
      return !isThisRectangleOccupied(0,nLines-n,nColumns -1,nLines-1);
   }

   /** If the board has enough empty space to shift right n lines it returns true, false otherwise.
    * @param n
    * @pre n >= 0
    * @post true
    * @return true if the board has enough space to shift right, false otherwise.
    */
   public boolean canShiftRight(int n){
      if(n == 0) return true;
      if(n <0 ) throw new IllegalArgumentException("n must be a natural integer(0 included)");
      return !isThisRectangleOccupied(nColumns-n,0, nColumns-1, nLines-1 );
   }

   /** If the board has enough empty space to shift left n lines it returns true, false otherwise.
    * @param n
    * @pre n >= 0
    * @post true
    * @return true if the board has enough space to shift left, false otherwise.
    */
   public boolean canShiftLeft(int n){
      if(n == 0) return true;
      if(n < 0 ) throw new IllegalArgumentException("n must be a natural integer(0 included)");
      return !isThisRectangleOccupied(0,0,n-1,nLines-1);
   }

   //workaround to get an array of hashTables because in java you can't have an array of generics.
   static private class HashTable{
      public Hashtable<Integer,Domino> hashtable = new Hashtable<>();

      public void put(int x,Domino domino){hashtable.put(x,domino);}
      public Domino get(int x){return hashtable.get(x);}
      public boolean isEmpty(){return hashtable.isEmpty();}
      public Integer[] getKeys(){ return hashtable.keySet().toArray(Integer[]::new) ; }
      public Domino[] getValues(){return hashtable.values().toArray(Domino[]::new);}
      public boolean contains(int x){return hashtable.containsKey(x);}
      public void remove(int x){hashtable.remove(x);}
   }
}

