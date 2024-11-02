package pages.propertiescategories;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PropertiesPage;

import java.time.Duration;
import java.util.List;

public class HouseForSale extends PropertiesPage {
    public HouseForSale(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[text() = 'House size']/ancestor::Button")
    public WebElement houseSizeDropDown;

    @FindBy(xpath="(//input[@aria-label='Max'])[2]")
    public WebElement maxHouseSize;

    @FindBy(xpath="(//input[@aria-label='Min'])[2]")
    public WebElement minHouseSize;

    @FindBy(xpath = "(//button[text() = 'Apply'])[2]")
    public WebElement applyRangeButtonForHouseSize;

    @FindBy(xpath = "//div[text() = 'Bedrooms']/ancestor::Button")
    public WebElement bedroomsDropDown;

    @FindBy(xpath = "//div[contains(@class, 'collapsible-section--WUsrw')]" +
            "//div[contains(text(), 'Bedrooms')]/ancestor::button/following-sibling::div" +
            "//button[span[contains(text(), 'Show more')]]/span ")
    public WebElement bedroomShowMoreBtn;

    @FindBy(xpath = "//div[contains(@class, 'collapsible-section--WUsrw')]" +
            "//div[contains(text(), 'Bathrooms')]/ancestor::button/following-sibling::div" +
            "//button[span[contains(text(), 'Show more')]]/span ")
    public WebElement bathroomShowMoreBtn;

    @FindBy(xpath = "//div[text() = 'Bathrooms']/ancestor::Button")
    public WebElement bathroomsDropDown;

    public void setHouseSizeRange(Double min, Double max) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(houseSizeDropDown)).click();
        if (min != null){
            wait.until(ExpectedConditions.visibilityOf(minHouseSize));
            minHouseSize.clear();
            minHouseSize.sendKeys(String.valueOf(min));
        }

        if (max != null) {
            wait.until(ExpectedConditions.visibilityOf(maxHouseSize));
            maxHouseSize.clear();
            maxHouseSize.sendKeys(String.valueOf(max));
        }
        applyRangeButtonForHouseSize.click();
    }

    public void selectNumberOfBedrooms(List<String> bedroomCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Opens the dropdown and clicks the show more button
        wait.until(ExpectedConditions.elementToBeClickable(bedroomsDropDown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(bedroomShowMoreBtn)).click();

        // loops through each bedroom count and selects them
        for (String count : bedroomCount) {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@id='bedrooms-" + count + "']")));
            wait.until(ExpectedConditions.elementToBeClickable(checkbox));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }

    public void selectNumberofBathrooms(List<String> bathroomCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Opens the dropdown and clicks the show more button
        wait.until(ExpectedConditions.elementToBeClickable(bathroomsDropDown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(bathroomShowMoreBtn)).click();

        // loops through each bathroom count and selects them
        for (String count : bathroomCount) {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@id='bathrooms-" + count + "']")));
            wait.until(ExpectedConditions.elementToBeClickable(checkbox));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }
}
