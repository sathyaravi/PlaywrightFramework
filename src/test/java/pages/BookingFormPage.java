package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.lang.Integer.parseInt;

public class BookingFormPage {

        Page page;

        private static final String TICKET_COUNT_ID="#ticket-count";
        private static final String FULL_NAME_LABEL="Full Name";
        private static final String EMAIL_ID="customer-email";
        private static final String PHONE_PLACEHOLDER="+91 98765 43210";

        public BookingFormPage(Page page){
            this.page=page;
        }

        public void fillAndConfirmBooking(String fullName,String email,String phone){

            //i want to add 3 tickets

            for(int i=1;i<=2;i++){

                page.locator("#ticket-count")
                        .locator("..")
                        .getByRole(AriaRole.BUTTON)
                        .nth(1)
                        .click();
            }

            //verify ticket count
            assertThat(page.locator(TICKET_COUNT_ID) ).hasText("3");

            //Enter customer details

            page.getByLabel(FULL_NAME_LABEL).fill(fullName);

            page.getByTestId(EMAIL_ID).fill(email);

            page.getByPlaceholder(PHONE_PLACEHOLDER).fill(phone);

            //confirm booking
            page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Confirm Booking")).click();

            assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        }

        public void verifyBooking(){
            //capture booking reference

            String bookingRef=page.locator(".booking-ref").innerText();

            System.out.println(bookingRef);

            //Go to My Bookings

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();


            //verify in booking history

            Locator bookingCards=page.locator("#booking-card");

            Locator targetCard=bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));


            assertThat(targetCard).isVisible();


        }

}
