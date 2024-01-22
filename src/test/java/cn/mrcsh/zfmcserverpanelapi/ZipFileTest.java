package cn.mrcsh.zfmcserverpanelapi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileTest {

    @Test
    public void test(){
        unzip();
    }

    public void unzip() {
        ZipUtil.unzip("C:/test/Downloads.zip","C://test");
    }
}
