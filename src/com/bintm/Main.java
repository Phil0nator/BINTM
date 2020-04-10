package com.bintm;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.Instant;
import java.util.ArrayList;


public class Main {

	static void sleep(int millis){
		long time = Instant.now().toEpochMilli();
		long elapsed = 0;

		while(elapsed<millis) {
			elapsed = Instant.now().toEpochMilli() - time;
		}
	}

    public static void main(String[] args) {
	    System.out.println("Hello World");
	    IOHandler io = new IOHandler();
	    SystemInfo sysinf = new SystemInfo();
	    System.out.println("Total Ram: "+sysinf.getRAMSpace()+" GB");
	    System.out.println("Ram in use: "+ sysinf.getRAMinUse()+" GB");
	    System.out.println("Ram Available: "+ sysinf.getAvailableRAM()+" GB");
	    System.out.println("Total Disk Space: "+ sysinf.getDiskSpace() + " GB");
	    System.out.println("Free Disk Space: "+sysinf.getAvailableDiskSpace() + " GB");
		System.out.println("GPU Name: "+sysinf.getGpuName());


	    TaskHandler th = new TaskHandler();
		th.fh.parseAll();
		th.fh.print();
		//th.startUpdateThread();
		JFrame frame = new JFrame("FrameDemo");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JTable table = new JTable(th.getInfoString(),th.getTableHeader());
		table.setModel(new DefaultTableModel());
		frame.getContentPane().add(table, BorderLayout.CENTER);

		String b = "penis";


		frame.pack();
		th.populate();
		th.startUpdateThread();


    }
}
