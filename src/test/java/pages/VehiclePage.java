package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class VehiclePage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Vehicles']/ancestor::Button")
    public WebElement PageTitle;

    public VehiclePage(WebDriver driver) {
        super(driver);
    }
}
