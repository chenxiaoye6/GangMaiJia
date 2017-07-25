package com.example.administrator.gangmaijia.Util;

import com.example.administrator.gangmaijia.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/10/14.
 */

public class CitySqliteDatabase {

    public String  CopySqliteFileFromRawToDatabases(String SqliteFileName) throws IOException {

        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

        try {
            System.out.println("----------dir---------"+"data/data/com.example.administrator.gangmaijia/databases");
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dir = new File("data/data/com.example.administrator.gangmaijia/databases");
//        LogUtil.i("!dir.exists()=" + !dir.exists());
//        LogUtil.i("!dir.isDirectory()=" + !dir.isDirectory());

        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }


        File file= new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream =null;

        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();

                inputStream = MyApplication.getInstance().getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len ;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }


            } catch (IOException e) {
                e.printStackTrace();

            }

            finally {

                if (outputStream != null) {

                    outputStream.flush();
                    outputStream.close();

                }
                if (inputStream != null) {
                    inputStream.close();
                }

            }

        }

        return file.getPath();

    }

}
