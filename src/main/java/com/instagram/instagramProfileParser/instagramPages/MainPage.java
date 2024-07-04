package com.instagram.instagramProfileParser.instagramPages;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

public class MainPage {
    public ProfilePage goToProfile(String link){
        open(link);
        return page(ProfilePage.class);
    }
}
