import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

/**
 * Class that implements a playable level.
 *
 * @author Vilija Pundyte
 * @author Luca Khatri
 * @version 1.1
 */

public class LevelController {

    private static final int ITEM_NUM = 8;
    private static final int TILE_SIZE = 64;
    private static int[] counters = new int[ITEM_NUM];

    private static final String defaultLevelRegex = "level-[1-5]";

    // For sounds
    private static final String DEATH_RAT_SOUND_1_PATH = "deathRatSound1.mp3";
    private static final String DEATH_RAT_SOUND_2_PATH = "deathRatSound2.mp3";
    private static final String DEATH_RAT_SOUND_3_PATH = "deathRatSound3.mp3";
    private static final double SOUND_VOLUME_RAT = 0.1f;

    // Game map
    private static Tile[][] tileMap = new Tile[0][0];

    private static int score;
    private static int currentTimeLeft;

    // Rat counters
    private static int femaleRatCounter;
    private static int maleRatCounter;
    private static int otherRatCounter;

    // For loading inventory from save
    private static int[] savedPowers;
    private Boolean powersImportedFromSave = true;

    // Images for different game items
    private final List<Image> itemImages = Arrays.asList((new Bomb(0, 0)).getImg(), (new Gas(0, 0, true)).getImg(),
            (new Lava(0,0,true)).getImg(), (new Poison(0, 0)).getImg(), (new MaleSwapper(0, 0)).getImg(),
                    (new FemaleSwapper(0, 0)).getImg(), (new StopSign(0, 0)).getImg(),
                    (new DeathRat(0, Rat.Direction.WEST, 0, 0, 0, 0)).getImg());

    // Size of game map
    private final int WIDTH;
    private final int HEIGHT;

    // Game losing conditions (maximum number of rats, time taken for a level)
    private int MAX_RATS;
    private int PAR_TIME;

    private int[] DROP_RATES = new int[ITEM_NUM];
    private final int[] timeUntilDrop = new int[ITEM_NUM];

    private final MenuController MAIN_MENU;
    private final String LEVEL_NAME;

    // Milliseconds between frames
    private int FRAME_TIME = 250;

    // Item toolbars
    private List<HBox> toolbars;

    // Game timeline
    private Timeline tickTimeline;

    @FXML
    public Canvas levelCanvas; // Game map canvas

    public Button saveLevelStateButton;

    // Toolbars for different game items
    public HBox bombToolbar;
    public HBox gasToolbar;
    public HBox sterilisationToolbar;
    public HBox poisonToolbar;
    public HBox maleSwapToolbar;
    public HBox femaleSwapToolbar;
    public HBox stopSignToolbar;
    public HBox deathRatToolbar;

    public Label timerLabel;
    public Label femaleRatCounterLabel;
    public Label maleRatCounterLabel;
    public Label ratCounterLabel;

    public Pane gameEndPane;
    public TextFlow gamePaneText;
    public TextFlow gamePaneScore;
    public TextFlow gamePaneLeaderboard;

    public Pane saveLevelPane;
    public TextField levelNameTextField;
    public Text savingErrorText;
    public Button saveAndExitButton;
    DifficultyClass difficulty;

    /**
     * Constructor for LevelController class.
     *
     * @param selectedLevelName  Number of level being played.
     * @param mainMenuController Reference to the main menu controller.
     */
    public LevelController(String selectedLevelName, MenuController mainMenuController, DifficultyClass difficulty) {
        LEVEL_NAME = selectedLevelName;
        MAIN_MENU = mainMenuController;
        WIDTH = LevelFileReader.getWidth();
        HEIGHT = LevelFileReader.getHeight();

        MAIN_MENU.setLevelController(this);

        this.difficulty = difficulty;

        System.out.println("new level controller made");

        if (this.difficulty instanceof EasyDifficulty) {
            System.out.println("the difficulty is Easy");
        } else if (this.difficulty instanceof MediumDifficulty) {
            System.out.println("the difficulty is Medium");
        } else if (this.difficulty instanceof HardDifficulty) {
            System.out.println("the difficulty is Hard");
        }


        if (this.difficulty instanceof EasyDifficulty) {
            MAX_RATS = difficulty.getPopulationCap();
            PAR_TIME = difficulty.getTime();
            FRAME_TIME = difficulty.getFrameTime();
            DROP_RATES = difficulty.getDropRate();
            counters = difficulty.getCounters();

        } else if (this.difficulty instanceof MediumDifficulty) {
            MAX_RATS = difficulty.getPopulationCap();
            PAR_TIME = difficulty.getTime();
            FRAME_TIME = difficulty.getFrameTime();
            DROP_RATES = difficulty.getDropRate();
            counters = difficulty.getCounters();


        } else if (this.difficulty instanceof HardDifficulty) {
            MAX_RATS = difficulty.getPopulationCap();
            PAR_TIME = difficulty.getTime();
            FRAME_TIME = difficulty.getFrameTime();
            DROP_RATES = difficulty.getDropRate();
            counters = difficulty.getCounters();


        }

        buildNewLevel();


        //DROP_RATES = LevelFileReader.getDropRates();

        System.out.println("The rat cap is: " + MAX_RATS);


    }

    /**
     * Returns current timer.
     *
     * @return Time in milliseconds.
     */
    public static int getCurrentTimeLeft() {
        return currentTimeLeft;
    }

    /**
     * Returns current item counters.
     *
     * @return number of each item.
     */
    public static int[] getCounters() {
        return counters;
    }

    /**
     * Initializes game.
     */
    public void initialize() {
        currentTimeLeft = PAR_TIME * 1000;
        timerLabel.setText(millisToString(currentTimeLeft));

        toolbars = Arrays.asList(bombToolbar, gasToolbar, sterilisationToolbar, poisonToolbar, maleSwapToolbar,
                femaleSwapToolbar, stopSignToolbar, deathRatToolbar);

        //Arrays.fill(counters, 2);

        renderAllItems();
        setupCanvasDragBehaviour();

        renderGame();

        if (LevelFileReader.getHasLoadedSavedLevel()) {
            System.arraycopy(LevelFileReader.getInProgInv(), 0, timeUntilDrop, 0, timeUntilDrop.length);
        } else {
            System.arraycopy(DROP_RATES, 0, timeUntilDrop, 0, timeUntilDrop.length);
        }

        tickTimeline = new Timeline(new KeyFrame(Duration.millis(FRAME_TIME), event -> tick()));
        tickTimeline.setCycleCount(Animation.INDEFINITE);
        tickTimeline.play();

        // Start the SeaShantySimulator (music player)
        SeaShantySimulator seaShantySimulator = new SeaShantySimulator();
        seaShantySimulator.initialize();
        seaShantySimulator.play();
    }

    /**
     * Builds new level from level file.
     */
    private void buildNewLevel() {
        otherRatCounter = 0;
        femaleRatCounter = 0;
        maleRatCounter = 0;
        score = 0;

        tileMap = LevelFileReader.getTileMap();

        for (Rat rat : LevelFileReader.getRatSpawns()) {
            ratAdded(rat);
        }
    }

    /**
     * Renders female and male counters on the screen.
     */
    public void renderCounters() {
        String mc = String.valueOf(maleRatCounter);
        String fc = String.valueOf(femaleRatCounter);
        String rc = (femaleRatCounter + maleRatCounter + otherRatCounter) + "/" + (MAX_RATS);

        maleRatCounterLabel.setText(mc);
        femaleRatCounterLabel.setText(fc);
        ratCounterLabel.setText(rc);
    }

    /**
     * Returns tile at position (x,y).
     *
     * @param x Horizontal position.
     * @param y Vertical position.
     * @return Tile at position.
     */
    public static Tile getTileAt(int x, int y) {
        try {
            return tileMap[x][y];
        } catch (Exception ArrayIndexOutOfBoundsException) {
            return null;
        }
    }

    /**
     * Returns whether tile at position (x,y) on canvas can be interacted with.
     *
     * @param x canvas x position.
     * @param y canvas y position.
     * @return interactivity.
     */
    private boolean tileInteractivityAt(double x, double y) {
        if (x >= (WIDTH * TILE_SIZE) || y >= (HEIGHT * TILE_SIZE)) {
            return false;
        } else {
            int xPos = (int) (Math.floor(x) / TILE_SIZE);
            int yPos = (int) (Math.floor(y) / TILE_SIZE);

            return tileMap[xPos][yPos].isInteractive();
        }
    }

    /**
     * Converts milliseconds (as int) to a string of format mm:ss.
     *
     * @param millis milliseconds as int.
     * @return String mm:ss.
     */
    public String millisToString(int millis) {
        int seconds = millis / 1000;
        int minutes = (int) TimeUnit.SECONDS.toMinutes(seconds);
        int remainSeconds = seconds - (int) TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, remainSeconds);
    }

    /**
     * Periodically refreshes game.
     */
    public void tick() {
        if ((femaleRatCounter + maleRatCounter + otherRatCounter) == 0) {
            endGame(true);
        } else if ((femaleRatCounter + maleRatCounter + otherRatCounter) >= MAX_RATS) {
            endGame(false);
        } else {
            addPowers();

            for (Tile[] tiles : tileMap) {
                for (Tile tile : tiles) {
                    tile.update();
                }
            }

            renderGame();
            renderCounters();

            if (currentTimeLeft > 0) {
                currentTimeLeft = currentTimeLeft - FRAME_TIME;
                timerLabel.setText(millisToString(currentTimeLeft));
            }
        }
    }

    /**
     * Copies power data to current level.
     *
     * @param powers power data.
     */
    public static void addPowersFromSave(int[] powers) {
        savedPowers = powers;
    }

    /**
     * Grants player items periodically.
     */
    private void addPowers() {

        int status = MenuController.getStatus();

        // Add powers to inventory from file save
        if (savedPowers != null && powersImportedFromSave) {
            for (int i = 0; i < savedPowers.length; i++) {
                while (savedPowers[i] > 0) {
                    //counters[i]++;
                    renderItem(i);
                    savedPowers[i] -= 1;
                }
            }
            powersImportedFromSave = false;
        }

        for (int i = 0; i < counters.length; i++) {

            timeUntilDrop[i] -= FRAME_TIME;

            if (i == 0) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getBombCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            } else if (i == 1) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getGasCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            } else if (i == 2) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getPoisonCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            } else if (i == 3) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getPoisonCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            } else if (i == 4) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getMaleSwapperCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            } else if (i == 5) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getFemaleSwapperCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            } else if (i == 6) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getStopSignCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            } else if (i == 7) {
                if (timeUntilDrop[i] <= 0 && counters[i] < difficulty.getDeathRatCap()) {
                    counters[i]++;
                    timeUntilDrop[i] = DROP_RATES[i];
                    renderItem(i);
                }
            }

        }
    }

    /**
     * Ends game in a win/lose scenario.
     *
     * @param wonGame whether level was won.
     */
    private void endGame(boolean wonGame) {
        tickTimeline.stop();
        disableToolbars();
        saveLevelStateButton.setDisable(true);

        gameEndPane.setVisible(true);
        saveAndExitButton.setVisible(false);

        if (wonGame) {
            score += currentTimeLeft / 1000;
            gamePaneText.getChildren().add(new Text("You've won! :)"));
            gamePaneScore.getChildren().add(new Text("Score: " + score));
            ProfileFileReader.saveScore(ProfileFileReader.getLoggedProfile(), LEVEL_NAME, score);
            HighScores.saveScore(ProfileFileReader.getLoggedProfile(), LEVEL_NAME, score);
        } else {
            gamePaneText.getChildren().add(new Text("You've lost! :("));
        }

        String[] highScores = HighScores.getTopScores(LEVEL_NAME);
        for (String text : highScores) {
            gamePaneLeaderboard.getChildren().add(new Text(text + "\n"));
        }
    }

    /**
     * Exits level and goes back to main menu.
     */
    @FXML
    private void exitGame() {
        MAIN_MENU.finishLevel();
    }

    /**
     * Lets interactive tiles on the game screen accept drag and drop events.
     */
    private void setupCanvasDragBehaviour() {

        levelCanvas.setOnDragOver(event -> {
            // Mark the drag as acceptable if the source is a draggable ImageView
            if (event.getGestureSource() instanceof ImageView) {
                if (tileInteractivityAt(event.getSceneX(), event.getSceneY()))
                    event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });

        // This code allows the canvas to react to a dragged object when it is finally
        // dropped.
        levelCanvas.setOnDragDropped(event -> {
            String dbContent = event.getDragboard().getString();
            try {
                int index = Integer.parseInt(dbContent);
                if (index >= 0 && index < ITEM_NUM) {
                    itemDropped(event, index);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * Renders current tile map.
     */
    private void renderGame() {
        GraphicsContext gc = levelCanvas.getGraphicsContext2D();
        if (tileMap != null) {
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j].draw(i, j, gc);
                }
            }
        }
    }

    /**
     * Renders all item toolbars.
     */
    private void renderAllItems() {
        for (int i = 0; i < counters.length; i++) {
            renderItem(i);
        }
    }

    /**
     * Adds item dropped by the player onto a Tile.
     *
     * @param event Drag and drop event.
     * @param index Which item has been dragged
     */
    private void itemDropped(DragEvent event, int index) {
        int x = (int) event.getX() / TILE_SIZE;
        int y = (int) event.getY() / TILE_SIZE;

        Power power = null;
        boolean addPower = true;
        switch (index) {
            case 0:
                power = new Bomb(x, y);
                break;
            case 1:
                power = new Gas(x, y, true);
                break;
            case 2:
                power = new Lava(x, y, true);
                break;
            case 3:
                power = new Poison(x, y);
                break;
            case 4:
                power = new MaleSwapper(x, y);
                break;
            case 5:
                power = new FemaleSwapper(x, y);
                break;
            case 6:
                power = new StopSign(x, y);
                break;
            case 7:
                SeaShantySimulator seaSim = new SeaShantySimulator();
                int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
                if (randomNum == 1) {
                    seaSim.playAudioClip(DEATH_RAT_SOUND_1_PATH, SOUND_VOLUME_RAT);
                } else if (randomNum == 2) {
                    seaSim.playAudioClip(DEATH_RAT_SOUND_2_PATH, SOUND_VOLUME_RAT);
                } else {
                    seaSim.playAudioClip(DEATH_RAT_SOUND_3_PATH, SOUND_VOLUME_RAT);
                }

                tileMap[x][y].addOccupantRat(new DeathRat(Rat.getDEFAULT_SPEED(), Rat.Direction.NORTH, 0, x, y, 0));
                addPower = false;
                break;
            default:
                addPower = false;
        }
        if (addPower) {
            tileMap[x][y].addActivePower(power);
        }

        counters[index]--;
        renderItem(index);
    }

    /**
     * Saves level state (if a valid level name is entered).
     */
    @FXML
    public void saveLevel() {
        boolean canBeSaved = false;
        String newLevelName = levelNameTextField.getText();
        if (newLevelName.contains(" ")) {
            savingErrorText.setText("Level name cannot contain spaces");
        } else if (newLevelName.matches(defaultLevelRegex)) {
            savingErrorText.setText("Level name cannot be the same as default level");
        } else if (newLevelName.length() == 0) {
            savingErrorText.setText("Level name cannot be empty");
        } else {
            savingErrorText.setText("");
            canBeSaved = true;
        }

        if (canBeSaved) {
            try {
                LevelFileReader.saveLevel(newLevelName);
                makeScreenShot(newLevelName);
                System.out.println("Screenshot was saved");
            } catch (IOException e) {
                e.printStackTrace();
                savingErrorText.setText("An error occurred saving game state.");
            }
            MAIN_MENU.finishLevel();
        }
    }

    /**
     * Makes screenshot of current tilemap.
     *
     * @param levelName name of level being screenshot.
     */
    public void makeScreenShot(String levelName) {
        File file = new File("src\\main\\resources\\saved_games_images\\" + ProfileFileReader.getLoggedProfile() + "\\"
                + levelName + ".png");

        WritableImage writableImage = new WritableImage(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);
        SnapshotParameters params = new SnapshotParameters();
        levelCanvas.snapshot(params, writableImage);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (Throwable th) {
            System.out.println("Screenshot wasn' saved :(");

        }

    }

    /**
     * Actions performed when user decides to save the game and exit to main menu.
     */
    @FXML
    public void openSaveDialogue() {
        tickTimeline.stop();
        disableToolbars();
        saveLevelStateButton.setDisable(true);
        savingErrorText.setText("");
        levelNameTextField.setText(LEVEL_NAME + "-inProgress");
        saveLevelPane.setVisible(true);
    }

    /**
     * Returns user from dialogue box back to level.
     */
    public void goBackToLevel() {
        saveLevelPane.setVisible(false);
        saveLevelStateButton.setDisable(false);

        renderAllItems();

        tickTimeline = new Timeline(new KeyFrame(Duration.millis(FRAME_TIME), event -> tick()));
        tickTimeline.setCycleCount(Animation.INDEFINITE);
        tickTimeline.play();
    }

    /**
     * Renders toolbar of one specific item type.
     *
     * @param index the number representing an item in the toolbar
     */
    private void renderItem(int index) {
        toolbars.get(index).getChildren().clear();

        ImageView[] items = new ImageView[counters[index]];
        for (int i = 0; i < counters[index]; i++) {
            ImageView item = new ImageView(itemImages.get(index));
            items[i] = item;
            toolbars.get(index).getChildren().add(items[i]);
            makeDraggable(item, index);
        }
    }

    /**
     * Disables side menu items (makes them non-draggable)
     */
    private void disableToolbars() {
        for (int i = 0; i < toolbars.size(); i++) {
            toolbars.get(i).getChildren().clear();

            ImageView[] items = new ImageView[counters[i]];
            for (int j = 0; j < counters[i]; j++) {
                ImageView item = new ImageView(itemImages.get(i));
                items[j] = item;
                toolbars.get(i).getChildren().add(items[j]);
            }
        }
    }

    /**
     * Makes ImageView draggable.
     *
     * @param item      item that is being made draggable.
     * @param dbContent String used in setupCanvasDragBehaviour.
     */
    private static void makeDraggable(final ImageView item, int dbContent) {
        item.setOnDragDetected(event -> {
            Dragboard dragboard = item.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(String.valueOf(dbContent));
            dragboard.setContent(clipboardContent);
            event.consume();
        });
    }

    /**
     * Removes rat from a rat counter.
     *
     * @param rat the rat being removed.
     */
    public static void ratRemoved(Rat rat) {
        if (rat instanceof AdultMale) {
            maleRatCounter--;
        } else if (rat instanceof AdultFemale) {
            femaleRatCounter--;
        } else if (rat instanceof ChildRat || rat instanceof AdultIntersex) {
            otherRatCounter--;
        }
    }

    /**
     * Adds rat to rat counter.
     *
     * @param rat the rat being added.
     */
    public static void ratAdded(Rat rat) {
        if (rat instanceof ChildRat || rat instanceof AdultIntersex) {
            otherRatCounter++;
        } else if (rat instanceof AdultMale) {
            maleRatCounter++;
        } else if (rat instanceof AdultFemale) {
            femaleRatCounter++;
        }
    }

    /**
     * Removes rat that has been killed from rat counter and adds to score.
     *
     * @param rat rat that has been killed.
     */
    public synchronized static void ratKilled(Rat rat) {
        if (rat instanceof AdultFemale) {
            if (((AdultFemale) rat).getRatFetusCount() > 0) {
                for (int i = 0; i < ((AdultFemale) rat).getRatFetusCount(); i++) {
                    score += 10;
                }
            }
            score += 10;
            femaleRatCounter--;
        } else if (rat instanceof AdultMale) {
            score += 10;
            maleRatCounter--;
        } else if (rat instanceof ChildRat) {
            score += 10;
            otherRatCounter--;
        } else if (rat instanceof AdultIntersex) {
            if (((AdultIntersex) rat).getRatFetusCount() > 0) {
                for (int i = 0; i < ((AdultIntersex) rat).getRatFetusCount(); i++) {
                    score += 10;
                }
            }
            score += 10;
            otherRatCounter--;
        }
    }

}
