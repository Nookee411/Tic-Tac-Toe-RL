package engine.player;

import engine.core.CellSigns;
import engine.core.Point;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class GameAgent {
    private HashMap<Integer, Double[]> qFunction;
    private Random rand = new Random();
    private TurnRecorder turnHistory = new TurnRecorder();
    private final double LEARNING_RATE = 0.9;
    private final double DISCOUNTING_FACTOR= 0.95;
    private CellSigns agentSign =CellSigns.CROSS;

    private Integer reuseCounter;

    public GameAgent() {
        qFunction = new HashMap<>();
        reuseCounter = 0;
        this.agentSign = agentSign;
    }

    public void setAgentSign(CellSigns agentSign) {
        this.agentSign = agentSign;
    }

    public Point getTurnPosition(Integer[][] currentEnvState){
        var turnCoordinates = new Point(-1,-1);
        if(isGameFinished(currentEnvState)){
            return turnCoordinates;
        }
        var currentHash = getHashFromState(currentEnvState);
        if(qFunction.containsKey(currentHash)) {
            reuseCounter++;
            var probs = qFunction.get(currentHash).clone();

            for (int i = 0; i < 9; i++) {
                var randomValue = rand.nextDouble();
                var maxProb = Arrays.stream(probs).max(Double::compareTo).get();
                var maxProbIndex = Arrays.asList(probs).indexOf(maxProb);
                if((randomValue<maxProb&&currentEnvState[maxProbIndex/3][maxProbIndex%3]==0)){
                    turnCoordinates = new Point(maxProbIndex/3,maxProbIndex%3);
                    break;
                }
                else
                    probs[maxProbIndex] = 0d;
            }
        }
        else
        {
            //Using average strategy. Default values are 0.6
            var prob = new Double[9];
            Arrays.fill(prob,0.6);
            qFunction.put(currentHash, prob);
        }
        if(turnCoordinates.getJ()==-1&&turnCoordinates.getI() == -1)
            do {
                turnCoordinates = new Point(Math.abs(rand.nextInt()) % 3, Math.abs(rand.nextInt()) % 3);
            }while(currentEnvState[turnCoordinates.getI()][turnCoordinates.getJ()]!=0);
        turnHistory.record(currentHash,turnCoordinates,currentEnvState);

        return turnCoordinates;
    }

    private int getHashFromState(Integer[][] currentEnvState) {
        int currentHash = 1;
        int sign;
        if(agentSign== CellSigns.CROSS)
            sign = 1;
        else
            sign = -1;
        //1-cross -1 - zero
        int digit = 10;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int currentValue = 0;
                if(currentEnvState[i][j] == 1)
                    currentValue = (sign ==1)?1:2;
                else if(currentEnvState[i][j]==2){
                    currentValue = (sign==-1)?1:2;
                }
                currentHash = currentHash*digit+currentValue;
            }
        }
        return currentHash;
    }

    private boolean isGameFinished(Integer[][] currentEnvState) {
        for (int i = 0; i <3; i++) {
            for (int j = 0; j < 3; j++) {
                if(currentEnvState[i][j]==0)
                    return false;
            }
        }
        return true;
    }

    public void acceptAward(Double award){

        var hash = turnHistory.getStateHash();
        var turn = turnHistory.getTurn();
        var turnIndex = turn.getI()*3+turn.getJ();

        qFunction.get(hash)[turnIndex] = award;
        double prevMax = Arrays.stream(qFunction.get(hash)).max(Comparator.naturalOrder()).get();
        turnHistory.eraseTurn();

        while (!turnHistory.isEmpty()){
            hash = turnHistory.getStateHash();
            turn = turnHistory.getTurn();
            turnIndex = turn.getI()*3+turn.getJ();
            var currentProb= qFunction.get(hash)[turnIndex];
            qFunction.get(hash)[turnIndex] = currentProb + LEARNING_RATE * (DISCOUNTING_FACTOR*prevMax-currentProb);
            turnHistory.eraseTurn();
            prevMax = Arrays.stream(qFunction.get(hash)).max(Comparator.naturalOrder()).get();
        }
    }

    public HashMap<Integer,Double[]> getqFunction(){
        return qFunction;
    }

    public void addState(Integer hash, Double[] turnProbs){
        if(!qFunction.containsKey(hash))
            qFunction.put(hash, turnProbs);

    }

    public Integer getReuseCounter() {
        return reuseCounter;
    }
}
