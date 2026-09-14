package tests;

import org.testng.annotations.Test;
import pages.BookingDetailsPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.MyBookingsPage;

public class BookingCancellationTest extends BaseTest {

    @Test(groups = "regression", description = "Verify booking cancellation flow")
    public void cancelBookingAndVerifySuccess() {

        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginApplication();

        MyBookingsPage myBookingsPage = dashboardPage.myBookingsPage();
        myBookingsPage.verifyPageLoaded();

        BookingDetailsPage bookingDetailsPage = myBookingsPage.openFirstBooking();
        bookingDetailsPage.verifyPageDisplayed();
        bookingDetailsPage.cancelBooking();

        myBookingsPage.verifyCancellationSuccess();
    }
}
