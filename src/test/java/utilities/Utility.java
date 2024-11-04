package utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Utility {
    protected BrowserFactory browserFactory;
    public static String RESOURCE_FILE_PATH = "src/test/java/resources/testData.xlsx";

    @BeforeTest
    public void initializeBrowser() {
        // Retrieves the instance of BrowserFactory to manage the driver.
        browserFactory = BrowserFactory.getBrowserFactory();
        browserFactory.getDriver().manage().window().maximize();
    }

    @AfterTest
    public void closeBrowser() {
        // Quits the browser session.
        browserFactory.getDriver().quit();
    }

    // Reads the data from the Excel sheet.
    public static Object[][] readTestDataFile (int sheetNumber) {
        Object[][] content = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(RESOURCE_FILE_PATH);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            content = new Object[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                String cellValue = cell.getStringCellValue();
                                content[i][j] = "null".equalsIgnoreCase(cellValue) ? null : cellValue;
                                break;
                            case NUMERIC:
                                content[i][j] = cell.getNumericCellValue();
                                break;
                            case BOOLEAN:
                                content[i][j] = cell.getBooleanCellValue();
                                break;
                            default:
                                content[i][j] = null;
                        }
                    } else {
                        content[i][j] = null;
                    }
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return content;
    }
}
