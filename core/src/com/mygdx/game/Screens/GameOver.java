package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.main.World;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameOver implements Screen {

    private SpriteBatch batch;
    private Texture background, background2;
    private int counter = 0;
    private boolean keyPressed = false;
    private boolean gameOver = false;
    public World world;

    private Clip bgclip0;

    private Thread musicThread;
    private boolean playMusic = true;

    public GameOver(World world) {
        this.world = world;
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/GO_screen1.jpg");
        background2 = new Texture("assets/Pages/GO_screen2.jpg");
    }

    public void stopGameOverMusic(){
        if (bgclip0 != null && bgclip0.isRunning()) {
            bgclip0.stop();
            bgclip0.close();
        }
    }

    public void playGameOverMusic(String filePath){
        try {
            // Open an audio input stream.
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource.
            bgclip0 = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            bgclip0.open(audioIn);

            // Adjust volume
            FloatControl gainControl = (FloatControl) bgclip0.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.1) / Math.log(10.0) * 20.0); // -16 dB
            gainControl.setValue(volume);

            // Loop the clip continuously.
            bgclip0.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public void show() {
        musicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                playGameOverMusic("assets/Audio/GameOver/GameOver.wav");
            }
        });
        musicThread.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (counter == 0) {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            // LEADERBOARD HERE
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                stopGameOverMusic();
                world.dispose();
            }
        } else if (counter == 1) {
            batch.draw(background2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                // RESET GAME
                stopGameOverMusic();
                world.resetAll();
            }
        }
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (!keyPressed) {
                counter = (counter + 1) % 2;
                keyPressed = true;
            }
        } else {
            keyPressed = false;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playMusic = false; // Stop the music thread
        try {
            musicThread.join(); // Wait for the music thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        batch.dispose();
        background.dispose();
        background2.dispose();
    }
}
