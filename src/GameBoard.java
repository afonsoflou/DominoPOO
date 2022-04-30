import java.util.*;

public class GameBoard {
   final private int nColumns;
   final private int nLines;
   final private HashTable[] board;

   public static void main(String[] args){
      var domino66 = new Domino(6,6);
      var domino61 = new Domino(6,1);
      var domino62 = new Domino(6,2);
      var domino63 = new Domino(6,3);
      var domino64 = new Domino(6,4);
      var domino11 = new Domino(1,1);
      var domino55 = new Domino(5,5);
      var domino44 = new Domino(4,4);
      var domino32 = new Domino(3,2);
      var domino34 = new Domino(3,4);
      var domino45 = new Domino(4,5);
      var domino12 = new Domino(1,2);
      var domino13 = new Domino(1,3);
      var domino24 = new Domino(2,4);
      var domino15 = new Domino(1,5);

/*

      var gameBoard = new GameBoard(80,11);
      var game = new GameLine(domino66,16,6,gameBoard);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino61,domino66);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino11,domino61);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino62,domino66);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino63,domino66);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino64,domino66);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino32,domino63);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino12,domino11);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino13,domino11);
      gameBoard.print();
      System.out.println(game.getCorners());

      //THIS is actually a great setup to test collision.
      // inserting on line 3/1 and 2/6, inserting on line 1/4 and seeing how intersection 1/1 will be blocked. (to test)
      game.insertDomino(domino34,domino13);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino45,domino34);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino24,domino62);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino15,domino11);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino55,domino45);
      gameBoard.print();
      System.out.println(game.getCorners());

      System.out.println(gameBoard.isThisRectangleOccupied(0,0,10,4));
      //very happy with this function is not as obvious as it appears to arrive to a data structure and an algorithm that checks for
      //occupied places efficiently while having a good print time.

      //6|6, 4|6, 2|6, 2|4, 2|3, 5|6, 3|3 , 3|6, 3|5, 3|1, 1|5, 0|1, 0|3,*/

/////////////////////
      var gameBoard = new GameBoard(40,9);
      var game = new GameLine(gameBoard);
      game.firstPlay(domino66,16,6);
      System.out.println(domino66.getUnconnected());
      gameBoard.print();
      System.out.println(game.getCorners());
      System.out.println(domino61.getUnconnected());
      game.insertDomino(domino61,domino66);
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino12,domino61);
      System.out.println(domino12.getUnconnected());
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino24,domino12);
      System.out.println(domino24.getUnconnected());
      gameBoard.print();
      System.out.println(game.getCorners());
      game.insertDomino(domino44,domino24);
      System.out.println(domino44.getUnconnected());
      gameBoard.print();
      gameBoard.shiftUp(2);
      gameBoard.print();
      gameBoard.shiftDown(1);
      gameBoard.print();
      gameBoard.shiftRight(2);
      gameBoard.print();
      gameBoard.shiftLeft(2);
      gameBoard.print();

   }

   public GameBoard(int nColumns, int nLines) {
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
         board[y].put(x,domino);
         board[y-1].put(x,domino);
         board[y-2].put(x,domino);
      } else { //horizontal
         if(x >= nColumns -1) throw new IllegalArgumentException("It's outside the board");
         board[y].put(x, domino);
         board[y].put(x+1,domino);
         if(domino.isDouble()) {
            if(x >= nColumns -2) throw new IllegalArgumentException("It's outside the board");
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
               dominoesInside.add(board[i].get(key));
         }
      }
      return dominoesInside;
   }

   //x1,y1 bottom left corner and x2,y2 upper left corner
   public boolean isThisRectangleOccupied(int x1,int y1,int x2,int y2){
      return getNDominoesOnThisRectangle(x1,y1,x2,y2) != 0;
   }

   public int getNDominoesOnThisRectangle(int x1,int y1,int x2,int y2){
      return getDominoesInThisRectangle(x1,y1,x2,y2).size();
   }

   public void shiftUp(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative shifting");
      for(int i = nLines -1; i >= 0 ; i--){
         swapColumns((i+x)%nLines,i);
      }
   }

   public void shiftDown(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative shifting");
      for(int i = 0; i < nLines; i++){
         swapColumns((i+x)%nLines,i);
      }
   }

   public void shiftRight(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative shifting");
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


   public void shiftLeft(int x){
      if(x < 0) throw new IllegalArgumentException("There cannot be negative shifting");
      for(int i = 0; i < nLines ; i++) {
         if(board[i].isEmpty()) continue;
         var keys = board[i].getKeys();
         var values = board[i].getValues();
         int N = keys.length;
         board[i] = new HashTable();
         for(int j = 0; j < N; j++)
            board[i].put((keys[j] - x)%nColumns, values[j]);
      }
   }


   private void swapColumns(int x,int y){
      HashTable temp;
      temp = board[x];
      board[x] = board[y];
      board[y] = temp;
   }

   //workaround to get an array of hashTables because in java you can't have an array of generics.
   static private class HashTable{
      public Hashtable<Integer,Domino> hashtable = new Hashtable<>();

      public void put(int x,Domino domino){hashtable.put(x,domino);}
      public Domino get(int x){return hashtable.get(x);}
      public boolean isEmpty(){return hashtable.isEmpty();}
      public Integer[] getKeys(){ return hashtable.keySet().toArray(Integer[]::new) ; }
      public Domino[] getValues(){return hashtable.values().toArray(Domino[]::new);}
   }
}

