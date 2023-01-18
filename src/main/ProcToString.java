/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcToString {


    private ProcessBuilder pb;
    private String out="";
    private Process proc=null;

    public ProcToString(String... cmd){
        pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
    }

    public int runProcess() throws IOException, InterruptedException{
        proc = pb.start();
        new Stream().start();
        return proc.waitFor();
    }

    public boolean hasResult(){
        return out!="";
    }

    public String getResult(){
        return out;
    }

    final class Stream extends Thread{

        @Override
        public void run() {
            StringBuilder sb=new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            try {
                while((line = br.readLine())!=null){
                    out+=line;
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out=sb.toString();
            }
        }
    }
}