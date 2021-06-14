/*
- Ruben Hiemstra 0924010
- Stefan Beenen 0963586
- Jordy Weijgertse 0974347
*/

package NeuralNet;

import java.util.ArrayList;

public class Dataset {

    private Data dataSet[] ;

    public Data[] getDataSet() {
        return dataSet;
    }
  
                        
    Dataset(){
        ArrayList<Data> dataSet = new  ArrayList<Data>();

        // data
            
        this.dataSet = new Data[dataSet.size()];

        for (int indexData = 0; indexData < dataSet.size(); indexData++) {
            this.dataSet[indexData] =dataSet.get(indexData); 
        }
        
        
    }    
}
