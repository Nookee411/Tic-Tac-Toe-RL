package engine.core;

public class Counter {
    private int value;

    public Counter() {
        value = 0;
    }

    public int getCounterValue(){
        return value;
    }

    public void updateCounter(){
        value++;
    }

    public void updateCounter(int addedValue){
        value+=addedValue;
    }

    public void resetCounter(){
        value = 0;
    }
}
