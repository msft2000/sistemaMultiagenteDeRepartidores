
package Python;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class PythonScripts {
    private static PythonScripts instance;

    public static PythonScripts getInstance(){
        if(instance==null){
            instance=new PythonScripts();
        }
        return instance;
    }
    public String[] getEnlaces(String idNodo,char input_output){
        //input_output: i - input     o - output
        //              usado para indicar si se quieren los enlaces entrantes o salientes de un nodo
        Process mProcess;         
        try{
            mProcess = Runtime.getRuntime().exec(new String[]{"python","main.py",idNodo,"Red2Way.net.xml",input_output+""},null,new File("."));
            InputStream stdout = mProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout,StandardCharsets.UTF_8));
            String line;
            String aux="";
            while((line = reader.readLine()) != null){
                aux=aux+line;
            }
            return aux.split(";");
        }catch(Exception e) {
           System.out.println("Exception Raised" + e.toString());
           return null;
        }
    }
    
}
