package hu.bme.mit.train.controller;

import hu.bme.mit.train.interfaces.TrainController;
import com.google.common.collect.*;

import java.lang.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TrainControllerImpl extends Thread implements TrainController {

    private int step = 0;
    private int referenceSpeed = 0;
    private int speedLimit = 0;
    private Table<Long, Integer, Integer> table = new ImmutableTable.Builder<Long, Integer, Integer>().put(System.currentTimeMillis(), step, referenceSpeed).build();
    private final static Logger LOGGER = Logger.getLogger(TrainControllerImpl.class.getName());
    @Override
    public void followSpeed() {
        if (referenceSpeed < 0) {
            referenceSpeed = 0;
        } else {
            if (referenceSpeed + step > 0) {
                referenceSpeed += step;
            } else {
                referenceSpeed = 0;
            }
        }

        enforceSpeedLimit();
    }

    @Override
    public int getReferenceSpeed() {
        return referenceSpeed;
    }

    @Override
    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
        enforceSpeedLimit();

    }

    private void enforceSpeedLimit() {
        if (referenceSpeed > speedLimit) {
            referenceSpeed = speedLimit;
        }
    }

    @Override
    public void setJoystickPosition(int joystickPosition) {
        this.step = joystickPosition;
    }

    public Table<Long, Integer, Integer> getTachometer() {
        return table;
    }

    @Override
    public void run() {
        while (true) {
            try {
                followSpeed();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               LOGGER.log(Level.WARNING,"Interrupted!",e);
                Thread.currentThread().interrupt();
            }
        }
    }

    //for testing
    public void setReferenceSpeed(int referenceSpeed) {
        this.referenceSpeed = referenceSpeed;
    }
}
