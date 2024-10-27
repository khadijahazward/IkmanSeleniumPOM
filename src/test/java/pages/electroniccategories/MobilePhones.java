package pages.electroniccategories;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import pages.ElectronicsPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MobilePhones extends ElectronicsPage {
    @FindBy(xpath = "//div[text() = 'Type of ad']/ancestor::Button")
    public WebElement adTypeDropdown;

    @FindBy(xpath = "//input[@value='for_sale']")
    public WebElement adTypeForSale;

    @FindBy(xpath = "//input[@value='to_buy']")
    public WebElement adTypeWanted;

    @FindBy(xpath = "//div[text() = 'Condition']/ancestor::Button")
    public WebElement conditionDropDown;

    @FindBy(xpath = "//input[@id = 'condition-new']")
    public WebElement conditionForNewPhone;

    @FindBy(xpath = "//input[@id = 'condition-used']")
    public WebElement conditionForUsedPhone;

    @FindBy(xpath = "//div[text() = 'Price (Rs)']/ancestor::Button")
    public WebElement priceDropDown;

    @FindBy(xpath="//input[@aria-label='Max']")
    public WebElement maxPrice;

    @FindBy(xpath="//input[@aria-label='Min']")
    public WebElement minPrice;

    @FindBy(xpath = "(//button[text() = 'Apply'])[1]")
    public WebElement applyRangeButtonForPrice;

    @FindBy(xpath = "//div[text() = 'Brand']/ancestor::button")
    public WebElement brandDropDown;

    @FindBy(xpath = "//input[@placeholder='Select model']")
    public WebElement modelSearchField;

    @FindBy(xpath = "(//button[text() = 'Apply'])[2]")
    public WebElement applyButtonForBrand;

    @FindBy(xpath = "//input[@placeholder='Add another brand']")
    public WebElement brandSearchField;

    public MobilePhones(WebDriver driver) {
        super(driver);
    }

    public void selectAdType(String adType) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(adTypeDropdown)).click();

        wait.until(ExpectedConditions.visibilityOf(adTypeForSale));

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement targetElement;
        switch (adType.toLowerCase()) {
            case "for sale":
                targetElement = adTypeForSale;
                break;
            case "wanted":
                targetElement = adTypeWanted;
                break;
            default:
                throw new IllegalArgumentException("Invalid ad type: " + adType);
        }
        jsExecutor.executeScript("arguments[0].click();", targetElement);
    }

    public void selectCondition(String condition) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(conditionDropDown)).click();

        wait.until(ExpectedConditions.visibilityOf(conditionForNewPhone));

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement targetElement;
        switch (condition.toLowerCase()) {
            case "used":
                targetElement = conditionForUsedPhone;
                break;
            case "new":
                targetElement = conditionForNewPhone;
                break;
            default:
                throw new IllegalArgumentException("Invalid condition type: " + condition);
        }
        jsExecutor.executeScript("arguments[0].click();", targetElement);
    }

    public void setPriceRange(int min, int max) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(priceDropDown)).click();

        wait.until(ExpectedConditions.visibilityOf(minPrice));
        minPrice.clear();
        minPrice.sendKeys(String.valueOf(min));

        wait.until(ExpectedConditions.visibilityOf(maxPrice));
        maxPrice.clear();
        maxPrice.sendKeys(String.valueOf(max));

        applyRangeButtonForPrice.click();
    }

    public void selectBrandAndModel(String brandName, String modelName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(brandDropDown)).click();

        // Opens the brand dropdown and type the brand name.
        wait.until(ExpectedConditions.visibilityOf(brandSearchField));
        brandSearchField.clear();
        brandSearchField.sendKeys(brandName);

        // Waits for the dropdown menu to show the filtered result based on the text entered
        // the search field, and selects the brand.
        String brandXpath = String.format("//li[contains(text(), '%s')]", brandName);
        WebElement brandOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(brandXpath)));
        brandOption.click();

        // Open the model dropdown and type the model name
        wait.until(ExpectedConditions.visibilityOf(modelSearchField));
        modelSearchField.clear();
        modelSearchField.sendKeys(modelName);

        // Waits for the dropdown results to appear and selects the model.
        String modelXpath = String.format("//li[contains(text(), '%s')]", modelName);
        WebElement modelOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(modelXpath)));
        modelOption.click();

        applyButtonForBrand.click();
    }

}
