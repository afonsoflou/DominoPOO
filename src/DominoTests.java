import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DominoTests{

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


}