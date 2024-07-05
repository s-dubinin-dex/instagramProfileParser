package com.instagram.instagramProfileParser.instagramPages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.instagram.instagramProfileParser.Main;
import com.instagram.instagramProfileParser.fileSystem.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    private static final String PROFILE_TO_PARSE = Main.getProfileToParse();
    private static final String BASE_PATH_TO_FOLDER_RESULTS = Main.getOutputPath();
    private static final String PROFILE_PATH = BASE_PATH_TO_FOLDER_RESULTS + "\\" + PROFILE_TO_PARSE;
    private static final String SCROLL_MODE = Main.getScrollMode();
    private static final String GALLERY_MODE = Main.getGalleryMode();

    private static final String PROFILE_AVATAR_LOCATOR = "section.x78zum5.xdt5ytf.x1iyjqo2.xg6iff7 img.xpdipgo.x972fbf.xcfux6l.x1qhh985.xm0m39n.xk390pu.x5yr21d.xdj266r.x11i5rnm.xat24cr.x1mh8g0r.xl1xv1r.xexx8yu.x4uap5.x18d9i69.xkhd6sd.x11njtxf.xh8yej3";
    private static final String[] PROFILE_DATA_LOCATORS = new String[]{
            ".x1lliihq.x1plvlek.xryxfnj.x1n2onr6.x193iq5w.xeuugli.x1fj9vlw.x13faqbe.x1vvkbs.x1s928wv.xhkezso.x1gmr53x.x1cpjm7i.x1fgarty.x1943h6x.x1i0vuye.xvs91rp.x1s688f.x5n08af.x10wh9bi.x1wdrske.x8viiok.x18hxmgj",
            "div._ap3a._aaco._aacu._aacy._aad6._aade",
            "span._ap3a._aaco._aacu._aacx._aad7._aade > div > span",
            "h1._ap3a._aaco._aacu._aacy._aad6._aade",
            "div > a> span> span.x1lliihq.x193iq5w.x6ikm8r.x10wlt62.xlyipyv.xuxw1ft"
    };

    private static final String POST_LOCATOR = "div.x1lliihq.x1n2onr6.xh8yej3.x4gyw5p.xfllauq.xo2y696.x11i5rnm.x2pgyrj > a";
    private static final String FIRST_PHOTO_LOCATOR  =  "div > div > img";

    private static final String OPENED_POST_LOCATOR = "span.x193iq5w.xeuugli.x1fj9vlw.x13faqbe.x1vvkbs.xt0psk2.x1i0vuye.xvs91rp.xo1l8bm.x5n08af.x10wh9bi.x1wdrske.x8viiok.x18hxmgj";
    private static final String OPENED_GALLERY_PHOTO_LOCATOR = "li._acaz > div >div > div > div._aagu > div > img";
    private static final String OPENED_GALLERY_NEXT_BUTTON_LOCATOR = "button._afxw._al46._al47";


    public void parseProfile() throws IOException, InterruptedException {

        Thread.sleep(5000);

        FileSystem.createFolder(PROFILE_PATH);

        parseProfileInfo();

        scrollProfilePage();

        parsePosts();

    }

    private void parseProfileInfo(){

        SelenideElement avatar = $(PROFILE_AVATAR_LOCATOR);
        String avatarSrc = avatar.getAttribute("src");
        FileSystem.createImage(PROFILE_PATH + "\\avatar.jpg", avatarSrc);

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

        FileSystem.createTxt(PROFILE_PATH + "\\profile_description.txt", profileInfo.toString());
    };

    private void scrollProfilePage() throws InterruptedException {
        if (SCROLL_MODE.equals("inf")){
            scrollProfilePageToTheEndOfPage();
        }
        else {
            int scrollTimes = Integer.parseInt(SCROLL_MODE);
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

        // Переменная для хранения предыдущей высоты страницы
        long lastHeight = (long) executeJavaScript("return document.body.scrollHeight");

        for (int scrollIteration = 0; scrollIteration < scrollTimes; scrollIteration++){

            // Оптимизировано, чтобы не терять время, если страница закончилась

            // Прокручиваем страницу вниз
            executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);

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

    private void parsePosts() throws IOException, InterruptedException {

        ElementsCollection posts = $$(POST_LOCATOR);

        if (GALLERY_MODE.equals("first")){
            int postNumber = 0;
            for (SelenideElement post: posts){
                String postPath = BASE_PATH_TO_FOLDER_RESULTS + "\\" + PROFILE_TO_PARSE + "\\" + postNumber;
                FileSystem.createFolder(postPath);

                String postUrl = post.getAttribute("href");
                FileSystem.createTxt(postPath + "\\post_url.txt", postUrl);

                SelenideElement firstPhoto = post.$(FIRST_PHOTO_LOCATOR);

                String postDescription = firstPhoto.getAttribute("alt");
                FileSystem.createTxt(postPath + "\\post_description.txt", postDescription);

                String imgSrc = firstPhoto.getAttribute("src");
                FileSystem.createImage(postPath + "\\image.jpg", imgSrc);

                postNumber++;
            }
        } else if(GALLERY_MODE.equals("all")) {
            int postNumber = 0;
            List<String> postURls = new ArrayList<>();

            for (SelenideElement post: posts){
                postURls.add(post.getAttribute("href"));
            }

            for (String postUrl: postURls){

                String postPath = BASE_PATH_TO_FOLDER_RESULTS + "\\" + PROFILE_TO_PARSE + "\\" + postNumber;
                FileSystem.createFolder(postPath);

                FileSystem.createTxt(postPath + "\\post_url.txt", postUrl);

                int imgNum = 0;
                open(postUrl);

                try {
                    if ($(OPENED_POST_LOCATOR).exists()){
                        String postDescription = $(OPENED_POST_LOCATOR).getText();
                        FileSystem.createTxt(postPath + "\\post_description.txt", postDescription);
                    }
                    else {
                        System.out.println("Элемент не найден по локатору: " + OPENED_POST_LOCATOR + "; У поста отсутствует описание");
                        FileSystem.createTxt(postPath + "\\post_description.txt", "Пост без описания");
                    }

                } catch (Exception e) {
                    System.out.println("Ошибка при обработке элемента: " + e.getMessage());
                }

                while (true){
                    Thread.sleep(500);
                    SelenideElement postPhoto = $$(OPENED_GALLERY_PHOTO_LOCATOR).first();
                    String imgSrc = postPhoto.getAttribute("src");
                    FileSystem.createImage(postPath + "\\image" + imgNum + ".jpg", imgSrc);

                    if ($(OPENED_GALLERY_NEXT_BUTTON_LOCATOR).exists()){
                        $(OPENED_GALLERY_NEXT_BUTTON_LOCATOR).click();
                    } else{
                        break;
                    }

                    imgNum++;

                }

                postNumber++;
            }

        }

    }

}
