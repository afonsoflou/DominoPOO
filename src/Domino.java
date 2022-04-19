public class Domino {

    private int x;
    private int y;

    public Domino(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }


    public int getY(){
        return this.y;
    }

    public int getValue(){
        return this.x + this.y;
    }
}
