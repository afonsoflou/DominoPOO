import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NPCTest {
   GameBoard board = new GameBoard(50,50);
   GameLine gameLine = new GameLine(board);

   LinkedList<Domino> dominoList2 = new LinkedList<>();
   {
      dominoList2.add(new Domino(1, 1));
      dominoList2.add(new Domino(1, 2));
      dominoList2.add(new Domino(0, 1));
      dominoList2.add(new Domino(4, 3));
      dominoList2.add(new Domino(5, 2));
      dominoList2.add(new Domino(4,4));
      dominoList2.add(new Domino(3, 3));
   }

   @Test
   void play() throws IOException {
      NPC npc = new NPC(gameLine,"ncp 1",dominoList2,board);
      gameLine.firstPlay(new Domino(6,6),25,25);
      gameLine.insertDomino(new Domino(6,5),new Domino(6,6));
      npc.play();
      assertTrue(() -> {
         var corners = gameLine.getCorners();
         for(var corner: corners)
            if(corner.isEqual(new Domino(5,2)))
               return true;
         return false;
      });

      npc.play();
      assertTrue(() -> {
         var corners = gameLine.getCorners();
         for(var corner: corners)
            if(corner.isEqual(new Domino(2,1)))
               return true;
         return false;
      });

      assertFalse(() -> {
         var corners = gameLine.getCorners();
         for(var corner: corners)
            if(corner.isEqual(new Domino(5,2)))
               return true;
         return false;
      });

   }
}