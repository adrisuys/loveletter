package be.adrisuys.myapplication.view;

public interface ViewInterface {
    void onDisplayMessage(String msg);

    void onSelectedCardValidation(int value);

    void displayChoiceWindow();

    void onDisplayWinnerRound(String name);

    void onDisplayWinnerMatch(String name);

    void onDisplayPlayerCards();

    void onDisplayWinnerTie();

    void onComputerSelectedCardValidation(int value);

    void onComputerDisplayMessage(String s);

    void reinitialize();
}
