package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingsPage {

    Page page;

    public MyBookingsPage(Page page) {
        this.page = page;
    }

    public void verifyPageLoaded() {
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
        assertThat(page).hasURL(Pattern.compile(".*\\/bookings$"));
    }

    public BookingDetailsPage openFirstBooking() {
        Locator viewDetails = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View Details")).first();
        assertThat(viewDetails).isVisible();
        viewDetails.click();

        return new BookingDetailsPage(page);
    }

    public Locator getNoBookingsMessage() {
        return page.getByText("No bookings yet");
    }

    public void verifyCancellationSuccess() {
        assertThat(page.getByText("Booking cancelled successfully")).isVisible();
        assertThat(page.getByText("No bookings yet")).isVisible();
    }
}
