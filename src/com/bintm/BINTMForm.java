package com.bintm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.Instant;
import java.util.ArrayList;
import javax.swing.text.html.*;
import javax.swing.text.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class BINTMForm {
    private JPanel panel1;
    private JProgressBar Cpubar;
    private JProgressBar TotalRamBat;
    private JPanel progressArea;
    private JProgressBar freeDiskSpaceBar;
    private JProgressBar totalDiskSpaceBar;
    private JProgressBar ramInUseBar;
    private JProgressBar ramAvailableBar;
    private JTabbedPane tabbedPane1;
    private JScrollBar scrollBar1;

    private JTable taskTable;
    private DefaultTableModel taskModel;

    //pablo classes:
    private WindowUpdater updater;
    private IOHandler io;
    private SystemInfo sysinf;
    private TaskHandler th;

    public BINTMForm()
    {
        System.out.println("Window Launch...");



        io = new IOHandler(); //
        sysinf = new SystemInfo(); //
        th = new TaskHandler();
        updater = new WindowUpdater();

        taskModel = new DefaultTableModel();
        taskModel.addColumn("Name");
        taskModel.addColumn("RAM %");
        taskModel.addColumn("CPU %");
        taskTable.setModel(taskModel);

        System.out.println("Total Ram: "+sysinf.getRAMSpace()+" GB");
        System.out.println("Ram in use: "+ sysinf.getRAMinUse()+" GB");
        System.out.println("Ram Available: "+ sysinf.getAvailableRAM()+" GB");
        System.out.println("Total Disk Space: "+ sysinf.getDiskSpace() + " GB");
        System.out.println("Free Disk Space: "+sysinf.getAvailableDiskSpace() + " GB");

        th.startUpdateThread();
        new Thread(updater).start();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1.setLayout(new FlowLayout());

    }

    class WindowUpdater implements Runnable
    {
        boolean stop = false;

        void sleep(int millis){
            long time = Instant.now().toEpochMilli();
            long elapsed = 0;
            if(stop){return;}
            while(elapsed<millis) {
                elapsed = Instant.now().toEpochMilli() - time;
            }
        }

        public void run(){
            while(true){
                sleep(1000);
                if(stop){return;}
                //update window VVVVVV

                updateWindowData();
            }
        }
    }


    void updateWindowData()
    {
        Cpubar.setValue(sysinf.getCpuUsage());
        ramAvailableBar.setValue((int)(sysinf.getAvailableRAM() / sysinf.getRAMSpace() * 100));
        ramInUseBar.setValue((int)(sysinf.getRAMinUse() / sysinf.getRAMSpace() * 100));
        freeDiskSpaceBar.setValue((int)(sysinf.getAvailableDiskSpace() / sysinf.getDiskSpace() * 100));

        taskModel.setRowCount(0);
        ArrayList<Task> impTasks = th.getImportantTasks();

       // System.out.println("Update: ");

        for (int i = 0; i < impTasks.size(); i++)
        {
            String[] rowData = new String[3];
            rowData[0] = impTasks.get(i).name;
            rowData[1] = "" + impTasks.get(i).getPercentOfRam();
            rowData[2] = "" + impTasks.get(i).getCpuPercent();

            //System.out.println("\t" + impTasks.get(i).getCpuPercent());

            taskModel.addRow(rowData);
        }

        taskModel.fireTableDataChanged();
        taskTable.setModel(taskModel);
    }

    public static StyleSheet loadStyleSheet(InputStream is) throws IOException
    {
        StyleSheet s = new StyleSheet();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        s.loadRules(br, null);
        br.close();

        return s;
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("BINTMForm");
        HTMLEditorKit kit = new HTMLEditorKit();
        try {
            kit.setStyleSheet(loadStyleSheet(new FileInputStream("visual/style.css")));
            StyleSheet s = loadStyleSheet(new FileInputStream("visual/style.css"));

        }catch(Exception e){
            e.printStackTrace();
        }

        frame.setContentPane(new BINTMForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
