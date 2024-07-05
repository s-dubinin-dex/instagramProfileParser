package com.instagram.instagramProfileParser.instagramPages;

import static com.codeborne.selenide.Selenide.open;

public class MainPage {
    public ProfilePage goToProfile(String profileToParse){
        ProfilePage profilePage = open("https://www.instagram.com/" + profileToParse, ProfilePage.class);
        profilePage.setProfileToParse(profileToParse);
        return profilePage;
    }
}
