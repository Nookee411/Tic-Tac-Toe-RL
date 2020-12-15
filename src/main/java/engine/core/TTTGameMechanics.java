package engine.core;

import engine.field.TicTacToeField;

import java.util.Random;

public class TTTGameMechanics {
    TicTacToeField gameField;
    Counter turnCounter;
    CellSigns currentPlayerSign;
    Random rand;

    public TTTGameMechanics() {
        this.gameField = new TicTacToeField(3);
        this.turnCounter = new Counter();
        rand = new Random();
        currentPlayerSign = (rand.nextDouble()>=0.5)? CellSigns.CROSS : CellSigns.ZERO;
    }

    public CellSigns getCurrentPlayerSign(){
        return currentPlayerSign;
    }

    public void makeTurn(Point coordinates){
        turnCounter.updateCounter();
        gameField.makeTurn(coordinates, currentPlayerSign);
        swapCurrentPlayer();
    }

    public CellSigns getWinnerSign(){
        return gameField.gameWinner();
    }

    public boolean isGameWon(){
        return gameField.gameWinner()!=CellSigns.EMPTY;
    }

    private void swapCurrentPlayer(){
        currentPlayerSign = (currentPlayerSign == CellSigns.CROSS)? CellSigns.ZERO : CellSigns.CROSS;
    }

    public int getTurnCounter(){
        return turnCounter.getCounterValue();
    }
}
