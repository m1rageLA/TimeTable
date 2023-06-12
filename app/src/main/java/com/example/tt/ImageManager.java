package com.example.tt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import java.util.Map;

public class ImageManager {
    public void displayXmlFile(String fileName, FileManagerXML fileManager, Map<String, Uri> imageUriMap, MainActivity mainActivity) {
        fileManager.displayXmlFile(fileName);
        ArrayAdapter<String> adapter = fileManager.getAdapterMap().get(fileName);

        // Проверяем, есть ли сохраненное изображение для текущего файла XML
        if (imageUriMap.containsKey(fileName)) {
            Uri imageUri = imageUriMap.get(fileName);
            mainActivity.displayImage(imageUri);
        }
    }

    public void onImageActivityResult(Uri imageUri, int currentIndex, String[] xmlFiles, Map<String, Uri> imageUriMap, MainActivity mainActivity) {
        imageUriMap.put(xmlFiles[currentIndex], imageUri); // Сохраняем изображение для текущего дня недели
        mainActivity.displayImage(imageUri);
    }

    public void showImageDialog(int currentIndex, String[] xmlFiles, Map<String, Uri> imageUriMap, FrameLayout xmlContainer, MainActivity mainActivity, FileManagerXML fileManager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Действие с изображением")
                .setMessage("Выберите одно из следующих действий:")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Выполнить действие удаления изображения
                        imageUriMap.remove(xmlFiles[currentIndex]);
                        xmlContainer.removeAllViews();
                        displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, mainActivity); // Добавьте эту строку для обновления пользовательского интерфейса
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Закрыть диалоговое окно
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public void onSaveInstanceState(Map<String, Uri> imageUriMap, String[] xmlFiles, String KEY_IMAGE_URI, Bundle outState) {
        // Сохраняем изображения для каждого дня недели
        for (String xmlFile : xmlFiles) {
            Uri imageUri = imageUriMap.get(xmlFile);
            if (imageUri != null) {
                outState.putString(KEY_IMAGE_URI + "_" + xmlFile, imageUri.toString());
            }
        }
    }


    public void onRestoreInstanceState(Bundle savedInstanceState, Map<String, Uri> imageUriMap, String[] xmlFiles, String KEY_IMAGE_URI) {
        // Восстанавливаем изображения для каждого дня недели
        for (String xmlFile : xmlFiles) {
            String imageUriString = savedInstanceState.getString(KEY_IMAGE_URI + "_" + xmlFile);
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                imageUriMap.put(xmlFile, imageUri);
            }
        }
    }
}