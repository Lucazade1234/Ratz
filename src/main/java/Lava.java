import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    void activate(ArrayList<Rat> rats, Tile currentTile) {
        //Places a bunch of new Gas on Tiles with isOriginal = false;
        if (isOriginal) {
            if (ticksActive == 1 || ticksActive % TICKS_TO_SPAWN_NEW_LAVA == 0) {
                SeaShantySimulator seaSim = new SeaShantySimulator();
                //seaSim.playAudioClip(GAS_SOUND_PATH, GAS_SOUND_VOLUME);
                lavaSurroundingPathTiles();
            }
        }

        //Where the rats get killed by lava.
        for(Rat r : currentTile.getOccupantRats()) {
            r.die();
        }
    }

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


    private void lavaSurroundingPathTiles() {
        getSurroundingNonDiagonals();
        getSurroundDiagonals();
    }

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

    public static int getTicksToSpawnNewLava() {
        return TICKS_TO_SPAWN_NEW_LAVA;
    }

    public static int getLIFETIME() {
        return LIFETIME;
    }

    public int getTicksActive() {
        return ticksActive;
    }

    public void setTicksActive(int ticksActive) {
        this.ticksActive = ticksActive;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public int getLavaCounterN() {
        return lavaCounterN;
    }

    public void setLavaCounterN(int lavaCounterN) {
        this.lavaCounterN = lavaCounterN;
    }

    public int getLavaCounterS() {
        return lavaCounterS;
    }

    public void setLavaCounterS(int lavaCounterS) {
        this.lavaCounterS = lavaCounterS;
    }

    public int getLavaCounterE() {
        return lavaCounterE;
    }

    public void setLavaCounterE(int lavaCounterE) {
        this.lavaCounterE = lavaCounterE;
    }

    public int getLavaCounterW() {
        return lavaCounterW;
    }

    public void setLavaCounterW(int lavaCounterW) {
        this.lavaCounterW = lavaCounterW;
    }

    public int getLavaCounterNW() {
        return lavaCounterNW;
    }

    public void setLavaCounterNW(int lavaCounterNW) {
        this.lavaCounterNW = lavaCounterNW;
    }

    public int getLavaCounterSW() {
        return lavaCounterSW;
    }

    public void setLavaCounterSW(int lavaCounterSW) {
        this.lavaCounterSW = lavaCounterSW;
    }

    public int getLavaCounterNE() {
        return lavaCounterNE;
    }

    public void setLavaCounterNE(int lavaCounterNE) {
        this.lavaCounterNE = lavaCounterNE;
    }

    public int getLavaCounterSE() {
        return lavaCounterSE;
    }

    public void setLavaCounterSE(int lavaCounterSE) {
        this.lavaCounterSE = lavaCounterSE;
    }
}
