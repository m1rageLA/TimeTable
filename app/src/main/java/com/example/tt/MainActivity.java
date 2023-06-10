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

        Button mondayButton = findViewById(R.id.mondayButton);
        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 0;
                imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
            }
        });

        Button tuesdayButton = findViewById(R.id.tuesdayButton);
        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 1;
                imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
            }
        });

        Button wednesdayButton = findViewById(R.id.wednesdayButton);
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 2;
                imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
            }
        });

        Button thursdayButton = findViewById(R.id.thursdayButton);
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 3;
                imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
            }
        });

        Button fridayButton = findViewById(R.id.fridayButton);
        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 4;
                imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
            }
        });

        Button saturdayButton = findViewById(R.id.saturdayButton);
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 5;
                imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
            }
        });

        Button sundayButton = findViewById(R.id.sundayButton);
        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 6;
                imageManager.displayXmlFile(xmlFiles[currentIndex], fileManager, imageUriMap, MainActivity.this);
            }
        });

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem addItem = new AddItem(MainActivity.this, lessonsMap.get(xmlFiles[currentIndex]), adapterMap.get(xmlFiles[currentIndex]));
                addItem.showAddLessonDialog(xmlFiles[currentIndex]);
            }
        });
    }
    public void displayImage(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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