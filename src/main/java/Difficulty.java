enum Difficulty{

    EASY(1,30,4,4,4,4,4),
    MEDIUM(3,25,2,3,3,3,3),
    HARD(5,20,3,1,3,3,3);

    private int ratSpeed;
    private int populationCap;
    private int bombCap;
    private int gasCap;
    private int poisonCap;
    private int maleSwapperCap;
    private int femaleSwapperCap;



    Difficulty(int ratSpeed ,int populationCap, int bombCap, int gasCap, int poisonCap, int maleSwapperCap, int femaleSwapperCap){
        this.ratSpeed = ratSpeed;
        this.populationCap = populationCap;
        this.bombCap = bombCap;
        this.gasCap = gasCap;
        this.poisonCap = poisonCap;
        this.maleSwapperCap = maleSwapperCap;
        this.femaleSwapperCap = femaleSwapperCap;
    }

}