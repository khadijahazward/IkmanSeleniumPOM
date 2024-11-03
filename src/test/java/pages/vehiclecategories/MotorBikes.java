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

public class MotorBikes extends VehiclePage {
    @FindBy(xpath = "//div[text() = 'Bike Type']/ancestor::Button")
    public WebElement bikeTypeDropDown;

    @FindBy(xpath = "//div[text() = 'Year of Manufacture']/ancestor::Button")
    public WebElement manufactureYearDropDown;

    @FindBy(xpath = "//div[text() = 'Mileage']/ancestor::Button")
    public WebElement mileageDropDown;

    @FindBy(xpath="(//input[@aria-label='Max'])[2]")
    public WebElement maxYearOfManufacture;

    @FindBy(xpath="(//input[@aria-label='Min'])[2]")
    public WebElement minYearOfManufacture;

    @FindBy(xpath = "(//button[text() = 'Apply'])[3]")
    public WebElement applyRangeButtonForYearOfManufacture;

    @FindBy(xpath="(//input[@aria-label='Max'])[3]")
    public WebElement maxMileage;

    @FindBy(xpath="(//input[@aria-label='Min'])[3]")
    public WebElement minMileage;

    @FindBy(xpath = "(//button[text() = 'Apply'])[4]")
    public WebElement applyRangeButtonForMileage;

    public void selectTypesOfBikes(List<String> bikeTypes) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Opens the dropdown
        wait.until(ExpectedConditions.elementToBeClickable(bikeTypeDropDown)).click();

        // loops through each bike type and selects them
        for (String bikeType : bikeTypes) {
            String path = "item_type-" + bikeType.toLowerCase().replace("-", "_");
            String xpathForBikeType = "//input[@id='" + path + "']";
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(xpathForBikeType)));
            wait.until(ExpectedConditions.elementToBeClickable(checkbox));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }

    public MotorBikes(WebDriver driver) {
        super(driver);
    }

    public void setYearOfManufactureRange(Double min, Double max) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(manufactureYearDropDown)).click();
        if (min != null){
            wait.until(ExpectedConditions.visibilityOf(minYearOfManufacture));
            minYearOfManufacture.clear();
            minYearOfManufacture.sendKeys(String.valueOf(min));
        }

        if (max != null) {
            wait.until(ExpectedConditions.visibilityOf(maxYearOfManufacture));
            maxYearOfManufacture.clear();
            maxYearOfManufacture.sendKeys(String.valueOf(max));
        }
        applyRangeButtonForYearOfManufacture.click();
    }

    public void setMileageRange(Double min, Double max) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(mileageDropDown)).click();
        if (min != null){
            wait.until(ExpectedConditions.visibilityOf(minMileage));
            minMileage.clear();
            minMileage.sendKeys(String.valueOf(min));
        }

        if (max != null) {
            wait.until(ExpectedConditions.visibilityOf(maxMileage));
            maxMileage.clear();
            maxMileage.sendKeys(String.valueOf(max));
        }
        applyRangeButtonForMileage.click();
    }

}
