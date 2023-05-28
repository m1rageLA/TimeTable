package com.example.tt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String[] xmlFiles = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    private int currentIndex = 0;
    private FrameLayout xmlContainer;
    private TextView xmlContentTextView;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private Map<String, List<String>> lessonsMap = new HashMap<>();
    private Map<String, ArrayAdapter<String>> adapterMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//вызывает родительский onCreate
        setContentView(R.layout.activity_main);//устанавливает макет activity_main.xml

        //---инициализация элементов по ID---
        xmlContainer = findViewById(R.id.xmlContainer);
        xmlContentTextView = findViewById(R.id.xmlContentTextView);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);

        initializeLessonsData();

        displayXmlFile(xmlFiles[currentIndex]);//отображает содержимое XML-файла в пользовательском интерфейсе

        //---PREVIOUSBUTTON---декрементирование индекса
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
        //---NEXTBUTTNO---инкрементирование индекса
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if (currentIndex >= xmlFiles.length) {
                    currentIndex = 0;
                }
                displayXmlFile(xmlFiles[currentIndex]);//отображение xml файла в зависимости от индекста
            }
        });

        ArrayAdapter<String> adapter = adapterMap.get(xmlFiles[currentIndex]);

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {//устанавливает обработчик событий для addButton
            @Override
            public void onClick(View v) {
                //при нажатии на кнопку создаем экземпляр класса AddItem
                //передает индекс дня в конструктор
                AddItem addItem = new AddItem(MainActivity.this, lessonsMap.get(xmlFiles[currentIndex]), adapter);
                addItem.showAddLessonDialog(xmlFiles[currentIndex]);
            }
        });
    }

    /*связывает xml файлы с своим списком уроков
    что-бы данные уроков могли быть отображены в соответствующем
    пользовательском интерфейсе и изменены при необходимости*/
    private void initializeLessonsData() {
        for (String xmlFile : xmlFiles) {
            List<String> lessonsList = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lessonsList);
            lessonsMap.put(xmlFile, lessonsList);
            adapterMap.put(xmlFile, adapter);
        }
    }


    private void displayXmlFile(String fileName) {
        int xmlResourceId = getResources().getIdentifier(fileName, "layout", getPackageName());
        View view = getLayoutInflater().inflate(xmlResourceId, xmlContainer, false);
        xmlContainer.removeAllViews();
        xmlContainer.addView(view);
        xmlContentTextView.setText("XML Content: " + fileName);

        ListView listView = view.findViewById(R.id.listView);

        ArrayAdapter<String> adapter = adapterMap.get(fileName); // Перемещение объявления adapter сюда

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditItem editItem = new EditItem(MainActivity.this, lessonsMap.get(xmlFiles[currentIndex]), adapter);
                editItem.showEditLessonDialog(xmlFiles[currentIndex], position);
            }
        });
    }

}
