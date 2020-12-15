package engine.field;

import engine.core.CellSigns;

public class TicTacToeFieldCell {
    private CellSigns currentSign;

    public TicTacToeFieldCell(){
        currentSign = CellSigns.EMPTY;
    }

    public CellSigns getCurrentSign() {
        return currentSign;
    }

    public void setSign(CellSigns sign) {
        if(currentSign!=CellSigns.EMPTY)
            throw new IllegalStateException("Signs cannot overlap.");
        this.currentSign = sign;
    }
}
