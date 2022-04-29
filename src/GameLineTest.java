import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

class GameLineTest {

   @Test
   void canPlayTest() {
      //setup
      GameBoard board = new GameBoard(50,50);
      GameLine gameLine = new GameLine(board);

      //cannot check without any corners
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.canPlay(new Domino(5,4),new Domino(6,6)));

      gameLine.firstPlay(new Domino(6,6),25,25);

      //cannot check non-existent corners
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.canPlay(new Domino(5,4),new Domino(6,2)));

      //It breaks class invariant there cannot be any duplicate pieces.
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.canPlay(new Domino(6,6),new Domino(6,6)));

      assertTrue(gameLine.canPlay(new Domino(6,2),new Domino(6,6)));
      assertFalse(gameLine.canPlay(new Domino(4,5),new Domino(6,6)));
      assertFalse(gameLine.canPlay(new Domino(5,5),new Domino(6,6)));

      gameLine.insertDomino(new Domino(6,2),new Domino(6,6));

      assertTrue(gameLine.canPlay(new Domino(2,2),new Domino(6,2)));
      //It breaks class invariant there cannot be any duplicate pieces.
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.canPlay(new Domino(6,2),new Domino(6,6)));
      assertFalse(gameLine.canPlay(new Domino(1,3),new Domino(6,2)));
      assertFalse(gameLine.canPlay(new Domino(5,5),new Domino(6,6)));

      gameLine.insertDomino(new Domino(2,2),new Domino(6,2));

      assertThrows(IllegalArgumentException.class, () ->
              gameLine.canPlay(new Domino(2,1),new Domino(6,2)));

      assertTrue(gameLine.canPlay(new Domino(2,1),new Domino(2,2)));
      assertFalse(gameLine.canPlay(new Domino(3,1),new Domino(2,2)));

      //saturates the corner 6,6
      gameLine.insertDomino(new Domino(6,1),new Domino(6,6));
      gameLine.insertDomino(new Domino(6,3),new Domino(6,6));
      gameLine.insertDomino(new Domino(6,4),new Domino(6,6));

      //this corner stops existing as it's saturated.
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.canPlay(new Domino(6,5),new Domino(6,6)));
   }

   @Test
   void insertDominoTest() {
      //setup
      GameBoard board = new GameBoard(50,50);
      GameLine gameLine = new GameLine(board);

      gameLine.firstPlay(new Domino(6,6),25,25);

      //cannot insert a domino that cannot be played.
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.insertDomino(new Domino(5,4),new Domino(6,6)));

      //cannot insert a domino on a non-existent corner.
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.insertDomino(new Domino(2,3),new Domino(2,2)));

      assertDoesNotThrow(() -> gameLine.insertDomino(new Domino(6,2),new Domino(6,6)));
      assertDoesNotThrow(() -> gameLine.insertDomino(new Domino(2,2),new Domino(6,2)));

      //6,2 should not exist anymore
      assertThrows(IllegalArgumentException.class, () ->
              gameLine.insertDomino(new Domino(2,2),new Domino(6,2)));

      //can't repeat dominoes
      assertThrows(IllegalArgumentException.class,() ->
              gameLine.insertDomino(new Domino(6,2),new Domino(6,6)));

      //saturated 6,6
      assertDoesNotThrow(() -> gameLine.insertDomino(new Domino(6,1),new Domino(6,6)));
      assertDoesNotThrow(() -> gameLine.insertDomino(new Domino(6,3),new Domino(6,6)));
      assertDoesNotThrow(() -> gameLine.insertDomino(new Domino(6,4),new Domino(6,6)));

      //6,6 should not exist anymore
      assertThrows(IllegalArgumentException.class, () ->
              gameLine.insertDomino(new Domino(6,5),new Domino(6,6)));

      assertDoesNotThrow(() -> gameLine.insertDomino(new Domino(2,1),new Domino(2,2)));
   }

   @Test
   void getCornersTest() {
      var dominoLinkedList = new LinkedList<Domino>();
      //setup
      GameBoard board = new GameBoard(50,50);
      GameLine gameLine = new GameLine(board);

      assertFalse(gameLine.getCorners().iterator().hasNext());

      gameLine.firstPlay(new Domino(6,6),25,25);
      dominoLinkedList.add(new Domino(6,6));
      assertTrue(() -> {
                 for(var corner : gameLine.getCorners())
                    if(dominoLinkedList.stream().noneMatch(x -> x.isEqual(corner)))
                       return false;
                 return true;
              }
      );

      gameLine.insertDomino(new Domino(6,2),new Domino(6,6));
      dominoLinkedList.add(new Domino(6,2));

      assertTrue(() -> {
                 for(var corner : gameLine.getCorners())
                    if(dominoLinkedList.stream().noneMatch(x -> x.isEqual(corner)))
                       return false;
                 return true;
              }
      );

      gameLine.insertDomino(new Domino(2,2),new Domino(6,2));
      dominoLinkedList.add(new Domino(2,2));
      dominoLinkedList.removeIf(x -> x.isEqual(new Domino(6,2)));

      assertTrue(() -> {
                 for(var corner : gameLine.getCorners())
                    if(dominoLinkedList.stream().noneMatch(x -> x.isEqual(corner)))
                       return false;
                 return true;
              }
      );

      gameLine.insertDomino(new Domino(6,1),new Domino(6,6));
      gameLine.insertDomino(new Domino(6,3),new Domino(6,6));
      gameLine.insertDomino(new Domino(6,4),new Domino(6,6));
      dominoLinkedList.add(new Domino(6,1));
      dominoLinkedList.add(new Domino(6,3));
      dominoLinkedList.add(new Domino(6,4));
      dominoLinkedList.removeIf(x -> x.isEqual(new Domino(6,6)));

      assertTrue(() -> {
                 for(var corner : gameLine.getCorners())
                    if(dominoLinkedList.stream().noneMatch(x -> x.isEqual(corner)))
                       return false;
                 return true;
              }
      );
   }
}