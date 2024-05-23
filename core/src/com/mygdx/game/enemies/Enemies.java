package com.mygdx.game.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemies {

    private int health;
    private int attackPower;
    private int defense;
    private String name;
    private boolean isAlive;
    private Vector2 position;
    private Vector2 size;
    private Rectangle hitbox;

    public Enemies(String name, int health, int attackPower, int defense, Vector2 position, Vector2 size, Rectangle hitbox) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.defense = defense;
        this.isAlive = true;
        this.position = position;
        this.size = size;
        this.hitbox = hitbox;
    }

    // Abstract methods that must be implemented by subclasses
    public abstract void attack();
    public abstract void takeDamage(int damage);
    public abstract void move();

    // Common methods that all enemies can use
    public void displayStatus() {
        System.out.println(name + " - Health: " + health + ", Attack: " + attackPower + ", Defense: " + defense);
    }

    // Getters and Setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Rectangle getEnemyHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
