public class Domino {
    private int x;
    private int y;
    private boolean isVertical = false;
    private boolean connectedX,connectedY;

    public void connectToX(){if(isDouble()) throw new IllegalArgumentException("Binary connections aren't made for doubles"); connectedX = true;}
    public void connectToY(){if(isDouble()) throw new IllegalArgumentException("Binary connections aren't made for doubles"); connectedY = true;}
    public boolean connectedX() {return connectedX ;}
    public boolean connectedY() {return connectedY;}
    public int getUnconnected() {if(!connectedX) return x; if(!connectedY) return y; throw new IllegalArgumentException("getUnconnected what happened?");}
    public Domino(int x, int y) { this.x = x; this.y = y; }
    public int getX(){ return this.x;}
    public int getY(){ return this.y;}
    public int getValue(){ return this.x + this.y; }
    public boolean isVertical(){return isVertical;}
    public boolean isDouble(){return x == y;}
    public boolean isEqual(Domino other){return (x == other.x && y == other.y) || (y == other.x && x == other.y);}
    public boolean canConnect(Domino other){return (x==other.x && !other.connectedX()) || (y==other.y && !other.connectedY())|| (x==other.y && !other.connectedY()) || (y==other.x && !other.connectedX());}
    public boolean isStarter(){return x+y == 12;}
    public void beVertical(){isVertical = true;}
    public void beHorizontal(){isVertical = false;}
    public void rotate(){isVertical = !isVertical;}
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