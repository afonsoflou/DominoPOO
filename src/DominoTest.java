import static org.junit.jupiter.api.Assertions.*;
import org.testng.annotations.Test;

public class DominoTest{

    public void constructorTest(){
        assertEquals(new Domino(1,2).getNum1(),1);
        assertEquals(new Domino(1,2).getNum2(),2);
        assertEquals(new Domino(3,4).getNum1(),3);
        assertEquals(new Domino(3,4).getNum2(),4);
    }

    public void valueTest(){
        assertEquals(new Domino(1,2).getValue(),3);
        assertEquals(new Domino(3,2).getNum1(),5);
        assertEquals(new Domino(5,6).getNum1(),11);
    }


}