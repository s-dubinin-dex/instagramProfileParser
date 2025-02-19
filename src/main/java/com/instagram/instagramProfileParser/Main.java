package com.instagram.instagramProfileParser;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.instagram.instagramProfileParser.instagramPages.LoginPage;
import com.instagram.instagramProfileParser.instagramPages.MainPage;
import com.instagram.instagramProfileParser.instagramPages.ProfilePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;

public class Main {
    private static String USER_LOGIN;
    private static String USER_PASSWORD;
    private static String PROFILES_TO_PARSE;
    // TODO: заменить SCROLL_STRATEGY на передачу количества постов.
    // Если all - крутим до конца,
    // если число - крутим, пока selenide не вернёт количество элементов по локатору поста >= кол-ва постов
    private static String SCROLL_MODE;
    private static String OUTPUT_PATH;
    private static String BROWSER_MODE;
    private static String GALLERY_MODE;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info("Starting...");

        processCLIArgs(args);

        setBrowserSettings();

        LoginPage loginPage = open("https://www.instagram.com/", LoginPage.class);
        MainPage mainPage = loginPage.login(USER_LOGIN, USER_PASSWORD);

        String[] profilesToParse = PROFILES_TO_PARSE.split(";");

        for (String profileToParse: profilesToParse){
            ProfilePage profilePage = mainPage.goToProfile(profileToParse);
            profilePage.parseProfile();
        }

        WebDriverRunner.closeWebDriver();
    }

    private static void setUserLogin(String userLogin) {
        USER_LOGIN = userLogin;
    }

    private static void setUserPassword(String userPassword) {
        USER_PASSWORD = userPassword;
    }

    private static void setProfilesToParse(String profilesToParse) {
        PROFILES_TO_PARSE = profilesToParse;
    }

    private static void setScrollStrategy(String scrollMode) {
        SCROLL_MODE = scrollMode;
    }

    private static void setOutputPath(String outputPath) {
        OUTPUT_PATH = outputPath;
    }

    private static void setBrowserMode(String browserMode) {
        BROWSER_MODE = browserMode;
    }

    private static void setGalleryMode(String galleryMode) {
        GALLERY_MODE = galleryMode;
    }

    public static String getOutputPath() {
        return OUTPUT_PATH;
    }

    public static String getScrollMode() {
        return SCROLL_MODE;
    }

    public static String getGalleryMode() {
        return GALLERY_MODE;
    }

    private static void processCLIArgs(String[] args){
        for (String argument: args){

            if (argument.charAt(0) == '-'){
                String parameter = argument.substring(2);

                switch (argument.charAt(1)){
                    case 'L' :
                        setUserLogin(parameter);
                        break;
                    case 'P' :
                        setUserPassword(parameter);
                        break;
                    case 'T' :
                        setProfilesToParse(parameter);
                        break;
                    case 'S' :
                        setScrollStrategy(parameter);
                        break;
                    case 'O' :
                        setOutputPath(parameter);
                        break;
                    case 'M' :
                        setBrowserMode(parameter);
                        break;
                    case 'G' :
                        setGalleryMode(parameter);
                        break;
                }
            } else {
                logger.warn("Cannot read property : {}", argument);
            }

        }
    }

    private static void setBrowserSettings(){
        switch (BROWSER_MODE){
            case "show":
                Configuration.headless = false;
                break;
            case "hide":
                Configuration.headless = true;
                break;
            default:
                Configuration.headless = false;
                break;
        }
    }
}
