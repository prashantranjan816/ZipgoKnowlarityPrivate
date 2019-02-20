package in.zipgo.ZipgoKnowlarityData;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {

   
	protected WebDriver driver;


    public void selectItemByValue(WebElement element, String itemToSelect) {
        getSelect(element).selectByValue(itemToSelect);
    }

    public void selectItemByText(WebElement element, String text) {
        getSelect(element).selectByVisibleText(text);
    }

    public void selectItemByIndex(WebElement element, int index) {
        getSelect(element).selectByIndex(index);
    }

    public void clickButton(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    public boolean hasNoElementAsExpected(By by) {
        WebElement element;
        element = new WebDriverWait(driver, 5).until(ExpectedConditions
                .presenceOfElementLocated(by));
        return element == null || !element.isDisplayed();
    }

    public String getRandomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String getRandomNumeric() {
        return RandomStringUtils.randomNumeric(3);
    }

    public JavascriptExecutor getJavaScriptExecutor() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js;
    }

    public void clickElementWithJavascript(WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click()", element);
        }
    }

    public void handledSleep(int sleepInSeconds) {
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.SECOND, sleepInSeconds);
        while (cal1.after(cal)) {
            cal = Calendar.getInstance();
        }
    }

    public void waitTillMultipleTabOpens() {
        Set<String> allWindows = driver.getWindowHandles();
        while (allWindows.size() == 1) {
            allWindows = driver.getWindowHandles();
        }
    }

    public boolean verifyElementSelected(WebElement element, boolean selected) {
        return (new WebDriverWait(driver, 5)).until(ExpectedConditions
                .elementSelectionStateToBe(element, selected));
    }


    public void switchToFirstTab() {
        Set<String> handles = driver.getWindowHandles();
        for (int i = handles.size(); i > 1; i--) {
            driver.switchTo().window(
                    handles.toArray(new String[handles.size()])[i - 1]);
            driver.close();
        }
        driver.switchTo().window(
                handles.toArray(new String[handles.size()])[0]);
    }


    public void selectRadioButtonByValue(By radioGroup, String ValueToSelect) {
        // Find the radio group element
        List<WebElement> radioLabels = driver.findElements(
                radioGroup);
        for (int i = 0; i < radioLabels.size(); i++) {
            if (radioLabels.get(i).getText().trim()
                    .equalsIgnoreCase(ValueToSelect.trim())) {
                radioLabels.get(i).click();
                break;
            }
        }
    }

    public Select getSelect(WebElement element) {
        Select select = new Select(element);
        return select;
    }

    public void sendValuesToWebElement(WebElement element, String value) {
        element.sendKeys(value);
    }

    public boolean isDisabled(WebElement element) {
        return element.getAttribute("disabled").equalsIgnoreCase("disabled");
    }

    public boolean isEnabled(WebElement element) {
        return element.getAttribute("enabled").equalsIgnoreCase("enabled");
    }
    
    public void implictwait(int i){
   	 driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);
		 
   }
    
   
   public void actionenter(){
   	Actions a=new Actions(driver);
   	a.sendKeys(Keys.ENTER).build().perform();
   }
   public void actiondown() {
	   Actions b=new Actions(driver);
       b.sendKeys(Keys.DOWN).build().perform();
   }
   
   public void scrollup(){
   	JavascriptExecutor js = ((JavascriptExecutor) driver);
   	js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
   }
  
   public void alertaccept(){
   	driver.switchTo().alert().accept();
   	
   }
//   Customize sendkeys method 
   public static void sendkey(WebDriver driver, WebElement element,int timeout,String value) {
	   new WebDriverWait(driver,timeout).until(ExpectedConditions.visibilityOf(element));
	   element.sendKeys(value);
   }
   
//   Customize Click method
   public static void clickOn(WebDriver driver, WebElement element,int timeout) {
	   new WebDriverWait(driver,timeout).until(ExpectedConditions.elementToBeClickable(element));
	   element.click();
   }
   
}
