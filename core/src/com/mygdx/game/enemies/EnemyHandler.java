package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.utilities.Collision;
import com.mygdx.game.weapons.Projectile;
import com.mygdx.game.weapons.Weapon;

import java.util.ArrayList;
import java.util.Random;

public class EnemyHandler {
    public SpriteBatch spriteBatch;
    private ArrayList<Enemies> enemies; // Change type to Enemies
    private Random random;

    // HP PERCENTAGE INCREASE EVERY AFTER WAVE;
    double hpPercentage;

    // SPAWN TIME
    private long lastSpawnTime;
    private static final long SPAWN_INTERVAL = 5000; // 5 seconds in milliseconds

    // ZOMBIE TEXTURES
    private Texture[] zombieTextures;

    // DEBUGGING
    private Array<Projectile> projectiles;
    private ShapeRenderer shapeRenderer;
    private Collision enemyCollision;
    private Weapon weapon;

    private Array<Enemies> dead_enemies;

    public EnemyHandler(Weapon weapon) {
        spriteBatch = new SpriteBatch();
        random = new Random();
        enemies = new ArrayList<>();
        lastSpawnTime = TimeUtils.millis();
        hpPercentage = 1;

        // Initialize zombie textures
        zombieTextures = new Texture[] {
                new Texture(Gdx.files.internal("assets/enemies/Zombie 1/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 2/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 3/Idle/0_Zombie_Villager_Idle_000.png"))
        };

        // DEBUGGING
        spawnEnemies();
        this.weapon = weapon;
        projectiles = weapon.getProjectiles();
        shapeRenderer = new ShapeRenderer();
        enemyCollision = new Collision();

        dead_enemies = new Array<>();
    }

    public void setHealthEnemies(int currentWave) {
        if(hpPercentage == 1) {
            hpPercentage = 1.2;
        }else {
            hpPercentage = Math.pow(1.2, currentWave - 1);
        }
    }

    public void handleWave(OrthographicCamera camera) {
        spriteBatch.begin();

        // DEBUGGING
        if (TimeUtils.timeSinceMillis(lastSpawnTime) >= SPAWN_INTERVAL) {
            spawnEnemies();
            lastSpawnTime = TimeUtils.millis();
        }

        // CHECK ZOMBIE TAKING DAMAGE OR DEAD
        projectiles = weapon.getProjectiles();
        Projectile collided_projectile = enemyCollision.enemyCollision(projectiles, enemies, weapon.damage , weapon.getDamage());
        if(collided_projectile != null){
            projectiles.removeValue(collided_projectile, true);
        }

        // HANDLES DEAD ENEMIES
        for(Enemies enemy: enemies){ // Iterate over Enemies, not Zombie1
            if(!enemy.isAlive()){
                dead_enemies.add(enemy);
            }
        }
        handleDeadEnemies();

        spriteBatch.setProjectionMatrix(camera.combined);

        // RENDERS FLOATING DAMAGE
        enemyCollision.renderFloatingDamages(spriteBatch);

        // DEBUGGING
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN);

        for (Enemies enemy : enemies) { // Iterate over Enemies, not Zombie1
            Rectangle enemy_hitbox = enemy.getEnemyHitbox();
            if (enemy instanceof Zombie1) {
                spriteBatch.draw(((Zombie1) enemy).enemy_texture, enemy.getPosition().x, enemy.getPosition().y, enemy.getSize().x, enemy.getSize().y);
            } else if (enemy instanceof Zombie2) {
                spriteBatch.draw(((Zombie2) enemy).enemy_texture, enemy.getPosition().x, enemy.getPosition().y, enemy.getSize().x, enemy.getSize().y);
            } else if (enemy instanceof Zombie3) {
                spriteBatch.draw(((Zombie3) enemy).enemy_texture, enemy.getPosition().x, enemy.getPosition().y, enemy.getSize().x, enemy.getSize().y);
            }
        }
        spriteBatch.end();
        shapeRenderer.end();
    }

    public void spawnEnemies() {
        for (int i = 0; i < 3; i++) {
            int type = random.nextInt(3);
            Enemies enemy;
            switch (type) {
                case 1:
                    enemy = new Zombie2((int) (25 * hpPercentage), zombieTextures[type], rand());
                    break;
                case 2:
                    enemy = new Zombie3((int) (30 * hpPercentage), zombieTextures[type], rand());
                    break;
                default:
                    enemy = new Zombie1((int) (20 * hpPercentage), zombieTextures[type], rand());
                    break;
            }
            enemies.add(enemy);
        }
    }

    // REMOVES THE ENEMIES FROM ARRAYLIST WHEN ISALIVE IS FALSE
    private void handleDeadEnemies() {
        for(Enemies dead_enemy: dead_enemies){
            enemies.remove(dead_enemy);
        }
    }

    private Vector2 rand() {
        Vector2 random_position = new Vector2();
        int random_x = random.nextInt(450 - 50) + 50;
        int random_y = random.nextInt(300 - 50) + 50;

        random_position.set(random_x, random_y);
        return random_position;
    }
}
