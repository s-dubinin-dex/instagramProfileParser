package com.instagram.instagramProfileParser.instagramPages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.instagram.instagramProfileParser.Main;
import com.instagram.instagramProfileParser.fileSystem.FileSystem;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    private static final String PROFILE_TO_PARSE = Main.getProfileToParse();
    private static final String BASE_PATH_TO_FOLDER_RESULTS = Main.getOutputPath();
    private static final String PROFILE_PATH = BASE_PATH_TO_FOLDER_RESULTS + "\\" + PROFILE_TO_PARSE;
    private static final String SCROLL_STRATEGY = Main.getScrollStrategy();

    private static final String[] PROFILE_DATA_LOCATORS = new String[]{
            ".x1lliihq.x1plvlek.xryxfnj.x1n2onr6.x193iq5w.xeuugli.x1fj9vlw.x13faqbe.x1vvkbs.x1s928wv.xhkezso.x1gmr53x.x1cpjm7i.x1fgarty.x1943h6x.x1i0vuye.xvs91rp.x1s688f.x5n08af.x10wh9bi.x1wdrske.x8viiok.x18hxmgj",
            "div._ap3a._aaco._aacu._aacy._aad6._aade",
            "span._ap3a._aaco._aacu._aacx._aad7._aade > div > span",
            "h1._ap3a._aaco._aacu._aacy._aad6._aade",
            "div > a> span> span.x1lliihq.x193iq5w.x6ikm8r.x10wlt62.xlyipyv.xuxw1ft"
    };

    private static final String FIRST_PHOTO_LOCATOR  = "div.x1lliihq.x1n2onr6.xh8yej3.x4gyw5p.xfllauq.xo2y696.x11i5rnm.x2pgyrj > a > div > div > img";


    public void parseProfile() throws IOException, InterruptedException {

        Thread.sleep(5000);

        FileSystem.createFolder(PROFILE_PATH);

        parseProfileInfo();

        scrollProfilePage();

        parsePosts();

    }

    private void parseProfileInfo(){

        // TODO: Добавить получение аватара

        StringBuilder profileInfo = new StringBuilder();

        for (String profileInfoBlock: PROFILE_DATA_LOCATORS){
            try {
                if ($(profileInfoBlock).exists()){
                    SelenideElement profileInfoPart = $(profileInfoBlock);
                    String profileInfoPartText = profileInfoPart.text();
                    profileInfo.append(profileInfoPartText).append("\n");
                }
                else {
                    System.out.println("Элемент не найден по локатору: " + profileInfoBlock);
                }

            } catch (Exception e) {
                System.out.println("Ошибка при обработке элемента: " + e.getMessage());
            }
        }

        FileSystem.createTxt(PROFILE_PATH + "\\Profile description.txt", profileInfo.toString());
    };

    private void scrollProfilePage() throws InterruptedException {
        System.out.println("SCROLL_STRATEGY = " + SCROLL_STRATEGY);
        if (SCROLL_STRATEGY.equals("inf")){
            scrollProfilePageToTheEndOfPage();
        }
        else {
            int scrollTimes = Integer.parseInt(SCROLL_STRATEGY);
            scrollProfilePageNTimes(scrollTimes);

        }
    }

    private void scrollProfilePageToTheEndOfPage() throws InterruptedException, NullPointerException {

        // Переменная для хранения предыдущей высоты страницы
        long lastHeight = (long) executeJavaScript("return document.body.scrollHeight");

        while (true) {
            // Прокручиваем страницу вниз
            executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");

            // Ждем некоторое время для подгрузки новых элементов
            Thread.sleep(2000); // Установите задержку, достаточную для загрузки новых элементов

            // Получаем новую высоту страницы
            long newHeight = (long) executeJavaScript("return document.body.scrollHeight");

            // Если новая высота равна предыдущей, значит новых элементов больше нет
            if (newHeight == lastHeight) {
                break;
            }

            // Обновляем предыдущую высоту страницы
            lastHeight = newHeight;
        }

    }

    private void scrollProfilePageNTimes(int scrollTimes) throws InterruptedException, NullPointerException {
        for (int scrollIteration = 0; scrollIteration < scrollTimes; scrollIteration++){
            executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);
        }
    }

    private void parsePosts() throws IOException {
        int postNumber = 0;

        ElementsCollection firstPostsPhotos = $$(FIRST_PHOTO_LOCATOR);

        for (SelenideElement firstPhoto: firstPostsPhotos){

            String postPath = BASE_PATH_TO_FOLDER_RESULTS + "\\" + PROFILE_TO_PARSE + "\\" + postNumber;
            FileSystem.createFolder(postPath);

            String postDescription = firstPhoto.getAttribute("alt");
            FileSystem.createTxt(postPath + "\\Post description.txt", postDescription);

            String imgSrc = firstPhoto.getAttribute("src");
            FileSystem.createImage(postPath, imgSrc);

            postNumber++;
        }
    }

}
