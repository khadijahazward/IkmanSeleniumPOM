package pages.homeandgardencategories;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomeAndGardenPage;

import java.time.Duration;
import java.util.List;

public class Furniture extends HomeAndGardenPage {
    @FindBy(xpath = "//div[text() = 'Furniture type']/ancestor::button")
    public WebElement furnitureTypeDropDown;

    @FindBy(xpath = "//div[contains(@class, 'collapsible-section--WUsrw')]//div[contains(text(), 'Furniture type')]" +
            "/ancestor::button/following-sibling::div//button[span[contains(text(), 'Show more')]]/span")
    public WebElement showMoreButtonForFurniture;

    @FindBy(xpath = "//div[text() = 'Brand']/ancestor::button")
    public WebElement brandDropDown;

    @FindBy(xpath = "//div[contains(@class, 'collapsible-section--WUsrw')]//div[contains(text(), 'Brand')]" +
            "/ancestor::button/following-sibling::div//button[span[contains(text(), 'Show more')]]/span")
    public WebElement showMoreButtonForBrand;

    public Furniture(WebDriver driver) {
        super(driver);
    }

    // selects furniture from the dropdown
    public void selectTypesOfFurniture(List<String> furnitureTypes) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Opens the dropdown and clicks the show more button
        wait.until(ExpectedConditions.elementToBeClickable(furnitureTypeDropDown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(showMoreButtonForFurniture)).click();

        // loops through each furniture type and selects them
        for (String type : furnitureTypes) {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), '" + type + "')]")));
            checkbox.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // selects brands from the dropdown
    public void selectBrands(List<String> brands) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Opens the dropdown and clicks the show more button
        wait.until(ExpectedConditions.elementToBeClickable(brandDropDown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(showMoreButtonForBrand)).click();

        // loops through each accessory type and selects them
        for (String brand : brands) {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), '" + brand + "')]")));
            checkbox.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
