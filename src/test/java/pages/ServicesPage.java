package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ServicesPage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Services']/ancestor::Button")
    public WebElement PageTitle;

    public ServicesPage(WebDriver driver) {
        super(driver);
    }
}
