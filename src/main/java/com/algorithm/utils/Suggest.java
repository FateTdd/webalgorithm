package com.algorithm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Suggest {
    public static void saveSuggest(String suggest){
        try {
            // To prevent file creation or reading failure, use catch to catch errors and print, or throw
            /* Write Txt file */
            String suggestPath="D:\\web\\algorithm\\"+System.currentTimeMillis()+".txt";
            File writename = new File(suggestPath);// Relative path, if not, create a new output. txt file
            if(!writename.exists()){
                writename.createNewFile(); // Create new file
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            StringBuilder sb=new StringBuilder(suggest);
            out.write(sb.toString());
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
