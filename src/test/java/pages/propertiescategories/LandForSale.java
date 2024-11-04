package pages.propertiescategories;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PropertiesPage;

import java.time.Duration;

public class LandForSale extends PropertiesPage {
    @FindBy(xpath = "//div[text() = 'Land size']/ancestor::Button")
    public WebElement landSizeDropDown;

    @FindBy(xpath="(//input[@aria-label='Max'])[2]")
    public WebElement maxLandSize;

    @FindBy(xpath="(//input[@aria-label='Min'])[2]")
    public WebElement minLandSize;

    @FindBy(xpath = "(//button[text() = 'Apply'])[2]")
    public WebElement applyRangeButtonForLandSize;

    public LandForSale(WebDriver driver) {
        super(driver);
    }

    // selects the land size range from the dropdown
    public void setLandSizeRange(Double min, Double max) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(landSizeDropDown)).click();
        if (min != null){
            wait.until(ExpectedConditions.visibilityOf(minLandSize));
            minLandSize.clear();
            minLandSize.sendKeys(String.valueOf(min));
        }

        if (max != null) {
            wait.until(ExpectedConditions.visibilityOf(maxLandSize));
            maxLandSize.clear();
            maxLandSize.sendKeys(String.valueOf(max));
        }
        applyRangeButtonForLandSize.click();
    }


}
