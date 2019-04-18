package com.olympus.simulation;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tan on 2/18/2016.
 * Improved by Jake on 4/4/2019.
 */
public class FileHelper {
    String fileName;
    String TAG = FileHelper.class.getName();

    public FileHelper() {
        fileName = "LastRun.json";
    }

    public FileHelper(String theFileName) {
        fileName = theFileName;
    }

    public void setFileName(String theFileName) {
        fileName = theFileName;
    }

    public String ReadFile( Context context){
        String line = "";

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(context.getExternalFilesDir(null), fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
            line = "";
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
            line = "";
        }
        return line;
    }

    /*public static boolean saveToFile( String data){
        try {
            new File(path  ).mkdir();
            File file = new File(path+ fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

            return true;
        }  catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }  catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return  false;


    }*/

    public boolean writeFileOnInternalStorage(Context mcoContext, String sBody){
        /*File file = new File(mcoContext.getFilesDir(),"mydir");
        if(!file.exists()){
            file.mkdir();
        }*/
        //File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(mcoContext.getExternalFilesDir(null), fileName);

        try{
            File gpxfile = file;//new File(file, fileName);
            gpxfile.createNewFile();
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            /*FileOutputStream stream = new FileOutputStream(file, true);
            stream.write((sBody + System.getProperty("line.separator")).getBytes());
            stream.close();*/
            //System.out.println(gpxfile.getAbsolutePath());
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String[] getFileNames(Context mcoContext) {
        File folder = mcoContext.getExternalFilesDir(null);
        File[] listOfFiles = folder.listFiles();
        String[] ret = new String[listOfFiles.length];
        for(int i = 0; i < listOfFiles.length; i++){
            ret[i] = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 5);
        }
        return ret;
    }
}
