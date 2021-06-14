package CarSimulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class DataSet{
    private transient Gson gson;
    private LinkedList<Properties> propertiesList;
    private LinkedList<Controls> controlsList;

    /**
     * Initialize a dataset with an empty list of properties and an empty list of controls
     */
    public DataSet(){
        gson = new Gson();
        propertiesList = new LinkedList<Properties>();
        controlsList = new LinkedList<Controls>();
    }

    /**
     * Add a properties object to the end of the list
     * @param properties
     */
    public void addProperties(Properties properties){
        propertiesList.add(properties);
    }

    /**
     * 
     * @return The list of properties objects
     */
    public LinkedList<Properties> getPropertiesList(){
        return propertiesList;
    }
    
    /**
     * 
     * @return Pop the first properties object from the list
     * @throws NoSuchElementException When the list is empty
     */
    public Properties getFirstProperties() throws NoSuchElementException{
        return propertiesList.removeFirst();
    }

    /**
     * Add a controls object to the end of the list
     * @param controls
     */
    public void addControls(Controls controls){
        controlsList.add(controls);
    }

    /**
     * 
     * @return The list of controls objects
     */
    public LinkedList<Controls> getControlsList(){
        return controlsList;
    }

    /**
     * 
     * @return Pop the first controls object from the list
     * @throws NoSuchElementException When the list is empty
     */
    public Controls getFirstControls() throws NoSuchElementException{
        return controlsList.removeFirst();
    }

    /**
     * Save the lists within this object to a json file
     * @param fileName The location and name of the json file where the data needs to be saved to
     */
    public void saveToJsonFile(String fileName){
        while(propertiesList.size() != controlsList.size()){
            if(propertiesList.size() > controlsList.size()){
                propertiesList.removeLast();
            } else if(propertiesList.size() < controlsList.size()){
                controlsList.removeLast();
            }
        }
        try{
            Writer writer = new FileWriter(fileName);
            gson.toJson(this, writer);
            writer.close();
        } catch(JsonIOException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Load a dataset object from a json file
     * @param fileName The location and name of the file where the json info needs to be loaded from
     * @return A dataset object with the lists in it
     */
    public DataSet loadFromJsonFile(String fileName){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            return gson.fromJson(reader, DataSet.class);
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
