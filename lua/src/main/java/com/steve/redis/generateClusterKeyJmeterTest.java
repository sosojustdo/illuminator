package com.steve.redis;

import java.io.*;

/**
 * Created by stevexu on 8/24/17.
 */
public class GenerateClusterKeyJmeterTest {

    public static void main(String args[]) throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            File file = new File("/Users/stevexu/projecttest/api.txt");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            for(int i=1;i<=99999;i++){
                StringBuilder str = new StringBuilder();
                for (int j=0; j<=9; j++){
                    str.append(i+j*100000);
                    if(j<9){
                        str.append(",");
                    }
                }
                bw.append(str);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(bw!=null){
                bw.close();
            }
            if(fw!=null){
                fw.close();
            }
        }
    }

}
