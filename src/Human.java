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

    public boolean check(Domino other,Domino corner){
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
        input = sc.nextLine();
        String[] cornerSplit = input.split("|");
        Domino corner = new Domino(Integer.parseInt(cornerSplit[0]), Integer.parseInt(cornerSplit[1]));

        while(!check(domino,corner)){
            System.out.println("Esta peça nao tens, dá input a outra!");
            input = sc.nextLine();
            dominoSplit = input.split("|");
            domino = new Domino(Integer.parseInt(dominoSplit[0]), Integer.parseInt(dominoSplit[1]));
            input = sc.nextLine();
            cornerSplit = input.split("|");
            corner = new Domino(Integer.parseInt(cornerSplit[0]), Integer.parseInt(cornerSplit[1]));
        }
    }
}