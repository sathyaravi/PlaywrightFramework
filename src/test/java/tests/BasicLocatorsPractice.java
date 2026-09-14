package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

public class BasicLocatorsPractice {

    Playwright playwright;

    Browser browser;

    Page page;

    @Test
    void BaseLocators(){

        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        page = browser.newPage();

        page.navigate("https://wishinfinite.com/playground#");

        //page.getByTestId("Forms").click();

        page.getByPlaceholder("Enter Text").fill("This is John Doe");

        page.getByLabel("Textarea").fill("Hello!! This is John Doe");

        page.locator("#password-input").fill("Test@123");

        page.getByLabel("Email Input").fill("John.doe@mail.com");

        page.onDialog(dialog -> {

            System.out.println("========== POPUP ==========");

            System.out.println("Type: " + dialog.type());

            System.out.println("Message:");
            System.out.println(dialog.message());

            System.out.println("===========================");

            // Click OK on the popup
            dialog.accept();
        });


        // This action causes the popup to appear
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Submit Form")
        ).click();



    }
}
