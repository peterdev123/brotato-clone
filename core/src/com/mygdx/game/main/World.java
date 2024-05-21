package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Screens.Pause;
import com.mygdx.game.TitleFight;
import com.mygdx.game.enemies.EnemyHandler;
import com.mygdx.game.player.Player;
import com.mygdx.game.Screens.Intermession;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class World implements Screen {
    private TitleFight titleFight;
    private final OrthogonalTiledMapRenderer renderer;
    private final Map map;
    private OrthographicCamera camera;
    private Sprite character;
    private SpriteBatch spriteBatch;
    private Player player;
    private Texture hpbarTexture;
    private Sprite hpbarSprite;
    private float hpbarWidth;
    private final float hpbarHeight;
    private final float CAMERA_SPEED = 150.0f;
    private final float VIRTUAL_WIDTH = 1440;  // Virtual width
    private final float VIRTUAL_HEIGHT = 900;  // Virtual height
    public BitmapFont font;
    //ENEMIES
    private EnemyHandler enemyHandler;

    //Intermession
    private boolean intermessionScreenShown = false;
    private Intermession intermessionScreen;

    //Pause
    public boolean gamePaused = false;
    private Pause pauseScreen;

    //BG music
    private Clip bgclip0;

    public World(TitleFight titleFight){
        this.titleFight = titleFight;
        font = new BitmapFont();
        map = new Map();
        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        spriteBatch = new SpriteBatch();
        renderer = map.makeMap();
        hpbarTexture = new Texture("assets/Extras/hpbar2.png");
        hpbarSprite = new Sprite(hpbarTexture);
        hpbarWidth = hpbarSprite.getWidth();
        hpbarHeight = hpbarSprite.getHeight();
        intermessionScreen = new Intermession();
        player = new Player(intermessionScreen);
        pauseScreen = new Pause(this);

        //ENEMIES
        enemyHandler = new EnemyHandler(player.getWeapon());
        playBackgroundMusic0("assets/Audio/Game/BattleTheme.wav");
    }

    public void show(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = .3f;
        player.character.setPosition(280, 200);
        camera.position.set(player.character.getX(), player.character.getY(), 0);
    }

    public void playBackgroundMusic0(String filePath) {
        try {
            // Open an audio input stream.
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource.
            bgclip0 = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            bgclip0.open(audioIn);

            //Adjust volume
            FloatControl gainControl = (FloatControl) bgclip0.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.1) / Math.log(10.0) * 20.0); // -16 dB
            gainControl.setValue(volume);

            // Loop the clip continuously.
            bgclip0.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic0() {
        if (bgclip0 != null && bgclip0.isRunning()) {
            bgclip0.stop();
            bgclip0.close();
        }
    }

    public void render(float delta){
        Gdx.gl.glClearColor(24 / 255f, 20 / 255f, 37 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //DEBUG
        //Render the intermession screen when true
        if (intermessionScreenShown) {
            intermessionScreen.render(delta);
            //Hides the intermession screen when "ESC" pressed
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                hideIntermessionScreen();
                playBackgroundMusic0("assets/Audio/Game/BattleTheme.wav");
                intermessionScreen.hide();
            }
            return; // Stop rendering the game world if the intermission screen is shown
        }

        //Pause Screen
        if (gamePaused) {
            pauseScreen.render(delta);
            pauseScreen.setGamePaused(gamePaused);
            return;
        }

        //Pauses the Screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gamePaused = true;
            stopBackgroundMusic0();
        }

        //DEBUG
        //Shows the intermession screen when "ESC" pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (!intermessionScreenShown) {
                stopBackgroundMusic0();
                showIntermessionScreen();
                intermessionScreen.show();
            }
        }

        zoom(); // Call zoom to adjust zoom level if keys are pressed

        //Clamps the camera to prevent out of bounds camera movement
        clampCamera();

        // Debug output
//        System.out.println("Player position: (" + player.character.getX() + ", " + player.character.getY() + ")");
//        System.out.println("Camera position: (" + camera.position.x + ", " + camera.position.y + ")");

        Vector3 position = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        renderData();
        camera.update();// Render the map and player
    }

    public void clampCamera(){
        float playerCenterX = player.character.getX();
        float playerCenterY = player.character.getY();

        float cameraHalfWidth = camera.viewportWidth * camera.zoom / 2;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom / 2;

        float minX = cameraHalfWidth;
        float minY = cameraHalfHeight;
        float maxX = map.MAP_WIDTH - cameraHalfWidth;
        float maxY = map.MAP_HEIGHT - cameraHalfHeight;

        camera.position.x = MathUtils.clamp(playerCenterX, minX, maxX);
        camera.position.y = MathUtils.clamp(playerCenterY, minY, maxY);
    }

    public void renderData(){
        renderer.setView(camera);
        renderer.render(new int[] {0, 1});
        player.handleMovement(camera);

        //ENEMIES: DEBUGGING
        enemyHandler.handleWave(camera);

        renderer.render(new int[] {2});

        spriteBatch.begin();
        // PRA HP HEALTH PANGUTANA LNG NKO PRA UPDATE2 NIYA KY LIBOG JD KUN E REDRAW NA SIYA NGA MAGBASE SA PLAYER HP
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            hpbarWidth -= 100;
            player.decreaseHealth(10);
            System.out.println("player health percentage: " + player.getHealthPercentage());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            hpbarWidth += 100;
            player.increaseHealth(10);
            System.out.println("player health percentage: " + player.getHealthPercentage());
            if (hpbarWidth < 0) {
                hpbarWidth = 0;
            }
        }

        // Draw the HP bar at the top left corner
        hpbarSprite.setSize(hpbarWidth, hpbarHeight);
        hpbarSprite.setPosition(10, Gdx.graphics.getHeight() - hpbarHeight - 10); // Top left corner
        hpbarSprite.draw(spriteBatch);

        // Draw current health and total health text
        String healthText = player.getCurrentHealth() + "/" + player.getMaxHealth();
        font.draw(spriteBatch, healthText, 20, Gdx.graphics.getHeight() - hpbarHeight - 30);
        spriteBatch.end();
    }


    public void zoom(){
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            camera.zoom += 0.05f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)){
            camera.zoom -= 0.05f;
        }
    }

    public void resize(int width, int height){
        camera.viewportWidth = VIRTUAL_WIDTH;
        camera.viewportHeight = VIRTUAL_HEIGHT;
        camera.zoom = 0.3f;
        camera.update();
    }

    public void showIntermessionScreen() {
        intermessionScreenShown = true;
    }

    public void hideIntermessionScreen() {
        intermessionScreenShown = false;
    }

    public void pause(){
        //TODO
    }

    public void resume(){
        //TODO
    }

    public void hide(){
        stopBackgroundMusic0();
    }

    public void dispose(){
        renderer.dispose();
        hpbarTexture.dispose();
        spriteBatch.dispose();
        font.dispose();
    }
}
