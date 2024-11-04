package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.vehiclecategories.AutoPartsAndAccessories;
import pages.vehiclecategories.MotorBikes;

import java.time.Duration;

public class VehiclePage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Vehicles']/ancestor::Button")
    public WebElement PageTitle;

    @FindBy(xpath = "//div[text() = 'Condition']/ancestor::Button")
    public WebElement conditionDropDown;

    @FindBy(xpath = "//input[@id = 'condition-new']")
    public WebElement conditionForNewVehicle;

    @FindBy(xpath = "//input[@id = 'condition-used']")
    public WebElement conditionForUsedVehicle;

    @FindBy(xpath = "//input[@id = 'condition-reconditioned']")
    public WebElement conditionForReconditionedVehicle;

    @FindBy(xpath = "//div[text() = 'Brand']/ancestor::button")
    public WebElement brandDropDown;

    @FindBy(xpath = "//input[@placeholder='Select model']")
    public WebElement modelSearchField;

    @FindBy(xpath = "(//button[text() = 'Apply'])[2]")
    public WebElement applyButtonForBrand;

    @FindBy(xpath = "//input[@placeholder='Select brand']")
    public WebElement brandSearchField;

    public VehiclePage(WebDriver driver) {
        super(driver);
    }

    // Selects the condition for the vehicle
    public void selectConditionForVehicle(String condition) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(conditionDropDown)).click();

        wait.until(ExpectedConditions.visibilityOf(conditionForNewVehicle));

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement targetElement;
        switch (condition.toLowerCase()) {
            case "used":
                targetElement = conditionForUsedVehicle;
                break;
            case "new":
                targetElement = conditionForNewVehicle;
                break;
            case "reconditioned":
                targetElement = conditionForReconditionedVehicle;
                break;
            default:
                throw new IllegalArgumentException("Invalid condition type: " + condition);
        }
        jsExecutor.executeScript("arguments[0].click();", targetElement);
    }

    // Selects the brand and model for the vehicle.
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

        // If model name is given.
        if (modelName != null && !modelName.isEmpty()) {
            // Opens the model dropdown and types the model name.
            wait.until(ExpectedConditions.visibilityOf(modelSearchField));
            modelSearchField.clear();
            modelSearchField.sendKeys(modelName);

            // Waits for the dropdown results to appear and selects the model.
            String modelXpath = String.format("//li[contains(text(), '%s')]", modelName);
            WebElement modelOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(modelXpath)));
            modelOption.click();
        }

        applyButtonForBrand.click();
    }

    // Returns the page object based on the category selected
    @Override
    public BasePage navigateToCategory(String categoryText) {
        super.navigateToCategory(categoryText);
        switch (categoryText.toLowerCase()) {
            case "auto parts & accessories":
                return PageFactory.initElements(driver, AutoPartsAndAccessories.class);
            case "motorbikes":
                return PageFactory.initElements(driver, MotorBikes.class);
            default:
                throw new IllegalArgumentException("Unknown category: " + categoryText);
        }
    }
}
