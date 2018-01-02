package com.willowtreeapps;

import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created on 5/23/17.
 */
public class HomePage extends BasePage {


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void validateTitleIsPresent() {
        WebElement title = driver.findElement(By.cssSelector("h1"));
        Assert.assertTrue(title != null);
    }


    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        //Wait for page to load
        sleep(6000);

        int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        driver.findElement(By.className("photo")).click();

        sleep(6000);

        int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        Assert.assertTrue(countAfter > count);

    }


    /* Verify the "streak" counter is incrementing on correct selections */
    public void validateCorrectSelectionIncrementsStreakCounter() {

        // Wait for page to load and verify page has loaded
        sleep(6000);
        validateAllPresent(By.className("photo"));

        // Get the streak counter amount before clicking photo
        int streakBefore = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        // Colleague's name game wants us to match
        String name = driver.findElement(By.id("name")).getText();

        // Find the correct photo that matches colleague's name and click - defined in BasePage.java
        clickPhoto(name);

        sleep(6000);

        // Get the streak counter amount after clicking photo
        int streakAfter = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        // Check that streak counter increased
        Assert.assertTrue(streakAfter > streakBefore);
    }


    /* Verify the multiple “streak” counter resets after getting an incorrect answer*/
    public void validateIncorrectSelectionResetsStreakCounter() {

        // Wait for page to load and verify page has loaded
        sleep(6000);
        validateAllPresent(By.className("photo"));

        // Colleague's name game wants us to match
        String name = driver.findElement(By.id("name")).getText();

        // Find the correct photo that matches colleague's name and click - defined in BasePage.java
        clickPhoto(name);

        sleep(6000);
        validateAllPresent(By.className("photo"));

        // Get streak counter amount before making an incorrect selection
        int streakBefore = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        // Click the first photo, BUT only if it is an incorrect match
        if(!getText(By.className("name")).equals(name)) {
            driver.findElements(By.className("photo")).get(0).click();
        }
        else {
            driver.findElements(By.className("photo")).get(1).click();
        }

        sleep(6000);

        // Get streak counter amount after making incorrect selection
        int streakAfter = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        // Check that streak counter reset
        Assert.assertTrue(streakAfter < streakBefore);
    }


    /*  Verify that after 10 random selections the correct counters are being incremented for
        tries and correct counters */
    public void validateMultipleSelectionsIncreaseCounters() {

        // Wait for page to load and verify that page has loaded
        sleep(6000);
        validateAllPresent(By.className("photo"));

        // To keep track of how many attempts and correct selections are made
        int attemptCounter = 0;
        int correctCounter = 0;

        // Keep selecting photos while 10 selections have not been made
        while(attemptCounter < 10) {

            // Current photos being displayed on webpage
            List<WebElement> photos = driver.findElements(By.className("photo"));

            // Keep selecting photos while there are photos left on the page to be selected,
            // and 10 selections overall have not been made
            for(int i = 0; i < photos.size() && attemptCounter < 10; i++) {

                photos.get(i).click();
                String classOfPhoto = photos.get(i).getAttribute("class");

                // If the photo selected is correct, wait for page to reload, increment necessary counters, and move on
                if(classOfPhoto.equals("photo correct")) {
                    sleep(6000);
                    validateAllPresent(By.className("photo"));
                    attemptCounter += 1;
                    correctCounter += 1;
                    break;
                }
                // Otherwise, just increment the attempts counter and move on
                else {
                    attemptCounter += 1;
                    sleep(1000);
                }
            }
        }

        // Get final amounts of attempts and correct selections counters
        int finalAttempts = Integer.parseInt(driver.findElement(By.className("attempts")).getText());
        int finalCorrect = Integer.parseInt(driver.findElement(By.className("correct")).getText());


        // Check that counters are correct amount
        Assert.assertTrue(finalAttempts == 10);
        Assert.assertTrue(finalCorrect == correctCounter);
    }


    /* Verify name and displayed photos change after selecting the correct answer */
    public void validateSelectionChangesNameAndPhotos() {

        // Wait for page to load and verify page has loaded
        sleep(6000);
        validateAllPresent(By.className("photo"));

        // Colleague's name game wants us to match
        String nameBefore = driver.findElement(By.id("name")).getText();

        // All photo URLs before selection is made
        List<WebElement> photosBefore = driver.findElements(By.cssSelector("img"));
        String[] urlsBefore = new String[photosBefore.size()];
        for(int i = 0; i < photosBefore.size(); i++) {
            urlsBefore[i] = photosBefore.get(i).getAttribute("src");
        }

        // Find the correct photo that matches colleague's name and click
        clickPhoto(nameBefore);

        sleep(6000);
        validateAllPresent(By.className("photo"));

        // New colleague's name game loads after selection
        String nameAfter = driver.findElement(By.id("name")).getText();

        // All new photo URLs after selection is made
        List<WebElement> photosAfter = driver.findElements(By.cssSelector("img"));
        String[] urlsAfter = new String[photosAfter.size()];
        for(int i = 0; i < photosAfter.size(); i++) {
            urlsAfter[i] = photosAfter.get(i).getAttribute("src");
        }

        // Check that all photos have changed
        for(int i = 0; i < urlsBefore.length; i++) {
            Assert.assertTrue(!urlsBefore[i].equals(urlsAfter[i]));
        }

        // Check that name has changed
        Assert.assertTrue(!nameBefore.equals(nameAfter));
    }


    /*  Bonus - Write a test to verify that failing to select one person’s name correctly makes
        that person appear more frequently than other “correctly selected” people.
        NOTE: This test will run for a long period of time with the given data set. It would be more manageable
        with a smaller set of photos to test against.*/
    public void validateProbabilityOfNameIncreasesWithIncorrectGuesses() {

        // Wait for page to load and verify page has loaded
        sleep(6000);
        validateAllPresent(By.className("photo"));

        // To keep track of how often the correctly selected name and incorrectly selected name occur during the game
        int correctNameCounter = 0;
        int incorrectNameCounter = 0;

        // Colleague name game wants us to match that will be selected correctly
        String correctName = driver.findElement(By.id("name")).getText();

        // Find the correct photo that matches colleague's name and click
        clickPhoto(correctName);

        // Wait for new photos to load and verify
        sleep(6000);
        validateAllPresent(By.className("photo"));

        // New colleague name game displays that will be selected incorrectly
        String incorrectName = driver.findElement(By.id("name")).getText();

        // Click the first photo, BUT only if it is an incorrect match
        if(!getText(By.className("name")).equals(incorrectName)) {
            driver.findElements(By.className("photo")).get(0).click();
        }
        else {
            driver.findElements(By.className("photo")).get(1).click();
        }

        // Now click the right photo match
        clickPhoto(incorrectName);

        // Wait for new photos to load and verify
        sleep(6000);
        validateAllPresent(By.className("photo"));

        while(incorrectNameCounter < 10) {

            // Current name game displays at top of page, for user to match
            String currentName = driver.findElement(By.id("name")).getText();

            // if the current name displayed is the name we originally guessed incorrectly,
            // guess incorrectly again and increment necessary counter
            if(currentName.equals(incorrectName))  {
                incorrectNameCounter += 1;

                // Click the first photo, BUT only if it is an incorrect match
                if(!getText(By.className("name")).equals(currentName)) {
                    driver.findElements(By.className("photo")).get(0).click();
                }
                else {
                    driver.findElements(By.className("photo")).get(1).click();
                }

                // then, click the correct photo and wait for page to reload
                clickPhoto(currentName);
                sleep(6000);
                validateAllPresent(By.className("photo"));
            }
            // else, if the current name displayed is the name we originally guessed correctly,
            // guess correctly again and incremented necessary counter
            else if(currentName.equals(correctName)) {
                correctNameCounter += 1;
                clickPhoto(currentName);
                sleep(6000);
                validateAllPresent(By.className("photo"));
            }
            // else, play game as intended
            else{
                clickPhoto(currentName);
                sleep(6000);
                validateAllPresent(By.className("photo"));
            }
        }

        // Check that the name guessed incorrectly appeared more frequently than the name guessed correctly
        Assert.assertTrue(incorrectNameCounter > correctNameCounter);
    }
}
