package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APIE2ETesting {

    @Test
    public void e2eTest(){


        Playwright playwright = Playwright.create();

        HashMap<Object,Object> loginPayload=new HashMap<>();
        loginPayload.put("email","sathya.ravichandran@aol.com");
        loginPayload.put("password","Testing@123");

        APIRequestContext apiRequest=playwright.request().newContext();

        APIResponse apiResponse=apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));

        Assert.assertTrue(apiResponse.ok());

        System.out.println(apiResponse.statusText());

        System.out.println(apiResponse.text());

        String token=JsonPath.read(apiResponse.text(),"$.token");

        System.out.println("Login Successful and extracted token:"+token);

        //Create Event
        HashMap<Object,Object> createPayload = new HashMap<>();
        createPayload.put("title","API Testing Using Playwright");
        createPayload.put("description","QA");
        createPayload.put("category","Conference");
        createPayload.put("venue","ABC Avenue");
        createPayload.put("city","Chennai");
        createPayload.put("eventDate","2026-10-14T13:30:00.000Z");
        createPayload.put("price",150);
        createPayload.put("totalSeats",100);

        APIResponse createEvent=apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization","Bearer "+token)
                        .setData(createPayload));

        Assert.assertTrue(createEvent.ok());

        Integer eventId=JsonPath.read(createEvent.text(),"$.data.id");

        System.out.println("Event created Sucessfully: "+eventId);

        //Get Event

        APIResponse getEvent=apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events/",
                RequestOptions.create().setQueryParam("id",eventId)
                        .setHeader("Authorization","Bearer "+token));



        System.out.println(getEvent.text());

        List<Integer> allEventIds = JsonPath.read(getEvent.text(),"$.data[*].id");

        Assert.assertTrue(allEventIds.contains(eventId),"Created Event Sucessfully");


        //Delete Event

        APIResponse deleteResponse=apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("Authorization","Bearer "+token));


        Assert.assertTrue(deleteResponse.ok());

        //verify the event deleted



        APIResponse getEventafterdeletion=apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events/",
                RequestOptions.create().setQueryParam("page",1)
                        .setQueryParam("limit",12)
                        .setHeader("Authorization","Bearer "+token));



        System.out.println(getEventafterdeletion.text());

        List<Integer> allEventAfterdeletion = JsonPath.read(getEventafterdeletion.text(),"$.data[*].id");

        Assert.assertFalse(allEventAfterdeletion.contains(eventId),"Deleted event no longer present in the list");




    }
}
