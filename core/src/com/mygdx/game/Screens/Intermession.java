package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Stage stage;
    private Texture placeholder;
    private Stage btnStage;
    private Skin nextWaveSkin;
    private Skin plusButtonSkin;
    private int[] progressBar;
    private BitmapFont font;

    public Intermession() {
        progressBar = new int[5];
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/UpgradeScreen.jpg");
        placeholder = new Texture("assets/Pages/Progress/prog00.png");

        progTextures = new Texture[10]; // Assuming you have 10 prog images
        for (int i = 0; i < progTextures.length; i++) {
            String texturePath = String.format("assets/Pages/Progress/prog%02d.png", i);
            progTextures[i] = new Texture(texturePath);
        }

        // Load the skin for the next wave button
        nextWaveSkin = new Skin(Gdx.files.internal("assets/jsonFiles/nextWaveButton.json"), new TextureAtlas(Gdx.files.internal("assets/jsonFiles/nextWaveButton.atlas")));

        // Load the skin for the plus buttons
        plusButtonSkin = new Skin(Gdx.files.internal("assets/jsonFiles/plusButton.json"), new TextureAtlas(Gdx.files.internal("assets/jsonFiles/plusButton.atlas")));

        // Initialize the font
        font = new BitmapFont(); // This uses the default font included with LibGDX
        font.setColor(Color.WHITE); // Set the font color to white (you can change this)
        font.getData().setScale(3); // Scale the font (adjust as necessary)

        // Create the stage and set it as the input processor
        btnStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(btnStage);

        // Create the next wave button
        createNextWaveButton();

        // Create and position the plus buttons
        createPlusButtons();

        // Initialize the stage
        stage = new Stage(new ScreenViewport());
    }

    private void createNextWaveButton() {
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
    }

    private void createPlusButtons() {
        // Assuming the button size is 140x140, adjust if needed
        float buttonWidth = 140;
        float buttonHeight = 140;

        // Calculate starting position to center the buttons vertically
        float verticalGap = 50; // Increased gap between buttons
        float startY = (Gdx.graphics.getHeight() - (5 * buttonHeight + 4 * verticalGap)) / 2;
        float centerX = (Gdx.graphics.getWidth() - buttonWidth) / 2;

        for (int i = 0; i < 5; i++) {
            Button plusButton = new Button(plusButtonSkin);

            // Set button size
            plusButton.setSize(buttonWidth, buttonHeight);

            // Position the button
            plusButton.setPosition(centerX + 110, startY + i * (buttonHeight + verticalGap));

            // Add a listener to handle button clicks
            final int buttonIndex = i; // Use final or effectively final variable
            plusButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Handle the button click event
                    System.out.println("Plus Button " + buttonIndex + " clicked!");
                    if (progressBar[buttonIndex] < 9) {
                        progressBar[buttonIndex]++;
                    }
                    for (int i = 0; i < 5; i++) {
                        System.out.print(progressBar[i] + " ");
                    }
                    System.out.println();
                }
            });

            // Add the button to the stage
            btnStage.addActor(plusButton);
        }
    }

    @Override
    public void show() {
        playBackgroundMusic("assets/Audio/UpgradeScreen/UpgradeMenuTheme.wav");
    }

    private void updateProgressBar() {
        Texture hpProg = progTextures[progressBar[0]];
        Texture dmgProg = progTextures[progressBar[1]];
        Texture frrProg = progTextures[progressBar[2]];
        Texture spdProg = progTextures[progressBar[3]];
        Texture armProg = progTextures[progressBar[4]];

        float newWidth = (float) (placeholder.getWidth() * 1.3);
        float newHeight = (float) (placeholder.getHeight() * 1.1);

        batch.draw(hpProg, 320, 865, newWidth, newHeight);
        batch.draw(dmgProg, 320, 685, newWidth, newHeight);
        batch.draw(frrProg, 320, 485, newWidth, newHeight);
        batch.draw(spdProg, 320, 305, newWidth, newHeight);
        batch.draw(armProg, 320, 105, newWidth, newHeight);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Update the progress bar before drawing
        updateProgressBar();

        // Draw the progress text
        for (int i = 0; i < progressBar.length; i++) {
            String progressText = Integer.toString(progressBar[i]);
            font.draw(batch, progressText, 1700, Gdx.graphics.getHeight() - 240 - i * 70);
        }

        batch.end();

        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Draw the UI components
        btnStage.act(Gdx.graphics.getDeltaTime());
        btnStage.draw();

        // Handle button click based on mouse coordinates
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y coordinate

            // Check if the mouse coordinates are within the bounds of the button
            if (isMouseOverButton(mouseX, mouseY)) {
                // Handle button click event here
                System.out.println("Button clicked!");
            }
        }
    }

    // Check if the mouse coordinates are over the button
    private boolean isMouseOverButton(float mouseX, float mouseY) {
        // Define the boundaries of the button (adjust according to your button's position and size)
        float buttonX = (float) Math.floor(Gdx.graphics.getWidth() / 2) + 65; // X position of the button
        float buttonWidth = 100; // Width of the button
        float buttonHeight = 110; // Height of the button

        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
            mouseY >= 860 && mouseY <= 860 + buttonHeight) {
            if (progressBar[0] < 9) {
                progressBar[0]++;
                return true;
            }
        } else if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= 660 && mouseY <= 660 + buttonHeight) {
            if (progressBar[1] < 9) {
                progressBar[1]++;
                return true;
            }
        } else if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= 475 && mouseY <= 475 + buttonHeight) {
            if (progressBar[2] < 9) {
                progressBar[2]++;
                return true;
            }
        } else if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= 285 && mouseY <= 285 + buttonHeight) {
            if (progressBar[3] < 9) {
                progressBar[3]++;
                return true;
            }
        } else if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= 90 && mouseY <= 90 + buttonHeight) {
            if (progressBar[4] < 9) {
                progressBar[4]++;
                return true;
            }
        }
        return false;
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

    public int getHpData() {
        return progressBar[0];
    }

    public int getDamageData() {
        return progressBar[1];
    }

    public int getFireRateData() {
        return progressBar[2];
    }

    public int getSpeedData() {
        return progressBar[3];
    }

    public int getArmorData() {
        return progressBar[4];
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
        plusButtonSkin.dispose();
    }
}
