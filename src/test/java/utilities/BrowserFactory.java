package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {
    static BrowserFactory browserFactory;
    ThreadLocal<WebDriver> threadLocal = ThreadLocal.withInitial(()->{
        WebDriver driver = null;
        String browserType = System.getProperty("browser", "chrome");
        switch(browserType){
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
//                chromeOptions.addArguments("--headless");
                driver = WebDriverManager.chromedriver().capabilities(chromeOptions).create();
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                driver  = WebDriverManager.firefoxdriver().capabilities(firefoxOptions).create();
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless");
                driver = WebDriverManager.edgedriver().capabilities(edgeOptions).create();
                break;
            default: new RuntimeException("The browser is not defined");
        }
        driver.manage().window().maximize();
        return driver;
    });

    private BrowserFactory(){
    }

    public static BrowserFactory getBrowserFactory(){
        if(browserFactory == null){
            browserFactory = new BrowserFactory();
        }
        return browserFactory;
    }

    public WebDriver getDriver(){
        return threadLocal.get();
    }
}
