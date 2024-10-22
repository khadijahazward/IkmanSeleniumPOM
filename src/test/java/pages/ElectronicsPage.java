package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ElectronicsPage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Electronics']/ancestor::Button")
    public WebElement PageTitle;

    public ElectronicsPage(WebDriver driver) {
        super(driver);
    }
}
