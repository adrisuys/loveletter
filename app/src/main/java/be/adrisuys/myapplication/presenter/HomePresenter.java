package be.adrisuys.myapplication.presenter;

import java.util.List;

import be.adrisuys.myapplication.models.Game;

public class HomePresenter {

    private Game game;

    public HomePresenter(Game game){
        this.game = game;
    }

    public List<Boolean> getWinRecap(){
        return game.getScoreRecap();
    }
}
