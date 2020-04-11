package com.bintm;

import java.time.Instant;

public class Task {
    String name = "";
    int memuse = 0;
    int cpuUse = 0;

    double cpuPercent = 0;

    SystemInfo sysinf = new SystemInfo();
    TaskHandler parent;
    int priorityForLevel(int level){

        switch(level){
            case 0:
                return 64;
            case 1:
                return 16384;
            case 2:
                return 32;
            case 3:
                return 32768;
            case 4:
                return 128;
            case 5:
                return 256;
        }
        return -400;

    }
    Task(String CSVDATA, TaskHandler p){
        parent = p;
        String[] properties = CSVDATA.replaceAll("\"","").split(",", 5);
        name = properties[0].replaceAll(".exe","");
        String preMemuse = properties[4].replaceAll(" K","").replaceAll(",","");
        if(preMemuse.equals("N/A")){
            memuse=0;
        }else {
            memuse = Integer.valueOf(preMemuse);
        }
    }
    void updateMemuse(int m){
        memuse=m;
    }
    int getSecondsOf(String s){
        if(s.equals("N/A")){return 0;}
        String[] dats = s.split(":");
        int outpt = 0;
        outpt += Integer.valueOf(dats[0])*60*60;
        outpt += Integer.valueOf(dats[1])*60;
        outpt += Integer.valueOf(dats[2]);
        return outpt;
    }
    void updateCpuUse(){
        /*
        long before = Instant.now().toEpochMilli();
        String[] data = parent.io.getStdoutArrayFor("TASKLIST /V /FO CSV /FI \"IMAGENAME eq "+this.name+".exe \"");
        long after = Instant.now().toEpochMilli();
        int totalTime =(int)(after-before);
        int i = 0;
        for(String s : data){
            if(i == 0){
                i++;
                continue;
            }
            this.cpuUse+=getSecondsOf(s.replace("\"","").split(",")[8]);
        }
        cpuPercent = ((double)cpuUse/(double)totalTime)/sysinf.getCpuUsage();
        */
        String[] data = parent.io.getStdoutArrayFor("typeperf \"process("+this.name+")\\% processor time\" -sc 1");
        this.cpuPercent = Double.valueOf(data[2].split(",")[1].replaceAll("\"",""));
    }
    int getPercentOfRam(){
        return (int)((memuse/(sysinf.getRAMSpace()*1024)));
    }
    double getCpuPercent(){
        return cpuPercent;
    }
    boolean kill(){

        try{
            String resp = parent.io.getStdoutFor("TASKKILL /T /F /IM" + name + ".exe");
            return resp.split(":")[0].equals("SUCCESS");
        }catch(Exception e){
            e.printStackTrace();
            return false;

        }

    }
    boolean setPriority(int level){

        try{

            parent.io.execute("wmic process where name=\"" +name+ "\" call setpriority \""+priorityForLevel(level)+"\"");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
