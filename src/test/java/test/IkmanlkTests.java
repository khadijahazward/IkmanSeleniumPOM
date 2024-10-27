package test;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.*;
import pages.electroniccategories.ComputerAccessories;
import pages.electroniccategories.ComputersTablets;
import pages.electroniccategories.MobilePhoneAccessories;
import pages.electroniccategories.MobilePhones;
import utilities.Utility;

import java.time.Duration;

public class IkmanlkTests extends Utility {
    public final String URL = "https://ikman.lk/";

    @DataProvider
    public static Object[][] dataForTestCategory() {
        return Utility.readTestDataFile(0);
    }

    @DataProvider
    public static Object[][] dataForSearchFunctionality(){
        return Utility.readTestDataFile(1);
    }

    @DataProvider
    public static Object[][] dataForSortResultsByOption() { return Utility.readTestDataFile(2);}

    @Test(dataProvider = "dataForTestCategory")
    public void testCategoryInHomePage(String location, String subLocation, String category) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);

        homePage.selectDistrict(location);
        homePage.selectLocalArea(subLocation);
        BasePage page = homePage.selectCategory(category);

        // Checks the type of the page object and verifies that the PageTitle element is displayed.
        if (page instanceof PropertiesPage){
            PropertiesPage propertiesPage = (PropertiesPage) page;
            Assert.assertTrue(propertiesPage.PageTitle.isDisplayed(),
                    "Expected element is not displayed on the Properties page");
        } else if (page instanceof ServicesPage){
            ServicesPage servicesPage = (ServicesPage) page;
            Assert.assertTrue(servicesPage.PageTitle.isDisplayed(),
                    "Expected element is not displayed on the Services page");
        } else if (page instanceof ElectronicsPage){
            ElectronicsPage electronicsPage = (ElectronicsPage) page;
            Assert.assertTrue(electronicsPage.PageTitle.isDisplayed(),
                    "Expected element is not displayed on the Electronics page");
        } else if (page instanceof HomeAndGardenPage){
            HomeAndGardenPage homeAndGardenPage = (HomeAndGardenPage) page;
            Assert.assertTrue(homeAndGardenPage.PageTitle.isDisplayed(),
                    "Expected element is not displayed on the Home & Garden page");
        } else if (page instanceof BusinessAndIndustryPage){
            BusinessAndIndustryPage businessAndIndustryPage = (BusinessAndIndustryPage) page;
            Assert.assertTrue(businessAndIndustryPage.PageTitle.isDisplayed(),
                    "Expected element is not displayed on the Business & Industry page");
        } else if (page instanceof VehiclePage){
            VehiclePage vehiclePage = (VehiclePage) page;
            Assert.assertTrue(vehiclePage.PageTitle.isDisplayed(),
                    "Expected element is not displayed on the Vehicles page");
        } else {
            throw new IllegalArgumentException("Unknown page!");
        }
    }

    @Test(dataProvider = "dataForSearchFunctionality")
    public void testSearchFunctionality(String searchQuery){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        SearchResultPage searchResultPage = homePage.enterSearchText(searchQuery);

        // Waits until the search result heading is loaded
        WebDriverWait wait = new WebDriverWait(browserFactory.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(searchResultPage.searchResultHeading));

        String resultText = searchResultPage.searchResultHeading.getText().toLowerCase();
        searchQuery = searchQuery.toLowerCase() + " in sri lanka";

        // Checks if the heading contains the search query.
        Assert.assertTrue(resultText.contains(searchQuery));

        // Checks if the search results page displays a 'No results found' message for an invalid search query,
        // or if valid search results are displayed for a valid query.
        if (searchResultPage.isNoResultsFound()) {
            Assert.assertTrue(searchResultPage.isNoResultsFound(),
                    "'No results found!' message was not displayed.");
        } else {
            Assert.assertTrue(searchResultPage.isResultsFound(),
                    "Search results are not displayed for: " + searchQuery);
        }
    }

    @Test(dataProvider = "dataForSortResultsByOption")
    public void testSortOptions(String optionId, String expectedOptionText, String pageType) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        BasePage page = homePage.selectCategory(pageType);
        page.selectSortOption(optionId);
        String selectedSortText = page.sortResultsByButton.getText();
        Assert.assertTrue(selectedSortText.contains(expectedOptionText));
    }

    @Test
    public void testFiltersInMobilePhonesPage() {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        ElectronicsPage page = homePage.selectCategory("Electronics");
        MobilePhones mobilePhones = (MobilePhones) page.navigateToCategory("Mobile Phones");
        mobilePhones.scrollPage(0,800);
//        mobilePhones.selectAdType("For Sale"); // For sale, Wanted
//        mobilePhones.selectCondition("New"); // New, Used (wanted doesnt have this)
//        mobilePhones.setPriceRange(50000, 100000);
        mobilePhones.selectBrandAndModel("Samsung", "Galaxy A15");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // test filters in mobile phone page
    // test filters in accessories page

}
