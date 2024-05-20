package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Intermession implements Screen {
    private SpriteBatch batch;
    private Texture background;
    private Clip backgroundClip;
    private Texture[] progTextures;
    private int currentProgIndex = 0;
    private Stage stage;
    private boolean prevKKeyPressed = false;
    private Texture placeholder;
    private Stage btnStage;
    private Skin nextWaveSkin;

    public Intermession() {
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/UpgradeScreen.jpg");
        placeholder = new Texture("assets/Pages/Progress/prog00.png");

        progTextures = new Texture[10]; // Assuming you have 10 prog images
        for (int i = 0; i < progTextures.length; i++) {
            String texturePath = String.format("assets/Pages/Progress/prog%02d.png", i);
            progTextures[i] = new Texture(texturePath);
        }

        // Load the skin
        nextWaveSkin = new Skin(Gdx.files.internal("assets/jsonFiles/nextWaveButton.json"), new TextureAtlas(Gdx.files.internal("assets/jsonFiles/nextWaveButton.atlas")));

        // Create the stage and set it as the input processor
        btnStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(btnStage);

        // Create a button
        Button nextWaveButton1 = new Button(nextWaveSkin);

        // Set button size
        nextWaveButton1.setSize(400, 200);

        // Position the button in the bottom right corner
        nextWaveButton1.setPosition(Gdx.graphics.getWidth() - nextWaveButton1.getWidth() - 150, 60);

        // Add a listener to handle button clicks
        nextWaveButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the button click event
                System.out.println("Next Wave button clicked!");
            }
        });

        // Add the button to the stage
        btnStage.addActor(nextWaveButton1);
        // Initialize the stage
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        playBackgroundMusic("assets/Audio/UpgradeScreen/UpgradeMenuTheme.wav");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        boolean kKeyPressed = Gdx.input.isKeyPressed(Input.Keys.K);
        if (kKeyPressed && !prevKKeyPressed) {
            currentProgIndex = (currentProgIndex + 1) % progTextures.length;
        }
        prevKKeyPressed = kKeyPressed;

        Texture currentProgTexture = progTextures[currentProgIndex];


        float newWidth = (float) (placeholder.getWidth() * 1.3);
        float newHeight = (float) (placeholder.getHeight() * 1.1);

        batch.draw(currentProgTexture, 320, 865, newWidth, newHeight);
        batch.draw(currentProgTexture, 320, 685,newWidth,newHeight);
        batch.draw(currentProgTexture, 320, 485,newWidth,newHeight);
        batch.draw(currentProgTexture, 320, 305,newWidth,newHeight);
        batch.draw(currentProgTexture, 320, 105,newWidth,newHeight);

        batch.end();

        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Draw the UI components
        btnStage.act(Gdx.graphics.getDeltaTime());
        btnStage.draw();
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);

            // Adjust volume
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.20) / Math.log(10.0) * 20.0);
            gainControl.setValue(volume);

            // Loop the clip continuously
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    @Override
    public void resize(int width, int height) {
        btnStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        stopBackgroundMusic();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        for (Texture texture : progTextures) {
            texture.dispose();
        }
        btnStage.dispose();
        nextWaveSkin.dispose();
        stopBackgroundMusic();
    }
}
