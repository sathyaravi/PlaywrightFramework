import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

public class RegisterFormUsingLocators {

    @Test
    public void registerTest(){

        Playwright playwright = Playwright.create();

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        Page page = browser.newPage();

        page.navigate("https://eventhub.rahulshettyacademy.com/register");

        page.getByTestId("register-email").fill("sathya.ravichandran@aol.com");

        page.getByTestId("register-password").fill("Testing@123");

        page.getByPlaceholder("Repeat your password").fill("Testing@123");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create Account")).click();


    }

}
