import java.util.List;
import java.util.Scanner;

public class Human extends Player{

    public Human(GameBoard board, String PlayerName, List<Domino> dominoes) {
        super(board, PlayerName, dominoes);
    }

    private void printPieces(){
        for(Domino domino : dominoes)
            domino.print();
    }

    private void printPlayablePieces(){
        for(Domino domino : dominoes)
            for(Domino corner : board.getCorners())
                if(domino.isEqual(corner))
                    domino.print();
    }

    public boolean check(Domino other){
        for(Domino d : dominoes){
            if(d.isEqual(other)){
                board.insertDomino(d, corner);
                dominoes.removeIf(x -> x.isEqual(other));
                return true;
            }}
        return false;
    }


    public void play() {
        if(!canPlay()) return;
        Scanner sc = new Scanner(System.in);
        printPlayablePieces();

        String Domino = sc.nextLine();
        String[] domino = Domino.split("|");
        Domino other = new Domino(Integer.parseInt(domino[0]), Integer.parseInt(domino[1]));

        while(!check(other)){
            System.out.println("Esta peça nao tens, dá input a outra!");
            other = new Domino(Integer.parseInt(domino[0]), Integer.parseInt(domino[1]));
        }
    }
}