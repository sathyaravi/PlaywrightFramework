package tests;

import com.microsoft.playwright.Locator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

public class FrameworkDataDrivenTest extends BaseTest{



    @DataProvider(name="eventBooking")

    public Object[][] eventBookingData() throws IOException {

       return  DataProviderUtil.getJsonData("/src/test/resources/eventBooking.json");


    }

    @Test(groups={"framework"},dataProvider = "eventBooking", description= "Create Event- Book and verify if its booked")
    public void createandBookEvent(HashMap<String,String> data){

        //Step 1 - Create an Event


        LoginPage login = new LoginPage(page,base_url);

        DashboardPage dashboard = login.loginApplication();

        AdminPage adminPage = dashboard.eventsPage();

        adminPage.createEvents(data.get("titleCard"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("event datetime"),
                data.get("price"),
                data.get("seats"));
        //Step 2 Verify a booked event

       // assertThat(page.getByText("Event created!")).isVisible();//By default, the timeout for assertions is set to 5 seconds

        EventPage event = new EventPage(page);

        event.goTo();

        Locator targetEvent=event.findEventCard(data.get("titleCard"));

        int seatBefore=event.getSeatsCount(targetEvent);

        BookingFormPage bookingFormPage=event.bookEvent(targetEvent);

        bookingFormPage.fillAndConfirmBooking(data.get("fullName"),
                data.get("email"),
                data.get("phone"));

        bookingFormPage.verifyBooking();

        int seatAfterBooking=event.getSeatsCountAfterBooking(data.get("titleCard"));

        //

        // Verify exactly 3 seats were deducted
        Assert.assertEquals(
                seatAfterBooking,
                seatBefore - 3,
                "Seat availability did not decrease by exactly 3"
        );

        System.out.println(
                "Seat Availability verified successfully"
        );
    }



}
