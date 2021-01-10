package view;

import engine.core.CellSigns;
import engine.core.Point;
import engine.core.TTTGameMechanics;
import engine.player.AgentSaver;
import engine.player.GameAgent;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Visual extends Application {

    private Scene scene;
    private BorderPane mainPane;
    private GridPane gridField;
    private MenuBar mainMenu;
    private HBox statusBar;
    private GameAgent agent;
    private Random rand;
    private CellSigns humanSign;

    private TTTGameMechanics game;

    private Stage stage;

    @Override @FXML
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        game = new TTTGameMechanics();
        mainPane = new BorderPane();
        initMainPane();
        scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setTitle("Tic-Tac-Toe");
        agent = new GameAgent();
        rand = new Random();
        stage.show();
    }

    private void initMainPane() {
        initGridField();
        mainPane.setCenter(gridField);
        initMenu();
        mainPane.setTop(mainMenu);
        initStatusBar();
        mainPane.setBottom(statusBar);
    }

    private void initGridField() {
        gridField = new GridPane();
        gridField.setHgap(10);
        gridField.setVgap(10);
        gridField.setPadding(new Insets(10));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                var button = new TicTacToeButton("", j, i);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        mainMenu.setVisible(false);
                        var currentPlayerSign = game.getCurrentPlayerSign();
                        var clickedButton = (TicTacToeButton) actionEvent.getSource();
                        var currentText = currentPlayerSign.toString();
                        agent.setAgentSign(currentPlayerSign == CellSigns.CROSS? CellSigns.ZERO:CellSigns.CROSS);
                        humanSign = game.getCurrentPlayerSign();
                        if (!makeTurn(new Point(clickedButton.getJ(), clickedButton.getI()))) {
                            //agent's turn
                            var coord = agent.getTurnPosition(game.getField());
                            makeTurn(coord);
                        }


                    }
                });
                gridField.add(button, j, i);
            }
        }
    }

    private boolean makeTurn(Point coordinates) {
        if(game.getField()[coordinates.getI()][coordinates.getJ()] != 0) {
            Alert overlapDialog = new Alert(Alert.AlertType.ERROR);
            overlapDialog.setHeaderText("Sign overlapped at:" + coordinates.getI() + ", " + coordinates.getJ());
            overlapDialog.showAndWait();
            return false;
        }

        int buttonIndex = coordinates.getI() * 3 + coordinates.getJ();
        if(buttonIndex<0){
            Alert overlapDialog = new Alert(Alert.AlertType.ERROR);
            overlapDialog.setHeaderText("index<0:" + coordinates.getI() + ", " + coordinates.getJ());
            overlapDialog.showAndWait();
        }
        TicTacToeButton turnButton = (TicTacToeButton) gridField.getChildren().get(buttonIndex);
        turnButton.setText(game.getCurrentPlayerSign()==CellSigns.CROSS? "X":"O");

        game.makeTurn(coordinates);
        return checkIsWon();
    }

    private boolean checkIsWon(){
        var congrats = new Alert(Alert.AlertType.INFORMATION);
        if(game.isGameWon()){
                if(game.getWinnerSign()== humanSign){
                    congrats.setHeaderText("Human won with "+ game.getWinnerSign());
                    agent.acceptAward(0d);
                }
                else{
                    congrats.setHeaderText("Agent won with" + game.getWinnerSign());
                    agent.acceptAward(1d);
            }
            congrats.showAndWait();
            startNewGame();
            return true;
        }
        if( game.isFieldFull()){
            congrats.setTitle("Draw");
            agent.acceptAward(0.5d);
            congrats.showAndWait();
            startNewGame();
            return true;
        }
        return false;
    }

    private void eraseField() {
        gridField.getChildren().forEach(ele->{
            var button = (TicTacToeButton)ele;
            button.setText("");
        });
    }

    private void initMenu() {
        var agentMenu = new Menu("Agent");
        var loadAgent = new MenuItem("load");
        loadAgent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                loadAgentFromFile();
            }
        });

        var saveAgent = new MenuItem("save");
        saveAgent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                saveAgentToFile();
            }
        });
        agentMenu.getItems().addAll(loadAgent, saveAgent);

        var train = new Menu("Train");
        var train1000 = new MenuItem("1000000");
        train1000.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                var trainCount = new Alert(Alert.AlertType.CONFIRMATION);
                trainCount.setTitle("1000 training rounds will be played.");
                trainCount.showAndWait();
               trainModel(10000000);
            }
        });
        train.getItems().add(train1000);
        mainMenu = new MenuBar(agentMenu, train);
    }

    private void saveAgentToFile() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            AgentSaver.saveAgent(file.getPath(),agent);
            System.out.println("Saving completed.");
        }
        else
            System.out.println("Saving impossible");
    }

    private void loadAgentFromFile() {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Choose agent model");
        var model = fChooser.showOpenDialog(stage);
        agent = AgentSaver.loadAgent(model.getPath());
    }

    private void initStatusBar() {
        statusBar = new HBox();
    }

    public static void main(String[] args) {
        launch();
    }

    public void startNewGame(){
        game.newGame();
        mainMenu.setVisible(true);
        for (int i = 0; i < gridField.getChildren().size(); i++) {
            ((TicTacToeButton)gridField.getChildren().get(i)).setText("");
        }
    }

    private void trainModel(Integer numOfTrainings){
        FileWriter fr = null;
        File file = null;
        try {
            file = new File("average_reward.txt");
            if(!file.exists())
                file.createNewFile();
            fr = new FileWriter("average_reward.txt",true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameAgent model2 = new GameAgent();
        TTTGameMechanics agentGame = new TTTGameMechanics();
        Double rewardAccumulator = 0d;
        for (int i = 0; i < numOfTrainings; i++) {
            boolean currentPlayer = rand.nextBoolean();
            if(!currentPlayer)
                agent.setAgentSign(game.getCurrentPlayerSign());
            while(!agentGame.isGameWon()|| agentGame.getTurnCounter()<9){
                //false - mainAgent
                //True - secondAgent
                Point turnPoint;
                if(!currentPlayer){
                    turnPoint = agent.getTurnPosition(agentGame.getField());
                }
                else{
                    turnPoint = model2.getTurnPosition(agentGame.getField());
                }
                if(turnPoint.getI() == -1&&turnPoint.getJ()==-1)
                    break;
                agentGame.makeTurn(turnPoint);
                currentPlayer = !currentPlayer;
            }
            if(agentGame.getTurnCounter()==9&&!agentGame.isGameWon()){
                agent.acceptAward(0.5d);
                rewardAccumulator+=0.5d;
                model2.acceptAward(0.5d);
            }
            else{
                if(currentPlayer){
                    agent.acceptAward(0d);
                    model2.acceptAward(1d);
                }
                else{
                    agent.acceptAward(1d);
                    model2.acceptAward(0d);
                }
            }
            if(i%10000 == 0){
                try{

                    fr.write(rewardAccumulator/10000+ " ");
                    rewardAccumulator=0d;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            agentGame.newGame();
        }
        try {
            fr.write("\n");
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert finish = new Alert(Alert.AlertType.INFORMATION);
        finish.setHeaderText("Training finished.\n Reused: " + agent.getReuseCounter());

        finish.showAndWait();
    }
}
