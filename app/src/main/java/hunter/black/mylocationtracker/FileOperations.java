package hunter.black.mylocationtracker;

/**
 * Created by FaizulHauqe on 8/17/2016.
 */
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Arifur on 8/16/2016.
 */
public class FileOperations {
    File file;
    ArrayList<LatLng> latLngs;
    public FileOperations(File file, ArrayList<LatLng> latLngs){
        this.file=file;
        this.latLngs=latLngs;
    }

    public boolean WriteData(){
        System.out.println(latLngs.size());
        try {
            FileWriter fileWriter=new FileWriter(file.getAbsoluteFile());
            for (LatLng latLng : latLngs) {
                fileWriter.write(latLng.latitude+" "+latLng.longitude);
                fileWriter.append('\n');
            }

            fileWriter.flush();
            String s="";
            FileReader fileReader=new FileReader(file);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while ((s=bufferedReader.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public ArrayList<LatLng> getAllData(){
        ArrayList<LatLng> list=new ArrayList<LatLng>();
        String s="";
        FileReader fileReader= null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
        }
        BufferedReader bufferedReader=new BufferedReader(fileReader);
        try {
            while ((s=bufferedReader.readLine()) != null) {
                String[] data=s.split(" ");
                LatLng latLng=new LatLng(Double.valueOf(data[0]),Double.valueOf(data[1]));
                list.add(latLng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getPath(){
        return file.getAbsolutePath();
    }
}
