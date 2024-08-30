package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Wrappers {
    private WebDriver driver;

    public Wrappers(WebDriver driver) {
        this.driver = driver;
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
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   
        WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(nextPageXPath)));
        nextPageButton.click();
   
}

}
