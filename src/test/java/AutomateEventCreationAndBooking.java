import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.lang.Integer.parseInt;
import static java.util.Collections.replaceAll;

public class AutomateEventCreationAndBooking {


    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    @BeforeMethod
    public void setUp(){

        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        context=browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page=context.newPage();

        page.navigate("https://eventhub.rahulshettyacademy.com/login");

        PlaywrightAssertions.setDefaultAssertionTimeout(7000);//Global time out

    }

    @Test(description= "Create Event- Book and verify if its booked")
    public void createandBookEvent(){

        //Step 1 - Create an Event
        System.out.println(page.title());

        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        page.getByPlaceholder("you@email.com").fill("sathya.ravichandran@aol.com");

        page.getByLabel("Password").fill("Testing@123");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        page.getByLabel("Title").fill("QA Summit", new Locator.FillOptions().setTimeout(8000));

       // page.locator("textarea[placeholder*='Describe the event…']").fill("QA automation testing");

        page.locator("#admin-event-form textarea").fill("QA Automation Testing");//parent to child traversal- css

        page.getByLabel("Category").selectOption("Conference");

        page.getByLabel("City").fill("Chennai");

        page.getByLabel("Venue").fill("AB Convention Centre");

        page.getByLabel("Event Date & Time").fill("2026-10-29T17:35");

        page.getByLabel("Price").fill("230");

        page.getByLabel("Total Seats").fill("50");

        page.locator("#add-event-btn").click();



        //Step 2 Verify a booked event

        assertThat(page.getByText("Event created!")).isVisible();//By default, the timeout for assertions is set to 5 seconds

        page.locator("#nav-events").click();

        Locator eventCards=page.getByTestId("event-card");

        Locator targetEvent=eventCards.filter(new Locator.FilterOptions().setHasText("QA Summit"));

        assertThat(targetEvent).isVisible();

        //capture seats before Booking

        String seatAvailability=targetEvent.getByText("seats").innerText();

        System.out.println("Before Booking:"+seatAvailability);

        int seatBefore =
                parseInt(seatAvailability.replaceAll("[^0-9]", ""));

        //click book- now button

        targetEvent.getByTestId("book-now-btn").click();


        //i want to add 3 tickets

        for(int i=1;i<=2;i++){

            page.locator("#ticket-count")
                    .locator("..")
                    .getByRole(AriaRole.BUTTON)
                    .nth(1)
                    .click();
        }

        //verify ticket count
        assertThat(page.locator("#ticket-count") ).hasText("3");

        //Enter customer details

        page.getByLabel("Full Name").fill("John Doe");

        page.getByTestId("customer-email").fill("John.doe@mail.com");

        page.getByPlaceholder("+91 98765 43210").fill("+91 98765 43210");

        //confirm booking
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Confirm Booking")).click();

        assertThat(page.getByText("Your tickets are reserved.")).isVisible();

        //capture booking reference

        String bookingRef=page.locator(".booking-ref").innerText();

        System.out.println(bookingRef);

        //Go to My Bookings

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();


        //verify in booking history

        Locator bookingCards=page.locator("#booking-card");

        Locator targetCard=bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));


        assertThat(targetCard).isVisible();

        //seat count reduction after booking
        page.locator("#nav-events").click();
        page.reload();

        Locator eventCardsAfterBooking=page.getByTestId("event-card");

        Locator targetEventAfterBooking=eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("QA Summit"));

        String seatAvailabilityAfterBooking=targetEventAfterBooking.getByText("seats").innerText();

        System.out.println("After Booking:"+seatAvailabilityAfterBooking);
        int seatAfter =
                parseInt(
                        seatAvailabilityAfterBooking.replaceAll("[^0-9]", "")
                );

        // Verify exactly 3 seats were deducted
        Assert.assertEquals(
                seatAfter,
                seatBefore - 3,
                "Seat availability did not decrease by exactly 3"
        );

        System.out.println(
                "Seat Availability verified successfully"
        );

    }

    @AfterMethod
    public void tearDown(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace1.zip")));

        browser.close();

        playwright.close();


    }

}
