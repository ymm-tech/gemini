package io.manbang.easybytecoder.runtimecommonapi.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author xujie
 */
public class FileUtils {
    public static void writeNIO(String filePath, String content) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filePath));
            FileChannel channel = fos.getChannel();
            ByteBuffer src = StandardCharsets.UTF_8.encode(content);
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            System.out.println("初始化容量和limit：" + src.capacity() + ","
                    + src.limit());
            int length = 0;

            while ((length = channel.write(src)) != 0) {
                /*
                 * 注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                 */
                System.out.println("写入长度:" + length);
            }
            channel.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String readNIO(String filePath) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(filePath));
            FileChannel channel = fin.getChannel();
            // 字节
            int capacity = 1048576 * 5;
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            StringBuilder builder = new StringBuilder();
            int length = -1;
            while ((length = channel.read(bf)) != -1) {
                bf.clear();
                byte[] bytes = bf.array();
                String str = new String(bytes, 0, length, "UTF-8");
                builder.append(str);
            }
            channel.close();
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    public static ArrayList<String> getFileName(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return null;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");
            } else {
                files.add(fs.getName());
            }
        }
        return files;
    }

}
