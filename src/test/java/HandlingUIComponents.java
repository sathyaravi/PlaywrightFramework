import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HandlingUIComponents {

    Playwright playwright;

    Browser browser;

    Page page;

    BrowserContext context;

    @BeforeMethod
    public void setUp() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        context=browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page=context.newPage();

        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");


    }
    @Test
    public void handleHiddenDialog(){

        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();

        page.locator("#hide-textbox").click();

        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();

        page.onDialog(dialog ->{
        System.out.println(dialog.message());

        System.out.println(dialog.type());
        dialog.accept();
        }
        );

        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Alert")).click();

        //Mouse Hover

        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Mouse Hover")).hover();

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();

        //page.waitForTimeout(2000);

    }

    @Test
    public void handleFrames(){

        FrameLocator frame = page.frameLocator("#courses-iframe");

        frame.getByRole(AriaRole.LINK,new FrameLocator.GetByRoleOptions().setName("Learning Paths")).click();

        String text=frame.locator(".inner-box h1").textContent();

        System.out.println(text);

    }

    @Test
    public void takeScreenshots(){

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pageScreenshot1.png")));

        Locator showDialogBox = page.getByPlaceholder("Hide/Show Example");

        showDialogBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("Locatorscreenshot1.png")));

        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pageafterhideScreenshot1.png")));

    }

    @AfterMethod
    public void tearDown(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("UIComponents.zip")));

    }
}
