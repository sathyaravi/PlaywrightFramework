package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DataProviderUtil {

    public static Object[][] getJsonData(String filePath) throws IOException {

        String fPath = System.getProperty("user.dir") + filePath;
        // Read JSON file String
        String jsonContent = Files.readString(Paths.get(fPath));
        // Create ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Convert JSON array into List of HashMaps
        List<HashMap<String, String>> data = objectMapper.readValue( jsonContent, new TypeReference<List<HashMap<String, String>>>() { } );
        // Convert List into Object[][]
        Object[][] testData = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++)
        {
            testData[i] = new Object[]{data.get(i)};
        }
        return testData;


    }
}
