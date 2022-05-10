import java.util.*;

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


   //given the left up corner, the direction and the domino it inserts the domino into the board
   public void insertDomino(int x,int y,Domino domino) {
      if(domino.isVertical()) { //vertical
         if(x < 0 || y < 2 || x >= nColumns || y >= nLines)
            throw new IllegalArgumentException("It's outside the board");
         if(board[y].contains(x) || board[y-1].contains(x) || board[y-2].contains(x)) throw new IllegalArgumentException("Cannot place a domino on top of another domino.");
         board[y].put(x,domino);
         board[y-1].put(x,domino);
         board[y-2].put(x,domino);
      } else { //horizontal
         if(x >= nColumns -1) throw new IllegalArgumentException("It's outside the board");
         if(board[y].contains(x) || board[y].contains(x+1)) throw new IllegalArgumentException("Cannot place a domino on top of another domino.");
         board[y].put(x, domino);
         board[y].put(x+1,domino);
         if(domino.isDouble()) {
            if(board[y].contains(x+2)) throw new IllegalArgumentException("Cannot place a domino on top of another domino.");
            if(x >= nColumns -2) {
               board[y].remove(x); board[y].remove(x+1); //avoids side effect if the client wants to catch the exception.
               throw new IllegalArgumentException("It's outside the board");
            }
            board[y].put(x + 2, domino);
         }
      }
   }



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

   public void printDominoSegment(int segment,Domino Domino){
      if(segment == 0) System.out.print(Domino.getX());
      else if(segment == 1 && (Domino.isDouble() || Domino.isVertical())) System.out.print("-");
      else System.out.print(Domino.getY());
   }

   public int getColumns(){ return nColumns;}
   public int getLines(){ return nLines;}

   public HashSet<Domino> getDominoesInThisRectangle(int x1,int y1,int x2,int y2){
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

   //x1,y1 bottom left corner and x2,y2 upper left corner
   private boolean isThisRectangleOccupied(int x1,int y1,int x2,int y2){
      return getNDominoesOnThisRectangle(x1,y1,x2,y2) != 0;
   }

   public int getNDominoesOnThisRectangle(int x1,int y1,int x2,int y2){
      return getDominoesInThisRectangle(x1,y1,x2,y2).size();
   }

   public void shiftUp(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative up shifting");
      for(int i = nLines -x-1; i >= 0; i--)
         board[i+x] = board[i];

      for(int i = x-1; i >= 0; i--){
         board[i] = new HashTable();
      }
   }

   public void shiftDown(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative down shifting");
      for(int i = x; i < nLines; i++)
         board[i-x] = board[i];

      for(int i = nLines -x; i < nLines; i++){
         board[i] = new HashTable();
      }
   }

   //right is positive left is negative.
   private void verticalShift(int x){
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

   public void shiftRight(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative right shifting");
      verticalShift(x);
   }


   public void shiftLeft(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative left shifting");
      verticalShift(-x);
   }

   public boolean canShiftDown(int n){
      if(n == 0) return true;
      if(n < 0 ) throw new IllegalArgumentException("n must be a natural integer(0 included)");
      return !isThisRectangleOccupied(0,0,nColumns -1,n-1);
   }

   public boolean canShiftUp(int n){
      if(n == 0) return true;
      if(n <0 ) throw new IllegalArgumentException("n must be a natural integer(0 included)");
      return !isThisRectangleOccupied(0,nLines-n,nColumns -1,nLines-1);
   }

   public boolean canShiftRight(int n){
      if(n == 0) return true;
      if(n <0 ) throw new IllegalArgumentException("n must be a natural integer(0 included)");
      return !isThisRectangleOccupied(nColumns-n,0, nColumns-1, nLines-1 );
   }

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

