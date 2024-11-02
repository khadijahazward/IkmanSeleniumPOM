package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.propertiescategories.HouseForSale;
import pages.propertiescategories.LandForSale;

public class PropertiesPage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Property']/ancestor::Button")
    public WebElement PageTitle;

    public PropertiesPage(WebDriver driver) {
        super(driver);
    }

    // Returns the page object based on the category selected
    @Override
    public BasePage navigateToCategory(String categoryText) {
        super.navigateToCategory(categoryText);
        switch (categoryText.toLowerCase()) {
            case "land for sale":
                return PageFactory.initElements(driver, LandForSale.class);
            case "houses for sale":
                return PageFactory.initElements(driver, HouseForSale.class);
            default:
                throw new IllegalArgumentException("Unknown category: " + categoryText);
        }
    }
}
