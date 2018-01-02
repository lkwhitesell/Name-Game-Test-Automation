package com.willowtreeapps;

import org.assertj.swing.assertions.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 5/23/17.
 */
public class BasePage {

    public WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public BasePage validateAttribute(String css, String attr, String regex) {
        return validateAttribute(By.cssSelector(css), attr, regex);
    }

    public BasePage validateAttribute(By by, String attr, String regex) {
        return validateAttribute(driver.findElement(by), attr, regex);
    }

    public BasePage validateAttribute(WebElement element, String attr, String regex) {
        String actual = null;
        try {
            actual = element.getAttribute(attr);
            if (actual.equals(regex)) {
                return this; // test passes
            }
        } catch (Exception e) {
            Assertions.fail(String.format("Attribute not fount! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s] [Message: %s]",
                    attr,
                    regex,
                    actual,
                    element.toString(),
                    e.getMessage()), e);
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(actual);

        Assertions.assertThat(m.find())
                .withFailMessage("Attribute doesn't match! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s]",
                        attr,
                        regex,
                        actual,
                        element.toString())
                .isTrue();
        return this;
    }

    public BasePage validateText(String css, String text) {
        return validateText(By.cssSelector(css), text);
    }

    /**
     * Validate Text ignores white spaces
     */
    public BasePage validateText(By by, String text) {
        Assertions.assertThat(text).isEqualToIgnoringWhitespace(getText(by));
        return this;
    }

    public String getText(By by) {
        WebElement e = driver.findElement(by);
        return e.getTagName().equalsIgnoreCase("input")
                || e.getTagName().equalsIgnoreCase("select")
                || e.getTagName().equalsIgnoreCase("textarea")
                ? e.getAttribute("value")
                : e.getText();
    }

    public BasePage validatePresent(String css) {
        return validatePresent(By.cssSelector(css));
    }

    public BasePage validatePresent(By by) {
        Assertions.assertThat(driver.findElements(by).size())
                .withFailMessage("Element not present: [Element: %s]", by.toString())
                .isGreaterThan(0);
        return this;
    }

    public BasePage validateAllPresent(By by) {
        Assertions.assertThat(driver.findElements(by).size())
                .withFailMessage("All elements not present: [Element: %s]", by.toString())
                .isEqualTo(5);
        return this;
    }

    public void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickPhoto(String photoName) {

        // Get the list of names of the photos displayed on the page
        List<WebElement> photoNames = driver.findElements(By.className("name"));

        // Find the correct photo that matches colleague's name and click
        for(int i = 0; i < photoNames.size(); i++) {
            if(photoNames.get(i).getText().equals(photoName)) {
                driver.findElements(By.className("photo")).get(i).click();
            }
        }
    }

}
