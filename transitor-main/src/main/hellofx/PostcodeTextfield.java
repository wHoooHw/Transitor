package main.hellofx;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class PostcodeTextfield extends TextField {

    private final boolean acceptsLettersOnly;

    public PostcodeTextfield(boolean acceptsLettersOnly) {
        this.acceptsLettersOnly = acceptsLettersOnly;
        initialize();
    }

    private void initialize() {
        setPrefWidth(40);
        setPrefHeight(20);
        setStyle("-fx-font-size: 12px;");
        setAlignment(Pos.CENTER);
        addEventFilter(KeyEvent.KEY_TYPED, this::restrictInput);
    }

    private void restrictInput(KeyEvent event) {
        String inputChar = event.getCharacter();
        if (acceptsLettersOnly) {
            if (!inputChar.matches("[a-zA-Z]")) {
                event.consume(); //Ignore non-letter characters
            } else {
                if (getText().length() < 1) {
                    setText(inputChar.toUpperCase()); //Convert to uppercase
                    event.consume(); //Prevent the event from propagating
                }
            }
        } else {
            if (!inputChar.matches("\\d")) {
                event.consume(); //Ignore non-digit characters
            }
        }
    }

    public void setMaxLength(int maxLength) {
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                setText(newValue.substring(0, maxLength));
            }
        });
    }
}
