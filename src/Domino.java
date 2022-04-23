public class Domino {
    private int x;
    private int y;
    private boolean isVertical = false;
    private Domino connectedX,connectedY;

    public void connectToX(Domino other){connectedX = other;}
    public void connectToY(Domino other){connectedY = other;}
    public boolean connectedX() {return connectedX !=null;}
    public boolean connectedY() {return connectedY !=null;}
    public Domino(int x, int y) { this.x = x; this.y = y; }
    public int getX(){ return this.x;}
    public int getY(){ return this.y;}
    public int getValue(){ return this.x + this.y; }
    public boolean isVertical(){return isVertical;}
    public boolean isDouble(){return x == y;}
    public boolean isEqual(Domino other){return (x == other.x && y == other.y) || (y == other.x && x == other.y);}
    public boolean canConnect(Domino other){return  (x == other.x || y == other.y) || (y == other.x || x == other.y);}
    public boolean isStarter(){return x+y == 12;}
    public void beVertical(){isVertical = true;}
    public void beHorizontal(){isVertical = false;}
    public void flip(){int temp = y; y = x; x = temp; } //swaps x and y
    public void print(){
        if(!isVertical){
            System.out.println(x+""+y);
            return;
        }
        System.out.println(x);
        System.out.printf("|");
        System.out.println(y);
    }

}
//