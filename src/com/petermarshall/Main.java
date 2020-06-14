package com.petermarshall;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class Main {
    final private static String BET_365_LINK = "https://www.bet365.com/#/AC/B1/C1/D13/F2/J99/Q1/F^24/";
    final private static String BET_365_LINK_ALL_LEAGUES = "https://www.bet365.com/#/AC/B1/C1/D13/E0/F2/J0/Q1/F^24/";


    public static void main(String[] args) {
        System.out.println(placeBet("Spain Segunda", "Deportivo La Coruna", "Sporting Gijon",
                Winner.HOME.getSetting(), 0.1, "2.60"));
    }

    public static boolean placeBet(String leagueName, String homeTeam, String awayTeam, int result, double amount, String expectedOdds) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 20);
        try {
            driver.get(BET_365_LINK_ALL_LEAGUES);
            wait.until(presenceOfElementLocated(By.cssSelector(".suf-CompetitionMarketGroup")));
            //login
            WebElement loginBtn = wait.until(presenceOfElementLocated(By.cssSelector(".hm-MainHeaderRHSLoggedOutWide_Login")));
            loginBtn.click();
            driver.findElement(By.cssSelector(".lms-StandardLogin_Username")).sendKeys(PrivateKeys.USER);
            driver.findElement(By.cssSelector(".lms-StandardLogin_Password")).sendKeys(PrivateKeys.PASSWORD + Keys.ENTER);
            //wait for market info, then clicking on odds
            Thread.sleep(5000);
            wait.until(presenceOfElementLocated(By.cssSelector(".suf-CompetitionMarketGroup")));
            ArrayList<WebElement> leagues = (ArrayList<WebElement>) driver.findElements(By.cssSelector(".suf-CompetitionMarketGroup"));
            leagueLoop:
            for (WebElement league: leagues) {
                String bet365LeagueName = league.findElement(By.cssSelector(".suf-CompetitionMarketGroupButton_Text")).getText();
                if (leagueName.equals(bet365LeagueName)) {
                    ArrayList<WebElement> games = (ArrayList<WebElement>) league.findElements(By.cssSelector("div[class$='ParticipantFixtureDetails_TeamNames']"));
                    if (games.size() == 0) {
                        league.click();
                        wait.until(presenceOfElementLocated(By.cssSelector(".suf-CompetitionMarketGroup_Wrapper")));
                        games = (ArrayList<WebElement>) league.findElements(By.cssSelector("div[class$='ParticipantFixtureDetails_TeamNames']"));
                    }
                    ArrayList<WebElement> odds = (ArrayList<WebElement>) league.findElements(By.cssSelector(".suf-MarketOddsExpand"));
                    for (int i = 0; i<games.size(); i++) {
                        WebElement game = games.get(i);
                        ArrayList<WebElement> teamNames = (ArrayList<WebElement>) game.findElements(By.cssSelector("div[class$='ParticipantFixtureDetails_Team']"));
                        if (teamNames.size() == 0) {
                            teamNames = (ArrayList<WebElement>) game.findElements(By.cssSelector("div[class$='ParticipantFixtureDetails_Team ']"));
                        }
                        if (teamNames.get(0).getText().equals(homeTeam) && teamNames.get(1).getText().equals(awayTeam)) {
                            WebElement oddsColumn = odds.get(result);
                            WebElement oddsVal = oddsColumn.findElements(By.cssSelector("span[class*='ParticipantOddsOnly80_Odds']")).get(i);
                            if (oddsVal.getText().equals(expectedOdds+"")) {
                                oddsVal.click();
                                break leagueLoop;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            };
            //filling out bet form and placing bet
            wait.until(presenceOfElementLocated(By.cssSelector(".bss-StakeBox_StakeValueInput"))).sendKeys(amount+"");
            driver.findElement(By.cssSelector(".bss-PlaceBetButton")).click();
            wait.until(presenceOfElementLocated(By.cssSelector(".bs-ReceiptContent_Done"))).click();

            //logging out
            driver.findElement(By.cssSelector(".hm-MainHeaderMembersWide_MembersMenuIcon")).click();
            wait.until(presenceOfElementLocated(By.cssSelector(".um-MainMenu")));
            ArrayList<WebElement> menuLinks = (ArrayList<WebElement>) driver.findElements(By.cssSelector(".um-MembersLinkRow"));
            menuLinks.get(menuLinks.size()-1).click();

            driver.quit();
            return true;
        } catch(Exception e) {
            System.out.println(e);
            driver.quit();
            return false;
        }
    }
}
