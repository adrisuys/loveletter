package be.adrisuys.myapplication.presenter;

import java.util.List;

import be.adrisuys.myapplication.models.Game;
import be.adrisuys.myapplication.view.ViewInterface;

public class Presenter {

    private Game game;
    private ViewInterface view;

    public Presenter(ViewInterface view, Game game){
        this.view = view;
        this.game = game;
        game.setPresenter(this);
    }

    public List<Integer> getPlayerCardsValue() {
        return game.getPlayerCardsValue();
    }

    public void selectCard(int index) {
        game.selectCard(index);
    }

    public void onDisplayMessage(String msg) {
        view.onDisplayMessage(msg);
    }

    public void onSelectedCardValidation(int value) {
        view.onSelectedCardValidation(value);
    }

    public void playCard(int index) {
        game.playCard(index);
    }

    public void displayChoiceWindow() {
        view.displayChoiceWindow();
    }

    public void pickVictim(int value) {
        game.pickVictim(value);
    }

    public void onDisplayWinnerRound(String name) {
        view.onDisplayWinnerRound(name);
    }

    public void onDisplayWinnerMatch(String name) {
        view.onDisplayWinnerMatch(name);
    }

    public void onDisplayPlayerCards() {
        view.onDisplayPlayerCards();
    }

    public void onDisplayWinnerTie() {
        view.onDisplayWinnerTie();
    }

    public void onComputerSelectedCardValidation(int value) {
        view.onComputerSelectedCardValidation(value);
    }

    public void playComputerCard() {
        game.playComputerCard();
    }

    public void onComputerDisplayMessage(String s) {
        view.onComputerDisplayMessage(s);
    }

    public void reinitialize() {
        game.reinitialize();
        view.reinitialize();
    }
}