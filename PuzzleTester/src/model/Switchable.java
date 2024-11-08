package model;

public interface Switchable {

    int numberOfStates();
    int currentState();
    void switchToNextState();

}
