package CarSimulator;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataSetTest{
    static Gson gson;
    DataSet dataSet;
    Properties properties;
    Controls controls;

    @BeforeClass
    public static void initializeClass(){
        gson = new Gson();
    }

    @Before
    public void initialize(){
        dataSet = new DataSet();
        controls = new Controls(10, 1);
        properties = gson.fromJson("{\"lidarDistances\":[1.0E20,7.1029,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,6.5159,1.0E20,1.0E20,1.0E20,8.4964,1.0E20,1.0E20,1.0E20,1.0E20,10.304,8.0743,12.3452,1.0E20,1.0E20,9.9589,12.0171,1.0E20,1.0E20,1.0E20,12.8513,1.0E20,1.0E20,1.0E20,12.6555,13.6439,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,12.0074,1.0E20,10.9972,1.0E20,1.0E20,9.5106,7.5145,5.5212,1.0E20,1.0E20,1.0E20,9.6156,1.0E20,7.6468,1.0E20,1.0E20,1.0E20,5.7,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.0243,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,2.5013,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,4.0302,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.2181,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.1235,4.47,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,6.1002,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.3122,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20],\"lidarHalfApertureAngle\":60,\"isOnTrack\":true,\"progress\":0.0,\"lapTime\":1.49,\"collided\":false}", Properties.class);
    }

    @Test
    public void addPropertiesTest(){
        dataSet.addProperties(properties);
        List<Properties> expecteds = new ArrayList<Properties>();
        expecteds.add(properties);
        List<Properties> actuals = dataSet.getPropertiesList();
        assertEquals(expecteds, actuals);
    }

    @Test
    public void getFirstPropertiesTest(){
        assertEquals(0, dataSet.getPropertiesList().size());
        dataSet.addProperties(properties);
        assertEquals(1, dataSet.getPropertiesList().size());
        assertEquals(properties, dataSet.getFirstProperties());
        assertEquals(0, dataSet.getPropertiesList().size());
    }

    @Test
    public void addControlsTest(){
        dataSet.addControls(controls);
        List<Controls> expecteds = new ArrayList<Controls>();
        expecteds.add(controls);
        List<Controls> actuals = dataSet.getControlsList();
        assertEquals(expecteds, actuals);
    }

    @Test
    public void getFirstControlsTest(){
        assertEquals(0, dataSet.getControlsList().size());
        dataSet.addControls(controls);
        assertEquals(1, dataSet.getControlsList().size());
        assertEquals(controls, dataSet.getFirstControls());
        assertEquals(0, dataSet.getControlsList().size());
    }

    @Test
    public void saveToJsonFileTest() throws IOException{
        String fileName = "test.json";
        dataSet.addProperties(properties);
        dataSet.addControls(controls);
        dataSet.saveToJsonFile(fileName);

        String expected = "{\"propertiesList\":[{\"lidarDistances\":[1.0E20,7.1029,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,6.5159,1.0E20,1.0E20,1.0E20,8.4964,1.0E20,1.0E20,1.0E20,1.0E20,10.304,8.0743,12.3452,1.0E20,1.0E20,9.9589,12.0171,1.0E20,1.0E20,1.0E20,12.8513,1.0E20,1.0E20,1.0E20,12.6555,13.6439,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,12.0074,1.0E20,10.9972,1.0E20,1.0E20,9.5106,7.5145,5.5212,1.0E20,1.0E20,1.0E20,9.6156,1.0E20,7.6468,1.0E20,1.0E20,1.0E20,5.7,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.0243,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,2.5013,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,4.0302,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.2181,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.1235,4.47,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,6.1002,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.3122,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20],\"lidarHalfApertureAngle\":60,\"isOnTrack\":true,\"progress\":0.0,\"lapTime\":1.49,\"collided\":false}],\"controlsList\":[{\"steeringAngle\":10.0,\"targetVelocity\":1.0}]}";
        BufferedReader reader = Files.newBufferedReader(Paths.get("./jsonFiles/dataset/" + fileName));
        String actual = reader.readLine();
        assertEquals(expected, actual);
        Files.delete(Paths.get("./jsonFiles/dataset/" + fileName));
    }

    @Test
    public void loadFromJsonFileTest() throws IOException{
        String fileName = "test.json";
        String jsonString = "{\"propertiesList\":[{\"lidarDistances\":[1.0E20,7.1029,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,6.5159,1.0E20,1.0E20,1.0E20,8.4964,1.0E20,1.0E20,1.0E20,1.0E20,10.304,8.0743,12.3452,1.0E20,1.0E20,9.9589,12.0171,1.0E20,1.0E20,1.0E20,12.8513,1.0E20,1.0E20,1.0E20,12.6555,13.6439,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,12.0074,1.0E20,10.9972,1.0E20,1.0E20,9.5106,7.5145,5.5212,1.0E20,1.0E20,1.0E20,9.6156,1.0E20,7.6468,1.0E20,1.0E20,1.0E20,5.7,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.0243,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,2.5013,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,4.0302,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.2181,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.1235,4.47,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,6.1002,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,5.3122,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20,1.0E20],\"lidarHalfApertureAngle\":60,\"isOnTrack\":true,\"progress\":0.0,\"lapTime\":1.49,\"collided\":false}],\"controlsList\":[{\"steeringAngle\":10.0,\"targetVelocity\":1.0}]}";
        BufferedWriter writer = new BufferedWriter(new FileWriter("./jsonFiles/dataset/" + fileName));
        writer.write(jsonString);
        writer.close();

        dataSet = DataSet.loadFromJsonFile(fileName);
        String expected = jsonString;
        String actual = gson.toJson(dataSet);
        assertEquals(expected, actual);
        Files.delete(Paths.get("./jsonFiles/dataset/" + fileName));
    }
}
