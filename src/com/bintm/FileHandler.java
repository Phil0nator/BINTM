package com.bintm;

import java.io.*;
import java.util.ArrayList;



///////////////////////////////
//File Format
//  [ Game / Important App Names ]  :
//  [ Apps To Kill while playing ]  :
//  [ High priority apps ]          :
//  [ Medium Priority apps ]        :
//  [ Low priority apps ]           :
///////////////////////////////
public class FileHandler {
    File file;

    String[] games;
    String[] deathRow;
    String[] highPriority;
    String[] mediumPriority;
    String[] lowPriority;

    FileHandler(){
        file = new File("saves/persistentData.bintm");
    }

    void parseGames(){
        try {


            BufferedReader br = new BufferedReader(new FileReader(file));
            String g = br.readLine();
            g=g.replaceAll(":","").replaceAll(" ","");
            games = g.split(",");


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void parseDeathRow(){
        try {


            BufferedReader br = new BufferedReader(new FileReader(file));
            String g = br.readLine();
            g = br.readLine();
            g=g.replaceAll(":","").replaceAll(" ","");
            deathRow = g.split(",");


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void parsePriorities(){
        try {


            BufferedReader br = new BufferedReader(new FileReader(file));
            String g = br.readLine();
            br.readLine();
            String[] priorities = {br.readLine(), br.readLine(), br.readLine()};
            highPriority = priorities[0].replaceAll(":","").replaceAll(" ","").split(",");
            mediumPriority = priorities[1].replaceAll(":","").replaceAll(" ","").split(",");
            lowPriority = priorities[2].replaceAll(":","").replaceAll(" ","").split(",");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void parseAll(){
        parseGames();
        parseDeathRow();
        parsePriorities();
    }

    void printArray(String[] arr){
        System.out.print("[");
        for(int i = 0 ; i < arr.length-2;i++){
            System.out.print(arr[i]+",");
        }
        System.out.print(arr[arr.length-1]+"]");
    }

    void print(){
        printArray(games);
        printArray(deathRow);
        printArray(highPriority);
        printArray(mediumPriority);
        printArray(lowPriority);
    }

    void save(){

        try {

            FileWriter fw = new FileWriter(file);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
