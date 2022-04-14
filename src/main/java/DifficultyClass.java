public class DifficultyClass {

    private int ratSpeed;
    private int populationCap;
    private int bombCap;
    private int gasCap;
    private int poisonCap;
    private int maleSwapperCap;
    private int femaleSwapperCap;


    DifficultyClass(int ratSpeed ,int populationCap, int bombCap, int gasCap, int poisonCap, int maleSwapperCap, int femaleSwapperCap){
        this.ratSpeed = ratSpeed;
        this.populationCap = populationCap;
        this.bombCap = bombCap;
        this.gasCap = gasCap;
        this.poisonCap = poisonCap;
        this.maleSwapperCap = maleSwapperCap;
        this.femaleSwapperCap = femaleSwapperCap;
    }

    public DifficultyClass() {

    }

    public int getRatSpeed() {
        return ratSpeed;
    }

    public int getPopulationCap() {
        return populationCap;
    }

    public int getBombCap() {
        return bombCap;
    }

    public int getGasCap() {
        return gasCap;
    }

    public int getPoisonCap() {
        return poisonCap;
    }

    public int getMaleSwapperCap() {
        return maleSwapperCap;
    }

    public int getFemaleSwapperCap() {
        return femaleSwapperCap;
    }

    public void setRatSpeed(int ratSpeed) {
        this.ratSpeed = ratSpeed;
    }

    public void setPopulationCap(int populationCap) {
        this.populationCap = populationCap;
    }

    public void setBombCap(int bombCap) {
        this.bombCap = bombCap;
    }

    public void setGasCap(int gasCap) {
        this.gasCap = gasCap;
    }

    public void setPoisonCap(int poisonCap) {
        this.poisonCap = poisonCap;
    }

    public void setMaleSwapperCap(int maleSwapperCap) {
        this.maleSwapperCap = maleSwapperCap;
    }

    public void setFemaleSwapperCap(int femaleSwapperCap) {
        this.femaleSwapperCap = femaleSwapperCap;
    }
}
