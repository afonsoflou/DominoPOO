import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DominoTests{

    @Test
    public void constructorTest(){
        assertEquals(new Domino(1,2).getNum1(),1);
        assertEquals(new Domino(1,2).getNum2(),2);
        assertEquals(new Domino(3,4).getNum1(),3);
        assertEquals(new Domino(3,4).getNum2(),4);
    }

    @Test
    public void valueTest(){
        assertEquals(new Domino(1,2).getValue(),3);
        assertEquals(new Domino(3,2).getValue(),5);
        assertEquals(new Domino(5,6).getValue(),11);
    }


}