package test;

import org.testng.annotations.DataProvider;
import utilities.Utility;

import java.util.Arrays;

public class TestDataProvider extends Utility {
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

    @DataProvider
    public static Object[][] dataForMobilePhonesPageFilters() {
        return Utility.readTestDataFile(3);
    }

    @DataProvider
    public static Object[][] dataForComputerAccessoriesPageFilters() { return Utility.readTestDataFile(4);}

    @DataProvider
    public static Object[][] dataForLandForSalePageFilters() { return Utility.readTestDataFile(5);}

    @DataProvider
    public static Object[][] dataForHouseForSalePageFilters() {
        Object[][] rawData = Utility.readTestDataFile(6);
        Object[][] processedData = new Object[rawData.length][7];
        for (int i = 0; i < rawData.length; i++) {
            processedData[i][0] = rawData[i][0];
            processedData[i][1] = rawData[i][1];
            processedData[i][2] = rawData[i][2];
            processedData[i][3] = rawData[i][3];
            processedData[i][4] = rawData[i][4];
            // Converts the number of Bedrooms to String
            processedData[i][5] = rawData[i][5] == null ? null : convertToString(rawData[i][5]);
            // Converts the number of Bathrooms to String
            processedData[i][6] = rawData[i][6] == null ? null : convertToString(rawData[i][6]);
        }
        return processedData;
    }

    @DataProvider
    public static Object[][] dataForAutoPartsPageFilters() { return Utility.readTestDataFile(7);}

    @DataProvider
    public static Object[][] dataForMotorBikesPageFilters() { return Utility.readTestDataFile(8);}

    @DataProvider
    public static Object[][] dataForFurniturePageFilters() { return Utility.readTestDataFile(9);}

    @DataProvider
    public static Object[][] dataForOtherHomeItemsPageFilters() {
        Object[][] data = Utility.readTestDataFile(9);
        Object[][] filteredData = new Object[data.length][4];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, filteredData[i], 0, 4);
        }
        return filteredData;
    }

    private static String convertToString(Object value) {
        if (value instanceof Double) {
            return String.valueOf(((Double) value).intValue());
        }
        return (String) value;
    }

}
