package utils;

import global.Global;

import java.util.Random;

public class RandUtil {

    public static int getRandNeedTime() {
        return getRand(Global.NUM_NEEDTIME_MIN, Global.NUM_NEEDTIME_MAX);
    }

    public static int getRandPrivilege() {
        return getRand(Global.NUM_PRIVILEGE_MIN, Global.NUM_PRIVILEGE_MAX);
    }

    public static int getRandNeedMemory() {
        return getRand(Global.NUM_NEEDTIME_MIN, Global.NUM_NEEDMEMORY_MAX);
    }

    public static int getRand(int a, int b) {
        Random rand = new Random();
        return rand.nextInt(b) + a;
    }

}
