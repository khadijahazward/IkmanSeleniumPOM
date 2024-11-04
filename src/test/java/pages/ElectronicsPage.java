package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.electroniccategories.ComputerAccessories;
import pages.electroniccategories.MobilePhones;

import java.time.Duration;

public class ElectronicsPage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Electronics']/ancestor::Button")
    public WebElement PageTitle;

    @FindBy(xpath = "//div[text() = 'Condition']/ancestor::Button")
    public WebElement conditionDropDown;

    @FindBy(xpath = "//input[@id = 'condition-new']")
    public WebElement conditionForNewPhone;

    @FindBy(xpath = "//input[@id = 'condition-used']")
    public WebElement conditionForUsedPhone;

    public ElectronicsPage(WebDriver driver) {
        super(driver);
    }

    // Navigates to the specified category on the Electronics page.
    @Override
    public BasePage navigateToCategory(String categoryText) {
        super.navigateToCategory(categoryText);
        switch (categoryText.toLowerCase()) {
            case "computer accessories":
                return PageFactory.initElements(driver, ComputerAccessories.class);
            case "mobile phones":
                return PageFactory.initElements(driver, MobilePhones.class);
            default:
                throw new IllegalArgumentException("Unknown category: " + categoryText);
        }
    }

    // Selects the condition for an item on the Electronics page
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
}
