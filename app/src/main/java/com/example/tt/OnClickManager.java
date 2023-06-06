package com.example.tt;

public class OnClickManager {
    public static void onClickDay(String[] xmlFiles, FileManagerXML fileManager, int dayIndex) {
        // Проверяем, что индекс дня находится в допустимом диапазоне
        if (dayIndex >= 0 && dayIndex < xmlFiles.length) {
            fileManager.displayXmlFile(xmlFiles[dayIndex]);
        }
    }
}

