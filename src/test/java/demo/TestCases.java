package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    Wrappers wrapper;

    ChromeDriver driver;

     @Test
    public void testCase01() throws IOException {
        wrapper.navigateToUrl("https://www.scrapethissite.com/pages/");
        wrapper.clickLinkByText("Hockey Teams: Forms, Searching and Pagination");

        List<HashMap<String, Object>> hockeyTeams = new ArrayList<>();
        int page = 1;

        while (page <= 4) {
            List<WebElement> rows = wrapper.findElementsByCssSelector("table tbody tr");
            for (WebElement row : rows) {
                String teamName = wrapper.getTextFromElement(row.findElement(By.xpath("//*[@class='name']")));
                String year = wrapper.getTextFromElement(row.findElement(By.xpath("//*[@class='year']")));
                String winPercentText = wrapper.getTextFromElement(row.findElement(By.xpath("//tr[@class='team']/td[6]")));
                double winPercent = Double.parseDouble(winPercentText.replace("%", "")) / 100.0;

                if (winPercent < 0.40) {
                    HashMap<String, Object> teamData = new HashMap<>();
                    teamData.put("Epoch Time of Scrape", System.currentTimeMillis());
                    teamData.put("Team Name", teamName);
                    teamData.put("Year", year);
                    teamData.put("Win %", winPercent);
                    hockeyTeams.add(teamData);
                }
            }

            wrapper.goToNextPage("//*[@class='pagination']");
            page++;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("hockey-team-data.json"), hockeyTeams);
    }
     @Test
    public void testCase02() throws IOException {
        wrapper.navigateToUrl("https://www.scrapethissite.com/pages/ajax-javascript/"); // Replace with the actual URL
        //wrapper.clickLinkByText("Oscar Winning Films");

        List<WebElement> years = driver.findElements(By.xpath("//a[@class='year-link']")); // Update with the correct XPath
        List<HashMap<String, Object>> dataList = new ArrayList<>();

        for (WebElement yearElement : years) {
            String year = yearElement.getText();
            yearElement.click();

            List<WebElement> movies = driver.findElements(By.xpath("//td[@class='film-title']")); // Update with the correct XPath
            boolean isWinner = false; // Set this based on your logic

            for (int i = 0; i < Math.min(movies.size(), 5); i++) {
                WebElement movieElement = movies.get(i);
                HashMap<String, Object> data = new HashMap<>();
                data.put("Epoch Time of Scrape", Instant.now().getEpochSecond());
                data.put("Year", year);
                data.put("Title", movieElement.findElement(By.xpath(".//td[@class='film-title']")).getText()); // Update XPath
                data.put("Nomination", movieElement.findElement(By.xpath("//td[@class='film-nominations']")).getText()); // Update XPath
                data.put("Awards", movieElement.findElement(By.xpath("//td[@class='film-awards']")).getText()); // Update XPath
                data.put("isWinner", isWinner);
                dataList.add(data);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        // File outputFile = new File("output/oscar-winner-data.json");
        // mapper.writeValue(outputFile, dataList);

        // Assert that the file exists and is not empty
        // Assert.assertTrue(outputFile.exists(), "JSON file does not exist");
        // Assert.assertTrue(outputFile.length() > 0, "JSON file is empty");
    }


    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);
        wrapper = new Wrappers(driver);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}