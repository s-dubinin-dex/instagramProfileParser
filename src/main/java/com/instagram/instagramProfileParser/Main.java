package com.instagram.instagramProfileParser;

import com.instagram.instagramProfileParser.instagramPages.LoginPage;
import com.instagram.instagramProfileParser.instagramPages.ProfilePage;
import com.instagram.instagramProfileParser.instagramPages.ProfilesToScan;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;

public class Main {
    private static String LOGIN;
    private static String PASSWORD;

    public static void main(String[] args) throws InterruptedException, IOException {

        processCLIArgs(args);

        LoginPage loginPage = open("https://www.instagram.com/", LoginPage.class);
        loginPage.login(LOGIN, PASSWORD);

        Thread.sleep(5000);

        for (String profile: ProfilesToScan.getProfiles()){

            ProfilePage profilePage = open("https://www.instagram.com/" + profile, ProfilePage.class);
            profilePage.savePostInfo(profile);

        }

    }

    private static void processCLIArgs(String[] args){
        for (String argument: args){

            String parameter = argument.substring(2);

            switch (argument.charAt(1)){
                case 'L' :
                    setLOGIN(parameter);
                    break;
                case 'P' :
                    setPASSWORD(parameter);
                    break;
            }
        }
    }

    private static void setLOGIN(String LOGIN) {
        Main.LOGIN = LOGIN;
    }

    private static void setPASSWORD(String PASSWORD) {
        Main.PASSWORD = PASSWORD;
    }

}
