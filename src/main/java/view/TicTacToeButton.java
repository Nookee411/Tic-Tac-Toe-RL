package view;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class TicTacToeButton extends Button {
    private int i;
    private int j;

    public TicTacToeButton(String s, int i, int j) {
        super(s);
        this.i = i;
        this.j = j;
        this.setPrefSize(150,150);
        this.setStyle("-fx-border: none");
        this.setFont(new Font(24));


    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }


}
