package be.adrisuys.loveletter.view;

public interface ViewInterface {
    void onDisplayMessage(String msg);

    void onSelectedCardValidation(int value);

    void displayChoiceWindow();

    void onDisplayWinnerRound(String name, String explanation);

    void onDisplayWinnerMatch(String name, String explanation);

    void onDisplayPlayerCards();

    void onDisplayWinnerTie();

    void onComputerSelectedCardValidation(int value);

    void onComputerDisplayMessage(String s);

    void reinitialize();
}
