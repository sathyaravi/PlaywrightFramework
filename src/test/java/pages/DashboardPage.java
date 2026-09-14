package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage {

    Page page;

    public DashboardPage(Page page){
        this.page=page;
    }

    public AdminPage eventsPage(){


        assertThat(page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        AdminPage adminPage = new AdminPage(page);

        return adminPage;
    }

    public MyBookingsPage myBookingsPage(){

        assertThat(page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.getByTestId("nav-bookings").click();

        return new MyBookingsPage(page);
    }
}
