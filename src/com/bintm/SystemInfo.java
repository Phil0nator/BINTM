package com.bintm;
import java.io.File;
import java.lang.management.ManagementFactory;




public class SystemInfo {
    com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    IOHandler io = new IOHandler();
    SystemInfo(){

    }
    //RAM data returns in GB
    long getRAMSpace(){ //returns in GB as double

        return Math.round((((double)os.getTotalPhysicalMemorySize()/1024)/1024)/1024);
    }
    double getRAMinUse(){

        double rmSpace = getRAMSpace();
        double open = getAvailableRAM();
        return (rmSpace - open);

    }
    double getAvailableRAM(){

        return ((os.getFreePhysicalMemorySize()/1024.0)/1024)/1024;

    }
    //Disk data returns in GB
    double getDiskSpace(){
        return ((new File("/").getTotalSpace()/1024.0)/1024.0)/1024.0;
    }
    double getAvailableDiskSpace(){
        return (((new File("/").getFreeSpace()/1024.0)/1024.0)/1024.0);
    }
    //returns a percent of total cpu capacity
    int getCpuUsage(){

        return Integer.valueOf(io.getStdoutArrayFor("wmic cpu get loadpercentage")[2].substring(0,2).replaceAll(" ",""));
    }
    String getGpuName(){

        return io.getStdoutArrayFor("wmic path win32_VideoController get name ")[2];
    }
    double getSystemLoad(){
        return os.getSystemLoadAverage();
    }


}
