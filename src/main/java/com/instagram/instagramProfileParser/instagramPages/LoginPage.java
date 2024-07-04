package com.instagram.instagramProfileParser.instagramPages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage {
    @FindBy(how = How.NAME, using = "username")
    private SelenideElement loginField;

    @FindBy(how = How.NAME, using = "password")
    private SelenideElement passwordField;

    @FindBy(xpath = "//button[div[text()='Войти']]")
    private SelenideElement loginButton;

    public LoginPage setLogin(String login){
        loginField.setValue(login);
        return this;
    }

    public LoginPage setPassword(String password){
        passwordField.setValue(password);
        return this;
    }

    public LoginPage clickLoginButton(){
        loginButton.click();
        return this;
    }

    public void login(String login, String password) throws InterruptedException {
        setLogin(login).setPassword(password).clickLoginButton();
        Thread.sleep(5000);
    }
}
