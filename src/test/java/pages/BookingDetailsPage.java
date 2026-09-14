package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingDetailsPage {

    Page page;

    public BookingDetailsPage(Page page) {
        this.page = page;
    }

    public void verifyPageDisplayed() {
        assertThat(page).hasURL(Pattern.compile(".*\\/bookings\\/\\d+$"));
        assertThat(page.getByText("Event Details")).isVisible();
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel Booking"))).isVisible();
    }

    public void cancelBooking() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel Booking")).first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes, cancel it")).click();
    }
}
