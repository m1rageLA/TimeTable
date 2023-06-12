package com.example.tt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String[] xmlFiles = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    private int currentIndex = 0;
    private FrameLayout xmlContainer;
    private TextView xmlContentTextView;
    private Map<String, List<String>> lessonsMap = new HashMap<>();
    private Map<String, ArrayAdapter<String>> adapterMap = new HashMap<>();
    private static final String KEY_IMAGE_URI = "image_uri";
    private Uri imageUri;
    private FileManagerXML fileManager;
    private Map<String, Uri> imageUriMap = new HashMap<>();
    private ImageManager imageManager;
    private Button[] dayButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlContainer = findViewById(R.id.xmlContainer);
        xmlContentTextView = findViewById(R.id.xmlContentTextView);

        fileManager = new FileManagerXML(this, xmlContainer, xmlContentTextView, lessonsMap, adapterMap);
        fileManager.initializeLessonsData();

        imageManager = new ImageManager();

        if (savedInstanceState != null) {
            // Восстанавливаем изображения для каждого дня недели из сохраненного состояния
            for (String xmlFile : xmlFiles) {
                String imageUriString = savedInstanceState.getString(KEY_IMAGE_URI + "_" + xmlFile);
                if (imageUriString != null) {
                    Uri imageUri = Uri.parse(imageUriString);
                    imageUriMap.put(xmlFile, imageUri);
                }
            }
        }

        imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, this);


        dayButtons = new Button[xmlFiles.length];
        dayButtons[0] = findViewById(R.id.mondayButton);
        dayButtons[1] = findViewById(R.id.tuesdayButton);
        dayButtons[2] = findViewById(R.id.wednesdayButton);
        dayButtons[3] = findViewById(R.id.thursdayButton);
        dayButtons[4] = findViewById(R.id.fridayButton);
        dayButtons[5] = findViewById(R.id.saturdayButton);
        dayButtons[6] = findViewById(R.id.sundayButton);

        for (int i = 0; i < dayButtons.length; i++) {
            final int index = i;
            dayButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex = index;
                    imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
                    updateButtonColors();
                }
            });
        }

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem addItem = new AddItem(MainActivity.this, lessonsMap.get(xmlFiles[currentIndex]), adapterMap.get(xmlFiles[currentIndex]));
                addItem.showAddLessonDialog(xmlFiles[currentIndex]);
            }
        });

        updateButtonColors();
    }

    private void updateButtonColors() {
        for (int i = 0; i < dayButtons.length; i++) {
            if (i == currentIndex) {
                dayButtons[i].setTextColor(getResources().getColor(R.color.active_day_color));
            } else {
                dayButtons[i].setTextColor(getResources().getColor(R.color.inactive_day_color));
            }
        }
    }

    public void displayImage(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)); // Обновленные параметры макета
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageManager.showImageDialog(currentIndex, xmlFiles, imageUriMap, xmlContainer, MainActivity.this, fileManager);
            }
        });

        xmlContainer.removeAllViews();
        xmlContainer.addView(imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddItem.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageManager.onImageActivityResult(imageUri, currentIndex, xmlFiles, imageUriMap, this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        imageManager.onSaveInstanceState(imageUriMap, xmlFiles, KEY_IMAGE_URI, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageManager.onRestoreInstanceState(savedInstanceState, imageUriMap, xmlFiles, KEY_IMAGE_URI);
    }


}