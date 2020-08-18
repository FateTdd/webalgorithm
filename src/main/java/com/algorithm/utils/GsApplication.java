package com.algorithm.utils;

import org.springframework.util.StringUtils;
import java.io.*;
import java.util.*;

public class GsApplication {
    public List<Man> allman=null;
    public List<Woman>  allwoman=null;
    public List<String> txtList=new ArrayList<String>();
    //    static String filePath="D:\\web\\algorithm\\wmmatch.txt";//File address.
    //Initialize the preference list
    public GsApplication(String manStr,String womanStr){
        String[] manStrArr=manStr.split("-");
        String[] womanStrArr=womanStr.split("-");
        txtList.addAll(Arrays.asList(manStrArr));
        txtList.addAll(Arrays.asList(womanStrArr));
        List<Man> manList=new ArrayList<Man>();
        List<Woman>  womanList=new ArrayList<Woman>();

        if(allman==null){
            int manSize=Integer.valueOf(StringUtils.trimAllWhitespace(manStrArr[0].replace("manNum=","")));
            int womanSize=Integer.valueOf(StringUtils.trimAllWhitespace(womanStrArr[0].replace("womanNum=","")));
            for(int i=1;i<=manSize;i++){
                manList.add(new Man(i));
            }
            for(int i=1;i<=womanSize;i++){
                womanList.add(new Woman(i));
            }
        }else{
            manList=allman;
            womanList=allwoman;
        }

        for(Man man:manList){
            Woman[] preWoman=new Woman[womanList.size()];
            String preStr="man"+man.getCode()+"=";
            for(String txtStr:txtList){
                if(txtStr.startsWith(preStr)){
                    preStr=txtStr.replace(preStr,"");
                    break;
                }
            }
            String[] preWomanCode=preStr.split(",");
            for(int i=0;i<preWomanCode.length;i++){
                preWoman[i]=getWomanInList(womanList,preWomanCode[i]);
            }
            man.setPreferWoman(preWoman);
        }
        for(Woman woman:womanList){
            Man[] preMan=new Man[manList.size()];
            String preStr="woman"+woman.getCode()+"=";
            for(String txtStr:txtList){
                if(txtStr.startsWith(preStr)){
                    preStr=txtStr.replace(preStr,"");
                    break;
                }
            }
            String[] preManCode=preStr.split(",");
            for(int i=0;i<preManCode.length;i++){
                preMan[i]=getManInList(manList,preManCode[i]);
            }
            woman.setPreferMan(preMan);
        }
        allman= manList;
        allwoman=womanList;

    }
    public void initAgain(){
        List<Man> manList=allman;
        List<Woman> womanList=allwoman;
        for(Man man:manList){
            Woman[] preWoman=new Woman[womanList.size()];
            String preStr="man"+man.getCode()+"=";
            for(String txtStr:txtList){
                if(txtStr.startsWith(preStr)){
                    preStr=txtStr.replace(preStr,"");
                    break;
                }
            }
            String[] preWomanCode=preStr.split(",");
            for(int i=0;i<preWomanCode.length;i++){
                preWoman[i]=getWomanInList(womanList,preWomanCode[i]);
            }
            man.setPreferWoman(preWoman);
        }
        for(Woman woman:womanList){
            Man[] preMan=new Man[manList.size()];
            String preStr="woman"+woman.getCode()+"=";
            for(String txtStr:txtList){
                if(txtStr.startsWith(preStr)){
                    preStr=txtStr.replace(preStr,"");
                    break;
                }
            }
            String[] preManCode=preStr.split(",");
            for(int i=0;i<preManCode.length;i++){
                preMan[i]=getManInList(manList,preManCode[i]);
            }
            woman.setPreferMan(preMan);
        }
        allman= manList;
        allwoman=womanList;
    }

    public Man getManInList(List<Man> manList,String manCode){//Get the male code from the list.
        for(Man man:manList){
            if(man.getCode()==Integer.valueOf(manCode)){//If the codes are equal, the male is returned.
                return man;
            }
        }
        return null;
    }
    public Woman getWomanInList(List<Woman> womanList,String womanCode){
        for(Woman man:womanList){
            if(man.getCode()==Integer.valueOf(womanCode)){//If the codes are equal, the female is returned.
                return man;
            }
        }
        return null;
    }
    //Check free men
    public Man findFreedomMan(){
        for(Man man:allman){
            if(man.isFreedom()){
                return man;
            }
        }
        return null;
    }

    //Match
//    public void match(Man man,Woman woman){
//        if(woman.isFreedom()){
//            //Freedom.together
//            man.setPartner(woman);//Set male partners.
//            man.setFreedom(false);//The man is not free.
//            woman.setPartner(man);
//            woman.setFreedom(false);
//        }else{
//            //Not free.In a boyfriend
//            man.setPartner(woman);
//            man.setFreedom(false);
//            Man preMan=woman.getPartner();
//            preMan.setFreedom(true);
//            preMan.setPartner(null);
//            woman.setPartner(man);
//        }
//
//    }
//
//    //Search for partners
//    public void searchPartner(Man man,List<Woman> womanTotal){
//        for(Woman tempWoman:man.getPreferWoman()){
//            if(tempWoman.isFreedom()){
//                //Freedom.Can be together
//                match(man,tempWoman);
//                break;
//            }else{
//                //Not free.To compare
//                Man currentMan=tempWoman.getPartner();
//                int manOrder=0;
//                int currentOrder=0;
//                for(int i=0;i<tempWoman.getPreferMan().length;i++){
//                    if(tempWoman.getPreferMan()[i].equals(man)){
//                        manOrder=i;
//                    }
//                    if(tempWoman.getPreferMan()[i].equals(currentMan)){
//                        currentOrder=i;
//                    }
//                }
//                if(manOrder<currentOrder){
//                    //Men and women are more compatible. Change
//                    match(man,tempWoman);
//                    break;
//                }
//            }
//        }
//    }


    public static void main(String[] args) {
//        //Enter the TXT file name and read the Settings in the file.
//        Scanner in = new Scanner(System.in);
//        System.out.println("Please enter the file you want to read.");
//        String str = in.nextLine();
//
//        GsApplication gsApplication=new GsApplication();
//        gsApplication.ReadFile(str);
//        while (true){
//            Man freeMan=gsApplication.findFreedomMan();
//            if(freeMan!=null){
//                gsApplication.searchPartner(freeMan,gsApplication.allwoman);
//            }else{
//                System.out.println("END=====================All the men have partners");
//                break;
//            }
//        }
//        gsApplication.ReadFile(str);
//        CheckUtil.hasBlockMatch(gsApplication.allman);
//        for(Man man:gsApplication.allman){
//            System.out.println(man.getName()+"===========Marry=========="+man.getPartner().getName());
//        }
    }
}