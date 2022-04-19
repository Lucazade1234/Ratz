import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.awt.event.ActionEvent;

public class Settings{
    MenuController menuController;
    int difficultyStatus = 0;

    @FXML
    private RadioButton easyButton = new RadioButton();
    @FXML
    private RadioButton mediumButton = new RadioButton();
    @FXML
    private RadioButton hardButton = new RadioButton();
    @FXML
    private ToggleGroup difficulties;


    public Settings(MenuController mainMenuController){
        this.menuController = mainMenuController;
    }

    public void setToEasy(){
        difficultyStatus = 0;
        System.out.println("changed to easy");
    }

    public void setToMedium(){
        difficultyStatus = 1;
        System.out.println("changed to medium");
    }

    public void setToHard(){
        difficultyStatus = 2;
        System.out.println("changed to hard");
    }
}
