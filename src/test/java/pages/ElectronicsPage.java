package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.electroniccategories.ComputerAccessories;
import pages.electroniccategories.ComputersTablets;
import pages.electroniccategories.MobilePhoneAccessories;
import pages.electroniccategories.MobilePhones;

public class ElectronicsPage extends BasePage{
    @FindBy(xpath = "//div[text() = 'Electronics']/ancestor::Button")
    public WebElement PageTitle;

    public ElectronicsPage(WebDriver driver) {
        super(driver);
    }

    // Returns the page object based on the category selected
    @Override
    public BasePage navigateToCategory(String categoryText) {
        super.navigateToCategory(categoryText);
        switch (categoryText.toLowerCase()) {
            case "mobile phone accessories":
                return PageFactory.initElements(driver, MobilePhoneAccessories.class);
            case "computers & tablets":
                return PageFactory.initElements(driver, ComputersTablets.class);
            case "computer accessories":
                return PageFactory.initElements(driver, ComputerAccessories.class);
            case "mobile phones":
                return PageFactory.initElements(driver, MobilePhones.class);
            default:
                throw new IllegalArgumentException("Unknown category: " + categoryText);
        }
    }

}
