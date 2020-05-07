package be.adrisuys.loveletter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    private List<Card> cards;
    private boolean isProtected;
    private List<Boolean> wins;

    public Player(){
        cards = new ArrayList<>();
        isProtected = false;
        wins = new ArrayList<>();
    }

    void drawCard(Card card) {
        if (card == null) return;
        cards.add(card);
    }

    Card getCard(){
        return cards.get(0);
    }

    List<Card> getCards() {
        return cards;
    }

    Card selectCard(int index){
        Card selected = cards.get(index);
        if (mustPlaySeven() && selected.getValue() != 7){
            return null;
        } else {
            return selected;
        }
    }

    Card discardCard(int index){
        return cards.remove(index);
    }

    void discardAndDraw(Card card){
        discardCard(0);
        if (card == null) return;
        drawCard(card);
    }

    boolean isProtected() {
        return isProtected;
    }

    void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    boolean hasCard(int value){
        for (Card c : cards){
            if (c.getValue() == value){
                return true;
            }
        }
        return false;
    }

    void winRound(){
        wins.add(true);
    }

    void loseRound(){
        wins.add(false);
    }

    boolean hasWinMatch(){
        return getWinsCount() >= 3;
    }

    int computerSelectBestCard(){
        if (cards.get(0).getValue() == 8) return 1;
        if (cards.get(1).getValue() == 8) return 0;
        if (mustPlaySeven()){
            if (cards.get(0).getValue() == 7) return 0;
            if (cards.get(1).getValue() == 7) return 1;
        }
        double random = Math.random();
        return (random < 0.5) ? 0 : 1;
    }

    List<Boolean> getWins() {
        return wins;
    }

    private boolean mustPlaySeven() {
        int ironManIndex = -1;
        for (int i = 0; i < cards.size(); i++){
            if (cards.get(i).getValue() == 7){
                ironManIndex = i;
            }
        }
        if (ironManIndex == -1) return false;
        int otherIndex = (ironManIndex == 0) ? 1 : 0;
        if (cards.get(otherIndex).getValue() == 5 || cards.get(otherIndex).getValue() == 5){
            return true;
        } else {
            return false;
        }
    }

    private int getWinsCount(){
        int nbOfWins = 0;
        for (boolean win: wins){
            if (win){
                nbOfWins++;
            }
        }
        return nbOfWins;
    }
}
