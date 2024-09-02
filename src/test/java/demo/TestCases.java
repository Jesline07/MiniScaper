package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.Duration;
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
        // Step 1: Navigate to the website and click on "Hockey Teams: Forms, Searching and Pagination"
        wrapper.navigateToUrl("https://www.scrapethissite.com/pages/");
        wrapper.clickLinkByText("Hockey Teams: Forms, Searching and Pagination");
        System.out.println("Navigated to Hockey Teams page.");
        
        // Step 2: Initialize an ArrayList to store team data
        List<HashMap<String, Object>> hockeyTeams = new ArrayList<>();
        int page = 1;
        
        // Step 3: Iterate through the pages (up to 4 pages)
        while (page <= 4) {
            List<WebElement> rows = wrapper.findElementsByCssSelector("table tbody tr");
            System.out.println("Processing page " + page + " with " + rows.size() + " rows.");
        
            for (WebElement row : rows) {
                String teamName = wrapper.getTextFromElement(row.findElement(By.xpath("//*[@class='name']")));
                String year = wrapper.getTextFromElement(row.findElement(By.xpath("//*[@class='year']")));
                String winPercentText = wrapper.getTextFromElement(row.findElement(By.xpath("//tr[@class='team']/td[6]")));
                double winPercent = Double.parseDouble(winPercentText.replace("%", "")) / 100.0;
        
                // Step 4: Collect data if Win % is less than 40%
                if (winPercent < 0.40) {
                    HashMap<String, Object> teamData = new HashMap<>();
                    teamData.put("Epoch Time of Scrape", System.currentTimeMillis());
                    teamData.put("Team Name", teamName);
                    teamData.put("Year", year);
                    teamData.put("Win %", winPercent);
                    hockeyTeams.add(teamData);
                    System.out.println("Added team: " + teamName + ", Year: " + year + ", Win %: " + winPercent);
                }
            }
        
            // Step 5: Go to the next page
            if (page < 4) {
                wrapper.goToNextPage("//*[@class='pagination']");
                System.out.println("Navigating to page " + (page + 1));
            }
            page++;
        }
    
        // Step 6: Create the output directory if it does not exist
        String userDir = System.getProperty("user.dir");
        String outputPath = userDir + "\\output";
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
            System.out.println("Output directory created at: " + outputDir.getAbsolutePath());
        }
    
        // Step 7: Convert the ArrayList to a JSON file using Jackson library
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File(outputPath + "\\hockey-team-data.json"), hockeyTeams);
            System.out.println("JSON file successfully created at: " + outputPath + "\\hockey-team-data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Step 8: Assert that the JSON file exists and is not empty
        File outputFile = new File(outputPath + "\\hockey-team-data.json");
        Assert.assertTrue(outputFile.exists(), "JSON file does not exist");
        Assert.assertTrue(outputFile.length() > 0, "JSON file is empty");
        System.out.println("Test case passed. JSON file successfully created.");
    }
    
    @Test
    public void testCase02() {
        // Step 1: Navigate to the website and click on "Oscar Winning Films"
        wrapper.navigateToUrl("https://www.scrapethissite.com/pages/");
        wrapper.clickLinkByText("Oscar Winning Films: AJAX and Javascript");
        System.out.println("Navigated to Oscar Winning Films page.");
    
        // Step 2: Initialize an ArrayList to store movie data
        List<HashMap<String, Object>> oscarWinners = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    
        // Step 3: Iterate through each year and collect the top 5 movies
        List<WebElement> years = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//a[@class='year-link']")));
    
        for (WebElement yearElement : years) {
            String yearText = wrapper.getTextFromElement(yearElement);
            int year = Integer.parseInt(yearText.trim());
            wrapper.clickElement(yearElement);
            System.out.println("Processing year: " + year);
    
            List<WebElement> movies = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//td[@class='film-title']")));
    
            for (int i = 0; i < Math.min(movies.size(), 5); i++) {
                WebElement movieElement = movies.get(i);
                String title = wrapper.getTextFromElement(movieElement.findElement(By.xpath("//td[@class='film-title']")));
                String nominations = wrapper.getTextFromElement(movieElement.findElement((By.xpath("//td[@class='film-nominations']"))));
                String awards = wrapper.getTextFromElement(movieElement.findElement(By.xpath("//td[@class='film-awards']")));
                boolean isWinner = movieElement.findElement(By.xpath("//i[@class='glyphicon glyphicon-flag']")).isDisplayed();
    
                // Step 4: Create a HashMap to store movie data
                HashMap<String, Object> movieData = new HashMap<>();
                movieData.put("Epoch Time of Scrape", System.currentTimeMillis());
                movieData.put("Year", year);
                movieData.put("Title", title);
                movieData.put("Nomination", nominations);
                movieData.put("Awards", awards);
                movieData.put("isWinner", isWinner);
    
                oscarWinners.add(movieData);
                System.out.println("Added movie: " + title + " from year: " + year + " (isWinner: " + isWinner + ")");
            }
        }
    
        // Step 5: Create the output directory if it does not exist
        String userDir = System.getProperty("user.dir");
        String outputPath = userDir + "\\output";
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
            System.out.println("Output directory created at: " + outputDir.getAbsolutePath());
        }
    
        // Step 6: Convert the ArrayList to a JSON file using Jackson library
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File(outputPath + "\\oscar-winner-data.json"), oscarWinners);
            System.out.println("JSON file successfully created at: " + outputPath + "\\oscar-winner-data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Step 7: Assert that the JSON file exists and is not empty
        File outputFile = new File(outputPath + "\\oscar-winner-data.json");
        Assert.assertTrue(outputFile.exists(), "JSON file does not exist");
        Assert.assertTrue(outputFile.length() > 0, "JSON file is empty");
        System.out.println("Test case passed. JSON file successfully created.");
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