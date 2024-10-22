package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PropertiesPage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Property']/ancestor::Button")
    public WebElement PageTitle;

    public PropertiesPage(WebDriver driver) {
        super(driver);
    }

}
