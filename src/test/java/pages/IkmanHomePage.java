package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IkmanHomePage extends BasePage {
    @FindBy(xpath = "//input[@name='query']")
    public WebElement SearchBox;

    @FindBy(xpath = "//button[contains(@class, 'search-button--1_VmY')]")
    public WebElement SearchButton;

    @FindBy(xpath = "//button[contains(@class, 'location-picker-btn--p3-uX')]")
    public WebElement LocationSelector;

    public IkmanHomePage(WebDriver driver) {
        super(driver);
    }

    // Enters text in the search bar and clicks the search icon
    public SearchResultPage enterSearchText(String inputText){
        SearchBox.sendKeys(inputText);
        SearchButton.click();
        return PageFactory.initElements(driver, SearchResultPage.class);
    }

    // Selects location
    public void selectDistrict(String location) {
        LocationSelector.click();
        // Generates the xpath for the selected location and waits until its clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement locationButton = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath("//div[text()='" + location + "']/ancestor::button")));
        locationButton.click();
    }

    //Selects sub-location
    public void selectLocalArea(String subLocation) {
        // Generates the xpath for the selected sub-location and waits until its clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement subLocationButton = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath("//div[text()='" + subLocation + "']/ancestor::button")));
        subLocationButton.click();
    }

    // Returns the relevant page object depending on the user interaction.
    public <T> T selectCategory(String category) {
        // Generates the xpath for the selected category and waits until its clickable.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement categoryButton = wait.until(ExpectedConditions.elementToBeClickable
                (By.xpath("//p[text()='" + category + "']/ancestor::a")));
        categoryButton.click();

        switch (category.toLowerCase()) {
            case "electronics":
                return (T) PageFactory.initElements(driver, ElectronicsPage.class);
            case "property":
                return (T) PageFactory.initElements(driver, PropertiesPage.class);
            case "vehicles":
                return (T) PageFactory.initElements(driver, VehiclePage.class);
            case "home & garden":
                return (T) PageFactory.initElements(driver, HomeAndGardenPage.class);
            case "services":
                return (T) PageFactory.initElements(driver, ServicesPage.class);
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }
}
