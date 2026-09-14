package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class BaseTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    String base_url;
    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {

        Properties prop = new Properties();

        FileInputStream fis =new FileInputStream("src/test/resources/config.properties");

        prop.load(fis);
        playwright = Playwright.create();
        //mvn test -PSmoke -Dbrowser=chrome
        String browserName=System.getProperty("browser")!=null?System.getProperty("browser"):prop.getProperty("browser");
        String envName=System.getProperty("env")!=null?System.getProperty("env"):prop.getProperty("env");

        if("firefox".equals(browserName)){

            browser=playwright.firefox().launch();
        }
        else if("safari".equals(browserName)){
            browser=playwright.webkit().launch();
        }
        else{
            //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser = playwright.chromium().launch();

        }


        context=browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page=context.newPage();

        base_url=prop.getProperty(envName+".base_url");

        page.navigate(base_url);

        PlaywrightAssertions.setDefaultAssertionTimeout(7000);//Global time out

    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(){

        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace1.zip")));

        browser.close();

        playwright.close();


    }
}
