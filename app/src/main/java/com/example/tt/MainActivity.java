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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlContainer = findViewById(R.id.xmlContainer);
        xmlContentTextView = findViewById(R.id.xmlContentTextView);

        fileManager = new FileManagerXML(this, xmlContainer, xmlContentTextView, lessonsMap, adapterMap);
        fileManager.initializeLessonsData();

        if (savedInstanceState != null) {
            String imageUriString = savedInstanceState.getString(KEY_IMAGE_URI);
            if (imageUriString != null) {
                imageUri = Uri.parse(imageUriString);
            }
        }

        displayXmlFile(xmlFiles[currentIndex]);

        Button mondayButton = findViewById(R.id.mondayButton);
        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 0;
                displayXmlFile(xmlFiles[currentIndex]);
            }
        });

        Button tuesdayButton = findViewById(R.id.tuesdayButton);
        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 1;
                displayXmlFile(xmlFiles[currentIndex]);
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

    private void displayXmlFile(String fileName) {
        fileManager.displayXmlFile(fileName);
        ArrayAdapter<String> adapter = adapterMap.get(fileName);

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem addItem = new AddItem(MainActivity.this, lessonsMap.get(fileName), adapter);
                addItem.showAddLessonDialog(fileName);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddItem.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            displayImage(imageUri);
        }
    }

    public void displayImage(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        xmlContainer.removeAllViews();
        xmlContainer.addView(imageView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageUri != null) {
            outState.putString(KEY_IMAGE_URI, imageUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String imageUriString = savedInstanceState.getString(KEY_IMAGE_URI);
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            displayImage(imageUri);
        }
    }
}
