package cn.mrcsh.zfmcserverpanelapi;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String str = "hello";
        Arrays.stream(str.split("")).forEach(System.out::println);
    }
}
