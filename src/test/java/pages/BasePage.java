package pages;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.io.*;

public class BasePage {
    protected WebDriver driver = null;

    public BasePage (WebDriver driver) {
        this.driver = driver;
    }

    public IkmanHomePage loadURL(String url) {
        driver.get(url);
        return PageFactory.initElements(driver, IkmanHomePage.class);
    }

    public void scrollPage(int x, int y) {
        new Actions(driver).scrollByAmount(x,y).perform();
    }
}
