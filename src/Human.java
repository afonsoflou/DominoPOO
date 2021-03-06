import java.util.List;
import java.util.Scanner;

public class Human extends Player{

    public Human(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
        super(gameLine, PlayerName, dominoes, board);
    }

    private boolean validInputCheck(Domino other,Domino corner){
       if(other == null)return false;
        if(gameLine.canPlay(other,corner) && dominoes.contains(other)){
                gameLine.insertDomino(other, corner);
                dominoes.remove(other);
                return true;
            }
        System.out.println("Please input a valid choice");
        return false;
    }

    //Asks for starting coordinates and inserts double six there

    public void firstPlay(){
        Scanner sc = new Scanner(System.in);
        System.out.println("First Player, Input Starting Coordinates");
        gameLine.firstPlay(getDoubleSix(),sc.nextInt(),sc.nextInt());
    }//

    public void play() {

        Scanner sc = new Scanner(System.in);

        printPlayablePieces();

        Domino domino;
        Domino corner = null;

       do{ //Keep Playing until a valid Domino is Played
          try {
             String input = sc.nextLine();
             String[] dominoSplit = input.split("\\|");
             domino = new Domino(Integer.parseInt(dominoSplit[0]), Integer.parseInt(dominoSplit[1]));
             for(Domino c : gameLine.getCorners()) {
                if(gameLine.canPlay(domino, c)) {
                   corner = c;
                   break;
                }
             }
       }catch(IllegalArgumentException|ArrayIndexOutOfBoundsException e){
          System.out.println("wrong Input");
          domino = null;
       }
       } while(!validInputCheck(domino,corner));
    }
}
