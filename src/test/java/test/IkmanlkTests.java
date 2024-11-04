package test;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import pages.*;
import pages.electroniccategories.ComputerAccessories;
import pages.electroniccategories.MobilePhones;
import pages.homeandgardencategories.Furniture;
import pages.homeandgardencategories.OtherHomeItems;
import pages.propertiescategories.HouseForSale;
import pages.propertiescategories.LandForSale;
import pages.vehiclecategories.AutoPartsAndAccessories;
import pages.vehiclecategories.MotorBikes;
import utilities.Utility;

import java.time.Duration;
import java.util.*;

public class IkmanlkTests extends Utility {
    public final String URL = "https://ikman.lk/";

    @Test(dataProvider = "dataForTestCategory", dataProviderClass = TestDataProvider.class)
    public void testCategoryInHomePage(String location, String subLocation, String category) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman.lk home page.", true);

        try {
            homePage.selectDistrict(location);
            Reporter.log("Selected district: " + location, true);

            homePage.selectLocalArea(subLocation);
            Reporter.log("Selected area: " + subLocation, true);

            BasePage page = homePage.selectCategory(category);
            Reporter.log("Selected category: " + category, true);

            Reporter.log("Verifying that the title is displayed for the selected category page.");
            // Checks the type of the page object and verifies that the PageTitle element is displayed.
            if (page instanceof PropertiesPage){
                PropertiesPage propertiesPage = (PropertiesPage) page;
                Assert.assertTrue(propertiesPage.PageTitle.isDisplayed(),
                        "Expected element is not displayed on the Properties page");
            } else if (page instanceof ElectronicsPage){
                ElectronicsPage electronicsPage = (ElectronicsPage) page;
                Assert.assertTrue(electronicsPage.PageTitle.isDisplayed(),
                        "Expected element is not displayed on the Electronics page");
            } else if (page instanceof HomeAndGardenPage){
                HomeAndGardenPage homeAndGardenPage = (HomeAndGardenPage) page;
                Assert.assertTrue(homeAndGardenPage.PageTitle.isDisplayed(),
                        "Expected element is not displayed on the Home & Garden page");
            } else if (page instanceof VehiclePage){
                VehiclePage vehiclePage = (VehiclePage) page;
                Assert.assertTrue(vehiclePage.PageTitle.isDisplayed(),
                        "Expected element is not displayed on the Vehicles page");
            } else {
                Reporter.log("Unknown page type encountered!", true);
                throw new IllegalArgumentException("Unknown page!");
            }
        } catch (Exception e) {
            Reporter.log("Test failed due to an exception: " + e.getMessage(), true);
            Assert.fail("Test encountered an error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForSearchFunctionality", dataProviderClass = TestDataProvider.class)
    public void testSearchFunctionality(String searchQuery){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman.lk home page.", true);
        try {
                SearchResultPage searchResultPage = homePage.enterSearchText(searchQuery);
                Reporter.log("Entered search query: " + searchQuery, true);

                WebDriverWait wait = new WebDriverWait(browserFactory.getDriver(), Duration.ofSeconds(10));
                wait.until(ExpectedConditions.visibilityOf(searchResultPage.searchResultHeading));

                String resultText = searchResultPage.searchResultHeading.getText().toLowerCase();
                searchQuery = searchQuery.toLowerCase() + " in sri lanka";

                // Checks if the heading contains the search query.
                Assert.assertTrue(resultText.contains(searchQuery));
                Reporter.log("Search result heading contains the search query.", true);

                // Check for 'No results found' message or valid search results
                if (searchResultPage.isNoResultsFound()) {
                    Assert.assertTrue(searchResultPage.isNoResultsFound());
                    Reporter.log("'No results found!' message displayed for query: " + searchQuery, true);
                } else {
                    Assert.assertTrue(searchResultPage.isResultsFound());
                    Reporter.log("Search results displayed for query: " + searchQuery, true);
                }
        } catch (Exception e) {
            Reporter.log("Test failed due to an exception: " + e.getMessage(), true);
            Assert.fail("Test encountered an error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForSortResultsByOption", dataProviderClass = TestDataProvider.class)
    public void testSortOptions(String optionId, String expectedOptionText, String pageType) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman home page.", true);
        try {
            BasePage page = homePage.selectCategory(pageType);
            Reporter.log("Selected category: " + pageType, true);

            page.selectSortOption(optionId);
            Reporter.log("Selected sort option with ID: " + optionId, true);

            String selectedSortText = page.sortResultsByButton.getText();
            Reporter.log("Retrieved selected sort text: " + selectedSortText, true);

            Reporter.log("Asserting that selected sort text contains expected option text: " + expectedOptionText, true);
            Assert.assertTrue(selectedSortText.contains(expectedOptionText));
        } catch (Exception e) {
            Reporter.log("Test failed due to an exception: " + e.getMessage(), true);
            Assert.fail("Test encountered an error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForMobilePhonesPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInMobilePhonesPage(String adType, String brandName, String modelName,
                                              String condition, Double minPrice, Double maxPrice) {
        try {
            BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
            IkmanHomePage homePage = basePage.loadURL(URL);
            Reporter.log("Navigated to Ikman.lk home page.", true);

            ElectronicsPage page = homePage.selectCategory("Electronics");
            Reporter.log("Selected category: Electronics", true);

            MobilePhones mobilePhones = (MobilePhones) page.navigateToCategory("Mobile Phones");
            Reporter.log("Navigated to Mobile Phones category.", true);

            mobilePhones.scrollPage(0,800);

            // Apply the adType filter
            mobilePhones.selectAdType(adType);
            Reporter.log("Applied ad type filter: " + adType, true);

            // Apply Condition filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
                mobilePhones.selectCondition(condition);
                Reporter.log("Applied condition filter: " + condition, true);
            }

            // Apply price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                mobilePhones.setPriceRange(minPrice, maxPrice);
                Reporter.log("Applied price range filter: Min = " + minPrice + ", Max = " + maxPrice, true);
            }

            // Apply Brand and Model if specified
            if (brandName != null && !brandName.isEmpty()) {
                mobilePhones.selectBrandAndModel(brandName, modelName);
                Reporter.log("Applied brand and model filters: Brand = " + brandName + ", Model = " + modelName, true);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("Current URL after applying filters: " + currentURL, true);

            // Asserts if the filters are applied.
            if ("For Sale".equals(adType)) {
                if (condition != null && !condition.isEmpty()) {
                    Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
                    Reporter.log("Verified that condition is present in the URL: " + "enum.condition=" + condition.toLowerCase(), true);
                }
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified that min price is present in the URL: " + "money.price.minimum=" + minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified that max price is present in the URL: " + "money.price.maximum=" + maxPrice.intValue(), true);
                }
                if (brandName != null && !brandName.isEmpty()) {
                    if (modelName != null && !modelName.isEmpty()) {
                        // If both brand and model are provided
                        Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase()
                                + "_" +  brandName.toLowerCase()+ "-" + modelName.toLowerCase().replace(" ", "-")));
                        Reporter.log("Verified that brand and model are present in the URL: tree.brand=" + brandName.toLowerCase()
                                + "_" + brandName.toLowerCase() + "-" + modelName.toLowerCase().replace(" ", "-"), true);
                    } else {
                        // If only brand is provided
                        Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase()));
                        Reporter.log("Verified that brand is present in the URL: " + brandName.toLowerCase(), true);
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log("Test failed due to an exception: " + e.getMessage(), true);
            Assert.fail("Test encountered an error: " + e.getMessage());
        }

    }

    @Test(dataProvider = "dataForComputerAccessoriesPageFilters",  dataProviderClass = TestDataProvider.class)
    public void testFiltersInComputerAccessoriesPage(String adType, String condition, Double minPrice,
                                                     Double maxPrice, String itemTypeString, String brandNameString){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman home page.", true);

        ElectronicsPage page = homePage.selectCategory("Electronics");
        Reporter.log("Selected category: Electronics", true);

        ComputerAccessories computerAccessories = (ComputerAccessories) page.navigateToCategory("Computer Accessories");
        Reporter.log("Navigated to Computer Accessories category.", true);

        try {
            computerAccessories.scrollPage(0,800);

            // Apply the adType filter
            computerAccessories.selectAdType(adType);
            Reporter.log("Applied ad type filter: " + adType, true);

            // Apply Condition filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
                computerAccessories.selectCondition(condition);
                Reporter.log("Applied condition filter: " + condition, true);
            }

            // Apply price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                computerAccessories.setPriceRange(minPrice, maxPrice);
                Reporter.log("Applied price range filter: Min = " + minPrice + ", Max = " + maxPrice, true);
            }

            // Selects items if the items list is not empty
            if (itemTypeString != null && !itemTypeString.trim().isEmpty()) {
                List<String> itemTypes = Arrays.asList(itemTypeString.split(","));
                computerAccessories.selectItemTypes(itemTypes);
                Reporter.log("Applied item type filters: " + itemTypeString, true);
            }

            // Selects brands if the brand name list is not empty
            if (brandNameString != null && !brandNameString.trim().isEmpty()) {
                List<String> brandNames = Arrays.asList(brandNameString.split(","));
                computerAccessories.selectBrands(brandNames);
                Reporter.log("Applied brand filters: " + brandNameString, true);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("URL after applying filters: " + currentURL, true);

            // Asserts if the filters are applied.
            if ("For Sale".equals(adType)) {
                if (condition != null && !condition.isEmpty()) {
                    Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
                    Reporter.log("Verified that condition is present in the URL: " + "enum.condition=" +
                            condition.toLowerCase(), true);
                }
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified that min price is present in the URL: " + "money.price.minimum=" +
                            minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified that max price is present in the URL: " + "money.price.maximum=" +
                            maxPrice.intValue(), true);
                }
                if (brandNameString != null && !brandNameString.trim().isEmpty()) {
                    String[] brands = brandNameString.split(",");
                    String urlBrands = currentURL.substring(currentURL.indexOf("enum.brand=")
                            + "enum.brand=".length()).toLowerCase();
                    for (String brand : brands) {
                        String formattedBrand = brand.trim().toLowerCase();
                        Assert.assertTrue(urlBrands.contains(formattedBrand));
                        Reporter.log("Verified that brand is present in the URL: " + formattedBrand, true);
                    }
                }
                if (itemTypeString != null && !itemTypeString.trim().isEmpty()) {
                    String[] itemTypes = itemTypeString.split(",");
                    String urlItems = currentURL.substring(currentURL.indexOf("enum.item_type=")
                            + "enum.item_type=".length()).toLowerCase();
                    for (String item : itemTypes) {
                        String formattedItem = item.trim().toLowerCase();
                        Assert.assertTrue(urlItems.contains(formattedItem));
                        Reporter.log("Verified that item type is present in the URL: " + formattedItem, true);
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log("An unexpected error occurred: " + e.getMessage(), true);
            Assert.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForLandForSalePageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInLandForSalePage(String adType, Double minPrice, Double maxPrice,
                                             Double minLandSize, Double maxLandSize){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman home page.", true);

        PropertiesPage page = homePage.selectCategory("Property");
        Reporter.log("Selected category: Property", true);

        LandForSale landForSale = (LandForSale) page.navigateToCategory("Land For Sale");
        Reporter.log("Navigated to Land For Sale category.", true);
        try {
            landForSale.scrollPage(0,800);

            // Apply the adType filter
            landForSale.selectAdType(adType);
            Reporter.log("Applied ad type filter: " + adType, true);

            // Apply price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                landForSale.setPriceRange(minPrice, maxPrice);
                Reporter.log("Applied price range filter: Min = " + minPrice + ", Max = " + maxPrice, true);
            }

            // Apply land size filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minLandSize != null || maxLandSize != null)) {
                landForSale.setLandSizeRange(minLandSize, maxLandSize);
                Reporter.log("Applied land size filter: Min = " + minLandSize + ", Max = " + maxLandSize, true);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("URL after applying filters: " + currentURL, true);

            // Asserts if the filters are applied.
            if ("For Sale".equals(adType)) {
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified that minimum price is present in the URL: " +
                            "money.price.minimum=" + minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified that maximum price is present in the URL: "
                            + "money.price.maximum=" + maxPrice.intValue(), true);
                }
                if (minLandSize != null) {
                    Assert.assertTrue(currentURL.contains("numeric.size.minimum=" + minLandSize.intValue()));
                    Reporter.log("Verified that minimum land size is present in the URL: "
                            + "numeric.size.minimum=" + minLandSize.intValue(), true);
                }
                if (maxLandSize != null) {
                    Assert.assertTrue(currentURL.contains("numeric.size.maximum=" + maxLandSize.intValue()));
                    Reporter.log("Verified that maximum land size is present in the URL: "
                            + "numeric.size.maximum=" + maxLandSize.intValue(), true);
                }
            }
        } catch (Exception e) {
            Reporter.log("An unexpected error occurred: " + e.getMessage(), true);
            Assert.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForHouseForSalePageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInHouseForSalePage(String adType, Double minPrice, Double maxPrice,
                                              Double minHouseSize, Double maxHouseSize, String noOfBedrooms, String noOfBathrooms){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman home page.", true);

        PropertiesPage page = homePage.selectCategory("Property");
        Reporter.log("Selected category: Property", true);

        HouseForSale houseForSale = (HouseForSale) page.navigateToCategory("Houses For Sale");
        Reporter.log("Navigated to Houses For Sale category.", true);

        try {
            houseForSale.scrollPage(0, 800);

            // Applies the adType filter
            houseForSale.selectAdType(adType);
            Reporter.log("Selected ad type: " + adType, true);

            // Applies price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                houseForSale.setPriceRange(minPrice, maxPrice);
                Reporter.log("Applied price range: " + minPrice + " - " + maxPrice, true);
            }

            // Applies house size filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minHouseSize != null || maxHouseSize != null)) {
                houseForSale.setHouseSizeRange(minHouseSize, maxHouseSize);
                Reporter.log("Applied house size range: " + minHouseSize + " - " + maxHouseSize, true);
            }

            // Selects number of bedrooms
            if (noOfBedrooms != null && !noOfBedrooms.trim().isEmpty()) {
                List<String> noOfBedroomsList = Arrays.asList(noOfBedrooms.split(","));
                houseForSale.selectNumberOfBedrooms(noOfBedroomsList);
                Reporter.log("Selected number of bedrooms: " + noOfBedrooms, true);
            }

            // Selects number of bathrooms
            if (noOfBathrooms != null && !noOfBathrooms.trim().isEmpty()) {
                List<String> noOfBathroomsList = Arrays.asList(noOfBathrooms.split(","));
                houseForSale.selectNumberofBathrooms(noOfBathroomsList);
                Reporter.log("Selected number of bathrooms: " + noOfBathrooms, true);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("URL after applying filters: " + currentURL, true);

            // Asserts if the filters are applied.
            if ("For Sale".equals(adType)) {
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified minimum price in URL: " + "money.price.minimum=" + minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified maximum price in URL: " + "money.price.maximum=" + maxPrice.intValue(), true);
                }
                if (minHouseSize != null) {
                    Assert.assertTrue(currentURL.contains("numeric.house_size.minimum=" + minHouseSize.intValue()));
                    Reporter.log("Verified minimum house size in URL: " + "numeric.house_size.minimum=" + minHouseSize.intValue(), true);
                }
                if (maxHouseSize != null) {
                    Assert.assertTrue(currentURL.contains("numeric.house_size.maximum=" + maxHouseSize.intValue()));
                    Reporter.log("Verified maximum house size in URL: " + "numeric.house_size.maximum=" + maxHouseSize.intValue(), true);
                }
                if (noOfBedrooms != null && !noOfBedrooms.isEmpty()) {
                    // Sorts the string in ascending order for assertion.
                    String[] bedroomArray = noOfBedrooms.split(",");
                    Arrays.sort(bedroomArray);
                    String sortedBedrooms = String.join(",", bedroomArray);
                    Assert.assertTrue(currentURL.contains("enum.bedrooms=" + sortedBedrooms));
                    Reporter.log("Verified number of bedrooms in URL: " + "enum.bedrooms=" + sortedBedrooms, true);
                }
                if (noOfBathrooms != null && !noOfBathrooms.isEmpty()) {
                    // Sorts the string in ascending order for assertion.
                    String[] bathroomArray = noOfBathrooms.split(",");
                    Arrays.sort(bathroomArray, Comparator.comparingInt(Integer::parseInt));
                    String sortedBathrooms = String.join(",", bathroomArray);
                    Assert.assertTrue(currentURL.contains("enum.bathrooms=" + sortedBathrooms));
                    Reporter.log("Verified number of bathrooms in URL: " + "enum.bathrooms=" + sortedBathrooms, true);
                }
            }
        } catch (Exception e) {
            Reporter.log("An unexpected error occurred: " + e.getMessage(), true);
            Assert.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForAutoPartsPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInAutoPartsPage(String adType, String condition, Double minPrice, Double maxPrice, String partType,
                                           String brandName, String modelName) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman home page.", true);

        VehiclePage page = homePage.selectCategory("Vehicles");
        Reporter.log("Selected category: Vehicles", true);

        AutoPartsAndAccessories autoPartsAndAccessories = (AutoPartsAndAccessories) page.navigateToCategory("Auto Parts & Accessories");
        Reporter.log("Navigated to Auto Parts & Accessories category.", true);

        try {
            autoPartsAndAccessories.scrollPage(0, 800);

            // Applies the adType filter
            autoPartsAndAccessories.selectAdType(adType);
            Reporter.log("Selected ad type: " + adType, true);

            // Applies price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                autoPartsAndAccessories.setPriceRange(minPrice, maxPrice);
                Reporter.log("Applied price range: " + minPrice + " - " + maxPrice, true);
            }

            // Apply Condition filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
                autoPartsAndAccessories.selectConditionForVehicle(condition);
                Reporter.log("Selected condition: " + condition, true);
            }

            // Selects part types if the parts list is not empty
            if (partType != null && !partType.trim().isEmpty()) {
                List<String> partTypes = Arrays.asList(partType.split(","));
                autoPartsAndAccessories.selectTypesOfAccessories(partTypes);
                Reporter.log("Selected part types: " + partType, true);
            }

            // Apply Brand and Model if specified
            if (brandName != null && !brandName.isEmpty()) {
                autoPartsAndAccessories.selectBrandAndModel(brandName, modelName);
                Reporter.log("Selected brand: " + brandName + ", model: " + modelName, true);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("Current URL: " + currentURL, true);

            // Asserts if the filters are applied.
            if ("For Sale".equals(adType)) {
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified minimum price in URL: money.price.minimum=" + minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified maximum price in URL: money.price.maximum=" + maxPrice.intValue(), true);
                }
                if (condition != null && !condition.isEmpty()) {
                    Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
                    Reporter.log("Verified condition in URL: enum.condition=" + condition.toLowerCase(), true);
                }
                if (partType != null && !partType.trim().isEmpty()) {
                    String[] parts = partType.split(",");
                    String urlParts = currentURL.substring(currentURL.indexOf("enum.item_type=")
                            + "enum.item_type=".length()).toLowerCase();
                    for (String part : parts) {
                        String formattedPart = part.trim().toLowerCase().replace("&", "").replaceAll("\\s+", "_");
                        Assert.assertTrue(urlParts.contains(formattedPart));
                        Reporter.log("Verified part type in URL: " + formattedPart, true);
                    }
                }
                if (brandName != null && !brandName.isEmpty()) {
                    if (modelName != null && !modelName.isEmpty()) {
                        // If both brand and model are provided
                        Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase() + "_" +
                                brandName.toLowerCase() + "-" + modelName.toLowerCase().replace(" ", "")));
                        Reporter.log("Verified brand and model in URL: tree.brand=" + brandName.toLowerCase() + "_" +
                                brandName.toLowerCase() + "-" + modelName.toLowerCase().replace(" ", ""), true);
                    } else {
                        // If only brand is provided
                        Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase()));
                        Reporter.log("Verified brand in URL: tree.brand=" + brandName.toLowerCase(), true);
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log("An unexpected error occurred: " + e.getMessage(), true);
            Assert.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForMotorBikesPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInMotorBikesPage(String adType, String bikeType, String condition, Double minPrice, Double maxPrice,
                                            String brandName, String modelName, Double minYearOfManufacture, Double maxYearOfManufacture,
                                            Double minMileage, Double maxMileage){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman.lk Home Page", true);

        VehiclePage page = homePage.selectCategory("Vehicles");
        Reporter.log("Selected category: Vehicles", true);

        MotorBikes motorBikes = (MotorBikes) page.navigateToCategory("Motorbikes");
        Reporter.log("Navigated to Motorbikes page", true);
        try {
            motorBikes.scrollPage(0,800);

            // Applies the adType filter
            motorBikes.selectAdType(adType);
            Reporter.log("Applied Ad Type filter: " + adType, true);

            // Applies price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                motorBikes.setPriceRange(minPrice, maxPrice);
                Reporter.log("Set price range filter: min = " + minPrice + ", max = " + maxPrice, true);
            }

            // Apply Condition filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
                motorBikes.selectConditionForVehicle(condition);
                Reporter.log("Applied Condition filter: " + condition, true);
            }

            // Selects bike types if the types of bike list is not empty
            if (bikeType != null && !bikeType.trim().isEmpty()) {
                List<String> bikeTypes = Arrays.asList(bikeType.split(","));
                motorBikes.selectTypesOfBikes(bikeTypes);
                Reporter.log("Applied Bike Types filter: " + bikeTypes, true);
            }

            // Applies year of manufacture filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minYearOfManufacture != null || maxYearOfManufacture != null)) {
                motorBikes.setYearOfManufactureRange(minYearOfManufacture, maxYearOfManufacture);
                Reporter.log("Set year of manufacture range: min = " + minYearOfManufacture
                        + ", max = " + maxYearOfManufacture, true);
            }

            // Applies mileage filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minMileage != null || maxMileage != null)) {
                motorBikes.setMileageRange(minMileage, maxMileage);
                Reporter.log("Set mileage range: min = " + minMileage + ", max = " + maxMileage, true);
            }

            // Apply Brand and Model if specified
            if (brandName != null && !brandName.isEmpty()) {
                motorBikes.selectBrandAndModel(brandName, modelName);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("URL after applying filters: " + currentURL, true);

            // Asserts if the conditions are applied.
            if ("For Sale".equals(adType)) {
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified minimum price in URL: money.price.minimum=" + minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified maximum price in URL: money.price.maximum=" + maxPrice.intValue(), true);
                }
                if (minYearOfManufacture != null) {
                    Assert.assertTrue(currentURL.contains("numeric.model_year.minimum=" + minYearOfManufacture.intValue()));
                    Reporter.log("Verified minimum year of manufacture in URL: numeric.model_year.minimum="
                            + minYearOfManufacture.intValue(), true);
                }
                if (maxYearOfManufacture != null) {
                    Assert.assertTrue(currentURL.contains("numeric.model_year.maximum=" + maxYearOfManufacture.intValue()));
                    Reporter.log("Verified maximum year of manufacture in URL: numeric.model_year.maximum="
                            + maxYearOfManufacture.intValue(), true);
                }
                if (minMileage != null) {
                    Assert.assertTrue(currentURL.contains("numeric.mileage.minimum=" + minMileage.intValue()));
                    Reporter.log("Verified minimum mileage in URL: numeric.mileage.minimum=" + minMileage.intValue(), true);
                }
                if (maxMileage != null) {
                    Assert.assertTrue(currentURL.contains("numeric.mileage.maximum=" + maxMileage.intValue()));
                    Reporter.log("Verified maximum mileage in URL: numeric.mileage.maximum=" + maxMileage.intValue(), true);
                }
                if (condition != null && !condition.isEmpty()) {
                    Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
                    Reporter.log("Verified condition in URL: enum.condition=" + condition.toLowerCase(), true);
                }
                if (bikeType != null && !bikeType.trim().isEmpty()) {
                    String[] parts = bikeType.split(",");
                    String urlParts = currentURL.substring(currentURL.indexOf("enum.item_type=")
                            + "enum.item_type=".length()).toLowerCase();
                    for (String part : parts) {
                        String formattedPart = part.trim().toLowerCase().replace("-", "_");
                        Assert.assertTrue(urlParts.contains(formattedPart));
                        Reporter.log("Verified bike type in URL: " + formattedPart, true);
                    }
                }
                if (brandName != null && !brandName.isEmpty()) {
                    if (modelName != null && !modelName.isEmpty()) {
                        // If both brand and model are provided
                        Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase() + "_" +
                                brandName.toLowerCase()+ "-" + modelName.toLowerCase().replace(" ", "-")));
                        Reporter.log("Verified brand and model in URL: tree.brand=" + brandName.toLowerCase() + "_" +
                                brandName.toLowerCase() + "-" + modelName.toLowerCase().replace(" ", "-"), true);
                    } else {
                        // If only brand is provided
                        Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase()));
                        Reporter.log("Verified brand in URL: tree.brand=" + brandName.toLowerCase(), true);
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log("An unexpected error occurred: " + e.getMessage(), true);
            Assert.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForFurniturePageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInFurniturePage(String adType, String condition, Double minPrice, Double maxPrice,
                                           String furnitureTypes, String brands){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman.lk Home Page", true);

        HomeAndGardenPage page = homePage.selectCategory("Home & Garden");
        Reporter.log("Selected category: Home & Garden", true);

        Furniture furniturePage = (Furniture) page.navigateToCategory("Furniture");
        Reporter.log("Navigated to Furniture page", true);

        try {
            furniturePage.scrollPage(0,800);

            // Applies the adType filter
            furniturePage.selectAdType(adType);
            Reporter.log("Selected ad type: " + adType, true);

            // Applies price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                furniturePage.setPriceRange(minPrice, maxPrice);
                Reporter.log("Set price range from " + minPrice + " to " + maxPrice, true);
            }

            // Apply Condition filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
                furniturePage.selectConditionForItem(condition);
                Reporter.log("Selected condition: " + condition, true);
            }

            // Selects furniture types if the list is not empty
            if (furnitureTypes != null && !furnitureTypes.trim().isEmpty()) {
                List<String> furniture = Arrays.asList(furnitureTypes.split(","));
                furniturePage.selectTypesOfFurniture(furniture);
                Reporter.log("Selected furniture types: " + furniture, true);
            }

            // Selects brands if the brand name list is not empty
            if (brands != null && !brands.trim().isEmpty()) {
                List<String> brandNames = Arrays.asList(brands.split(","));
                furniturePage.selectBrands(brandNames);
                Reporter.log("Selected brands: " + brandNames, true);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("URL after applying filters: " + currentURL, true);

            // Asserts if the filters are applied.
            if ("For Sale".equals(adType)) {
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified min price in URL: money.price.minimum=" + minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified max price in URL: money.price.maximum=" + maxPrice.intValue(), true);
                }
                if (condition != null && !condition.isEmpty()) {
                    Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
                    Reporter.log("Verified condition in URL: enum.condition=" + condition.toLowerCase(), true);
                }
                Map<String, String> furnitureTypeMapping = getFurnitureTypeMapping();
                if (furnitureTypes != null && !furnitureTypes.trim().isEmpty()) {
                    String[] typesOfFurniture = furnitureTypes.split(",");
                    String urlFurniture = currentURL.substring(currentURL.indexOf("enum.item_type=")
                            + "enum.item_type=".length()).toLowerCase();
                    for (String furniture : typesOfFurniture) {
                        String mappedFurnitureType = furnitureTypeMapping.get(furniture.trim());
                        Assert.assertTrue(urlFurniture.contains(mappedFurnitureType));
                        Reporter.log("Verified furniture type in URL: " + mappedFurnitureType, true);
                    }
                }
                if (brands != null && !brands.trim().isEmpty()) {
                    String[] brandTypes = brands.split(",");
                    String urlBrands = currentURL.substring(currentURL.indexOf("enum.brand=")
                            + "enum.brand=".length()).toLowerCase();
                    for (String brand : brandTypes) {
                        String formattedBrand = brand.trim().toLowerCase().replace("&", "").replaceAll("\\s+", "_");
                        Assert.assertTrue(urlBrands.contains(formattedBrand));
                        Reporter.log("Verified brand in URL: " + formattedBrand, true);
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log("An unexpected error occurred: " + e.getMessage(), true);
            Assert.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    @Test(dataProvider = "dataForOtherHomeItemsPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInOtherHomeItemsPage(String adType, String condition, Double minPrice, Double maxPrice){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        Reporter.log("Navigated to Ikman.lk Home Page", true);

        HomeAndGardenPage page = homePage.selectCategory("Home & Garden");
        Reporter.log("Selected 'Home & Garden' category", true);

        OtherHomeItems otherHomeItemsPage = (OtherHomeItems) page.navigateToCategory("Other Home Items");
        Reporter.log("Navigated to 'Other Home Items' page", true);

        try {
            otherHomeItemsPage.scrollPage(0,800);

            // Applies the adType filter
            otherHomeItemsPage.selectAdType(adType);
            Reporter.log("Selected ad type: " + adType, true);

            // Applies price filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
                otherHomeItemsPage.setPriceRange(minPrice, maxPrice);
                Reporter.log("Set price range from " + minPrice + " to " + maxPrice, true);
            }

            // Apply Condition filter if specified and adType is "For Sale"
            if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
                otherHomeItemsPage.selectConditionForItem(condition);
                Reporter.log("Selected condition: " + condition, true);
            }

            String currentURL = browserFactory.getDriver().getCurrentUrl();
            Reporter.log("URL after applying filters: " + currentURL, true);

            if ("For Sale".equals(adType)) {
                if (minPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
                    Reporter.log("Verified min price in URL: money.price.minimum=" + minPrice.intValue(), true);
                }
                if (maxPrice != null) {
                    Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
                    Reporter.log("Verified max price in URL: money.price.maximum=" + maxPrice.intValue(), true);
                }
                if (condition != null && !condition.isEmpty()) {
                    Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
                    Reporter.log("Verified condition in URL: enum.condition=" + condition.toLowerCase(), true);
                }
            }
        } catch (Exception e) {
            Reporter.log("An unexpected error occurred: " + e.getMessage(), true);
            Assert.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    // Maps between values in the page and URL values for furniture types.
    private static Map<String, String> getFurnitureTypeMapping() {
        Map<String, String> furnitureTypeMapping = new HashMap<>();
        furnitureTypeMapping.put("Bedroom Furniture", "bedroom");
        furnitureTypeMapping.put("Tables & Chairs", "table_chair");
        furnitureTypeMapping.put("Living Room Furniture", "living_room");
        furnitureTypeMapping.put("Shelves & Pantry Cupboards", "storage");
        furnitureTypeMapping.put("TV / stereo", "tv_stereo");
        furnitureTypeMapping.put("Other", "other");
        furnitureTypeMapping.put("Antique Furnitures", "art");
        furnitureTypeMapping.put("Lighting", "lightning");
        furnitureTypeMapping.put("Outdoor & Garden", "outdoor_n_garden");
        furnitureTypeMapping.put("Textiles / decoration", "decoration");
        return furnitureTypeMapping;
    }
}
