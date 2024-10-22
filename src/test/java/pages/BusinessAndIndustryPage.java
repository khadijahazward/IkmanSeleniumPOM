package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BusinessAndIndustryPage extends BasePage {
    @FindBy(xpath = "//div[text() = 'Business & Industry']/ancestor::Button")
    public WebElement PageTitle;

    public BusinessAndIndustryPage(WebDriver driver) {
        super(driver);
    }
}
