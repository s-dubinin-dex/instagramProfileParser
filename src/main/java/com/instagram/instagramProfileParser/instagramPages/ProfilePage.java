package com.instagram.instagramProfileParser.instagramPages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.instagram.instagramProfileParser.fileSystem.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    private static final String BASE_PATH_TO_FOLDER_RESULTS = "D:\\InstagramParserData";
    private static final int TIME_FOR_POST_LOADING = 2000;
    private ElementsCollection postsFromProfileForFirstPhoto = $$("div.x1lliihq.x1n2onr6.xh8yej3.x4gyw5p.xfllauq.xo2y696.x11i5rnm.x2pgyrj > a > div > div > img");

    private String[] profileDataLocators = new String[]{
            ".x1lliihq.x1plvlek.xryxfnj.x1n2onr6.x193iq5w.xeuugli.x1fj9vlw.x13faqbe.x1vvkbs.x1s928wv.xhkezso.x1gmr53x.x1cpjm7i.x1fgarty.x1943h6x.x1i0vuye.xvs91rp.x1s688f.x5n08af.x10wh9bi.x1wdrske.x8viiok.x18hxmgj",
            "div._ap3a._aaco._aacu._aacy._aad6._aade",
            "span._ap3a._aaco._aacu._aacx._aad7._aade > div > span",
            "h1._ap3a._aaco._aacu._aacy._aad6._aade",
            "div > a> span> span.x1lliihq.x193iq5w.x6ikm8r.x10wlt62.xlyipyv.xuxw1ft"
    };

    public void scrollProfilePageToTheEndOfPage() throws InterruptedException, NullPointerException {

        // Переменная для хранения предыдущей высоты страницы
        long lastHeight = (long) executeJavaScript("return document.body.scrollHeight");

        while (true) {
            // Прокручиваем страницу вниз
            executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");

            // Ждем некоторое время для подгрузки новых элементов
            Thread.sleep(TIME_FOR_POST_LOADING); // Установите задержку, достаточную для загрузки новых элементов

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

    public void scrollProfilePageNTimes(int scrollTimes) throws InterruptedException, NullPointerException {
        for (int scrollIteration = 0; scrollIteration < scrollTimes; scrollIteration++){
            executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(TIME_FOR_POST_LOADING);
        }
    }

    public List<String> getPostsLinks(){
        List<String> result = new ArrayList<>();
        ElementsCollection divs = $$("div.x1lliihq.x1n2onr6.xh8yej3.x4gyw5p.xfllauq.xo2y696.x11i5rnm.x2pgyrj");


        for (SelenideElement div: divs){
            result.add(div.$("a").getAttribute("href"));
        }
        return result;
    }

    public void savePostInfo(String profile) throws IOException, InterruptedException {

        Thread.sleep(5000);

        StringBuilder profileInfo = new StringBuilder();

        for (String profileInfoBlock: this.profileDataLocators){
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

        String profilePath = BASE_PATH_TO_FOLDER_RESULTS + "\\" + profile;
        FileSystem.createFolder(profilePath);

        FileSystem.createTxt(profilePath + "\\Profile description.txt", profileInfo.toString());

        this.scrollProfilePageNTimes(1);

        int elemN = 0;

        for (SelenideElement firstPhoto: postsFromProfileForFirstPhoto){

            String postPath = BASE_PATH_TO_FOLDER_RESULTS + "\\" + profile + "\\" + elemN;
            FileSystem.createFolder(postPath);

            String postDescription = firstPhoto.getAttribute("alt");
            FileSystem.createTxt(postPath + "\\Post description.txt", postDescription);

            String imgSrc = firstPhoto.getAttribute("src");
            FileSystem.createImage(postPath, imgSrc);

            elemN++;
        }
    }

}
