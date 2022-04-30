import java.util.List;
import java.util.Scanner;

public class Human extends Player{

    public Human(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
        super(gameLine, PlayerName, dominoes, board);
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
                if(gameLine.canPlay(domino,corner)){
                    domino.print();
                    break;}

    }

    private boolean validInputCheck(Domino other,Domino corner){
        if(gameLine.canPlay(other,corner) && dominoes.stream().anyMatch(x -> x.equals(other))){
                gameLine.insertDomino(other, corner);
                removeDomino(other);
                return true;
            }
        return false;
    }

    private boolean printPlayAgainMessage(){
        System.out.println("Please input a valid choice");
        return true;
    }

    public void play() {

        System.out.println("Your Dominoes");
        printPieces();

        Scanner sc = new Scanner(System.in);

        if(isFirst()){
            System.out.println("First Player, Input Starting Coordinates");
            gameLine.firstPlay(getDoubleSix(),sc.nextInt(),sc.nextInt());
            return;
        }

        printPlayablePieces();

        Domino domino;
        Domino corner = null;

       do{ //Keep Playing until a valid Domino is Played
            String input = sc.nextLine();
            String[] dominoSplit = input.split("\\|");
            //this is kinda shit you should use the dominos you have instead of creating, shit may happen such as duplicated dominoes.
            domino = new Domino(Integer.parseInt(dominoSplit[0]), Integer.parseInt(dominoSplit[1]));
            for(Domino c: gameLine.getCorners()){
                if(gameLine.canPlay(domino,c)){
                    corner = c;
                    break;
                }
            }
        } while(!validInputCheck(domino,corner) && printPlayAgainMessage());
    }
}
