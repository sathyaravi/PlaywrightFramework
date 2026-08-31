import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class HandlingChildWindow {

    Playwright playwright;

    Browser browser;

    Page page;

    BrowserContext context;

    @BeforeMethod
    public void SetUp(){

        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        context=browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page=context.newPage();

        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");

    }


    @Test
    public  void childWindowHandle() {

        Locator blinkingTexts = page.locator(".blinkingText");

        Page newPage = context.waitForPage(() -> blinkingTexts.first().click());

        newPage.waitForLoadState();

        String text=newPage.locator(".red").textContent();

        String email=text.split(" ")[4];

        page.getByLabel("Username").fill(email);

        System.out.println(page.getByLabel("Username").inputValue());


    }

    @Test
    public void handlingUI(){

        //Radio buttons,checkbox and select(combo)
        Locator usrRdBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));

        usrRdBtn.click();

        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Okay")).click();

        Assert.assertTrue(usrRdBtn.isChecked());


        //checkbox
        Locator checkTerms = page.getByRole(AriaRole.CHECKBOX,new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));

        checkTerms.check();//we can give .click() too

        Assert.assertTrue(checkTerms.isChecked());

        //Select option using combo

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");

    }

    @AfterMethod
    public void tearDown(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));

    }

}
