public class EasyDifficulty extends DifficultyClass {

    EasyDifficulty() {
        super();
        this.setRatSpeed(4);
        this.setPopulationCap(30);
        this.setBombCap(4);
        this.setGasCap(4);
        this.setPoisonCap(4);
        this.setMaleSwapperCap(4);
        this.setFemaleSwapperCap(4);
        this.setTime(120);
        this.setFrameTime(250);
        int[] dropRates = {0,10000,0,0,0,0,0,0};
        this.setDropRate(dropRates);
        int[] counters = {2,2,2,2,2,2,2,2};
        this.setCounters(counters);

    }


    @Override
    public int getRatSpeed() {
        return super.getRatSpeed();
    }

    @Override
    public int getPopulationCap() {
        return super.getPopulationCap();
    }

    @Override
    public int getBombCap() {
        return super.getBombCap();
    }

    @Override
    public int getGasCap() {
        return super.getGasCap();
    }

    @Override
    public int getPoisonCap() {
        return super.getPoisonCap();
    }

    @Override
    public int getMaleSwapperCap() {
        return super.getMaleSwapperCap();
    }

    @Override
    public int getFemaleSwapperCap() {
        return super.getFemaleSwapperCap();
    }

    @Override
    public void setRatSpeed(int ratSpeed) {
        super.setRatSpeed(ratSpeed);
    }

    @Override
    public void setPopulationCap(int populationCap) {
        super.setPopulationCap(populationCap);
    }

    @Override
    public void setBombCap(int bombCap) {
        super.setBombCap(bombCap);
    }

    @Override
    public void setGasCap(int gasCap) {
        super.setGasCap(gasCap);
    }

    @Override
    public void setPoisonCap(int poisonCap) {
        super.setPoisonCap(poisonCap);
    }

    @Override
    public void setMaleSwapperCap(int maleSwapperCap) {
        super.setMaleSwapperCap(maleSwapperCap);
    }

    @Override
    public void setFemaleSwapperCap(int femaleSwapperCap) {
        super.setFemaleSwapperCap(femaleSwapperCap);
    }
}
