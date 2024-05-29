package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.main.World;
import com.mygdx.game.player.Player;

public class Leaderboard implements Screen {

    private World world;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont font, scoreFont;
    private StringBuilder inputTextBuilder = new StringBuilder();
    private static final int MAX_CHARACTERS = 10;

    public Leaderboard(World world) {
        this.world = world;
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/LeaderboardScreen.jpg");

        // Create a default BitmapFont
        font = new BitmapFont();
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.RED);
        font.getData().setScale(4);
        scoreFont.getData().setScale(4);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Append letters and numbers based on keyboard input
        handleInput();

        // Draw the input text
        scoreFont.draw(batch, Integer.toString(Player.totalScore), ((float) Gdx.graphics.getWidth() / 2) - 50, ((float) Gdx.graphics.getHeight() / 2) + 170);
        font.draw(batch, inputTextBuilder.toString(), ((float) Gdx.graphics.getWidth() / 2) - 50, ((float) Gdx.graphics.getHeight() / 2) + 30);

        batch.end();
    }

    private void handleInput() {
        if (inputTextBuilder.length() < MAX_CHARACTERS) {
            for (int key = Input.Keys.A; key <= Input.Keys.Z; key++) {
                if (Gdx.input.isKeyJustPressed(key)) {
                    char letter = (char) (key - Input.Keys.A + 'A');
                    inputTextBuilder.append(letter);
                }
            }

            for (int key = Input.Keys.NUM_0; key <= Input.Keys.NUM_9; key++) {
                if (Gdx.input.isKeyJustPressed(key)) {
                    char number = (char) (key - Input.Keys.NUM_0 + '0');
                    inputTextBuilder.append(number);
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                inputTextBuilder.append(" ");
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && inputTextBuilder.length() > 0) {
            inputTextBuilder.deleteCharAt(inputTextBuilder.length() - 1);
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
        batch.dispose();
        background.dispose();
        font.dispose();
        scoreFont.dispose();
    }
}
