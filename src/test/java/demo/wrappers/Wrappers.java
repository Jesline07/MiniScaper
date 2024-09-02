package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Wrappers {
    private WebDriver driver;
    private WebDriverWait wait;

    public Wrappers(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 
    }

    // Navigate to a URL
    public void navigateToUrl(String url) {
        driver.get(url);
    }

    // Click a link by link text
    public void clickLinkByText(String linkText) {
        WebElement link = driver.findElement(By.linkText(linkText));
        link.click();
    }

    // Click a button or link by its CSS selector
    public void clickByCssSelector(String cssSelector) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        element.click();
    }

    // Click any element passed as a parameter
    public void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    // Find element by CSS selector
    public WebElement findElementByCssSelector(String cssSelector) {
        return driver.findElement(By.cssSelector(cssSelector));
    }

    // Find elements by CSS selector
    public List<WebElement> findElementsByCssSelector(String cssSelector) {
        return driver.findElements(By.cssSelector(cssSelector));
    }

    // Extract text from an element
    public String getTextFromElement(WebElement element) {
        return element.getText();
    }

    // Handle pagination
    public void goToNextPage(String nextPageXPath) {
        WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(nextPageXPath)));
        nextPageButton.click();
    }
}
