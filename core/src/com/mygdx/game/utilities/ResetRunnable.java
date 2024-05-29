package com.mygdx.game.utilities;

import com.mygdx.game.Screens.Intermession;
import com.mygdx.game.enemies.EnemyHandler;

public class  ResetRunnable implements Runnable {
    private WaveHandler waveTimerThread;
    private EnemyHandler enemyHandler;
    private Intermession intermession;

    public ResetRunnable(WaveHandler waveTimerThread, EnemyHandler enemyHandler, Intermession intermession) {
        this.waveTimerThread = waveTimerThread;
        this.enemyHandler = enemyHandler;
        this.intermession = intermession;
    }

    @Override
    public void run() {
        intermession.setStatPoints(3);
        waveTimerThread.setWaveTimer(30);
        waveTimerThread.setWave();
        enemyHandler.setHealthEnemies(waveTimerThread.getCurrentWave());
    }
}
