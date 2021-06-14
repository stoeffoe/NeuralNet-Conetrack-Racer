package CarSimulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class DataSet{
    private transient Gson gson;
    private LinkedList<Properties> propertiesList;
    private LinkedList<Controls> controlsList;

    public DataSet(){
        gson = new Gson();
        propertiesList = new LinkedList<Properties>();
        controlsList = new LinkedList<Controls>();
    }

    public void addProperties(Properties properties){
        propertiesList.add(properties);
    }

    public LinkedList<Properties> getPropertiesList() {
        return propertiesList;
    }
    
    public Properties getFirstProperties(){
        return propertiesList.removeFirst();
    }

    public void addControls(Controls controls){
        controlsList.add(controls);
    }

    public LinkedList<Controls> getControlsList() {
        return controlsList;
    }

    public Controls getFirstControls(){
        return controlsList.removeFirst();
    }

    public void saveToJsonFile(String fileName){
        try{
            Writer writer = new FileWriter(fileName);
            gson.toJson(this, writer);
            writer.close();
        } catch (JsonIOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public DataSet loadFromJsonFile(String fileName){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            return gson.fromJson(reader, DataSet.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
