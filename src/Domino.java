public class Domino {
    private int x;
    private int y;
    private boolean isVertical = false;
    private boolean connectedX = false,connectedY= false;
    public Domino(int x, int y){
        if(x < 0 || x > 6 || y < 0 || y > 6) throw new IllegalArgumentException("class invariant is broken");
        this.x = x; this.y = y;
    }


    public void connectToX(){if(isDouble()) throw new IllegalArgumentException("Binary connections aren't made for doubles"); connectedX = true;}
    public void connectToY(){if(isDouble()) throw new IllegalArgumentException("Binary connections aren't made for doubles"); connectedY = true;}
    public int getUnconnected() {if(!connectedX) return x; if(!connectedY) return y; throw new IllegalArgumentException("getUnconnected what happened?");}
    public boolean canConnect(Domino other){int z = getUnconnected(); return z == other.x || z == other.y ;}
    public int getX(){ return this.x;}
    public int getY(){ return this.y;}
    public int getValue(){ return this.x + this.y; }
    public boolean isVertical(){return isVertical;}
    public boolean isDouble(){return x == y;}
    public boolean isStarter(){return x+y == 12;}
    public void beVertical(){isVertical = true;}
    public void beHorizontal(){isVertical = false;}
    public void flip(){int temp = x; x = y;y = temp;} //swaps x and y

    public void print(){
        if(!isVertical){
            System.out.println(x+"|"+y);
            return;
        }
        System.out.println(x);
        System.out.println("|");
        System.out.println(y);
    }

    public String toString() {
        return "[" + x + "|" + y + "]";
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Domino domino = (Domino) o;

        return (x == domino.x && y == domino.y) || (y == domino.x && x == domino.y);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}