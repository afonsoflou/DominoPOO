/**
 * @inv 0 <= x <= 6
 * @inv 0 <= y <= 6
 * @inv x and y can't be altered, however they can be swapped
 */
public class Domino {
    private int x;
    private int y;
    private boolean isVertical = false;
    private boolean connectedX = false,connectedY= false;

    public Domino(int x, int y){
        if(x < 0 || x > 6 || y < 0 || y > 6) throw new IllegalArgumentException("class invariant is broken");
        this.x = x; this.y = y;
    }

    /** Changes connectedX to true
     * @pre isDouble == false
     * @post connectedX == true
     */
    public void connectToX(){if(isDouble()) throw new IllegalArgumentException("Binary connections aren't made for doubles"); connectedX = true;}

    /** Changes connectedY to true
     * @pre isDouble == false
     * @post connectedY == true
     */
    public void connectToY(){if(isDouble()) throw new IllegalArgumentException("Binary connections aren't made for doubles"); connectedY = true;}

    /** Returns a side that isn't connected to another domino
     * @pre true
     * @post state = old state
     * @return return a side that isn't connected to another domino
     */
    public int getUnconnected() {if(!connectedX) return x; if(!connectedY) return y; throw new IllegalArgumentException("getUnconnected what happened?");}

    /** Returns if the domino can connect to other
     * @pre true
     * @post state = old state
     * @return return if the domino can connect to other
     */
    public boolean canConnect(Domino other){int z = getUnconnected(); return z == other.x || z == other.y ;}

    /** Returns X value
     * @pre true
     * @post state = old state
     * @return X value
     */
    public int getX(){ return this.x;}

    /** Returns Y value
     * @pre true
     * @post state = old state
     * @return Y value
     */
    public int getY(){ return this.y;}

    /** Returns value of X+Y
     * @pre true
     * @post state = old state
     * @return value of X+Y
     */
    public int getValue(){ return this.x + this.y; }

    /** Returns if the domino is vertical
     * @pre true
     * @post state = old state
     * @return true if the domino is vertical
     */
    public boolean isVertical(){return isVertical;}

    /** Returns if the domino is double
     * @pre true
     * @post state = old state
     * @return true if the domino is double
     */
    public boolean isDouble(){return x == y;}

    /** Returns if the domino is a double six
     * @pre true
     * @post state = old state
     * @return true if the domino is a double six
     */
    public boolean isStarter(){return x+y == 12;}

    /** Returns if the domino can connect to other
     * @pre true
     * @post state = old state
     * @return true if the domino can connect to other
     */
    public void beVertical(){isVertical = true;}

    /** Returns if the domino is horizontal
     * @pre true
     * @post state = old state
     * @return true if the domino is horizontal
     */
    public void beHorizontal(){isVertical = false;}

    /** Flips the domino
     * @pre true
     * @post x and y are swapped
     * */
    public void flip(){int temp = x; x = y;y = temp;} //swaps x and y

    /** Prints domino
     * @pre true
     * @post state = old state
     */
    public void print(){
        if(!isVertical){
            System.out.println(x+"|"+y);
            return;
        }
        System.out.println(x);
        System.out.println("|");
        System.out.println(y);
    }

    /** Returns the string representation of the domino
     * @pre true
     * @post state = old state
     * @return string representation of domino
     */
    public String toString() {
        return "[" + x + "|" + y + "]";
    }

    /** Returns if the domino is equal to Object o
     * @pre true
     * @post state = old state
     * @return true if this == o
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Domino domino = (Domino) o;

        return (x == domino.x && y == domino.y) || (y == domino.x && x == domino.y);
    }

    /** Returns clone of the domino
     * @pre true
     * @post state = old state
     * @return new Domino with this.x and this.y
     */
    static public Domino equalsClone(Domino domino){
        return new Domino(domino.getX(),domino.getY());
    }

    /** Returns hashCode of this domino
     * @pre true
     * @post state = old state
     * @return hashCode of this domino (31*x + y)
     */
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}