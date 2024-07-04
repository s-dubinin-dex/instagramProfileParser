package com.instagram.instagramProfileParser.fileSystem;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystem {
    public static void createFolder(String path) throws IOException {
        Path profilePath = Paths.get(path);
        Files.createDirectories(profilePath);
    }
    public static void createTxt(String filePath, String fileContent){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Записываем содержимое в файл
            writer.write(fileContent);
            System.out.println("File created successfully.");
        } catch (IOException e) {
            System.err.println("Failed to create file: " + e.getMessage());
        }
    }
    public static void createImage(String filePath, String fileUrl){

        try {
            // Открываем соединение с URL
            URL url = new URL(fileUrl);
            InputStream inputStream = url.openStream();

            // Создаем поток для сохранения файла
            OutputStream outputStream = new FileOutputStream(filePath + "\\image.jpg");

            // Читаем данные из InputStream и записываем в OutputStream
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Закрываем потоки
            inputStream.close();
            outputStream.close();

            System.out.println("Image downloaded successfully.");
        } catch (IOException e) {
            System.err.println("Failed to download image: " + e.getMessage());
        }

    }
}
