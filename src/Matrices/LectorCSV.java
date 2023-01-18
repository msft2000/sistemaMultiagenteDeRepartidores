
package Matrices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LectorCSV {
    private ArrayList<String[]> Matrix;
    public LectorCSV() {
    }
    
    private void readFile(String path){
        String line = "";
        String cvsSplitBy = ";";//Separador de columnas

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy,-1);
                Matrix.add(data);//se agrega la fila la matriz
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
     public ArrayList<String[]> getMatrix(String path){
         Matrix=new ArrayList<>();
         readFile(path);
         return Matrix;
     }
}
