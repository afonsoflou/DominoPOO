public class Domino {

    private int num1;
    private int num2;

    public Domino(int num1, int num2){
        this.num1 = num1;
        this.num2 = num2;
    }

    public int getNum1(){
        return this.num1;
    }

    public int getNum2(){
        return this.num2;
    }

    public int getValue(){
        return this.num1 + this.num2;
    }
}
