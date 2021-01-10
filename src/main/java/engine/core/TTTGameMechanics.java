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

    public void newGame(){
        gameField = new TicTacToeField(3);
        turnCounter = new Counter();
        currentPlayerSign = (rand.nextDouble()>=0.5)? CellSigns.CROSS : CellSigns.ZERO;
    }

    public Integer[][] getField(){
        var field = gameField.getGameField();
        Integer[][] newField = new Integer[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (field[i][j].getCurrentSign()){
                    case CROSS:
                        newField[i][j] = 1;
                        break;
                    case ZERO:
                        newField[i][j] = -1;
                        break;
                    case EMPTY:
                        newField[i][j] = 0;
                        break;
                }
            }
        }
        return newField;
    }

    public boolean isFieldFull() {
        return gameField.isDraw();
    }
}
