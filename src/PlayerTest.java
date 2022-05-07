import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
   GameBoard board = new GameBoard(50,50);
   GameLine gameLine = new GameLine(board);
   List<Domino> dominoList1 = new LinkedList<>();
   {
      dominoList1.add(new Domino(1, 1));
      dominoList1.add(new Domino(1, 2));
      dominoList1.add(new Domino(0, 1));
      dominoList1.add(new Domino(4, 3));
      dominoList1.add(new Domino(5, 2));
      dominoList1.add(new Domino(6,4));
      dominoList1.add(new Domino(6, 6));
   }

   Player player1 = new Player(gameLine, "Bob",dominoList1, board) {
      @Override
      public void play() {}
   };

   List<Domino> dominoList2 = new LinkedList<>();
   {
      dominoList2.add(new Domino(1, 1));
      dominoList2.add(new Domino(1, 2));
      dominoList2.add(new Domino(0, 1));
      dominoList2.add(new Domino(4, 3));
      dominoList2.add(new Domino(5, 2));
      dominoList2.add(new Domino(4,4));
      dominoList2.add(new Domino(3, 3));
   }


   Player player2 = new Player(gameLine,"Ana",dominoList2, board) {
      @Override
      public void play() {}
   };

   Player player3 = new Player(gameLine,"Marco",dominoList2, board) {
      @Override public void play() {dominoes = new LinkedList<>();} // empties the dominoes
   };
   {player3.play();} //doesn't break the class invariant and let's marco have an empty hand.

   @Test
   void getName() {
      assertEquals("Bob",(player1.getName()));
      assertEquals("Ana",player2.getName());
      assertNotEquals("Bobs",player1.getName());
      assertNotEquals("",player2.getName());
   }

   @Test
   void isFirst() {
      assertTrue(player1.isFirst());
      assertFalse(player2.isFirst());
      assertFalse(player3.isFirst());
   }

   @Test
   void canPlay() {
      gameLine.firstPlay(new Domino(6,6),25,25);
      assertFalse(player2.canPlay());
      assertTrue(player1.canPlay());
      gameLine.insertDomino(new Domino(6,5),new Domino(6,6));
      assertTrue(player1.canPlay());
      assertTrue(player2.canPlay());
      assertFalse(player3.canPlay());
   }

   @Test
   void getPoints() {
      assertEquals(42,player1.getPoints());
      assertEquals(34,player2.getPoints());
      assertEquals(0,player3.getPoints());
   }

   @Test
   void isWinner() {
      assertFalse(player1.isWinner());
      assertFalse(player2.isWinner());
      assertTrue(player3.isWinner());
   }

   @Test
   @SuppressWarnings("all")
   void classInvariantTest(){

      //testing if a Player initialized with less than 7 elements throws an exception.
      assertThrows(IllegalArgumentException.class,() ->
              new Player(gameLine, "Bob1",new LinkedList<Domino>(), board) {
                 @Override
                 public void play() {}
              });

      assertThrows(IllegalArgumentException.class,() ->
              new Player(gameLine,"bob",null, board){
                 @Override
                 public void play(){}
              });

      assertDoesNotThrow(() ->
              new Player(gameLine, "Bob1", dominoList2, board) {
                 @Override
                 public void play() {}
              });
   }

}