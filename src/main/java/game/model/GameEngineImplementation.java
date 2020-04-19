package game.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameEngineImplementation implements GameEngine {
    private ArrayList<String> jsonLevels= new ArrayList<String>();
    private int jsonIndex=1;
    private int lId;

    /**
     * The height of the game engine
     */
    private double height;

    /**
     * The current level
     */
    private Level currentLevel;

    /**
     * Map of all the levels
     */
    private Map<Integer, Level> levels;

    /**
     * Used to create distinct level id's for each level
     */
    private int levelId;

    /**
     * Level id of the current level
     */
    private int currentLevelId;
    private int secondLevelId;
    private int thirdLevelId;

    /**
     * Json path to the level configuration file
     */
    private String jsonPath;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Instant start;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Duration interval;

    /**
     * The number of lives the hero has
     */
    private int lives;

    ///
    private int score;

    /**
     * Creates the game engine using the specified json configuration file and height
     * @param jsonPath The path to the json configuration file containing the level information
     * @param height The height of the game engine's window
     */
    public GameEngineImplementation(String jsonPath, double height) {
        this.jsonPath = jsonPath;
        this.height = height;
        this.levels = new HashMap<>();
        this.levelId = 1;
        this.currentLevelId = 1;
        this.secondLevelId=2;
        this.thirdLevelId=3;
        this.lives = 3;
        this.lId=1;
        jsonLevels.add("a");
        jsonLevels.add("level_1.json");
        jsonLevels.add("level_2.json");
        jsonLevels.add("level_3.json");
        ////
        this.score=60;

        createLevels();
        startLevel();
    }

    /**
     * Creates the levels associated with the json file
     */
    public void createLevels() {
        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.levelId, levelDirector.getLevel());
        levelId += 1;
    }

    @Override
    public void startLevel() {
        this.currentLevel = levels.get(currentLevelId);
        start = Instant.now();
    }

    @Override
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    @Override
    public boolean jump() {
        return this.currentLevel.jump();
    }

    @Override
    public boolean moveLeft() {
        return this.currentLevel.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.currentLevel.moveRight();
    }

    @Override
    public boolean stopMoving() {
        return this.currentLevel.stopMoving();
    }

    @Override
    public void tick() {
        this.currentLevel.tick();
        interval = Duration.between(start, Instant.now());
    }

    @Override
    public void resetCurrentLevel() {
        this.lives--;
        if (this.lives == 0) {
            return;
        }
        LevelBuilder levelBuilder = new LevelBuilder(jsonLevels.get(currentLevelId));
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.currentLevelId, levelDirector.getLevel());
        startLevel();
    }

    @Override
    public void levelChange(boolean start){
        currentLevelId++;

        this.lives=3;
        System.out.println("I work");
        if(start) {
            LevelBuilder levelBuilder = new LevelBuilder("level_2.json");
            LevelDirector levelDirector = new LevelDirector(levelBuilder);
            levelDirector.buildLevel();
            this.levels.put(this.currentLevelId, levelDirector.getLevel());
            startLevel();
            lId++;
        }
        return;
    }

    @Override
    public void secondLevelChange(boolean start){
        currentLevelId++;
        lId++;
        this.lives=3;
        System.out.println("I work too");
        if(start) {
            LevelBuilder levelBuilder = new LevelBuilder("level_3.json");
            LevelDirector levelDirector = new LevelDirector(levelBuilder);
            levelDirector.buildLevel();
            this.levels.put(this.currentLevelId, levelDirector.getLevel());
            startLevel();
        }
        return;

    }
    @Override
    public void thirdLevelChange(boolean start){
        lId++;

    }

    @Override
    public boolean isFinished() {
        return currentLevel.isFinished();
    }

    @Override
    public Duration getDuration() {
        return interval;
    }

    @Override
    public boolean gameOver() {
        return this.lives == 0;
    }

    @Override
    public int getLives() {
        return this.lives;
    }

    @Override
    public int score(){
        return this.score;
    }

    @Override
    public double showScore(){
        return  levels.get(currentLevelId).getScore();
       // this.score+=100;
    }
    @Override
    public int copyLevelId(){
        return lId;
    }


}

