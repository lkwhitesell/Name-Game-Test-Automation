package com.willowtreeapps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebTest {

    private WebDriver driver;

    /**
     * Change the prop if you are on Windows or Linux to the corresponding file type
     * The chrome WebDrivers are included on the root of this project, to get the
     * latest versions go to https://sites.google.com/a/chromium.org/chromedriver/downloads
     */
    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        Capabilities capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        driver.navigate().to("http://www.ericrochester.com/name-game/");
    }

    @Test
    public void test_validate_title_is_present() {
        new HomePage(driver)
                .validateTitleIsPresent();
    }

    @Test
    public void test_clicking_photo_increases_tries_counter() {
        new HomePage(driver)
                .validateClickingFirstPhotoIncreasesTriesCounter();
    }

    @Test
    public void test_correct_selection_increases_streak_counter() {
        new HomePage(driver)
                .validateCorrectSelectionIncrementsStreakCounter();
    }

    @Test
    public void test_incorrect_selection_resets_streak_counter() {
        new HomePage(driver)
                .validateIncorrectSelectionResetsStreakCounter();
    }

    @Test
    public void test_multiple_selections_increase_counters() {
        new HomePage(driver)
                .validateMultipleSelectionsIncreaseCounters();
    }

    @Test
    public void test_selection_changes_name_and_photos() {
        new HomePage(driver)
                .validateSelectionChangesNameAndPhotos();
    }

    @Test
    public void test_probability_increases_with_incorrect_guesses() {
        new HomePage(driver)
                .validateProbabilityOfNameIncreasesWithIncorrectGuesses();
    }

    @After
    public void teardown() {
        driver.quit();
        System.clearProperty("webdriver.chrome.driver");
    }

}
