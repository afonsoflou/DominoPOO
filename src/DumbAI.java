import java.util.*;

public class DumbAI extends Player{
	public DumbAI(GameLine gameLine, String PlayerName, List<Domino> dominoes, GameBoard board) {
		super(gameLine, PlayerName, dominoes, board);
	}
//

	//Inserts Double six into the middle of the board
	public void firstPlay(){
		gameLine.firstPlay(getDoubleSix(),board.getColumns()/2, board.getLines()/2);
	}

	public void play() {

		//Store Playable Dominos in a list
		LinkedList<Domino> playableDominoes = new LinkedList<>();
		for(Domino domino : dominoes)
			for(Domino corner : gameLine.getCorners())
				if(gameLine.canPlay(domino,corner))
					playableDominoes.add(domino);

		//Play Domino with the highest Value
		playableDominoes.sort(Comparator.comparingInt(Domino::getValue));
		Domino playedDomino = playableDominoes.removeLast();
		dominoes.remove(playedDomino);

		Domino playedCorner = null;

		for(Domino corner : gameLine.getCorners())
			if(gameLine.canPlay(playedDomino,corner)) {
				playedCorner = corner;
				break;
			}
		gameLine.insertDomino(playedDomino,playedCorner);
	}
}