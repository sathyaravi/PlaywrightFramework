package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.lang.Integer.parseInt;

public class FrameworkBuildTest extends BaseTest{



    @Test(groups = "smoke", description= "Create Event- Book and verify if its booked")
    public void createandBookEvent(){

        //Step 1 - Create an Event

        String titleCard="QA Summit12";

        LoginPage login = new LoginPage(page,base_url);

        DashboardPage dashboard = login.loginApplication();

        AdminPage adminPage = dashboard.eventsPage();

        adminPage.createEvents(titleCard,"QA Automation Testing 101","Conference","Chennai","Deccan Hall","2026-10-29T17:35","150","100" );

        //Step 2 Verify a booked event

       // assertThat(page.getByText("Event created!")).isVisible();//By default, the timeout for assertions is set to 5 seconds

        EventPage event = new EventPage(page);

        event.goTo();

        Locator targetEvent=event.findEventCard(titleCard);

        int seatBefore=event.getSeatsCount(targetEvent);

        BookingFormPage bookingFormPage=event.bookEvent(targetEvent);

        bookingFormPage.fillAndConfirmBooking("John Doe","john.doe@mail.com","9876543210");

        bookingFormPage.verifyBooking();

        page.waitForTimeout(2000);

        int seatAfterBooking=event.getSeatsCountAfterBooking(titleCard,seatBefore);

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
