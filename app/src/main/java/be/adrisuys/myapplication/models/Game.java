package be.adrisuys.myapplication.models;

import android.content.Context;
import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.adrisuys.myapplication.R;
import be.adrisuys.myapplication.presenter.Presenter;

public class Game implements Serializable {

    private enum GameState {AI, PLAYER, EMPTY_DECK}

    private Presenter presenter;
    private List<Card> cards;
    private Player player;
    private Computer computer;
    private GameState state;
    private int computerIndex;
    private int memoryTime;

    public Game(){
        player = new Player();
        computer = new Computer();
        cards = new ArrayList<>();
        populateDeck();
        player.drawCard(drawFromDeck());
        computer.drawCard(drawFromDeck());
        player.drawCard(drawFromDeck());
        state = GameState.PLAYER;
        computerIndex = 0;
        memoryTime = -1;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        cards = new ArrayList<>();
        populateDeck();
    }

    public List<Integer> getPlayerCardsValue() {
        List<Integer> values = new ArrayList<>();
        for (Card card : player.getCards()){
            values.add(card.getValue());
        }
        return values;
    }

    public void selectCard(int index) {
        if (state.equals(GameState.AI)) return;
        Card selected = player.selectCard(index);
        if (selected == null){
            presenter.onDisplayMessage("You must play Iron Man !");
        } else {
            presenter.onSelectedCardValidation(selected.getValue());
        }
    }

    public void playCard(int index) {
        Card played = player.discardCard(index);
        if (played.getValue() == 4){
            handleActions(played);
        } else {
            if (computer.isProtected()){
                presenter.onDisplayMessage("The computer is protected...");
                setComputersTurn();
            } else {
                handleActions(played);
            }
        }
    }

    public void playComputerCard(){
        Card card = computer.discardCard(computerIndex);
        if (card.getValue() == 4){
            handleActions(card);
        } else {
            if (player.isProtected()){
                presenter.onComputerDisplayMessage("The computer plays " + card.getName() + " but you are protected");
                setState(GameState.PLAYER);
            } else {
                handleComputerActions(card);
            }
        }
    }

    public void pickVictim(int value) {
        boolean isGuessValid = computer.hasCard(value);
        if (isGuessValid){
            playerWins("You guessed right !");
        } else {
            presenter.onDisplayMessage("Hawkeye missed its target...");
            setComputersTurn();
        }
    }

    public List<Boolean> getScoreRecap(){
        return computer.getWins();
    }

    public void reinitialize() {
        cards.clear();
        populateDeck();
        player.getCards().clear();
        computer.getCards().clear();
        player.drawCard(drawFromDeck());
        computer.drawCard(drawFromDeck());
        player.drawCard(drawFromDeck());
        state = GameState.PLAYER;
        computerIndex = 0;
        memoryTime = -1;
        computer.setMemory(null);
    }

    // private methods

    private void computerSelectCard(){
        computerIndex = computer.computerSelectBestCard();
        presenter.onComputerSelectedCardValidation(computer.getCards().get(computerIndex).getValue());
    }

    private void handleActions(Card played) {
        switch (played.getValue()){
            case 1:
                handleActionOne();
                break;
            case 2:
                handleActionTwo();
                break;
            case 3:
                handleActionThree();
                break;
            case 4:
                handleActionFour();
                break;
            case 5:
                handleActionFive();
                break;
            case 6:
                handleActionSix();
                break;
            case 7:
                handleActionSeven();
                break;
            case 8:
                handleActionEight();
                break;
        }
    }

    private void handleComputerActions(Card card) {
        switch (card.getValue()){
            case 1:
                handleActionComputerOne();
                break;
            case 2:
                handleActionComputerTwo();
                break;
            case 3:
                handleActionComputerThree();
                break;
            case 4:
                handleActionComputerFour();
                break;
            case 5:
                handleActionComputerFive();
                break;
            case 6:
                handleActionComputerSix();
                break;
            case 7:
                handleActionComputerSeven();
                break;
            case 8:
                handleActionComputerEight();
                break;
        }
    }

    private void handleActionOne() {
        presenter.displayChoiceWindow();
    }

    private void handleActionTwo() {
        presenter.onDisplayMessage("Computer's card : " + computer.getCard().getName());
        setComputersTurn();
    }

    private void handleActionThree() {
        Card computerCard = computer.getCard();
        Card playerCard = player.getCard();
        if (playerCard.getValue() > computerCard.getValue()){
            playerWins(playerCard.getName() + " beats " + computerCard.getName() + " !");
        } else if (playerCard.getValue() < computerCard.getValue()){
            computerWins(playerCard.getName() + " is beaten by " + computerCard.getName() + " !");
        } else {
            player.discardAndDraw(drawFromDeck());
            computer.discardAndDraw(drawFromDeck());
            presenter.onDisplayMessage("It's a draw ! Nobody wins.");
            setComputersTurn();
        }
    }

    private void handleActionFour() {
        player.setProtected(true);
        presenter.onDisplayMessage("You are now protected for a round !");
        setComputersTurn();
    }

    private void handleActionFive() {
        presenter.onDisplayMessage("Your opponent must discard its card and draw a new one");
        if (computer.getCard().getValue() == 8){
            playerWins("The component had to discard Captain America !");
        } else {
            computer.discardAndDraw(drawFromDeck());
            setComputersTurn();
        }
    }

    private void handleActionSix() {
        Card tmp = player.getCard();
        player.discardAndDraw(computer.getCard());
        computer.discardAndDraw(tmp);
        presenter.onDisplayMessage("The cards have been traded");
        setComputersTurn();
    }

    private void handleActionSeven() {
        presenter.onDisplayMessage("Nothing happens...");
        setComputersTurn();
    }

    private void handleActionEight() {
        computerWins("You discarded Captain America !");
    }

    private void populateDeck() {
        cards.add(new Card("HAWKEYE", 1));
        cards.add(new Card("HAWKEYE", 1));
        cards.add(new Card("HAWKEYE", 1));
        cards.add(new Card("HAWKEYE", 1));
        cards.add(new Card("HAWKEYE", 1));
        cards.add(new Card("DR STRANGE", 2));
        cards.add(new Card("DR STRANGE", 2));
        cards.add(new Card("HULK", 3));
        cards.add(new Card("HULK", 3));
        cards.add(new Card("NICK FURY", 4));
        cards.add(new Card("NICK FURY", 4));
        cards.add(new Card("BLACK WIDOW", 5));
        cards.add(new Card("BLACK WIDOW", 5));
        cards.add(new Card("THOR", 6));
        cards.add(new Card("IRON MAN", 7));
        cards.add(new Card("CAPTAIN AMERICA", 8));
        Collections.shuffle(cards);
    }

    private Card drawFromDeck(){
        Card c = cards.remove(0);
        if (cards.isEmpty()){
            setState(GameState.EMPTY_DECK);
        }
        return c;
    }

    private void playerWins(String explanation){
        player.winRound();
        computer.loseRound();
        if (player.hasWinMatch()){
            presenter.onDisplayWinnerMatch("You have", explanation);
        } else {
            presenter.onDisplayWinnerRound("You have", explanation);
        }
    }

    private void computerWins(String explanation){
        computer.winRound();
        player.loseRound();
        if (player.hasWinMatch()){
            presenter.onDisplayWinnerMatch("The computer has", explanation);
        } else {
            presenter.onDisplayWinnerRound("The computer has", explanation);
        }
    }

    private void tieGame(){
        presenter.onDisplayWinnerTie();
    }

    private void setState(GameState state) {
        this.state = state;
        if (state.equals(GameState.AI)){
            computer.setProtected(false);
            handleMemory();
            playAsComputer();
        } else if (state.equals(GameState.PLAYER)) {
            player.setProtected(false);
            player.drawCard(drawFromDeck());
            presenter.onDisplayPlayerCards();
        } else {
            Card computerCard = computer.getCard();
            Card playerCard = player.getCard();
            if (playerCard.getValue() > computerCard.getValue()){
                playerWins(playerCard.getName() + " beats " + computerCard.getName() + " !");
            } else if (playerCard.getValue() < computerCard.getValue()){
                computerWins(playerCard.getName() + " is beaten by " + computerCard.getName() + " !");
            } else {
                tieGame();
            }
        }
    }

    private void handleMemory(){
        if (memoryTime != -1){
            memoryTime++;
            if (memoryTime >= 2){
                memoryTime = -1;
                computer.setMemory(null);
            }
        }
    }

    private void playAsComputer() {
        computer.drawCard(drawFromDeck());
        computerSelectCard();
        //setState(GameState.PLAYER);
    }

    private void setComputersTurn(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(GameState.AI);
            }
        }, 2000);
    }

    private void handleActionComputerOne() {
        Card memory = computer.getMemory();
        int value = memory == null ? (int)(Math.random() * ((8 - 2) + 1)) + 2 : memory.getValue();
        boolean isGuessValid = player.hasCard(value);
        if (isGuessValid){
            computerWins("The computer guessed right.");
        } else {
            if (memory != null){
                memoryTime = -1;
                computer.setMemory(null);
            }
            presenter.onComputerDisplayMessage("The computer plays Hawkeye but misses");
            setState(GameState.PLAYER);
        }
    }

    private void handleActionComputerTwo() {
        presenter.onComputerDisplayMessage("The computer plays Dr Strange");
        memoryTime = 0;
        computer.setMemory(player.getCard());
        setState(GameState.PLAYER);
    }

    private void handleActionComputerThree() {
        Card computerCard = computer.getCard();
        Card playerCard = player.getCard();
        if (playerCard.getValue() > computerCard.getValue()){
            playerWins(playerCard.getName() + " beats " + computerCard.getName() + " !");
        } else if (playerCard.getValue() < computerCard.getValue()){
            computerWins(playerCard.getName() + " is beaten by " + computerCard.getName() + " !");
        } else {
            player.discardAndDraw(drawFromDeck());
            computer.discardAndDraw(drawFromDeck());
            presenter.onComputerDisplayMessage("The computer plays Hulk but it's a draw");
            setState(GameState.PLAYER);
        }
    }

    private void handleActionComputerFour() {
        computer.setProtected(true);
        presenter.onComputerDisplayMessage("The computer plays Nick Fury and is protected");
        setState(GameState.PLAYER);
    }

    private void handleActionComputerFive() {
        presenter.onComputerDisplayMessage("The computer plays Black Widow, you must replace your card");
        if (player.getCard().getValue() == 8){
            computerWins("You had to discard Captain America !");
        } else {
            player.discardAndDraw(drawFromDeck());
            setState(GameState.PLAYER);
        }
    }

    private void handleActionComputerSix() {
        Card tmp = player.getCard();
        player.discardAndDraw(computer.getCard());
        computer.discardAndDraw(tmp);
        presenter.onComputerDisplayMessage("The computer plays Thor. The cards have been traded");
        setState(GameState.PLAYER);
    }

    private void handleActionComputerSeven() {
        presenter.onComputerDisplayMessage("The computer plays Iron Man. Nothing happens...");
        setState(GameState.PLAYER);
    }

    private void handleActionComputerEight() {
        playerWins("The computer discarded Captain America !");
    }
}
