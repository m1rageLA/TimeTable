package com.example.tt;

public class OnClickManager {
    public static void onClickPrevious(String[] xmlFiles, int currentIndex, FileManagerXML fileManager) {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = xmlFiles.length - 1;
        }
        fileManager.displayXmlFile(xmlFiles[currentIndex]);
    }

    public static void onClickNext(String[] xmlFiles, int currentIndex, FileManagerXML fileManager) {
        currentIndex++;
        if (currentIndex >= xmlFiles.length) {
            currentIndex = 0;
        }
        fileManager.displayXmlFile(xmlFiles[currentIndex]);
    }
}

