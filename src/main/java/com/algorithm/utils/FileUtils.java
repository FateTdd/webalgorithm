package com.algorithm.utils;

import com.google.common.base.Joiner;

import java.io.*;
import java.util.*;


/*
Create a TXT file, enter the number of people, and generate a txt file.
 */
public class FileUtils {
    static String suggestPath="D:\\suggest";
    //    static int manNum=6;
//    static int manNum;
//    static int womanNum=6;
//    static int womanNum;
//    static String filePath="D:\\web\\algorithm\\wmmatch.txt";//TXT file address.D:\zuoye\SM

//    public static int getManNum(int i){
//            int manNum = i;
//            return manNum;
//    }
//
//    public static int getWomanNuma(int i){
//        int womanNum = i;
//        return womanNum;
//    }

    public static void createTxt(int n,String str){
        //Create a TXT file to save the input data and the generated preference list.
        int manNum = n;//The number of men and women.
        int womanNum = n;
        File filePath = new File("D:\\web\\algorithm\\"+ str +".txt");
        try {
            File writename = new File(String.valueOf(filePath));// Create a relative path. if not, create a new output. txt file
            if(!writename.exists()){
                writename.createNewFile(); // Create new file
            }

            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            StringBuilder sb=new StringBuilder("manNum="+manNum);//Enter the number of men and women.
            sb.append("\r\n");//Change the line.
            sb.append("womanNum="+womanNum);
            sb.append("\r\n");//Change the line.

            List<Integer> manList=new ArrayList<Integer>();//Create a list of men. The size of the list is related to the input value N.
            for (int i =1;i<=manNum;i++){
                manList.add(i);
            }
            List<Integer> womanList=new ArrayList<Integer>();//Create a list of women. The size of the list is related to the input value N.
            for (int i =1;i<=womanNum;i++){
                womanList.add(i);
            }
            for (int i =0;i<manList.size();i++){//Shuffle the order and get a random preference list.
                Collections.shuffle(womanList);
                sb.append("man"+manList.get(i)+"="+ Joiner.on(",").join(womanList));
                sb.append("\r\n");
            }
            Collections.sort(womanList);//Shuffle the order and get a random preference list.
            for (int i =0;i<womanList.size();i++){
                Collections.shuffle(manList);
                sb.append("woman"+womanList.get(i)+"="+Joiner.on(",").join(manList));
                sb.append("\r\n");// \r\n means line break
            }
            out.write(sb.toString());
            out.flush(); // Push the contents of the buffer into the file
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSuggest(String suggest){
        try {
            // To prevent file creation or reading failures, use catch to catch errors and print, or throw
            /* Write to Txt file */
            String suggestPath="D:\\suggest\\"+System.currentTimeMillis()+".txt";
            File writename = new File(suggestPath);// Relative path, if not, create a new output.txt file.
            if(!writename.exists()){
                writename.createNewFile();
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
    public static List<Suggest> getAllSuggest(){
        List<Suggest> suggestList=new ArrayList<Suggest>();
        String[] list=new File(suggestPath).list();
        for (String str : list) {
            File tempFIle=new File(suggestPath+"\\"+str);
            Suggest suggest=new Suggest();
            try {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(tempFIle));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                String content="";
                while((lineTxt = bufferedReader.readLine()) != null){
                    content+=lineTxt;
                }
                suggest.setContent(content);
                suggest.setTime(Long.valueOf(str.replace(".txt","")));
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            suggestList.add(suggest);
        }


        return suggestList;
    }
    public static String saveLog(String msgStr) {
        String logPath="D:"+System.currentTimeMillis()+"-log.txt";

        try {
            // To prevent file creation or reading failures, use catch to catch errors and print, or throw
            /* Write to Txt file */
            File writename = new File(logPath);// Relative path, if not, create a new output.txt file.
            if(!writename.exists()){
                writename.createNewFile(); // Create a new file
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            StringBuilder sb=new StringBuilder();
            String[] msgArr=msgStr.split("###");
            for(String tempMsg:msgArr){
                sb.append(tempMsg);
                sb.append("\r\n");
            }
            out.write(sb.toString());
            out.flush();
            out.close();
            return logPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logPath;

    }

    public static void main(String[] args) {
        //Enter the TXT file name and read the Settings in the file.

        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the file name that you want to create.");
        String str = in.nextLine();


        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the number of people：");
        int n = 0;
//        if (scan.hasNextInt()) {
//            // Determine whether the input is an integer
//            n = scan.nextInt();
//            // Receive integer
//            System.out.println("The number of men and women is：" + n);
//        } else {
//            System.out.println("Please enter an integer!");
//        }
//        System.out.println("The preference list for men and women will be randomly generated.");
        while(true) {
            try {
                n = scan.nextInt();			//If the input is not an integer, an InputMismatchException will be thrown.
                break;								//If it is an integer, exit the while loop.
            }catch(Exception e) {				//This Exception is caught with an Exception.
                System.out.println("What you entered is not an integer, please continue to enter an integer!");
                scan.next();
            }
        }
        System.out.println("The number of men and women is："+n);
        System.out.println("The preference list for men and women will be randomly generated.");

//        getManNum(n);
//        getWomanNuma(n);
        createTxt(n,str);
    }
}