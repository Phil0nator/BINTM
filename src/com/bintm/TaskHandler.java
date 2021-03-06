package com.bintm;

import jdk.jfr.SettingDefinition;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Vector;

enum SortOrderType{

    SORT_NONE, SORT_ALPHA, SORT_RAM, SORT_CPU

}



public class TaskHandler {

    Vector<Task> tasks = new Vector<Task>(0);
    IOHandler io = new IOHandler();
    TaskUpdater updater;
    boolean populating = false;
    FileHandler fh = new FileHandler();
    SortOrderType order = SortOrderType.SORT_NONE;
    static String[] osTaskNames = {
            "svchost.exe",
            ""
    };
    TaskHandler(){}

    boolean notOsTask(String name){
        for(String s: osTaskNames){
            if(name.equals(s)){
                return false;
            }
        }
        return true;
    }

    void populate(){

        String[] taskdata = io.getStdoutArrayFor("Tasklist /FO csv");
        for(int i = 1 ; i < taskdata.length;i++){
            Task t = new Task(taskdata[i],this);
            if (notOsTask(t.name+".exe")) {
                addTask(t);

            }

        }
        System.out.println("populate");
        for( int i = 0 ; i < tasks.size();i++){
            tasks.get(i).updateCpuUse();
        }

    }

    void printChart(){

        for(Task t : getImportantTasks()){
                System.out.println(t.name);
                System.out.println("\t" + t.getPercentOfRam() + "% of ram @" + t.memuse + " Kb");
                System.out.println("\t" + t.getCpuPercent() + "% of CPU");
        }

    }
    String[] getTableHeader(){
        String[] out = {"Name","Ram Usage", "CPU Usage"};
        return out;
    }

    String[][] getInfoString(){
        String[][] out;
        ArrayList<Task> ts = getImportantTasks();
        out = new String[ts.size()][3];
        int i  = 0;
        for(Task t : ts){
            out[i][0]= t.name + "\n";
            out[i][1]= "\t" + t.getPercentOfRam() + "% of ram @" + t.memuse + " Kb" + "\n";
            out[i][2]="\t" + t.getCpuPercent() + "% of CPU"+"\n";
            i++;
        }
        return out;

    }

    void addTask(Task t){
        boolean exists = false;
        for(Task tE : tasks){
            if (tE.name.equals(t.name)){
                tE.memuse+=t.memuse;
                exists=true;
            }
        }
        if(!exists){
            tasks.add(t);
        }
    }

    void setSortOrder(SortOrderType type){
        order = type;
    }

    void sort(ArrayList<Task> t){

        switch (order){
            case SORT_NONE:
                break;
            case SORT_ALPHA:
                break;
        }
    }



    ArrayList<Task> getImportantTasks(){


        ArrayList<Task> outpt = new ArrayList<Task>(1);
        for(int i = 0; i < tasks.size();i++){
            Task t = tasks.get(i);
            if(t.getPercentOfRam()>0) {
                outpt.add(t);
            }

        }



        return outpt;

    }

    void updateCpuUsage(){

        CPUUpdater cpu = new CPUUpdater(tasks);
        //new Thread(cpu).start();
        cpu.run();
    }

    void startUpdateThread(){
        System.out.println("Making thread");
        updater = new TaskUpdater(this);
        new Thread(updater).start();
    }

    void stopUpdateThread(){
        if(updater == null){return;}
        updater.kill();
    }


}

class TaskUpdater implements Runnable{

    TaskHandler t;
    boolean stop = false;
    TaskUpdater(TaskHandler t){
        this.t=t;
    }

    void sleep(int millis){
        long time = Instant.now().toEpochMilli();
        long elapsed = 0;
        if(stop){return;}
        while(elapsed<millis) {
            elapsed = Instant.now().toEpochMilli() - time;
        }
    }

    public void run(){
        System.out.println("run");
        while(true){
            sleep(5000);
            if(stop){return;}
            t.populate();
        }

    }

    public void kill(){
        stop = true;
    }

}

class CPUUpdater implements Runnable{
    Vector<Task> tasks;
    CPUUpdater(Vector<Task> t){
        tasks=t;
    }

    public void run(){

        for(int i = 0 ; i < tasks.size();i++){
            tasks.get(i).updateCpuUse();
        }

    }


}