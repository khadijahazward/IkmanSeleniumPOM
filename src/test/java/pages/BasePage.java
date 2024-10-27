package pages;

import org.openqa.selenium.By;
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

    public BasePage (WebDriver driver) {
        this.driver = driver;
    }

    public IkmanHomePage loadURL(String url) {
        driver.get(url);
        return PageFactory.initElements(driver, IkmanHomePage.class);
    }

    public void scrollPage(int x, int y) {
        new Actions(driver).scrollByAmount(x,y).perform();
    }

    public void selectSortOption(String optionId) {
        sortResultsByButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(sortResultsByListBox));
        WebElement optionToSelect = driver.findElement(By.xpath("//li[@id='" + optionId + "']"));
        optionToSelect.click();
    }

    // Clicks on a category link.
    public BasePage navigateToCategory(String categoryText) {
        WebElement categoryLink = driver.findElement(By.xpath("//span[text()='" + categoryText + "']/ancestor::a"));
        categoryLink.click();
        return this;
    }
}
