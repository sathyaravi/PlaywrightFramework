package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;

public class DataProviderDemoTest {

    @DataProvider(name="basicData")//return multi dimensional data objects
    public Object[][] testData(){

        return new Object[][] {{"user1@yahoo.com","password1"},{"user2@gmail.com","password2"}};
    }

    @Test(dataProvider="basicData")
    public void testFillform(String email,String password){

        System.out.println(email);
        System.out.println(password);
    }

    //Dataprovider using hashMap data

    @DataProvider(name = "hashMapdata")
    public Object[][] hashMapdata(){

        HashMap<String,String> user1 = new HashMap<>();
        user1.put("email","user1@ymail.com");
        user1.put("password","password1");

        HashMap<String,String> user2 = new HashMap<>();
        user2.put("email","user2@hmail.com");
        user2.put("password","password2");

        return new Object[][]{{user1},{user2}};
    }

    @Test(dataProvider = "hashMapdata")
    public void fillform2(HashMap<String,String> data){

        System.out.println(data.get("email"));
        System.out.println(data.get("password"));

    }

    @DataProvider(name = "jsonData") public Object[][] jsonData() throws IOException
    {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/testDAta_tc1.json";
        // Read JSON file String
        String jsonContent = Files.readString(Paths.get(filePath));
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
    @Test(dataProvider = "jsonData")
    public void fillFormUsingJson(HashMap<String, String> data)
    {
        System.out.println("Email: " + data.get("email"));
        System.out.println("Password: " + data.get("password"));
    }


}
