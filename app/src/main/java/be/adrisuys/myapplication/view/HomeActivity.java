package be.adrisuys.myapplication.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import be.adrisuys.myapplication.R;
import be.adrisuys.myapplication.models.Game;
import be.adrisuys.myapplication.presenter.HomePresenter;

public class HomeActivity extends AppCompatActivity {

    private Game game;
    private SharedPreferences sp;
    private Gson gson;
    private ImageView[] playerResults;
    private ImageView[] computerResults;
    private TextView continueGameBtn;
    private TextView statsGames, statsWins, statsRatio;
    private boolean clickable;
    private HomePresenter presenter;
    private int playedCount;
    private int wonCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        gson = new Gson();
        sp = getApplicationContext().getSharedPreferences("LoveLetter", MODE_PRIVATE);
        clickable = false;
        setUpUI();
        retrieveGame();
    }

    public void newGame(View v){
        game = new Game();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    public void continueGame(View v){
        if (!clickable) return;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    private void setUpUI(){
        ImageView playerResultOne = findViewById(R.id.player_game_one);
        ImageView playerResultTwo = findViewById(R.id.player_game_two);
        ImageView playerResultThree = findViewById(R.id.player_game_three);
        ImageView playerResultFour = findViewById(R.id.player_game_four);
        ImageView playerResultFive = findViewById(R.id.player_game_five);
        playerResults = new ImageView[]{playerResultOne, playerResultTwo, playerResultThree, playerResultFour, playerResultFive};
        ImageView computerResultOne = findViewById(R.id.ai_game_one);
        ImageView computerResultTwo = findViewById(R.id.ai_game_two);
        ImageView computerResultThree = findViewById(R.id.ai_game_three);
        ImageView computerResultFour = findViewById(R.id.ai_game_four);
        ImageView computerResultFive = findViewById(R.id.ai_game_five);
        computerResults = new ImageView[]{computerResultOne, computerResultTwo, computerResultThree, computerResultFour, computerResultFive};
        continueGameBtn = findViewById(R.id.continue_game_btn);
        statsGames = findViewById(R.id.stats_played);
        statsWins = findViewById(R.id.stats_won);
        statsRatio = findViewById(R.id.stats_ratio);
    }

    private void retrieveGame(){
        game = (Game) getIntent().getSerializableExtra("game");
        if (game == null){
            retrieveGameFromSharedPref();
        } else {
            presenter = new HomePresenter(game);
            displayScores();
            clickable = true;
        }
        retrieveStatsFromSharedPref();
    }

    private void retrieveGameFromSharedPref(){
        String json = sp.getString("game", "");
        if (json.equals("")){
            continueGameBtn.setTextColor(Color.TRANSPARENT);
        } else {
            continueGameBtn.setTextColor(Color.WHITE);
            try {
                game = gson.fromJson(json, Game.class);
                presenter = new HomePresenter(game);
                displayScores();
                clickable = true;
            } catch (Exception e){
                continueGameBtn.setTextColor(Color.TRANSPARENT);
            }
        }
        retrieveStatsFromSharedPref();
    }

    private void displayScores() {
        for (int i = 0; i < presenter.getWinRecap().size(); i++){
            if (presenter.getWinRecap().get(i)){
                computerResults[i].setImageResource(R.drawable.affection_token);
                playerResults[i].setImageResource(R.drawable.affection_token_greyed);
            } else {
                computerResults[i].setImageResource(R.drawable.affection_token_greyed);
                playerResults[i].setImageResource(R.drawable.affection_token);
            }
        }
    }

    private void retrieveStatsFromSharedPref(){
        playedCount = sp.getInt("nbGames", 0);
        wonCount = sp.getInt("nbWins", 0);
        displayStats(String.valueOf(playedCount), String.valueOf(wonCount));
    }

    private void displayStats(String nbGames, String nbWins){
        String ratio;
        if (!nbGames.equals("0")){
            double r = Double.parseDouble(nbWins) / Double.parseDouble(nbGames);
            ratio = String.format("%.2f", r);
        } else {
            ratio = "0.00";
        }
        statsGames.setText(nbGames);
        statsWins.setText(nbWins);
        statsRatio.setText(ratio);
    }
}
