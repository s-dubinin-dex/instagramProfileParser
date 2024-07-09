package com.instagram.instagramProfileParser.instagramPages;

import com.codeborne.selenide.SelenideElement;
import com.instagram.instagramProfileParser.Timer;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.page;

public class LoginPage {
    @FindBy(how = How.NAME, using = "username")
    private SelenideElement loginField;

    @FindBy(how = How.NAME, using = "password")
    private SelenideElement passwordField;

    @FindBy(xpath = "//button[div[text()='Войти']]")
    private SelenideElement loginButtonRu;

    @FindBy(xpath = "//button[div[text()='Log in']]")
    private SelenideElement loginButtonEn;

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage setLogin(String login){
        loginField.setValue(login);
        return this;
    }

    public LoginPage setPassword(String password){
        passwordField.setValue(password);
        return this;
    }

    public LoginPage clickLoginButton(){
        if (loginButtonRu.exists()){
            loginButtonRu.click();
        } else if(loginButtonEn.exists()){
            loginButtonEn.click();
        } else {
            logger.error("Unable to find \"Log in\" button");
        }
        return this;
    }

    public MainPage login(String login, String password) throws InterruptedException {
        LoginPage loginPage = setLogin(login).setPassword(password).clickLoginButton();
        Thread.sleep(Timer.getRandomTimeForLogIn());
        return page(MainPage.class);
    }
}
