package com.example.rakeshyadav.intimeflowchart;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class InternalStorage extends Activity {
    private InternalStorage() {
    }

    public static void writeObject(Context context, String fileName, String data) throws IOException {
        Writer writer;
        File root = Environment.getExternalStorageDirectory();
        File outDir = new File(root.getAbsolutePath() + File.separator + "DroiDia_Project");
        if (!outDir.isDirectory()) {
            outDir.mkdir();
        }
        try {
            if (!outDir.isDirectory()) {
                throw new IOException(
                        "Unable to create directory DroiDia_Project. Maybe the SD card is mounted?");
            }
            File outputFile = new File(outDir, fileName);
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage() + " Unable to write to external storage.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public static String readObject(Context context, String fileName) throws IOException, ClassNotFoundException {
        String line1="ll";
        File root = Environment.getExternalStorageDirectory();
        String paths=root.getAbsolutePath() + File.separator + "DroiDia_Project" + "/"+ fileName;
        StringBuffer text = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(paths));
            String line;

            while ((line = br.readLine()) != null)
            {

                text.append(line);
                text.append('\n');
            }
            line1=text.toString();
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return line1;
    }
}