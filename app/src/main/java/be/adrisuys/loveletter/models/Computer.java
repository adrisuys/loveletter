package be.adrisuys.loveletter.models;

public class Computer extends Player {

    private Card memory;

    public Computer(){
        super();
    }

    public void setMemory(Card memory) {
        this.memory = memory;
    }

    public Card getMemory() {
        return memory;
    }
}
