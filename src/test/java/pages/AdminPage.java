package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminPage {

        Page page;

        private static final String event_title = "Title";
        private static final String event_Description="#admin-event-form textarea";
        private static final String event_category="Category";
        private static final String event_city="City";
        private static final String event_venue="Venue";
        private static final String event_DateTime="Event Date & Time";
        private static final String event_price="Price";
        private static final String event_totalSeats="Total Seats";




    public AdminPage(Page page) {

        this.page=page;
    }

    public void createEvents(String title,String description,String category,String city,String venue,String DateTime,String price,String totalSeats ){


            page.getByLabel(event_title).fill(title, new Locator.FillOptions().setTimeout(8000));

            // page.locator("textarea[placeholder*='Describe the event…']").fill("QA automation testing");

            page.locator(event_Description).fill(description);//parent to child traversal- css

            page.getByLabel(event_category).selectOption(category);

            page.getByLabel(event_city).fill(city);

            page.getByLabel(event_venue).fill(venue);

            page.getByLabel(event_DateTime).fill(DateTime);

            page.getByLabel(event_price).fill(price);

            page.getByLabel(event_totalSeats).fill(totalSeats);

            page.locator("#add-event-btn").click();

            assertThat(page.getByText("Event created!")).isVisible();//By default, the timeout for assertions is set to 5 seconds


    }
}
