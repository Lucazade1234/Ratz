import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main class to begin, and the one that handles the main menu
 * 
 * @author harvey
 * @version 1.0
 */
public class MainMenuController extends Application {
	// The dimensions of the window
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 500;

    // The various labels we'll use
	private Label loggedProfile;
	private Label loggedLabel;
	private Label scoresHeading;
	private Label[] profileScore;
	private Label[] scoresLabel;
	private Label scoreHeading;
	private Stage mainStage;
	private Scene mainScene;

	/**
	 * Launches the application
	 *
	 * @param args The arguments to launch with
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Called when we begin the application
	 *
	 * @param primaryStage The stage we're currently on
	 */
	@Override
	public void start(Stage primaryStage) {

        // Start the SeaShantySimulator
		SeaShantySimulator seaShantySimulator = new SeaShantySimulator();
		seaShantySimulator.initialize();
		seaShantySimulator.play();

        // Create the stage
		mainStage = primaryStage;
		primaryStage.setResizable(false);

		// Create the layout
		BorderPane root = new BorderPane();
		HBox bottom = getBottomRatsMain();
		HBox top = getTopRatsMain();

		// Create a scene based on the pane.
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		mainScene = scene;
        // Load the css
		File f = new File("source/menu.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

		VBox centre = getCentreMain(scene, primaryStage);

		root.setTop(top);
		root.setBottom(bottom);
		root.setCenter(centre);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

    /**
     * Returns the buttons in the centre of the main menu
     *
     * @param scene The scene we're currently on
     * @param primaryStage The current stage
     * @return The VerticalBox with all the buttons inside
     */
	private VBox getCentreMain(Scene scene, Stage primaryStage) {
        // Creates rhe base for the layout
		VBox centre = new VBox(5);
		ImageView ratzImageView = getRatzImageViewMain();

        // Sets the MOTD
		Label motd = new Label(MOTD.GETMotd());

        // Logged in text
		Label loggedProfileText = new Label("You are logged as ");
		loggedProfile = new Label();
		if (ProfileFileReader.getLoggedProfile() == null) {
			loggedProfile.setText("NOBODY. Please log in before starting the game");
		} else {
			loggedProfile.setText(ProfileFileReader.getLoggedProfile());
		}

        // Login css
		HBox loggedProfileBox = new HBox();
		loggedProfileBox.setAlignment(Pos.CENTER);
		loggedProfileBox.setStyle("-fx-text-fill: #Fd062a");
		loggedProfileBox.getChildren().addAll(loggedProfileText, loggedProfile);

        // Select level button
		Button playButton = new Button("Select level!");
		playButton.setPrefWidth(100);
		playButton.setOnAction(event -> {
			if (ProfileFileReader.getLoggedProfile() == null) {
				alert("You are not logged in.\nPlease log in before starting the game");
			} else {
				primaryStage.setScene(loadLevelSelect(primaryStage, scene));
				primaryStage.show();
			}
		});

        // Select profile button
		Button selectProfile = new Button("Select Profile!");
		selectProfile.setPrefWidth(100);
		selectProfile.setPadding(new Insets(10, 0, 0, 0));
		selectProfile.setOnAction(event -> {
			primaryStage.setScene(selectProfiles(primaryStage, scene));
			primaryStage.show();
		});

        // Button to quite the application
		Button exitButton = new Button("Exit!");
		exitButton.setPrefWidth(100);
		exitButton.setPadding(new Insets(10, 0, 0, 0));
		exitButton.setOnAction(event -> primaryStage.close());

        // Adds the items to the layout
		centre.getChildren().addAll(ratzImageView, motd, loggedProfileBox, playButton, selectProfile,
				exitButton);
		centre.setAlignment(Pos.CENTER);

		return centre;
	}

    /**
     * Displays the rats title
     * @return The image title
     */
	private ImageView getRatzImageViewMain() {
		FileInputStream inputstream = null;

		try {
			inputstream = new FileInputStream("resources/ratzLabel.png");
		} catch (FileNotFoundException ignored) { }

        assert inputstream != null;
        Image ratzImage = new Image(inputstream);

        return new ImageView(ratzImage);
	}

    /**
     * Displays the rats at the top of the menu
     *
     * @return The layout elements with the rats added
     */
	private HBox getTopRatsMain() {
		HBox top = new HBox();

		FileInputStream inputstream = null;
		try {
			inputstream = new FileInputStream("resources/adultmaleSOUTH.png");
		} catch (FileNotFoundException ignored) { }

        assert inputstream != null;
        Image image = new Image(inputstream);

		top.setAlignment(Pos.BASELINE_RIGHT);
		top.setSpacing(60);
		top.getChildren().addAll(newImageViews(image));
		return top;
	}

    /**
     * Generates the layout element for the rats at the bottom of the main menu
     *
     * @return The layout element to display the rats at the bottom of the main menu
     */
	private HBox getBottomRatsMain() {
		HBox bottom = new HBox();
		FileInputStream inputstream = null;
		try {
			inputstream = new FileInputStream("resources/adultfemaleNORTH.png");
		} catch (FileNotFoundException ignored) { }
        assert inputstream != null;
        Image image = new Image(inputstream);

		bottom.setSpacing(60);
		bottom.getChildren().addAll(newImageViews(image));
		return bottom;
	}

    /**
     * Generates the image views for the rats on the main menu
     *
     * @param image The Image to generate the image views for
     * @return The ImageViews for the rats
     */
    private ImageView[] newImageViews(Image image) {
        ImageView[] result = new ImageView[6];

        for(int i = 0; i < result.length; i++)
        {
            result[i] = new ImageView(image);
        }

        return result;
    }

    /**
     * Creates the menu to select a profile
     *
     * @param profileStage The stage of the menu
     * @param scene The current scene
     * @return The Scene to set to display the select profiles menu
     */
	public Scene selectProfiles(Stage profileStage, Scene scene) {
		// Create reader if we don't have one yet

		// Layout items
		BorderPane profilePane = new BorderPane();

        // Generate each part of the layout
		HBox top = getTopLogin();
		HBox selectMenuPics = getSelectMenuPicsLogin();
		VBox centre = getCentreLogin();
		centre.getChildren().add(selectMenuPics);
		VBox left = getLeftLogin();
		VBox right = getRightLogin(profileStage, scene, left);
		HBox bottom = getBottomLogin(left);

		// Adds the elements to the layout
		profilePane.setCenter(centre);
		profilePane.setTop(top);
		profilePane.setRight(right);
		profilePane.setLeft(left);
		profilePane.setBottom(bottom);

        // Adds the css for the menu
		Scene profileScene = new Scene(profilePane, WINDOW_WIDTH, WINDOW_HEIGHT);
		File f = new File("source/menu.css");
		profileScene.getStylesheets().clear();
		profileScene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

		return profileScene;
	}

    /**
     * Generates the Add profile elements at the bottom of the login screen
     *
     * @param left The left box on the profiles' menu layout
     * @return The layout element for the profiles menu
     */
	private HBox getBottomLogin(VBox left) {
        // Generate base layout
		HBox bottom = new HBox(20);
		bottom.setAlignment(Pos.TOP_CENTER);
		bottom.setPadding(new Insets(10, 80, 30, 0));

		// Generates the elements
		Label newProfLabel = new Label("Add new profile: ");
		TextField newProfField = new TextField();
		Button addProfButton = new Button("Add");
		addProfButton.setPrefWidth(70);
        // Adds the actions to the button
		addProfButton.setOnAction(event -> {
			try {
                // Check we don't have too many profiles already
				if (left.getChildren().size() > 8) {
					alert("Too many profiles!");
                    // Check there's at least something in the text box
                    // and the profiles doesn't already exist
				} else if (!newProfField.getText().equals("")
						&& !ProfileFileReader.doesProfileExist(newProfField.getText())) {

					ProfileFileReader.createNewProfile(newProfField.getText());

                    // Add the new profile's button
					Button newProfButton = new Button(newProfField.getText());
					newProfButton.setPrefWidth(100);
					ProfileFileReader.loginProfile(newProfButton.getText());

                    // Add the login action to the button
					newProfButton.setOnAction(event2 -> {
						// Logging in
						ProfileFileReader.loginProfile(newProfButton.getText());
						// Changing a labels
						displayProfileBests(loggedLabel, scoresHeading, profileScore);
					});
					left.getChildren().add(newProfButton);
				}
                // If the profile already exists
                else if (!newProfField.getText().equals("")) {
                    alert("Profile already exists");
                }
                // There's nothing in the text box
				else {
					alert("Please, type a name");
				}

			} catch (Exception e) {
				System.out.println("Problem here/ Adding button action");
			}
		});

		bottom.getChildren().addAll(newProfLabel, newProfField, addProfButton);
		return bottom;
	}

    /**
     * Creates the layout for the right of the profiles' login menu
     *
     * @param profileStage The profileMenu stage
     * @param scene The current scene
     * @param left The left layout element
     * @return The layout element with to return to main menu
     */
	private VBox getRightLogin(Stage profileStage, Scene scene, VBox left) {
		VBox right = new VBox(10);
		right.setAlignment(Pos.TOP_CENTER);
		right.setPadding(new Insets(10, 100, 10, 0));
		// Return to the main menu
		Button goBack = new Button("Main menu");
		goBack.setPrefWidth(100);
		goBack.setOnAction(event -> {
			profileStage.setScene(scene);
			profileStage.show();
		});

		// Removes a profile
		Button removeProfile = new Button("Remove profile");
		removeProfile.setMinWidth(100);
		removeProfile.setOnAction(event -> {
			try {
				ProfileFileReader.deleteProfile(ProfileFileReader.getLoggedProfile());
				HighScores.deleteProfile(ProfileFileReader.getLoggedProfile());
				ObservableList<Node> obL = left.getChildren();

				// remove profile button and change the labels
				boolean isRemoved = false;
				for (int i = 0; i < obL.size(); i++) {
					if (obL.get(i).toString().contains("'" + ProfileFileReader.getLoggedProfile() + "'")) {
						isRemoved = true;
						obL.remove(obL.get(i));
						loggedLabel.setText("...");
						scoresHeading.setText("Best ...'s scores:");

						for (int j = 0; j < ProfileFileReader.getNumberOfLevels(); j++) {
							profileScore[j].setText("Lvl" + (j + 1) + " 0");
						}
					}
				}
				if (!isRemoved) {
					alert("No profile is selected");
				}

				ProfileFileReader.logout();
				loggedProfile.setText("NOBODY. Please log in before starting the game");
			} catch (IOException ignored) {
			}
		});
		right.getChildren().addAll(goBack, removeProfile);

		return right;
	}

    /**
     * Generates the layout and buttons for the profiles on the profile menu
     *
     * @return The layout for the profile buttons
     */
	private VBox getLeftLogin() {
        // Generates the base layout
		VBox left = new VBox(10);
		left.setAlignment(Pos.TOP_CENTER);
		left.setPadding(new Insets(10, 10, 10, 40));

		// Get the profiles
		String[] s = { "" };
		try {
			s = ProfileFileReader.getProfiles();
		} catch (FileNotFoundException e) {
			s[0] = "No profiles. Please Create a profile";
		}

		// Display a button for each profile
		Button[] profButton = new Button[s.length];
		for (int i = 0; i < profButton.length; i++) {
			profButton[i] = new Button(s[i]);
			profButton[i].setPrefWidth(100);
			left.getChildren().add(profButton[i]);

			final int buttonIndex = i;
            // Adds the action for each button
			profButton[i].setOnAction(event -> {
				ProfileFileReader.loginProfile(profButton[buttonIndex].getText());
				loggedProfile.setText(ProfileFileReader.getLoggedProfile());
				displayProfileBests(loggedLabel, scoresHeading, profileScore);
			});
		}
		return left;
	}

    /**
     * Generates the display for the poison images on the profiles menu
     *
     * @return The layout element for the images
     */
	private HBox getSelectMenuPicsLogin() {
		FileInputStream inputstream = null;
		try {
			inputstream = new FileInputStream("resources/poison.png");
		} catch (FileNotFoundException ignored) { }
        assert inputstream != null;
        Image image = new Image(inputstream);
		ImageView imageView = new ImageView(image);
		ImageView imageView2 = new ImageView(image);
		HBox selectMenuPics = new HBox();
		selectMenuPics.setSpacing(100);
		selectMenuPics.setPadding(new Insets(40, 130, 0, 160));
		selectMenuPics.getChildren().addAll(imageView, imageView2);
		return selectMenuPics;
	}

    /**
     * Displays the best scores for the selected user
     *
     * @return The layout element with the high scores
     */
	private VBox getCentreLogin() {
        // Generate the base layout
		VBox centre = new VBox(10);
		centre.setAlignment(Pos.TOP_CENTER);
		centre.setPadding(new Insets(5, 0, 5, 0));

        // Get the scores for the profile
		profileScore = new Label[ProfileFileReader.getNumberOfLevels()];

        // TODO - Comment
		if (ProfileFileReader.getLoggedProfile() != null) {
			scoresHeading = new Label("Best " + ProfileFileReader.getLoggedProfile() + "'s scores:");
            centre.getChildren().add(scoresHeading);

			boolean unlocked = true;
			for (int i = 0; i < profileScore.length; i++) {
				try {
					if (ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1) > 0 && unlocked) {
						profileScore[i] = new Label("Lvl" + (i + 1) + " "
								+ ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1));
					} else if (ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1) == 0
							&& unlocked) {
						profileScore[i] = new Label("Lvl" + (i + 1) + " "
								+ ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1));
						unlocked = false;
					} else {
						profileScore[i] = new Label("Lvl" + (i + 1) + " is locked");
					}
				} catch (IOException e) {
					profileScore[i] = new Label("Lvl" + (i + 1) + " unknown error");
				}
				centre.getChildren().add(profileScore[i]);
			}
		} else {
			scoresHeading = new Label("Best ... scores:");
			centre.getChildren().add(scoresHeading);

			for (int i = 0; i < profileScore.length; i++) {
				profileScore[i] = new Label("Lvl" + (i + 1) + " 0");
				centre.getChildren().add(profileScore[i]);
			}
		}
		return centre;
	}

    /**
     * Generate the headers for the profiles' menu
     *
     * @return The layout containing the title and logged in text
     */
	private HBox getTopLogin() {
		HBox top = new HBox();

        // Generates the logged in label
		Label loggedLabelText = new Label("You are logged as ");
		loggedLabelText.getStyleClass().add("loggingLabel");
		loggedLabel = new Label();
		loggedLabel.getStyleClass().add("loggingLabel");
		loggedLabel.setStyle("-fx-text-fill: #Fd062a");

        // Displays the profile currently logged in
		if (ProfileFileReader.getLoggedProfile() == null) {
			loggedLabel.setText("...");
		} else {
			loggedLabel.setText(ProfileFileReader.getLoggedProfile());
		}

		top.getChildren().addAll(loggedLabelText, loggedLabel);
		top.setAlignment(Pos.CENTER);
		return top;
	}

    /**
     *
     *
     * @param loggedLabel
     * @param scoresHeading
     * @param profileScore
     */
	private void displayProfileBests(Label loggedLabel, Label scoresHeading, Label[] profileScore) {
		loggedLabel.setText(ProfileFileReader.getLoggedProfile());
		scoresHeading.setText("Best " + ProfileFileReader.getLoggedProfile() + "'s scores:");

		boolean unlocked = true;
		for (int i = 0; i < profileScore.length; i++) {
			try {
				if (ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1) > 0) {
					profileScore[i].setText("Lvl" + (i + 1) + " "
							+ ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1));
				} else if (ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1) == 0
						&& unlocked) {
					profileScore[i].setText("Lvl" + (i + 1) + " "
							+ ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1));
					unlocked = false;
				} else {
					profileScore[i].setText("Lvl" + (i + 1) + " is locked");
				}
			} catch (IOException e) {
				profileScore[i].setText("Lvl" + (i + 1) + " unknown error");
			}
		}
	}

    /**
     * Generates a popup alert
     *
     * @param message The message to display in the alert
     */
	private void alert(String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Alert");
		window.setWidth(300);
		window.setHeight(150);

		Label label = new Label(message);
		Button button = new Button("Ok");
		button.setPrefWidth(100);
		button.setOnAction(event -> window.close());

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, button);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		File f = new File("source/menu.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        window.setScene(scene);
		window.setResizable(false);
		window.showAndWait();
	}

	/**
	 * Displays the level select screen
	 *
	 * @param selectStage the stage
	 */
	private Scene loadLevelSelect(Stage selectStage, Scene scene) {
		BorderPane root = new BorderPane();

		AtomicInteger selectedLevel = new AtomicInteger(1);

		VBox topBox = getTopLevel();
		VBox centreBox = getCentreLevel(selectedLevel);
		VBox leftBox = getLeftLevel(selectedLevel);
		VBox rightBox = getRightLevel(selectStage, scene, selectedLevel);
		HBox bottomBox = getBottomLevel();
		
		root.setCenter(centreBox);
		root.setTop(topBox);
		root.setRight(rightBox);
		root.setLeft(leftBox);
		root.setBottom(bottomBox);

		Scene levelsScene = new Scene(root, 800, 500);

		File f = new File("source/menu.css");
		levelsScene.getStylesheets().clear();
		levelsScene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

		return levelsScene;
	}

    /**
     * 
     *
     * @return
     */
	private HBox getBottomLevel() {
		HBox bottomBox = new HBox(82);
		bottomBox.setPrefHeight(80);
		FileInputStream inputs1 = null;
		FileInputStream inputs2 = null;
		try {
			inputs1 = new FileInputStream("resources/childratEAST.png");
			inputs2 = new FileInputStream("resources/deathratEAST.png");
		} catch (FileNotFoundException ignored) { }
        assert inputs1 != null;
        assert inputs2 != null;
        Image imageB1 = new Image(inputs1);
        Image imageB2 = new Image(inputs2);
        ImageView[] imageViews = new ImageView[6];
        for (int i = 0; i < imageViews.length; i++)
        {
            switch (i % 2)
            {
                case 0:
                    imageViews[i] = new ImageView(imageB1);
                    break;
                case 1:
                    imageViews[i] = new ImageView(imageB2);
            }
        }

		bottomBox.getChildren().addAll(imageViews);
		return bottomBox;
	}

    /**
     *
     * @param selectStage
     * @param scene
     * @param selectedLevel
     * @return
     */
	private VBox getRightLevel(Stage selectStage, Scene scene, AtomicInteger selectedLevel) {
		VBox rightBox = new VBox(5);
		rightBox.setAlignment(Pos.CENTER_LEFT);
		rightBox.setPrefWidth(180);
		rightBox.setPadding(new Insets(5, 0, 5, 0));

		FileInputStream[] inputStreams = new FileInputStream[3];
		try {
			inputStreams[0] = new FileInputStream("resources/bomb1.png");
			inputStreams[1] = new FileInputStream("resources/bomb3.png");
			inputStreams[2] = new FileInputStream("resources/bomb4.png");
		} catch (FileNotFoundException ignored) { }
        assert inputStreams[0] != null;
        Image image1 = new Image(inputStreams[0]);
        assert inputStreams[1] != null;
        Image image2 = new Image(inputStreams[1]);
        assert inputStreams[2] != null;
        Image image3 = new Image(inputStreams[2]);

        ImageView[] imageViews = new ImageView[3];
        imageViews[0] = new ImageView(image1);
        imageViews[1] = new ImageView(image2);
        imageViews[2] = new ImageView(image3);

		Button playButton = new Button("Play!");
		playButton.setPrefWidth(80);
		playButton.setOnAction(event -> {
			try {
				// TODO - Get the level number
				loadLevel(selectStage, selectedLevel.get());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Button backToMenu = new Button("Main Menu");
		backToMenu.setOnAction(event -> {
			selectStage.setScene(scene);
			selectStage.show();
		});
		backToMenu.setPrefWidth(85);

		rightBox.getChildren().addAll(imageViews[0], playButton, imageViews[1], backToMenu, imageViews[2]);
		return rightBox;
	}

    /**
     *
     * @param selectedLevel
     * @return
     */
	private VBox getLeftLevel(AtomicInteger selectedLevel) {
		VBox leftBox = new VBox(10);
		leftBox.setAlignment(Pos.CENTER_RIGHT);
		leftBox.setPrefWidth(180);

		boolean[] isUnlocked = new boolean[5];
		boolean unlocked = true;
		for (int i = 0; i < ProfileFileReader.getNumberOfLevels(); i++) {
			try {
				if (ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1) > 0) {
					isUnlocked[i] = true;
				} else if (ProfileFileReader.getBestScore(ProfileFileReader.getLoggedProfile(), i + 1) == 0
						&& unlocked) {
					isUnlocked[i] = true;
					unlocked = false;
				} else {
					isUnlocked[i] = false;
				}
			} catch (Exception e) {
				isUnlocked[i] = false;
			}
		}

		Button[] lvl = new Button[5];
		for (int i = 0; i < lvl.length; i++) {
			int levelIndex = i + 1;
			lvl[i] = new Button("Level " + (levelIndex));
			lvl[i].setPrefWidth(70);

			if (isUnlocked[i]) {
				lvl[i].setOnAction(event -> {
					scoreHeading.setText("Lvl " + (levelIndex) + " best scores:");
					selectedLevel.set(levelIndex);

					String[] newScores = null;
					try {
						newScores = HighScores.getTopScores(levelIndex);
					} catch (FileNotFoundException ignored) {
					}

					for (int j = 0; j < 10; j++) {
						try {
							assert newScores != null;
							scoresLabel[j].setText((j + 1) + " " + newScores[j]);
						} catch (Exception e2) {
							scoresLabel[j].setText((j + 1) + " ...");
						}
					}
				});
			} else {
				lvl[i].setOnAction(event -> alert("You haven't unlocked this level"));
				lvl[i].getStyleClass().add("buttonBlocked");
			}

			leftBox.getChildren().add(lvl[i]);
		}
		return leftBox;
	}

    /**
     *
     * @param selectedLevel
     * @return
     */
	private VBox getCentreLevel(AtomicInteger selectedLevel) {
		VBox centreBox = new VBox();
		centreBox.setAlignment(Pos.CENTER);

		scoreHeading = new Label("Lvl " + selectedLevel + " best scores:");
		scoreHeading.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold");
		centreBox.getChildren().add(scoreHeading);

		scoresLabel = new Label[10];
		String[] scoresString = null;
		try {
			scoresString = HighScores.getTopScores(selectedLevel.get());
		} catch (FileNotFoundException ignored) {
		}

		for (int i = 0; i < 10; i++) {
			scoresLabel[i] = new Label();
			scoresLabel[i].setPadding(new Insets(3, 0, 3, 0));
			try {
				assert scoresString != null;
				scoresLabel[i].setText((i + 1) + " " + scoresString[i]);
			} catch (Exception e2) {
				scoresLabel[i].setText((i + 1) + " ...");
			}
			centreBox.getChildren().add(scoresLabel[i]);
		}
		return centreBox;
	}

    /**
     *
     * @return
     */
	private VBox getTopLevel() {
		VBox topBox = new VBox();
		topBox.setAlignment(Pos.CENTER);
		topBox.setPrefHeight(80);
		Label title = new Label("Level Select");
		Separator separator = new Separator();
		title.getStyleClass().add("loggingLabel");
		topBox.getChildren().addAll(title, separator);
		return topBox;
	}

	/**
	 * Loads a level through the LevelController
	 *
	 * @param levelStage The stage
	 * @throws IOException If we cannot load a level
	 */
	private void loadLevel(Stage levelStage, int levelNumber) throws IOException {
		LevelFileReader.loadLevelFile("./resources/level-" + levelNumber);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("level.fxml"));
		LevelController levelController = new LevelController(levelNumber, this);

		loader.setController(levelController);

		Pane root = loader.load();

		Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

		levelStage.setScene(scene);
	}

    /**
     * Called when a level is finished
     */
	public void finishLevel() {
		mainStage.setScene(mainScene);
		mainStage.show();
	}
}
