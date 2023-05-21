package com.example.tt;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String[] xmlFiles = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    private int currentIndex = 0;
    private FrameLayout xmlContainer;
    private TextView xmlContentTextView;
    private ImageButton previousButton;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlContainer = findViewById(R.id.xmlContainer);
        xmlContentTextView = findViewById(R.id.xmlContentTextView);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);

        displayXmlFile(xmlFiles[currentIndex]);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = xmlFiles.length - 1;
                }
                displayXmlFile(xmlFiles[currentIndex]);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if (currentIndex >= xmlFiles.length) {
                    currentIndex = 0;
                }
                displayXmlFile(xmlFiles[currentIndex]);
            }
        });
    }

    private void displayXmlFile(String fileName) {
        int resId = getResources().getIdentifier(fileName, "xml", getPackageName());
        xmlContainer.removeAllViews();
        View view = getLayoutInflater().inflate(resId, null);
        xmlContainer.addView(view);
        xmlContentTextView.setText("XML Content: " + fileName);
    }
}

