package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MySql.InsertData;
import com.mygdx.game.MySql.Rankings;
import com.mygdx.game.MySql.ReadData;
import com.mygdx.game.MySql.UpdateData;
import com.mygdx.game.main.World;
import com.mygdx.game.player.Player;

import java.sql.SQLException;
import java.util.List;

public class Leaderboard implements Screen {

    private World world;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont font, scoreFont;
    private StringBuilder inputTextBuilder = new StringBuilder();
    private static final int MAX_CHARACTERS = 12;
    private InsertData insertData;
    private UpdateData updateData;
    private ReadData readData;

    public Leaderboard(World world) {
        this.world = world;
        batch = new SpriteBatch();
        background = new Texture("assets/Pages/LeaderboardScreen.jpg");

        font = new BitmapFont();
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.RED);
        font.getData().setScale(4);
        scoreFont.getData().setScale(4);
        insertData = new InsertData();
        updateData = new UpdateData();
        readData = new ReadData();
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

        handleInput();

        scoreFont.draw(batch, Integer.toString(Player.totalScore), (Gdx.graphics.getWidth() / 2f) - 50, (Gdx.graphics.getHeight() / 2f) + 170);
        font.draw(batch, inputTextBuilder.toString(), (Gdx.graphics.getWidth() / 2f) - 50, (Gdx.graphics.getHeight() / 2f) + 30);

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && inputTextBuilder.length() > 0) {
            String username = inputTextBuilder.toString();
            int score = Player.totalScore;

            insertData.setData(username, String.valueOf(score));
            try {
                List<Rankings> rankings = readData.readData();
                updateData.updateRanking(rankings);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            world.dispose();
            System.exit(0);
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
