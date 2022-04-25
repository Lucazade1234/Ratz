import java.util.ArrayList;
import java.util.Objects;

/**
 * Class models the behaviour of lava weapon
 * @author Luca Khatri
 * @version 1.0.0
 */
public class Lava extends Power {
    private static final int TICKS_TO_SPAWN_NEW_LAVA = 4;
    private static final int LIFETIME = 24;

    private int ticksActive = 0; //Tick counter since creation of this class.
    private boolean isOriginal;

    private int lavaCounterN = 1; // Counts how many gas was placed North
    private int lavaCounterS = 1; // Counts how many gas was placed South
    private int lavaCounterE = 1; // Counts how many gas was placed East
    private int lavaCounterW = 1; // Counts how many gas was placed West

    private int lavaCounterNW = 1; // Counts how many gas was placed North-West
    private int lavaCounterSW = 1; // Counts how many gas was placed South-West
    private int lavaCounterNE = 1; // Counts how many gas was placed North-East
    private int lavaCounterSE = 1; // Counts how many gas was placed South-East




    /**
     * Constructs a new lava
     * @param xPos The x position
     * @param yPos The y position
     * @param isOriginal boolean represents whether or not its the original lava
     */
    public Lava(int xPos, int yPos, boolean isOriginal){
        super(true, xPos, yPos);
        this.isOriginal = isOriginal;
    }

    /**
     * abstract method that tells every power to do their thing
     * for lava - spawn new lava to surrounding tiles and kill rats
     * in the same tile as them
     * @param rats used to interact with all rats that stepped on the power.
     * @param currentTile used for removing power from its Tile.
     */
    @Override
    void activate(ArrayList<Rat> rats, Tile currentTile) {
        //Places a bunch of new Gas on Tiles with isOriginal = false;
        if (isOriginal) {
            if (ticksActive == 1 || ticksActive % TICKS_TO_SPAWN_NEW_LAVA == 0) {
                lavaSurroundingPathTiles();
            }
        }

        //Where the rats get killed by lava.
        for(Rat r : currentTile.getOccupantRats()) {
            r.die();
        }
    }

    /**
     * Abstract method for certain powers that need to activate after a
     * certain amount of time.
     *
     * for lava - removes lava after lifecycle and spawns rock
     * @param rats used for updating the rat arraylist every game tick.
     * @param currentTile used for calling removeActivePower(this).
     */
    @Override
    void onTick(ArrayList<Rat> rats, Tile currentTile) {
        ticksActive++;
        if (ticksActive <= LIFETIME) {
            activate(rats, currentTile);
        } else {
            currentTile.removeActivePower(this);
            currentTile.addActivePower(new Rock(this.xPos,this.yPos));
        }

    }


    /**
     * method spawns new lava to surrounding tiles
     */
    private void lavaSurroundingPathTiles() {
        getSurroundingNonDiagonals();
        getSurroundDiagonals();
    }

    /**
     * method spawns new lava in neighbouring tiles
     * Does this for North, South, east and West
     */
    private void getSurroundingNonDiagonals() {
        // North
        if (LevelController.getTileAt(this.xPos, this.yPos + lavaCounterN)
                != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos,
                    this.yPos + lavaCounterN)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos,
                        this.yPos + lavaCounterN)).addActivePower(new Lava(this.xPos,
                        this.yPos + lavaCounterN, false));
                lavaCounterN++;
            }
        }

        // South
        if (LevelController.getTileAt(this.xPos, this.yPos - lavaCounterS)
                != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos,
                    this.yPos - lavaCounterS)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos,
                        this.yPos - lavaCounterS)).addActivePower(new Lava(
                        this.xPos, this.yPos - lavaCounterS, false));
                lavaCounterS++;
            }
        }

        // East
        if (LevelController.getTileAt(this.xPos + lavaCounterE, this.yPos) != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos
                    + lavaCounterE, this.yPos)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos
                        + lavaCounterE, this.yPos)).addActivePower(new Lava(
                        this.xPos + lavaCounterE, this.yPos, false));
                lavaCounterE++;
            }
        }

        // South
        if (LevelController.getTileAt(this.xPos-lavaCounterW, this.yPos) != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos -
                    lavaCounterW, this.yPos)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos -
                        lavaCounterW, this.yPos)).addActivePower(new Lava (
                        this.xPos - lavaCounterW, this.yPos, false));
                lavaCounterW++;
            }
        }
    }


    /**
     * Method spawns new lava in neighbouring tiles
     * Does this for North-East, South-East, South-West and North-West
     */
    private void getSurroundDiagonals() {
        // North East
        if (LevelController.getTileAt(this.xPos+lavaCounterNE,
                this.yPos+lavaCounterNE) != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos +
                    lavaCounterNE, this.yPos + lavaCounterNE)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos +
                        lavaCounterNE, this.yPos + lavaCounterNE)).
                        addActivePower(new Lava(this.xPos + lavaCounterNE,
                                this.yPos + lavaCounterNE, false));
                lavaCounterNE++;
            }
        }

        // South East
        if (LevelController.getTileAt(this.xPos+lavaCounterSE,
                this.yPos-lavaCounterSE) != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos +
                    lavaCounterSE, this.yPos - lavaCounterSE)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos +
                        lavaCounterSE, this.yPos - lavaCounterSE)).
                        addActivePower(new Lava(this.xPos + lavaCounterSE,
                                this.yPos - lavaCounterSE, false));
                lavaCounterSE++;
            }
        }

        // North West
        if (LevelController.getTileAt(this.xPos - lavaCounterNW,
                this.yPos + lavaCounterNW) != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos -
                    lavaCounterNW, this.yPos + lavaCounterNW)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos -
                        lavaCounterNW, this.yPos + lavaCounterNW)).
                        addActivePower(new Lava(this.xPos - lavaCounterNW,
                                this.yPos + lavaCounterNW, false));
                lavaCounterNW++;
            }
        }

        // South West
        if (LevelController.getTileAt(this.xPos - lavaCounterSW,
                this.yPos - lavaCounterSW) != null) {
            if (Objects.requireNonNull(LevelController.getTileAt(this.xPos -
                    lavaCounterSW, this.yPos - lavaCounterSW)).isPassable()) {
                Objects.requireNonNull(LevelController.getTileAt(this.xPos -
                        lavaCounterSW, this.yPos - lavaCounterSW)).
                        addActivePower(new Lava(this.xPos - lavaCounterSW,
                                this.yPos - lavaCounterSW, false));
                lavaCounterSW++;
            }
        }
    }


    /**
     * Gets how many ticks since the creation of the class
     *
     * @return current value of ticksActive
     */
    public int getTicksActive() {
        return ticksActive;
    }

    /**
     * Sets the value of ticksActive
     *
     * @param ticksActive the new value of ticksActive
     */
    public void setTicksActive(int ticksActive) {
        this.ticksActive = ticksActive;
    }

    /**
     * Gets if the lava is the original lava the player placed
     *
     * @return current value of isOriginal
     */
    public boolean isOriginal() {
        return isOriginal;
    }


}
