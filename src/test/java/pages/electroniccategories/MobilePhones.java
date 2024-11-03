package pages.electroniccategories;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.ElectronicsPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MobilePhones extends ElectronicsPage {

    @FindBy(xpath = "//div[text() = 'Brand']/ancestor::button")
    public WebElement brandDropDown;

    @FindBy(xpath = "//input[@placeholder='Select model']")
    public WebElement modelSearchField;

    @FindBy(xpath = "(//button[text() = 'Apply'])[2]")
    public WebElement applyButtonForBrand;

    @FindBy(xpath = "//input[@placeholder='Select brand']")
    public WebElement brandSearchField;

    public MobilePhones(WebDriver driver) {
        super(driver);
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

}
