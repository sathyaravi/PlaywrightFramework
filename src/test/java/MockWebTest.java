import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        page = browser.newPage();

        page.navigate("https://eventhub.rahulshettyacademy.com/login");

    }

    @Test(description = "Mocking the total number of events as 6 in the banner")
    public void loginEvent() {

        //Step 1 - Create an Event
        System.out.println(page.title());

        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        page.getByPlaceholder("you@email.com").fill("sathya.ravichandran@aol.com");

        page.getByLabel("Password").fill("Testing@123");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_mock.json"))
        ));


        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        page.waitForTimeout(8000);

        Locator eventCards=page.getByTestId("event-card");

        assertThat(eventCards.first()).isVisible();

        Assert.assertEquals(eventCards.count(),6);

        assertThat(page.locator(".mx-1").first()).isVisible();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4_mock.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards1=page.getByTestId("event-card");

        assertThat(eventCards1.first()).isVisible();

        Assert.assertEquals(eventCards1.count(),4);

        assertThat(page.locator(".mx-1").first()).isHidden();



    }

    @Test
    public void routeResumeTest(){

        page.getByPlaceholder("you@email.com").fill("sathya.ravichandran@aol.com");

        page.getByLabel("Password").fill("Testing@123");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        //navigate to bookings
        page.getByTestId("nav-bookings").click();

        page.route("**/api/bookings**", route -> route.resume
                (new Route.ResumeOptions()
                        .setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/142373")));

        page.waitForTimeout(7000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();

        assertThat(page.getByText("Booking Not found")).isVisible();
    }


}
