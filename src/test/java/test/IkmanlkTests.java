package test;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
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

        homePage.selectDistrict(location);
        homePage.selectLocalArea(subLocation);
        BasePage page = homePage.selectCategory(category);

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
            throw new IllegalArgumentException("Unknown page!");
        }
    }

    @Test(dataProvider = "dataForSearchFunctionality", dataProviderClass = TestDataProvider.class)
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

    @Test(dataProvider = "dataForSortResultsByOption", dataProviderClass = TestDataProvider.class)
    public void testSortOptions(String optionId, String expectedOptionText, String pageType) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        BasePage page = homePage.selectCategory(pageType);
        page.selectSortOption(optionId);
        String selectedSortText = page.sortResultsByButton.getText();
        Assert.assertTrue(selectedSortText.contains(expectedOptionText));
    }

    @Test(dataProvider = "dataForMobilePhonesPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInMobilePhonesPage(String adType, String brandName, String modelName,
                                              String condition, Double minPrice, Double maxPrice) {
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        ElectronicsPage page = homePage.selectCategory("Electronics");
        MobilePhones mobilePhones = (MobilePhones) page.navigateToCategory("Mobile Phones");

        mobilePhones.scrollPage(0,800);

        // Apply the adType filter
        mobilePhones.selectAdType(adType);

        // Apply Condition filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
            mobilePhones.selectCondition(condition);
        }

        // Apply price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            mobilePhones.setPriceRange(minPrice, maxPrice);
        }

        // Apply Brand and Model if specified
        if (brandName != null && !brandName.isEmpty()) {
            mobilePhones.selectBrandAndModel(brandName, modelName);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();

        if ("For Sale".equals(adType)) {
            if (condition != null && !condition.isEmpty()) {
                Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
            }
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (brandName != null && !brandName.isEmpty()) {
                if (modelName != null && !modelName.isEmpty()) {
                    // If both brand and model are provided
                    Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase() + "_" +  brandName.toLowerCase()+ "-" + modelName.toLowerCase().replace(" ", "-")));
                } else {
                    // If only brand is provided
                    Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase()));
                }
            }
        }
    }

    @Test(dataProvider = "dataForComputerAccessoriesPageFilters",  dataProviderClass = TestDataProvider.class)
    public void testFiltersInComputerAccessoriesPage(String adType, String condition, Double minPrice,
                                                     Double maxPrice, String itemTypeString, String brandNameString){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        ElectronicsPage page = homePage.selectCategory("Electronics");
        ComputerAccessories computerAccessories = (ComputerAccessories) page.navigateToCategory("Computer Accessories");

        computerAccessories.scrollPage(0,800);
        // Apply the adType filter
        computerAccessories.selectAdType(adType);

        // Apply Condition filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
            computerAccessories.selectCondition(condition);
        }

        // Apply price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            computerAccessories.setPriceRange(minPrice, maxPrice);
        }

        // Selects items if the items list is not empty
        if (itemTypeString != null && !itemTypeString.trim().isEmpty()) {
            List<String> itemTypes = Arrays.asList(itemTypeString.split(","));
            computerAccessories.selectItemTypes(itemTypes);
        }

        // Selects brands if the brand name list is not empty
        if (brandNameString != null && !brandNameString.trim().isEmpty()) {
            List<String> brandNames = Arrays.asList(brandNameString.split(","));
            computerAccessories.selectBrands(brandNames);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();

        if ("For Sale".equals(adType)) {
            if (condition != null && !condition.isEmpty()) {
                Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
            }
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (brandNameString != null && !brandNameString.trim().isEmpty()) {
                String[] brands = brandNameString.split(",");
                String urlBrands = currentURL.substring(currentURL.indexOf("enum.brand=")
                        + "enum.brand=".length()).toLowerCase();
                for (String brand : brands) {
                    String formattedBrand = brand.trim().toLowerCase();
                    Assert.assertTrue(urlBrands.contains(formattedBrand));
                }
            }
            if (itemTypeString != null && !itemTypeString.trim().isEmpty()) {
                String[] itemTypes = itemTypeString.split(",");
                String urlItems = currentURL.substring(currentURL.indexOf("enum.item_type=")
                        + "enum.item_type=".length()).toLowerCase();
                for (String item : itemTypes) {
                    String formattedItem = item.trim().toLowerCase();
                    Assert.assertTrue(urlItems.contains(formattedItem));
                }
            }
        }
    }

    @Test(dataProvider = "dataForLandForSalePageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInLandForSalePage(String adType, Double minPrice, Double maxPrice,
                                             Double minLandSize, Double maxLandSize){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        PropertiesPage page = homePage.selectCategory("Property");
        LandForSale landForSale = (LandForSale) page.navigateToCategory("Land For Sale");

        landForSale.scrollPage(0,800);

        // Apply the adType filter
        landForSale.selectAdType(adType);

        // Apply price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            landForSale.setPriceRange(minPrice, maxPrice);
        }

        // Apply price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minLandSize != null || maxLandSize != null)) {
            landForSale.setLandSizeRange(minLandSize, maxLandSize);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();
        System.out.println(currentURL);

        if ("For Sale".equals(adType)) {
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (minLandSize != null) {
                Assert.assertTrue(currentURL.contains("numeric.size.minimum=" + minLandSize.intValue()));
            }
            if (maxLandSize != null) {
                Assert.assertTrue(currentURL.contains("numeric.size.maximum=" + maxLandSize.intValue()));
            }
        }
    }

    @Test(dataProvider = "dataForHouseForSalePageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInHouseForSalePage(String adType, Double minPrice, Double maxPrice,
                                              Double minHouseSize, Double maxHouseSize, String noOfBedrooms, String noOfBathrooms){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        PropertiesPage page = homePage.selectCategory("Property");
        HouseForSale houseForSale = (HouseForSale) page.navigateToCategory("Houses For Sale");

        houseForSale.scrollPage(0,800);

        // Applies the adType filter
        houseForSale.selectAdType(adType);

        // Applies price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            houseForSale.setPriceRange(minPrice, maxPrice);
        }

        // Applies house size filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minHouseSize != null || maxHouseSize != null)) {
            houseForSale.setHouseSizeRange(minHouseSize, maxHouseSize);
        }

        // Selects number of bedrooms
        if (noOfBedrooms != null && !noOfBedrooms.trim().isEmpty()) {
            List<String> noOfBedroomsList = Arrays.asList(noOfBedrooms.split(","));
            houseForSale.selectNumberOfBedrooms(noOfBedroomsList);
        }

        // Selects number of bathrooms
        if (noOfBathrooms != null && !noOfBathrooms.trim().isEmpty()) {
            List<String> noOfBathroomsList = Arrays.asList(noOfBathrooms.split(","));
            houseForSale.selectNumberofBathrooms(noOfBathroomsList);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();

        if ("For Sale".equals(adType)) {
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (minHouseSize != null) {
                Assert.assertTrue(currentURL.contains("numeric.house_size.minimum=" + minHouseSize.intValue()));
            }
            if (maxHouseSize != null) {
                Assert.assertTrue(currentURL.contains("numeric.house_size.maximum=" + maxHouseSize.intValue()));
            }
            if (noOfBedrooms != null && !noOfBedrooms.isEmpty()) {
                // Sorts the string in ascending order for assertion.
                String[] bedroomArray = noOfBedrooms.split(",");
                Arrays.sort(bedroomArray);
                String sortedBedrooms = String.join(",", bedroomArray);
                Assert.assertTrue(currentURL.contains("enum.bedrooms=" + sortedBedrooms));
            }
            if (noOfBathrooms != null && !noOfBathrooms.isEmpty()) {
                // Sorts the string in ascending order for assertion.
                String[] bathroomArray = noOfBathrooms.split(",");
                Arrays.sort(bathroomArray, Comparator.comparingInt(Integer::parseInt));
                String sortedBedrooms = String.join(",", bathroomArray);
                Assert.assertTrue(currentURL.contains("enum.bathrooms=" + sortedBedrooms));
            }
        }

    }

    @Test(dataProvider = "dataForAutoPartsPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInAutoPartsPage(String adType, String condition, Double minPrice, Double maxPrice, String partType,
                                           String brandName, String modelName){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        VehiclePage page = homePage.selectCategory("Vehicles");
        AutoPartsAndAccessories autoPartsAndAccessories = (AutoPartsAndAccessories) page.navigateToCategory("Auto Parts & Accessories");

        autoPartsAndAccessories.scrollPage(0,800);

        // Applies the adType filter
        autoPartsAndAccessories.selectAdType(adType);

        // Applies price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            autoPartsAndAccessories.setPriceRange(minPrice, maxPrice);
        }

        // Apply Condition filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
            autoPartsAndAccessories.selectConditionForVehicle(condition);
        }

        // Selects part types if the parts list is not empty
        if (partType != null && !partType.trim().isEmpty()) {
            List<String> partTypes = Arrays.asList(partType.split(","));
            autoPartsAndAccessories.selectTypesOfAccessories(partTypes);
        }

        // Apply Brand and Model if specified
        if (brandName != null && !brandName.isEmpty()) {
            autoPartsAndAccessories.selectBrandAndModel(brandName, modelName);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();

        if ("For Sale".equals(adType)) {
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (condition != null && !condition.isEmpty()) {
                Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
            }
            if (partType != null && !partType.trim().isEmpty()) {
                String[] parts = partType.split(",");
                String urlParts = currentURL.substring(currentURL.indexOf("enum.item_type=")
                        + "enum.item_type=".length()).toLowerCase();
                for (String part : parts) {
                    String formattedPart = part.trim().toLowerCase().replace("&", "").replaceAll("\\s+", "_");
                    Assert.assertTrue(urlParts.contains(formattedPart));
                }
            }
            if (brandName != null && !brandName.isEmpty()) {
                if (modelName != null && !modelName.isEmpty()) {
                    // If both brand and model are provided
                    Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase() + "_" +  brandName.toLowerCase()+ "-" + modelName.toLowerCase().replace(" ", "")));
                } else {
                    // If only brand is provided
                    Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase()));
                }
            }
        }
    }
    
    @Test(dataProvider = "dataForMotorBikesPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInMotorBikesPage(String adType, String bikeType, String condition, Double minPrice, Double maxPrice,
                                            String brandName, String modelName, Double minYearOfManufacture, Double maxYearOfManufacture,
                                            Double minMileage, Double maxMileage){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        VehiclePage page = homePage.selectCategory("Vehicles");
        MotorBikes motorBikes = (MotorBikes) page.navigateToCategory("Motorbikes");

        motorBikes.scrollPage(0,800);

        // Applies the adType filter
        motorBikes.selectAdType(adType);

        // Applies price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            motorBikes.setPriceRange(minPrice, maxPrice);
        }

        // Apply Condition filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
            motorBikes.selectConditionForVehicle(condition);
        }

        // Selects bike types if the types of bike list is not empty
        if (bikeType != null && !bikeType.trim().isEmpty()) {
            List<String> bikeTypes = Arrays.asList(bikeType.split(","));
            motorBikes.selectTypesOfBikes(bikeTypes);
        }

        // Applies year of manufacture filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minYearOfManufacture != null || maxYearOfManufacture != null)) {
            motorBikes.setYearOfManufactureRange(minYearOfManufacture, maxYearOfManufacture);
        }

        // Applies mileage filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minMileage != null || maxMileage != null)) {
            motorBikes.setMileageRange(minMileage, maxMileage);
        }

        // Apply Brand and Model if specified
        if (brandName != null && !brandName.isEmpty()) {
            motorBikes.selectBrandAndModel(brandName, modelName);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();

        if ("For Sale".equals(adType)) {
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (minYearOfManufacture != null) {
                Assert.assertTrue(currentURL.contains("numeric.model_year.minimum=" + minYearOfManufacture.intValue()));
            }
            if (maxYearOfManufacture != null) {
                Assert.assertTrue(currentURL.contains("numeric.model_year.maximum=" + maxYearOfManufacture.intValue()));
            }
            if (minMileage != null) {
                Assert.assertTrue(currentURL.contains("numeric.mileage.minimum=" + minMileage.intValue()));
            }
            if (maxMileage != null) {
                Assert.assertTrue(currentURL.contains("numeric.mileage.maximum=" + maxMileage.intValue()));
            }
            if (condition != null && !condition.isEmpty()) {
                Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
            }
            if (bikeType != null && !bikeType.trim().isEmpty()) {
                String[] parts = bikeType.split(",");
                String urlParts = currentURL.substring(currentURL.indexOf("enum.item_type=")
                        + "enum.item_type=".length()).toLowerCase();
                for (String part : parts) {
                    String formattedPart = part.trim().toLowerCase().replace("-", "_");
                    Assert.assertTrue(urlParts.contains(formattedPart));
                }
            }
            if (brandName != null && !brandName.isEmpty()) {
                if (modelName != null && !modelName.isEmpty()) {
                    // If both brand and model are provided
                    Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase() + "_" +  brandName.toLowerCase()+ "-" + modelName.toLowerCase().replace(" ", "-")));
                } else {
                    // If only brand is provided
                    Assert.assertTrue(currentURL.contains("tree.brand=" + brandName.toLowerCase()));
                }
            }
        }
    }

    @Test(dataProvider = "dataForFurniturePageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInFurniturePage(String adType, String condition, Double minPrice, Double maxPrice,
                                           String furnitureTypes, String brands){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        HomeAndGardenPage page = homePage.selectCategory("Home & Garden");
        Furniture furniturePage = (Furniture) page.navigateToCategory("Furniture");

        furniturePage.scrollPage(0,800);

        // Applies the adType filter
        furniturePage.selectAdType(adType);

        // Applies price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            furniturePage.setPriceRange(minPrice, maxPrice);
        }

        // Apply Condition filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
            furniturePage.selectConditionForItem(condition);
        }

        // Selects furniture types if the list is not empty
        if (furnitureTypes != null && !furnitureTypes.trim().isEmpty()) {
            List<String> furniture = Arrays.asList(furnitureTypes.split(","));
            furniturePage.selectTypesOfFurniture(furniture);
        }

        // Selects brands if the brand name list is not empty
        if (brands != null && !brands.trim().isEmpty()) {
            List<String> brandNames = Arrays.asList(brands.split(","));
            furniturePage.selectBrands(brandNames);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();

        if ("For Sale".equals(adType)) {
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (condition != null && !condition.isEmpty()) {
                Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
            }
            Map<String, String> furnitureTypeMapping = getFurnitureTypeMapping();
            if (furnitureTypes != null && !furnitureTypes.trim().isEmpty()) {
                String[] typesOfFurniture = furnitureTypes.split(",");
                String urlFurniture = currentURL.substring(currentURL.indexOf("enum.item_type=")
                        + "enum.item_type=".length()).toLowerCase();
                for (String furniture : typesOfFurniture) {
                    String mappedFurnitureType = furnitureTypeMapping.get(furniture.trim());
                    Assert.assertTrue(urlFurniture.contains(mappedFurnitureType));
                }
            }
            if (brands != null && !brands.trim().isEmpty()) {
                String[] brandTypes = brands.split(",");
                String urlBrands = currentURL.substring(currentURL.indexOf("enum.brand=")
                        + "enum.brand=".length()).toLowerCase();
                for (String brand : brandTypes) {
                    String formattedBrand = brand.trim().toLowerCase().replace("&", "").replaceAll("\\s+", "_");
                    Assert.assertTrue(urlBrands.contains(formattedBrand));
                }
            }
        }
    }

    @Test(dataProvider = "dataForOtherHomeItemsPageFilters", dataProviderClass = TestDataProvider.class)
    public void testFiltersInOtherHomeItemsPage(String adType, String condition, Double minPrice, Double maxPrice){
        BasePage basePage = PageFactory.initElements(browserFactory.getDriver(), BasePage.class);
        IkmanHomePage homePage = basePage.loadURL(URL);
        HomeAndGardenPage page = homePage.selectCategory("Home & Garden");
        OtherHomeItems otherHomeItemsPage = (OtherHomeItems) page.navigateToCategory("Other Home Items");

        otherHomeItemsPage.scrollPage(0,800);

        // Applies the adType filter
        otherHomeItemsPage.selectAdType(adType);

        // Applies price filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && (minPrice != null || maxPrice != null)) {
            otherHomeItemsPage.setPriceRange(minPrice, maxPrice);
        }

        // Apply Condition filter if specified and adType is "For Sale"
        if ("For Sale".equals(adType) && condition != null && !condition.isEmpty()) {
            otherHomeItemsPage.selectConditionForItem(condition);
        }

        String currentURL = browserFactory.getDriver().getCurrentUrl();
        if ("For Sale".equals(adType)) {
            if (minPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.minimum=" + minPrice.intValue()));
            }
            if (maxPrice != null) {
                Assert.assertTrue(currentURL.contains("money.price.maximum=" + maxPrice.intValue()));
            }
            if (condition != null && !condition.isEmpty()) {
                Assert.assertTrue(currentURL.contains("enum.condition=" + condition.toLowerCase()));
            }
        }
    }

    // Maps between data provider values and URL-compatible values for furniture types.
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
