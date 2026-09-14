package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.lang.Integer.parseInt;

public class EventPage {
    Page page;

    public EventPage(Page page){
        this.page = page;
    }

    public void goTo(){

        page.locator("#nav-events").click();

    }

    public Locator waitForEventsToLoad(){

        Locator eventCards=page.getByTestId("event-card");

        assertThat(eventCards.first()).isVisible();

        return eventCards;
    }

    public Locator findEventCard(String titleCard){

        Locator eventCards=waitForEventsToLoad();

        Locator targetEvent= eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));

        assertThat(targetEvent).isVisible();

        return targetEvent;
    }

    public int getSeatsCount(Locator targetEvent){
       // assertThat(targetEvent).isVisible();

        //capture seats before Booking


        String seatAvailability=targetEvent.getByText("seats").innerText();

        System.out.println("Before Booking:"+seatAvailability);

        return parseInt(seatAvailability.replaceAll("[^0-9]", ""));

        //click book- now button


    }

    public BookingFormPage bookEvent(Locator targetEvent){

        targetEvent.getByTestId("book-now-btn").click();

        return new BookingFormPage(page);

    }

    public int getSeatsCountAfterBooking(String titleCard){
        //seat count reduction after booking
        page.locator("#nav-events").click();

        page.reload();

        Locator eventCardsAfterBooking=page.getByTestId("event-card");

        Locator targetEventAfterBooking=eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(titleCard));

        String seatAvailabilityAfterBooking=targetEventAfterBooking.getByText("seats").innerText();

        System.out.println("After Booking:"+seatAvailabilityAfterBooking);

        return parseInt(seatAvailabilityAfterBooking.replaceAll("[^0-9]", ""));





    }
}
