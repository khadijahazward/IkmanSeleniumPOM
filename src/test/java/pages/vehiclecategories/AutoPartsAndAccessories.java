package pages.vehiclecategories;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.VehiclePage;

import java.time.Duration;
import java.util.List;

public class AutoPartsAndAccessories extends VehiclePage {
    @FindBy(xpath = "//div[contains(@class, 'collapsible-section--WUsrw')]" +
            "//div[contains(text(), 'Part or Accessory Type')]/ancestor::button/following-sibling::div" +
            "//button[span[contains(text(), 'Show more')]]/span")
    public WebElement partOrAccessoryTypeShowMoreBtn;

    @FindBy(xpath = "//div[text() = 'Part or Accessory Type']/ancestor::Button")
    public WebElement partOrAccessoryTypeDropDown;

    public AutoPartsAndAccessories(WebDriver driver) {
        super(driver);
    }

    // selects types of accessories from the dropdown
    public void selectTypesOfAccessories(List<String> accessoryTypes) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Opens the dropdown and clicks the show more button
        wait.until(ExpectedConditions.elementToBeClickable(partOrAccessoryTypeDropDown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(partOrAccessoryTypeShowMoreBtn)).click();

        // loops through each accessory type and selects them
        for (String accessoryType : accessoryTypes) {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), '" + accessoryType + "')]")));
            checkbox.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
