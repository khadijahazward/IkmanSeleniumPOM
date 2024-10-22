package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SearchResultPage extends BasePage {
    @FindBy(xpath = "//div[@class='no-result-text--16bWr']")
    public List<WebElement> noResultList;

    @FindBy(xpath = "//h1[@class='heading--2eONR undefined ad-list-header--3g7Pb block--3v-Ow']")
    public WebElement searchResultHeading;

    @FindBy(xpath = "//ul[@class = 'list--3NxGO']")
    public WebElement searchResultsList;

    @FindBy(xpath = "//li[@class = 'normal--2QYVk gtm-normal-ad']")
    public List<WebElement> resultItems;

    // Checks if there are any search results displayed on the page.
    public boolean isResultsFound() {
        return searchResultsList.isDisplayed() && resultItems.size() > 0;
    }

    // Checks if the "No results found" message is displayed on the page.
    public boolean isNoResultsFound() {
        if (noResultList.size() > 0) {
            return noResultList.get(0).isDisplayed();
        } else {
            return false;
        }
    }

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }
}
