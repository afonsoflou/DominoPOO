import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DominoTests{

    Domino other = new Domino(1,1);

    @Test
    public void constructorTest(){
        assertEquals(new Domino(1,2).getX(),1);
        assertEquals(new Domino(1,2).getY(),2);
        assertEquals(new Domino(3,4).getX(),3);
        assertEquals(new Domino(3,4).getY(),4);
    }
    //
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
        assertTrue(new Domino(1, 1).isEqual(other));
        assertFalse(new Domino(1, 2).isEqual(other));
        assertFalse(new Domino(1, 3).isEqual(other));
        assertFalse(new Domino(1, 4).isEqual(other));
    }

    @Test
    public void canConnectTest(){
        assertTrue(new Domino(1,3).canConnect(other));
        assertTrue(new Domino(1,2).canConnect(other));
        assertFalse(new Domino(2,3).canConnect(other));
        assertFalse(new Domino(3,3).canConnect(other));
    }

    @Test
    public void isStarterTest(){
        assertTrue(new Domino(6,6).isStarter());
        assertFalse(new Domino(5,6).isStarter());
        assertFalse(new Domino(4,6).isStarter());
        assertFalse(new Domino(3,1).isStarter());
    }



}