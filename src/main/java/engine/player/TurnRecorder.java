package engine.player;

import engine.core.Point;

import java.util.Stack;

public class TurnRecorder {
    Stack<Integer> hash;
    Stack<Point> turns;
    Stack<Integer[][]> states;

    public TurnRecorder() {
        hash = new Stack<>();
        turns = new Stack<>();
        states = new Stack<>();
    }
    public void record(Integer currentHash, Point turn, Integer[][] state){
        hash.push(currentHash);
        turns.push(turn);
        states.push(state);
    }

    public boolean isEmpty(){
        return hash.isEmpty();
    }

    public Integer getStateHash(){
        return hash.peek();
    }

    public Point getTurn(){
        return turns.peek();
    }

    public void eraseTurn(){
        hash.pop();
        turns.pop();
        states.pop();
    }

    public Integer count(){
        return hash.size();
    }
}
