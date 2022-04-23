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
                if(domino.canConnect(corner))
                    domino.print();
    }

    private boolean validInputCheck(Domino other,Domino corner){
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

        String input = sc.nextLine();
        String[] dominoSplit = input.split("|");
        Domino domino = new Domino(Integer.parseInt(dominoSplit[0]), Integer.parseInt(dominoSplit[1]));

        Domino corner = null;

        for(Domino c: board.getCorners()){
            if(c.canConnect(domino)){
                corner = c;
                break;
            }
        }

        while(!validInputCheck(domino,corner)){
            System.out.println("Invalid input, please input a valid domino");
            input = sc.nextLine();
            dominoSplit = input.split("|");
            domino = new Domino(Integer.parseInt(dominoSplit[0]), Integer.parseInt(dominoSplit[1]));
            for(Domino c: board.getCorners()){
                if(c.canConnect(domino)){
                    corner = c;
                    break;
                }
            }
        }
    }
}