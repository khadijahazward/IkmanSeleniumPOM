package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver = null;

    @FindBy(xpath = "(//button[@id='dd-button'])[1]")
    public WebElement sortResultsByButton;

    @FindBy(xpath = "//ul[@role='listbox']")
    public WebElement sortResultsByListBox;

    @FindBy(xpath = "//div[text() = 'Type of ad']/ancestor::Button")
    public WebElement adTypeDropdown;

    @FindBy(xpath = "//input[@value='for_sale']")
    public WebElement adTypeForSale;

    @FindBy(xpath = "//input[@value='to_buy']")
    public WebElement adTypeWanted;

    @FindBy(xpath = "//div[text() = 'Price (Rs)']/ancestor::Button")
    public WebElement priceDropDown;

    @FindBy(xpath="(//input[@aria-label='Max'])[1]")
    public WebElement maxPrice;

    @FindBy(xpath="(//input[@aria-label='Min'])[1]")
    public WebElement minPrice;

    @FindBy(xpath = "(//button[text() = 'Apply'])[1]")
    public WebElement applyRangeButtonForPrice;

    public BasePage (WebDriver driver) {
        this.driver = driver;
    }

    // Loads the specified URL and returns the Ikman.lk HomePage object
    public IkmanHomePage loadURL(String url) {
        driver.get(url);
        return PageFactory.initElements(driver, IkmanHomePage.class);
    }

    // Scrolls the page by specified x and y values
    public void scrollPage(int x, int y) {
        new Actions(driver).scrollByAmount(x,y).perform();
    }

    // Selects the sorting option based on provided option ID
    public void selectSortOption(String optionId) {
        sortResultsByButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(sortResultsByListBox));
        WebElement optionToSelect = driver.findElement(By.xpath("//li[@id='" + optionId + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionToSelect);
    }

    // Clicks on the link of a specified category
    public BasePage navigateToCategory(String categoryText) {
        WebElement categoryLink = driver.findElement(By.xpath("//span[text()='" + categoryText + "']/ancestor::a"));
        categoryLink.click();
        return this;
    }

    // Sets the price range by entering minimum and maximum values
    public void setPriceRange(Double min, Double max) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(priceDropDown)).click();
        if (min != null){
            wait.until(ExpectedConditions.visibilityOf(minPrice));
            minPrice.clear();
            minPrice.sendKeys(String.valueOf(min));
        }

        if (max != null) {
            wait.until(ExpectedConditions.visibilityOf(maxPrice));
            maxPrice.clear();
            maxPrice.sendKeys(String.valueOf(max));
        }
        applyRangeButtonForPrice.click();
    }

    // Selects the ad type based on the provided option
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
}
