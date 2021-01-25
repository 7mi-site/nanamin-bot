package xyz.n7mn.dev.Command.game.jinro.ClassPlayer;

import xyz.n7mn.dev.Command.game.jinro.PlayerClass;

public class Detective implements PlayerClass {
    @Override
    public String getClassName() {
        return "探偵";
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isWerewolf() {
        return false;
    }
}
