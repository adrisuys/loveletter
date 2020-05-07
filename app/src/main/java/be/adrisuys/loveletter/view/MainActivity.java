package be.adrisuys.loveletter.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import be.adrisuys.loveletter.presenter.Presenter;
import be.adrisuys.loveletter.R;
import be.adrisuys.loveletter.models.Game;

public class MainActivity extends AppCompatActivity implements ViewInterface {

    private Presenter presenter;
    private int touchIndex;

    private ImageView[] playerCards;
    private ImageView[] animatedCards;
    private ImageView currentlyAnimatedCard;
    private ImageView currentlyPlayedCard;
    private ImageView discard;
    private ImageView computerAnimatedCard;
    private TextView computerMove;
    private LinearLayout animationLayout;
    private Dialog dialogPicker;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private Gson gson;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gson = new Gson();
        sp = getApplicationContext().getSharedPreferences("LoveLetter", MODE_PRIVATE);
        spEditor = sp.edit();
        game = (Game)getIntent().getSerializableExtra("game");
        presenter = new Presenter(this, game);
        game.reinitialize();
        setUpLayout();
        displayPlayerCards();
    }

    public void playCard(View view){
        touchIndex = Integer.parseInt(view.getTag().toString());
        presenter.selectCard(touchIndex);
    }

    public void pickVictim(View v){
        int value = Integer.parseInt(v.getTag().toString());
        dialogPicker.cancel();
        presenter.pickVictim(value);
    }

    @Override
    public void onDisplayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSelectedCardValidation(int value) {
        currentlyAnimatedCard = animatedCards[touchIndex];
        currentlyPlayedCard = playerCards[touchIndex];
        currentlyPlayedCard.setImageResource(0);
        animationLayout.setVisibility(View.VISIBLE);
        setBackgroundImage(currentlyAnimatedCard, value);
        moveCardToDeck(value);
    }

    @Override
    public void onComputerSelectedCardValidation(int value) {
        animationLayout.setVisibility(View.VISIBLE);
        setBackgroundImage(computerAnimatedCard, value);
        moveComputerCardToDeck(value);
    }

    @Override
    public void onComputerDisplayMessage(String message) {
        computerMove.setText(message.toLowerCase());
    }

    @Override
    public void reinitialize() {
        displayPlayerCards();
        setBackgroundImage(discard, 0);
        animationLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayChoiceWindow() {
        dialogPicker = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogPicker.setContentView(R.layout.char_picker_dialog);
        dialogPicker.show();
    }

    @Override
    public void onDisplayWinnerRound(String name, String explanation) {
        backUp(false, !name.contains("computer"));
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_custom_win);
        TextView tv = dialog.findViewById(R.id.text_message);
        TextView yesBtn = dialog.findViewById(R.id.btn_yes);
        TextView noBtn = dialog.findViewById(R.id.btn_no);
        tv.setText(explanation.toLowerCase() + " " + name.toLowerCase() + " won ! do you want to continue ?");
        yesBtn.setText("yes");
        noBtn.setText("no");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reinitialize();
                dialog.cancel();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome(false);
                dialog.cancel();
            }
        });
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.bubble));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.height = 900;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onDisplayWinnerMatch(String name, String explanation) {
        backUp(true, !name.contains("computer"));
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_custom_win);
        TextView tv = dialog.findViewById(R.id.text_message);
        TextView yesBtn = dialog.findViewById(R.id.btn_yes);
        TextView noBtn = dialog.findViewById(R.id.btn_no);
        tv.setText(explanation.toLowerCase() + " " + name.toLowerCase() + " won the whole serie");
        yesBtn.setText("ok");
        noBtn.setText("");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome(true);
                dialog.cancel();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.bubble));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.height = 900;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onDisplayPlayerCards() {
        displayPlayerCards();
    }

    @Override
    public void onDisplayWinnerTie() {
        backUp(false, false);
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_custom_win);
        TextView tv = dialog.findViewById(R.id.text_message);
        TextView yesBtn = dialog.findViewById(R.id.btn_yes);
        TextView noBtn = dialog.findViewById(R.id.btn_no);
        tv.setText("it's a tie. nobody wins... do you want to continue ?");
        yesBtn.setText("yes");
        noBtn.setText("no");
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reinitialize();
                dialog.cancel();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome(false);
                dialog.cancel();
            }
        });
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.bubble));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.height = 900;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void setUpLayout(){
        animationLayout = findViewById(R.id.front);
        animationLayout.setVisibility(View.INVISIBLE);
        ImageView playerCardOne = findViewById(R.id.player_first_card);
        ImageView playerCardTwo = findViewById(R.id.player_second_card);
        playerCards = new ImageView[]{playerCardOne, playerCardTwo};
        ImageView animatedCardOne = findViewById(R.id.card_animated_first);
        ImageView animatedCardTwo = findViewById(R.id.card_animated_second);
        animatedCards = new ImageView[]{animatedCardOne, animatedCardTwo};
        discard = findViewById(R.id.last_card_played);
        computerAnimatedCard = findViewById(R.id.ai_card);
        computerMove = findViewById(R.id.ai_move_explanation);
    }

    private void displayPlayerCards() {
        for (int i = 0; i < presenter.getPlayerCardsValue().size(); i++){
            setBackgroundImage(playerCards[i], presenter.getPlayerCardsValue().get(i));
        }
    }

    private void setBackgroundImage(ImageView img, Integer value) {
        switch (value){
            case 1:
                img.setImageResource(R.drawable.hawkeye);
                break;
            case 2:
                img.setImageResource(R.drawable.dr_strange);
                break;
            case 3:
                img.setImageResource(R.drawable.hulk);
                break;
            case 4:
                img.setImageResource(R.drawable.nick_fury);
                break;
            case 5:
                img.setImageResource(R.drawable.black_widow);
                break;
            case 6:
                img.setImageResource(R.drawable.thor);
                break;
            case 7:
                img.setImageResource(R.drawable.iron_man);
                break;
            case 8:
                img.setImageResource(R.drawable.captain_america);
                break;
            default:
                img.setImageResource(0);
                break;
        }
    }

    private void moveCardToDeck(final int value) {
        Animation animation = new TranslateAnimation(0f, getDeltaX(), 0f, getDeltaY());
        animation.setDuration(1000);
        animation.setZAdjustment(Animation.ZORDER_TOP);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onTranslationDone(value);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        currentlyAnimatedCard.requestLayout();
        currentlyAnimatedCard.startAnimation(animation);
    }

    private void moveComputerCardToDeck(final int value) {
        Animation animation = new TranslateAnimation(0f, 0f, 0f, 1000f);
        animation.setDuration(1000);
        animation.setZAdjustment(Animation.ZORDER_TOP);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onComputerTranslationDone(value);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        computerAnimatedCard.requestLayout();
        computerAnimatedCard.startAnimation(animation);
    }

    private void onTranslationDone(int value) {
        hideAnimationLayout();
        setBackgroundImage(discard, value);
        presenter.playCard(touchIndex);
    }

    private void onComputerTranslationDone(int value){
        hideAnimationLayout();
        setBackgroundImage(discard, value);
        presenter.playComputerCard();
    }

    private float getDeltaX() {
        RectF one = calculeRectOnScreen(discard);
        RectF two = calculeRectOnScreen(currentlyPlayedCard);
        return (one.right - two.right) + discard.getWidth() / 4;
    }

    private float getDeltaY() {
        RectF one = calculeRectOnScreen(discard);
        RectF two = calculeRectOnScreen(currentlyPlayedCard);
        return (one.bottom - two.bottom);
    }

    private RectF calculeRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(), location[1] + view.getMeasuredHeight());
    }

    private void hideAnimationLayout(){
        for (ImageView img: animatedCards){
            img.setImageResource(0);
        }
        computerAnimatedCard.setImageResource(0);
        animationLayout.setVisibility(View.INVISIBLE);
    }

    private void backUp(boolean isOver, boolean hasWon){
        if (isOver){
            spEditor.putString("game", "");
            saveStats(hasWon);
        } else {
            game.setPresenter(null);
            String json = gson.toJson(game);
            spEditor.putString("game", json);
        }
        spEditor.commit();
        game.setPresenter(presenter);
    }

    private void backToHome(boolean isOver){
        Intent intent = new Intent(this, HomeActivity.class);
        if (!isOver){
            game.setPresenter(null);
            intent.putExtra("game", game);
        }
        startActivity(intent);
    }

    private void saveStats(boolean hasWon){
        int games = sp.getInt("nbGames", 0);
        int wins = sp.getInt("nbWins", 0);
        spEditor.putInt("nbGames", games + 1);
        if (hasWon){
            spEditor.putInt("nbWins", wins + 1);
        }
    }
}
