import java.util.*;

public class GameBoard {
   final private int height;
   final private int width;
   private Graph<Domino> board;
   private LinkedList<Domino> corners;

   //height will be changed to 2/3 or something, I still need to initiate the graph and corner, but I will do that later.
   public GameBoard(int height,int width, Domino firstDomino,int x,int y){ this.height = height; this.width = width; }
   public void insertDomino(Domino dominoPlayed,Domino corner){};
   public Iterable<Domino> getCorners(){return corners; };
   private void updateCorner(Domino corner){};
   public void print(){};

   class Graph<T> {  }
}
