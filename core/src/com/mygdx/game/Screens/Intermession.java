package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TitleFight;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Intermession implements Screen {
    private SpriteBatch batch;
    private Texture background;
    private Clip backgroundClip;

    public Intermession() {
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/UpgradeScreen.jpg");
    }

    @Override
    public void show() {
        playBackgroundMusic("assets/Audio/UpgradeScreen/UpgradeMenuTheme.wav"); // Specify the path to your audio file
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    private void playBackgroundMusic(String filePath) {
        try {
            // Open an audio input stream.
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource.
            backgroundClip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            backgroundClip.open(audioIn);

            //Adjust volume
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.20) / Math.log(10.0) * 20.0); // -12 dB
            gainControl.setValue(volume);

            // Loop the clip continuously.
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stopBackgroundMusic();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stopBackgroundMusic(); // Ensure the music is stopped and resources are released
    }

}
