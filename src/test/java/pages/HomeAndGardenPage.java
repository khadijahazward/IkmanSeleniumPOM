package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.homeandgardencategories.Furniture;
import pages.homeandgardencategories.OtherHomeItems;
import pages.vehiclecategories.AutoPartsAndAccessories;
import pages.vehiclecategories.MotorBikes;

import java.time.Duration;

public class HomeAndGardenPage extends BasePage{
    @FindBy(xpath="//div[text() = 'Home & Garden']/ancestor::Button")
    public WebElement PageTitle;

    @FindBy(xpath = "//div[text() = 'Condition']/ancestor::Button")
    public WebElement conditionDropDown;

    @FindBy(xpath = "//input[@id = 'condition-new']")
    public WebElement conditionForNewItem;

    @FindBy(xpath = "//input[@id = 'condition-used']")
    public WebElement conditionForUsedItem;

    public HomeAndGardenPage(WebDriver driver) {
        super(driver);
    }

    public void selectConditionForItem(String condition) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(conditionDropDown)).click();

        wait.until(ExpectedConditions.visibilityOf(conditionForNewItem));

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement targetElement;
        switch (condition.toLowerCase()) {
            case "used":
                targetElement = conditionForUsedItem;
                break;
            case "new":
                targetElement = conditionForNewItem;
                break;
            default:
                throw new IllegalArgumentException("Invalid condition type: " + condition);
        }
        jsExecutor.executeScript("arguments[0].click();", targetElement);
    }

    // Returns the page object based on the category selected
    @Override
    public BasePage navigateToCategory(String categoryText) {
        super.navigateToCategory(categoryText);
        switch (categoryText.toLowerCase()) {
            case "furniture":
                return PageFactory.initElements(driver, Furniture.class);
            case "other home items":
                return PageFactory.initElements(driver, OtherHomeItems.class);
            default:
                throw new IllegalArgumentException("Unknown category: " + categoryText);
        }
    }
}
