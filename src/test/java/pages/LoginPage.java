package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {

    Page page;
    String base_url;
    private static  final String emailPlaceholder="you@email.com";
    private static final String passwordLabel="Password";

    public LoginPage(Page page,String baseUrl){

        this.page=page;
        this.base_url=baseUrl;

    }

    public DashboardPage loginApplication(){

        System.out.println(page.title());

        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        page.getByPlaceholder(emailPlaceholder).fill("sathya.ravichandran@aol.com");

        page.getByLabel(passwordLabel).fill("Testing@123");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        DashboardPage dashboard = new DashboardPage(page);

        return dashboard;
    }
}
