package engine.field;

import engine.core.CellSigns;
import engine.core.Point;

public class TicTacToeField {
    private final int size;
    private TicTacToeFieldCell[][] gameField;

    public TicTacToeField(int size) {
        this.size = size;
        gameField = new TicTacToeFieldCell[size][size];
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                gameField[i][j] = new TicTacToeFieldCell();
            }
        }
    }

    public TicTacToeFieldCell getCell(Point coordinates){
        return gameField[coordinates.getI()][coordinates.getJ()];
    }

    public void makeTurn(Point coordinates, CellSigns sign){
        var i = coordinates.getI();
        var j = coordinates.getJ();
        if(i>=size|| j>=size)
            throw new IllegalArgumentException("Coordinates must be less than size.");
        if(i<0||j<0)
            throw new IllegalArgumentException("Coordinates must be more than zero");
        gameField[i][j].setSign(sign);
    }

    public CellSigns gameWinner(){
        //Returns sign of winner if game was won, otherwise returns CellSigns.EMPTY


        //Check for horizontal lines
        for (int i = 0; i < size; i++) {
            var firstSign = gameField[i][0].getCurrentSign();
            int j;
            for (j = 1; j < size; j++) {
                var currentSign = gameField[i][j].getCurrentSign();
                if(firstSign !=currentSign)
                    break;
            }
            if(j == size-1)
                return firstSign;
        }

        //Check vertical lines
        for (int j = 0; j < size; j++) {
            var firstSign = gameField[j][0].getCurrentSign();
            int i;
            for (i = 1; i < size; i++) {
                var currentSign = gameField[i][j].getCurrentSign();
                if(firstSign !=currentSign)
                    break;
            }
            if(i == size-1)
                return firstSign;
        }

        //Check diagonal lines

        var mainDiagonalSign = gameField[0][0].getCurrentSign();
        int i;
        for (i = 1; i < size; i++) {
            if(mainDiagonalSign != gameField[i][i].getCurrentSign())
                break;
        }
        if(i == size-1)
            return mainDiagonalSign;

        var secondaryDiagonalSign = gameField[0][size-1].getCurrentSign();
        for (i = 1; i < size; i++) {
            if(secondaryDiagonalSign!= gameField[i][size-1-i].getCurrentSign())
                break;
        }
        if(i==size-1)
            return secondaryDiagonalSign;

        //No winner
        return CellSigns.EMPTY;
    }
}
