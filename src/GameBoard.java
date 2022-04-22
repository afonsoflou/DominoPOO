import java.util.*;

public class GameBoard {
   final private int nColumns;
   final private int nLines;
   private RedBlackTree<CoordinateOfDominoSegment,Domino> board;
   private Hashtable<Domino,CoordinateOfDominoSegment> corners;

   public GameBoard(int nColumns,int nLines, Domino firstDomino,int x,int y){
      this.nColumns = nColumns -2; this.nLines = nLines -2;
      var board = new RedBlackTree<CoordinateOfDominoSegment,Domino>();
      var corner = new Hashtable<Domino,CoordinateOfDominoSegment>();


   }
   public void insertDomino(Domino dominoPlayed,Domino corner){};
   public Iterable<Domino> getCorners(){return corners.keySet(); };
   private void updateCorner(Domino corner){};
   public void print(){};


   //It has the coordinates to a corresponding segment of the domino.
   private record CoordinateOfDominoSegment(int x, int y,int segment) implements Comparable<CoordinateOfDominoSegment>{
      public int compareTo(CoordinateOfDominoSegment other) {
         int result = y - other.y;
         if(result == 0)
            result = x - other.x;
         return result;
      }
   }

}
