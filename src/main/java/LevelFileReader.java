import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * A class which reads levels from files, and saves levels to file.
 *
 * @author James McWilliams
 */
public class LevelFileReader {
    private static final float milli = 1000f;
    private static int height;
    private static int width;
    private static int maxRats;
    private static int parTime;
    private static final int[] DROP_RATES = new int[8];
    private static final ArrayList<Power> POWER_ARRAY_LIST = new ArrayList<>();
    private static final ArrayList<Rat> RAT_ARRAY_LIST = new ArrayList<>();
    private static Tile[][] tileMap;
    private static String levelPath;

    private static int inProgTimer;
    private static int[] inProgInv = new int[8];
    private static boolean hasLoadedSavedLevel;

    /**
     * Gets the height of the level
     *
     * @return the height of the level
     */
    public static int getHeight() {
        return height;
    }

    /**
     * Gets the width of the level
     *
     * @return the width of the level
     */
    public static int getWidth() {
        return width;
    }

    /**
     * Gets the rats that are currently in play
     *
     * @return An array of rats on the map
     */
    public static Rat[] getRatSpawns() {
        return RAT_ARRAY_LIST.toArray(new Rat[0]);
    }

    /**
     * Returns current powers on the map
     *
     * @return An array of powers on the map
     */
    public static Power[] getPowers() {
        return POWER_ARRAY_LIST.toArray(new Power[0]);
    }

    /**
     * Gets the maximum amount of rats that can appear in the level
     *
     * @return the maximum amount of rats before the level ends
     */
    public static int getMaxRats() {
        return maxRats;
    }

    /**
     * The time in seconds, the level has been active
     *
     * @return time in seconds since level start
     */
    public static int getParTime() {
        return parTime;
    }

    /**
     * Gets if the level has been saved previously
     *
     * @return if the level has been saved previously
     */
    public static boolean getHasLoadedSavedLevel() {
        return hasLoadedSavedLevel;
    }

    /**
     * dropRates will contain exactly 8 ints, each representing the odds of a
     * certain power being given to the player. These powers are, in order: Bombs,
     * Gas, Sterilisation items, Poison, Male sex changes, Female sex changes,
     * No-entry signs, and Death rats.
     *
     * @return dropRates the chances that each power will drop.
     */
    public static int[] getDropRates() {
        return DROP_RATES;
    }

    public static int getInProgTimer() {
        return inProgTimer;
    }

    /**
     * Gets the items in a player's saved inventory. The powers are in the following
     * order: Bombs, Gas, Sterilisation items, Poison, Male sex changes, Female sex
     * changes, No-entry signs, and Death rats.
     *
     * @return powers in the player's inventory
     */
    public static int[] getInProgInv() {
        return inProgInv;
    }

    /**
     * Writes a level to a .txt file in a format that can be read back by the file
     * reader.
     *
     * @param levelName The name for the level. Formatted as "level-X-USER.txt"
     *                  where X is a number and USER is the profile's name.
     * @throws IOException if it can't find the file specified.
     */
    public static void saveLevel(String levelName) throws IOException {
        File saveFile = new File("src/main/resources/levels/saved_games/" + ProfileFileReader.getLoggedProfile() + "/"
                + levelName + ".txt");
        // if an in progress file doesn't exist yet
//		if (!new File("src/main/resources/levels/saved_games/" + ProfileFileReader.getLoggedProfile() + "/" + levelName
//				+ ".txt").isFile()) {
//			try {
//				saveFile.createNewFile();
//			} catch (IOException e) {
//				System.out.println("Error occurred creating save file.");
//				e.printStackTrace();
//			}
//		}
        FileWriter writer = new FileWriter(saveFile);

        StringBuilder inventory = new StringBuilder();

        for (int i = 0; i < LevelController.getCounters().length; i++) {
            inventory.append(LevelController.getCounters()[i]).append(",");
        }

        StringBuilder allObjects = new StringBuilder();

        // add rats to file
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Objects.requireNonNull(LevelController.getTileAt(x, y)).getOccupantRats().size() > 0) {
                    for (Rat rat : Objects.requireNonNull(LevelController.getTileAt(x, y)).getOccupantRats()) {
                        allObjects.append("(").append(ratToStr(rat)).append(")\n");
                    }
                }
            }
        }

        // add powers to file
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Objects.requireNonNull(LevelController.getTileAt(x, y)).getActivePowers().size() > 0) {
                    for (Power power : Objects.requireNonNull(LevelController.getTileAt(x, y)).getActivePowers()) {
                        allObjects.append("(").append(powerToStr(power)).append(")\n");
                    }
                }
            }
        }

        int timeInSeconds = (int) Math.floor(LevelController.getCurrentTimeLeft() / milli);

        String fileString = String.format("%s\n%d\n%s\n%s\n", levelPath, timeInSeconds, inventory, allObjects);
        writer.write(fileString);
        writer.close();
    }

    /**
     * Turns a rat into a string that can be pasted into a level file to be loaded
     * later.
     *
     * @param rat The rat to convert
     * @return A string that can be read by the file reader
     */
    public static String ratToStr(Rat rat) {
        String type = null;
        String speed;
        String direction;
        String gasTimer;
        String xPos;
        String yPos;
        // the following items can't be named because they vary from rat to rat
        String item6;
        String item7;
        String item8;

        speed = Integer.toString(rat.getSpeed());
        direction = Integer.toString(directionEnumToInt(rat.getDirection()));
        gasTimer = Integer.toString(rat.getGasTimer());
        xPos = Integer.toString(rat.getXPos());
        yPos = Integer.toString(rat.getYPos());

        if (rat instanceof ChildRat) {

            if (((ChildRat) rat).getRatSex() == Rat.Sex.FEMALE) {
                type = "f";
            }

            if (((ChildRat) rat).getRatSex() == Rat.Sex.MALE) {
                type = "m";
            }

            if (((ChildRat) rat).getRatSex() == Rat.Sex.INTERSEX) {
                type = "i";
            }

            if (((ChildRat) rat).getFertile()) {
                item6 = "1";
            } else {
                item6 = "0";
            }
            item7 = Integer.toString(((ChildRat) rat).getAge());

            return type + "," + speed + "," + direction + "," + gasTimer + "," + xPos + "," + yPos + "," + item6 + ","
                    + item7;
        }

        if (rat instanceof AdultFemale) {
            type = "F";
            if (((AdultFemale) rat).getFertile()) {
                item6 = "1";
            } else {
                item6 = "0";
            }
            item7 = Integer.toString(((AdultFemale) rat).getPregnancyTime());
            item8 = Integer.toString(((AdultFemale) rat).getRatFetusCount());

            return type + "," + speed + "," + direction + "," + gasTimer + "," + xPos + "," + yPos + "," + item6 + ","
                    + item7 + "," + item8;
        }

        if (rat instanceof AdultMale) {
            type = "M";

            if (((AdultMale) rat).getFertile()) {
                item6 = "1";
            } else {
                item6 = "0";
            }

            return type + "," + speed + "," + direction + "," + gasTimer + "," + xPos + "," + yPos + "," + item6;

        }

        if (rat instanceof AdultIntersex) {
            type = "I";
            if (((AdultIntersex) rat).getFertile()) {
                item6 = "1";
            } else {
                item6 = "0";
            }
            item7 = Integer.toString(((AdultIntersex) rat).getPregnancyTime());
            item8 = Integer.toString(((AdultIntersex) rat).getRatFetusCount());

            return type + "," + speed + "," + direction + "," + gasTimer + "," + xPos + "," + yPos + "," + item6 + ","
                    + item7 + "," + item8;
        }

        if (rat instanceof DeathRat) {
            type = "D";

            item6 = Integer.toString(((DeathRat) rat).getKillCounter());

            return type + "," + speed + "," + direction + "," + gasTimer + "," + xPos + "," + yPos + "," + item6;
        }

        // if nothing has been returned yet, someone's added in some kind of new rat.
        return null;
    }

    /**
     * Turns a power into a string that can be pasted into a level file to be loaded
     * later.
     *
     * @param power The power to convert
     * @return A string that can be read by the file reader
     */
    public static String powerToStr(Power power) {
        String type;
        String xPos;
        String yPos;
        String special;

        xPos = Integer.toString(power.getXPos());
        yPos = Integer.toString(power.getYPos());

        if (power instanceof Bomb) {
            type = "B";
            special = String.valueOf(((Bomb) power).getTicksActive());
            return type + "," + xPos + "," + yPos + "," + special;
        }
        if (power instanceof Gas) {
            type = "G";
            special = String.valueOf(((Gas) power).getTicksActive());
            boolean isOriginal = ((Gas) power).isOriginal();
            return type + "," + xPos + "," + yPos + "," + isOriginal + "," + special;
        }
        if (power instanceof Sterilisation) {
            type = "S";
            special = String.valueOf(((Sterilisation) power).getTicksActive());
            return type + "," + xPos + "," + yPos + "," + special;
        }
        if (power instanceof Poison) {
            type = "P";
            return type + "," + xPos + "," + yPos;
        }
        if (power instanceof MaleSwapper) {
            type = "T";
            return type + "," + xPos + "," + yPos;
        }
        if (power instanceof FemaleSwapper) {
            type = "E";
            return type + "," + xPos + "," + yPos;
        }
        if (power instanceof StopSign) {
            type = "N";
            special = String.valueOf(((StopSign) power).getHP());
            return type + "," + xPos + "," + yPos + "," + special;
        }
        if (power instanceof Lava ) {
            type = "L";
            special = String.valueOf(((Lava) power).getTicksActive());
            boolean isOriginal = ((Lava) power).isOriginal();
            return type + "," + xPos + "," + yPos + "," + isOriginal + "," + special;
        }

        // if nothing has been returned yet, someone's added in a new power.
        return null;
    }

    /**
     * Converts a direction stored as an int into a Direction enum.
     *
     * @param directionInt int from 0-3 representing north, east, south, west
     * @return Direction
     */
    private static Rat.Direction directionIntToEnum(int directionInt) {
        switch (directionInt) {
            case 0:
                return Rat.Direction.NORTH;
            case 1:
                return Rat.Direction.EAST;
            case 2:
                return Rat.Direction.SOUTH;
            default:
                return Rat.Direction.WEST;
        }
    }

    /**
     * Converts a Direction enum to an int.
     *
     * @param directionEnum Direction
     * @return int from 0-3 representing north, east, south, west.
     */
    private static int directionEnumToInt(Rat.Direction directionEnum) {
        switch (directionEnum) {
            case NORTH:
                return 0;
            case EAST:
                return 1;
            case SOUTH:
                return 2;
            default:
                return 3;
        }
    }

    /**
     * Loads a level from a txt file. The "default objects" referred to by the
     * loadDefaultObjects parameter are the rats which spawn at the start of the
     * level. This should be false if this function is loading a saved level.
     *
     * @param filename           the level file to load
     * @param loadDefaultObjects whether the level's default objects should be
     *                           loaded
     * @throws FileNotFoundException if the level file isn't found
     */
    public static void loadNormalLevelFile(String filename, boolean loadDefaultObjects) throws FileNotFoundException {
        File levelData = new File(filename + ".txt");

        levelPath = filename;
        Scanner reader = new Scanner(levelData);

        RAT_ARRAY_LIST.clear();
        inProgInv = null;
        inProgTimer = -1;

        // get level width, height, max rats, par time
        if (reader.hasNextLine()) {
            String[] levelStats = reader.nextLine().split(",");
            width = Integer.parseInt(levelStats[0]);
            height = Integer.parseInt(levelStats[1]);
            maxRats = Integer.parseInt(levelStats[2]);
            parTime = Integer.parseInt(levelStats[3]);
        }

        // get drop rate data
        String[] dropRatesString = reader.nextLine().split(",");
        for (int i = 0; i < DROP_RATES.length; i++) {
            DROP_RATES[i] = Integer.parseInt(dropRatesString[i]);
        }

        // get tile data
        String currentTiles = "";
        for (int i = 0; i < height; i++) {
            if (reader.hasNext()) {
                currentTiles = currentTiles.concat(reader.nextLine());
            }
        }

        // this ugly regex splits currentTiles based on the level's width
        String[] tiles = currentTiles.split("(?<=\\G.{" + width + "})");

        tileMap = tilesToTileMap(tiles);

        hasLoadedSavedLevel = false;
        if (loadDefaultObjects) {
            readObjects(reader, loadDefaultObjects);
        }

        reader.close();

    }

    /**
     * Loads content from a level's save file.
     *
     * @param filename the level save file to load.
     * @throws FileNotFoundException if the file can't be found.
     */
    public static void loadSavedLevelFile(String filename) throws FileNotFoundException {
        File levelData = new File(filename + ".txt");
        Scanner reader = new Scanner(levelData);
        levelPath = reader.nextLine();
        loadNormalLevelFile(levelPath, false);
        inProgInv = new int[8];
        if (reader.hasNextLine()) {
            String[] savedInfo = reader.nextLine().split(",");
            inProgTimer = Integer.parseInt(savedInfo[0]);
            String[] inProgInvString = reader.nextLine().split(",");
            for (int i = 0; i < inProgInvString.length; i++) {
                inProgInv[i] = Integer.parseInt(inProgInvString[i]);
                LevelController.addPowersFromSave(inProgInv);
            }
            readObjects(reader, false);
        }
    }

    /**
     * Deletes all in progress files which are connected to chosen level.
     *
     * @param levelName name of the level
     */
    public static void deleteAllConnectedFiles(String levelName) {
        String savedGamesPath = "src/main/resources/levels/saved_games";

        File directoryPath = new File(savedGamesPath);
        String[] contents = directoryPath.list();

        if (contents != null) {
            for (String content : contents) {
                deleteAllConnectedFilesFromProfile(savedGamesPath + "/" + content, levelName, content);
            }
        }
    }

    /**
     * Deletes all in progress files (from profile directory) which are connected to chosen level.
     *
     * @param pathToProfileSaves path to file in progress
     * @param levelName          path to level file
     * @param profileName        name of the profile directory
     */
    public static void deleteAllConnectedFilesFromProfile(String pathToProfileSaves, String levelName,
                                                          String profileName) {
        File directoryPath = new File(pathToProfileSaves);
        String[] contents = directoryPath.list();

        if (contents != null) {
            for (String content : contents) {
                try {
                    if (isConnectedLevel(levelName, pathToProfileSaves + "/" + content)) {
                        new File(pathToProfileSaves + "/" + content).delete();
                        new File("src/main/resources/saved_games_images/" + profileName + "/"
                                + content.substring(0, content.length() - 4) + ".png").delete();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Check unexpected exception");
                }
            }
        }
    }

    /**
     * Checks if file in progress is connected to level file.
     *
     * @param levelName       directory to the level file
     * @param levelInProgress directory to the file in progress
     * @return true if file is connected, false otherwise
     * @throws FileNotFoundException if file in progress is missing
     */
    public static boolean isConnectedLevel(String levelName, String levelInProgress) throws FileNotFoundException {
        File levelData = new File(levelInProgress);
        Scanner reader = new Scanner(levelData);
        levelPath = reader.nextLine();
        reader.close();
        return levelPath.equals(levelName + ".txt");
    }

    /**
     * Reads rats and powers from the level file.
     *
     * @param reader The scanner to read data from
     */
    private static void readObjects(Scanner reader, boolean readRats) {
        while (reader.hasNextLine()) {
            String[] currentItem = reader.nextLine().replaceAll("[()]", "").split(",");
            // if current item is a female baby rat
            switch (currentItem[0]) {
                case "f": {
                    int speed = Integer.parseInt(currentItem[1]);
                    int directionInt = Integer.parseInt(currentItem[2]);
                    Rat.Direction direction = directionIntToEnum(directionInt);
                    int gasTimer = Integer.parseInt(currentItem[3]);
                    int xPos = Integer.parseInt(currentItem[4]);
                    int yPos = Integer.parseInt(currentItem[5]);
                    boolean isFertile;
                    isFertile = currentItem[6].equals("1");
                    int age = Integer.parseInt(currentItem[7]);
                    ChildRat newRat = new ChildRat(speed, direction, gasTimer, xPos, yPos, isFertile, age, Rat.Sex.FEMALE);
                    RAT_ARRAY_LIST.add(newRat);
                    tileMap[xPos][yPos].addOccupantRat(newRat);
                    break;
                }

                // if current item is a male baby rat
                case "m": {
                    int speed = Integer.parseInt(currentItem[1]);
                    int directionInt = Integer.parseInt(currentItem[2]);
                    Rat.Direction direction = directionIntToEnum(directionInt);
                    int gasTimer = Integer.parseInt(currentItem[3]);
                    int xPos = Integer.parseInt(currentItem[4]);
                    int yPos = Integer.parseInt(currentItem[5]);
                    boolean isFertile;
                    isFertile = currentItem[6].equals("1");
                    int age = Integer.parseInt(currentItem[7]);
                    ChildRat newRat = new ChildRat(speed, direction, gasTimer, xPos, yPos, isFertile, age, Rat.Sex.MALE);
                    RAT_ARRAY_LIST.add(newRat);
                    tileMap[xPos][yPos].addOccupantRat(newRat);
                    break;
                }

                // if current item is an intersex baby rat
                case "i": {
                    int speed = Integer.parseInt(currentItem[1]);
                    int directionInt = Integer.parseInt(currentItem[2]);
                    Rat.Direction direction = directionIntToEnum(directionInt);
                    int gasTimer = Integer.parseInt(currentItem[3]);
                    int xPos = Integer.parseInt(currentItem[4]);
                    int yPos = Integer.parseInt(currentItem[5]);
                    boolean isFertile;
                    isFertile = currentItem[6].equals("1");
                    int age = Integer.parseInt(currentItem[7]);
                    ChildRat newRat = new ChildRat(speed, direction, gasTimer, xPos, yPos, isFertile, age,
                            Rat.Sex.INTERSEX);
                    RAT_ARRAY_LIST.add(newRat);
                    tileMap[xPos][yPos].addOccupantRat(newRat);
                    break;
                }

                // if current item is a female adult rat
                case "F": {
                    int speed = Integer.parseInt(currentItem[1]);
                    int directionInt = Integer.parseInt(currentItem[2]);
                    Rat.Direction direction = directionIntToEnum(directionInt);
                    int gasTimer = Integer.parseInt(currentItem[3]);
                    int xPos = Integer.parseInt(currentItem[4]);
                    int yPos = Integer.parseInt(currentItem[5]);
                    boolean isFertile;
                    isFertile = currentItem[6].equals("1");
                    int pregnancyTimer = Integer.parseInt(currentItem[7]);
                    int ratFetusCount = Integer.parseInt(currentItem[8]);
                    AdultFemale newRat = new AdultFemale(speed, direction, gasTimer, xPos, yPos, isFertile, pregnancyTimer,
                            ratFetusCount);
                    RAT_ARRAY_LIST.add(newRat);
                    tileMap[xPos][yPos].addOccupantRat(newRat);
                    break;
                }

                // if current item is a male adult rat
                case "M": {
                    int speed = Integer.parseInt(currentItem[1]);
                    int directionInt = Integer.parseInt(currentItem[2]);
                    Rat.Direction direction = directionIntToEnum(directionInt);
                    int gasTimer = Integer.parseInt(currentItem[3]);
                    int xPos = Integer.parseInt(currentItem[4]);
                    int yPos = Integer.parseInt(currentItem[5]);
                    boolean isFertile;
                    isFertile = currentItem[6].equals("1");
                    AdultMale newRat = new AdultMale(speed, direction, gasTimer, xPos, yPos, isFertile);
                    RAT_ARRAY_LIST.add(newRat);
                    tileMap[xPos][yPos].addOccupantRat(newRat);
                    break;
                }

                // if current item is an intersex adult rat
                case "I": {
                    int speed = Integer.parseInt(currentItem[1]);
                    int directionInt = Integer.parseInt(currentItem[2]);
                    Rat.Direction direction = directionIntToEnum(directionInt);
                    int gasTimer = Integer.parseInt(currentItem[3]);
                    int xPos = Integer.parseInt(currentItem[4]);
                    int yPos = Integer.parseInt(currentItem[5]);
                    boolean isFertile;
                    isFertile = currentItem[6].equals("1");
                    int pregnancyTimer = Integer.parseInt(currentItem[7]);
                    int ratFetusCount = Integer.parseInt(currentItem[8]);
                    AdultIntersex newRat = new AdultIntersex(speed, direction, gasTimer, xPos, yPos, isFertile,
                            pregnancyTimer, ratFetusCount);
                    RAT_ARRAY_LIST.add(newRat);
                    tileMap[xPos][yPos].addOccupantRat(newRat);
                    break;
                }

                // if current item is a death rat
                case "D": {
                    int speed = Integer.parseInt(currentItem[1]);
                    int directionInt = Integer.parseInt(currentItem[2]);
                    Rat.Direction direction = directionIntToEnum(directionInt);
                    int gasTimer = Integer.parseInt(currentItem[3]);
                    int xPos = Integer.parseInt(currentItem[4]);
                    int yPos = Integer.parseInt(currentItem[5]);
                    int killCounter = Integer.parseInt(currentItem[6]);
                    DeathRat newRat = new DeathRat(speed, direction, gasTimer, xPos, yPos, killCounter);
                    RAT_ARRAY_LIST.add(newRat);
                    tileMap[xPos][yPos].addOccupantRat(newRat);
                    break;
                }

                // if currentItem item is a bomb
                case "B": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    int ticksActive = Integer.parseInt(currentItem[3]);
                    Bomb newBomb = new Bomb(xPos, yPos);
                    newBomb.setTicksActive(ticksActive);
                    POWER_ARRAY_LIST.add(newBomb);
                    tileMap[xPos][yPos].addActivePower(newBomb);
                    break;
                }

                // if currentItem item is gas
                case "G": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    boolean isOriginal;
                    isOriginal = currentItem[3].equals("1");
                    int ticksActive = Integer.parseInt(currentItem[4]);
                    Gas newGas = new Gas(xPos, yPos, isOriginal);
                    newGas.setTicksActive(ticksActive);
                    POWER_ARRAY_LIST.add(newGas);
                    tileMap[xPos][yPos].addActivePower(newGas);
                    break;
                }

                // if currentItem item is lava
                case "L": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    boolean isOriginal;
                    isOriginal = currentItem[3].equals("1");
                    int ticksActive = Integer.parseInt(currentItem[4]);
                    Lava newLava = new Lava(xPos, yPos, isOriginal);
                    newLava.setTicksActive(ticksActive);
                    POWER_ARRAY_LIST.add(newLava);
                    tileMap[xPos][yPos].addActivePower(newLava);
                    break;
                }

                // if currentItem item is steriliser
                case "S": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    int ticksActive = Integer.parseInt(currentItem[3]);
                    Sterilisation newSterilisation = new Sterilisation(xPos, yPos);
                    newSterilisation.setTicksActive(ticksActive);
                    POWER_ARRAY_LIST.add(newSterilisation);
                    tileMap[xPos][yPos].addActivePower(newSterilisation);
                    break;
                }

                // if currentItem item is poison
                case "P": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    Poison newPoison = new Poison(xPos, yPos);
                    POWER_ARRAY_LIST.add(newPoison);
                    tileMap[xPos][yPos].addActivePower(newPoison);
                    break;
                }

                // if currentItem item is a male sex change
                case "T": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    MaleSwapper newMaleSwapper = new MaleSwapper(xPos, yPos);
                    POWER_ARRAY_LIST.add(newMaleSwapper);
                    tileMap[xPos][yPos].addActivePower(newMaleSwapper);
                    break;
                }

                // if currentItem item is a female sex change
                case "E": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    FemaleSwapper newFemaleSwapper = new FemaleSwapper(xPos, yPos);
                    POWER_ARRAY_LIST.add(newFemaleSwapper);
                    tileMap[xPos][yPos].addActivePower(newFemaleSwapper);
                    break;
                }

                // if currentItem item is a no-entry sign (StopSign)
                case "N": {
                    int xPos = Integer.parseInt(currentItem[1]);
                    int yPos = Integer.parseInt(currentItem[2]);
                    int HP = Integer.parseInt(currentItem[3]);
                    StopSign newStopSign = new StopSign(xPos, yPos);
                    newStopSign.setHP(HP);
                    POWER_ARRAY_LIST.add(newStopSign);
                    tileMap[xPos][yPos].addActivePower(newStopSign);
                    break;
                }
            }

        }
    }

    /**
     * Converts the strings that store the tiles into a 2d array that can be used by
     * other classes.
     *
     * @param tiles The strings to be converted
     * @return a tile map
     */
    private static Tile[][] tilesToTileMap(String[] tiles) {
        Tile[][] tileMap = new Tile[width][height];
        for (int i = 0; i < tileMap[0].length; i++) {
            int charPos = -1;
            for (int j = 0; j < tileMap.length; j++) {
                charPos++;
                switch (tiles[i].charAt(charPos)) {
                    case 'G':
                        tileMap[j][i] = new Grass();
                        break;
                    case 'P':
                        tileMap[j][i] = new Path();
                        break;
                    case 'T':
                        tileMap[j][i] = new Tunnel();
                        break;
                    case 'g':
                        tileMap[j][i] = new GrassB();
                        break;
                    case 'p':
                        tileMap[j][i] = new PathB();
                        break;
                    case 't':
                        tileMap[j][i] = new TunnelB();
                        break;
                }
            }
        }
        return tileMap;
    }

    public static Tile[][] getTileMap() {
        return tileMap;
    }
}
