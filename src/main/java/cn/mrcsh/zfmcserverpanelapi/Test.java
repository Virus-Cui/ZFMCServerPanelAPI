package cn.mrcsh.zfmcserverpanelapi;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

@Slf4j
public class Test {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println(System.getProperty("java.specification.version"));
        System.out.println(System.getProperty("java.vm.name"));
        System.out.println(System.getProperty("sun.arch.data.model"));
        System.out.println(System.getProperty("os.arch"));
    }
}
