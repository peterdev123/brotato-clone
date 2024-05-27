package com.mygdx.game.utilities;

import com.mygdx.game.Screens.Intermession;
import com.mygdx.game.main.World;

public class WaveHandler extends Thread {
    private int currentWave = 1;
    private int waveTimer = 30; // Duration of each wave in seconds
    private boolean running = true;
    private boolean paused = false;
    private boolean headStart = true;

    @Override
    public void run() {
        while (running) {
            if(headStart) {
                if(!paused) {
                    try {
                        Thread.sleep(3000); // Headstart for 3 seconds
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    headStart = false;
                }
            }
            System.out.println(headStart);
            if(!headStart) {
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                    if (!paused) {
                        waveTimer -= 1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setHeadStart() {
        headStart = true;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getWaveTimer() {
        return waveTimer;
    }

    public void stopTimer() {
        running = false;
    }

    public void setWaveTimer(int time) {
        waveTimer = time;
    }

    public void setWave(int wave) {
        currentWave += wave;
    }

    public void pauseTimer() {
        paused = true;
    }

    public void resumeTimer() {
        paused = false;   }

    public void startWave() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        start();
    }
}


