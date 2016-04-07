package csc550.memegenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Christopher on 4/7/2016.
 */
public class MemeGeneratorRepository {
    private String baseURL = "http://version1.api.memegenerator.net/";

    public ArrayList<LinkedHashMap> GetInstancesByPopular(Integer numberOfResults, String generatorName) {

        String requestURL = baseURL + "Instances_Select_ByPopular?languageCode=en";
        if(numberOfResults != null) {
            requestURL += "&pageSize=" + numberOfResults;
        }
        if(generatorName != null) {
            requestURL += "&urlName=" + generatorName;
        }
        final ArrayList<LinkedHashMap>[] result = new ArrayList[]{null};
        new RetrieveJsonDataFromUrlTask( new RetrieveJsonDataFromUrlTask.AsyncResponse() {

            @Override
            public void processFinish(Map<String, Object> output) {
                result[0] = (ArrayList<LinkedHashMap>)output.get("result");
            }
        }).execute("http://version1.api.memegenerator.net/Instances_Select_ByPopular");

        return result[0];
    }

}
