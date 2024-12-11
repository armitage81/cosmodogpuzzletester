package puzzletester.interfaces;

public interface Switchable {

    int numberOfStates();
    int currentState();
    void switchToNextState();

}
