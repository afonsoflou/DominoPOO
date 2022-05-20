import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DominoTests{

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

    @Test
    public void constructorTest(){
        assertEquals(new Domino(1,2).getX(),1);
        assertEquals(new Domino(1,2).getY(),2);
        assertEquals(new Domino(3,4).getX(),3);
        assertEquals(new Domino(3,4).getY(),4);
    }

    @Test
    public void classInvariantTest(){
        assertThrows(IllegalArgumentException.class,() -> new Domino(7,6));
        assertThrows(IllegalArgumentException.class,() -> new Domino(6,7));
        assertDoesNotThrow(() -> new Domino(1,2));
        assertThrows(IllegalArgumentException.class,() -> new Domino(-1,-1));
    }

    @Test
    public void valueTest(){
        assertEquals(new Domino(1,2).getValue(),3);
        assertEquals(new Domino(3,2).getValue(),5);
        assertEquals(new Domino(5,6).getValue(),11);
    }

    @Test
    public void isDoubleTest(){
        assertTrue(new Domino(2, 2).isDouble());
        assertFalse(new Domino(1, 2).isDouble());
        assertTrue(new Domino(3, 3).isDouble());
        assertFalse(new Domino(1, 3).isDouble());
    }

    @Test
    public void isEqualTest(){
        assertEquals(new Domino(1, 1), domino11);
        assertNotEquals(new Domino(1, 2), domino11);
        assertEquals(new Domino(3, 1), domino13);
    }

    @Test
    public void isStarterTest(){
        assertTrue(new Domino(6,6).isStarter());
        assertFalse(new Domino(5,6).isStarter());
        assertFalse(new Domino(4,6).isStarter());
        assertFalse(new Domino(3,1).isStarter());
    }

    @Test
    public void isVerticalTests(){
        domino00.beVertical();
        domino01.beVertical();
        domino02.beHorizontal();
        domino22.beHorizontal();
        assertTrue(domino00.isVertical());
        assertTrue(domino01.isVertical());
        assertFalse(domino02.isVertical());
        assertFalse(domino22.isVertical());
        domino00.beHorizontal(); //restore the horizontal
        domino01.beHorizontal(); //restore the horizontal
    }

    @Test
    public void canConnectTests(){
        assertTrue(domino00.canConnect(domino01));
        assertTrue(domino01.canConnect(domino00));
        assertFalse(domino22.canConnect(domino33));
        assertFalse(domino12.canConnect(domino34));
    }

    @Test
    public void isStarterTests(){
        assertTrue(domino66.isStarter());
        assertFalse(domino00.isStarter());
        assertFalse(domino01.isStarter());
    }

    @Test
    public void flipTests(){
        domino01.flip();
        assertTrue(domino01.getX() == 1 && domino01.getY() == 0);
        domino01.flip();
        assertTrue(domino01.getX() == 0 && domino01.getY() == 1);
        domino66.flip();
        assertTrue(domino66.getX() == 6 && domino66.getY() == 6);
        domino66.flip();
        assertTrue(domino66.getX() == 6 && domino66.getY() == 6);
    }
}