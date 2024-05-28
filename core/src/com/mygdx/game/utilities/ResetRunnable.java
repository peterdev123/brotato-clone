package com.mygdx.game.utilities;

import com.mygdx.game.enemies.EnemyHandler;

public class  ResetRunnable implements Runnable {
    private WaveHandler waveTimerThread;
    private EnemyHandler enemyHandler;

    public ResetRunnable(WaveHandler waveTimerThread, EnemyHandler enemyHandler) {
        this.waveTimerThread = waveTimerThread;
        this.enemyHandler = enemyHandler;
    }

    @Override
    public void run() {
        waveTimerThread.setWaveTimer(30);
        waveTimerThread.setWave(1);
        enemyHandler.setHealthEnemies(waveTimerThread.getCurrentWave());
    }
}
