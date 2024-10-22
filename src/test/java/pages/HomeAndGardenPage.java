package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomeAndGardenPage extends BasePage{
    @FindBy(xpath="//div[text() = 'Home & Garden']/ancestor::Button")
    public WebElement PageTitle;

    public HomeAndGardenPage(WebDriver driver) {
        super(driver);
    }
}
