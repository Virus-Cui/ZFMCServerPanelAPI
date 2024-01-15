package cn.mrcsh.zfmcserverpanelapi;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String log = "[22:54:22 INFO]: Generating keypair";
        String[] split = log.split("INFO]:");
        System.out.println(split[0]+"INFO]:"+split[1]);
    }
}
