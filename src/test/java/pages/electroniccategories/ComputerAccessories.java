package pages.electroniccategories;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ElectronicsPage;

import java.time.Duration;
import java.util.List;

public class ComputerAccessories extends ElectronicsPage {
    @FindBy(xpath = "//div[text() = 'Item type']/ancestor::button")
    public WebElement itemTypeDropDown;

    @FindBy(xpath = "//div[contains(@class, 'collapsible-section--WUsrw')]//div[contains(text(), 'Item type')]" +
            "/ancestor::button/following-sibling::div//button[span[contains(text(), 'Show more')]]/span")
    public WebElement showMoreButtonForItem;

    @FindBy(xpath = "//div[text() = 'Brand']/ancestor::button")
    public WebElement brandDropDown;

    @FindBy(xpath = "//div[contains(@class, 'collapsible-section--WUsrw')]//div[contains(text(), 'Brand')]" +
            "/ancestor::button/following-sibling::div//button[span[contains(text(), 'Show more')]]/span")
    public WebElement showMoreButtonForBrand;

    public ComputerAccessories(WebDriver driver) {
        super(driver);
    }

    public void selectItemTypes(List<String> itemTypes) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(itemTypeDropDown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(showMoreButtonForItem)).click();

        for (String itemType : itemTypes) {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), '" + itemType + "')]")));
            checkbox.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void selectBrands(List<String> brands) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(brandDropDown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(showMoreButtonForBrand)).click();

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
