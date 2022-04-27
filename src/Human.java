import java.util.List;
import java.util.Scanner;

public class Human extends Player{

    public Human(GameLine gameLine, String PlayerName, List<Domino> dominoes) {
        super(gameLine, PlayerName, dominoes);
    }

    private void printPieces(){
        for(Domino domino : dominoes)
            domino.print();
    }

    private void printPlayablePieces(){
        System.out.println();
        System.out.println("Select Domino");
        for(Domino domino : dominoes)
            for(Domino corner : gameLine.getCorners())
                if(domino.canConnect(corner)){
                    domino.print();
                    break;}

    }

    private boolean validInputCheck(Domino other,Domino corner){
        for(Domino d : dominoes){
            if(d.isEqual(other)){
                gameLine.insertDomino(d, corner);
                removeDomino(d);
                return true;
            }}
        return false;
    }


    public void play() {
        System.out.println("Your Dominoes");
        printPieces();
        if(!canPlay()) {
            System.out.println("No playable pieces");
            return;
        }
        Scanner sc = new Scanner(System.in);
        printPlayablePieces();

        String input = sc.nextLine();
        String[] dominoSplit = input.split("|");
        Domino domino = new Domino(Integer.parseInt(dominoSplit[0]), Integer.parseInt(dominoSplit[2]));

        Domino corner = null;

        for(Domino c: gameLine.getCorners()){
            if(c.canConnect(domino)){
                corner = c;
                break;
            }
        }

        while(!validInputCheck(domino,corner)){
            System.out.println("Invalid input, please input a valid domino");
            input = sc.nextLine();
            dominoSplit = input.split("|");
            domino = new Domino(Integer.parseInt(dominoSplit[0]), Integer.parseInt(dominoSplit[2]));
            for(Domino c: gameLine.getCorners()){
                if(c.canConnect(domino)){
                    corner = c;
                    break;
                }
            }
        }
    }
}
