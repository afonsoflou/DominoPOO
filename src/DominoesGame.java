import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class DominoesGame {

    private int nLines;
    private int nColumns;
    private Player[] players;

    public DominoesGame(int nLines, int nColumns){
        this.nLines = nLines;
        this.nColumns = nColumns;
        this.players = new Player[4];

    }

    public void startGame(){
        LinkedList<Domino> Dominoes = new LinkedList<>();
        for(int i = 0; i<7; i++)
            for(int j = i; j<7; j++)
                Dominoes.add(new Domino(i,j));
        Collections.shuffle(Dominoes);




    }


}
//