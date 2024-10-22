package test;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import utilities.Utility;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    @Test(dataProvider = "dataForTestCategory")
    public void testCategoryInHomePage(String location, String subLocation, String category) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);

        homePage.selectDistrict(location);
        homePage.selectLocalArea(subLocation);
        BasePage page = homePage.selectCategory(category);

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

        WebDriverWait wait = new WebDriverWait(browserFactory.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(searchResultPage.searchResultHeading));

        String resultText = searchResultPage.searchResultHeading.getText().toLowerCase();

        searchQuery = searchQuery.toLowerCase() + " in sri lanka";
        System.out.println(searchQuery + "  " + resultText);
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
}
