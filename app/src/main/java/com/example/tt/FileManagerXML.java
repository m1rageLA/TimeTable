package com.example.tt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManagerXML {
    private Context context;
    private FrameLayout xmlContainer;
    private TextView xmlContentTextView;
    private Map<String, List<String>> lessonsMap;
    private Map<String, ArrayAdapter<String>> adapterMap;
    private String[] xmlFiles = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};

    public FileManagerXML(Context context, FrameLayout xmlContainer, TextView xmlContentTextView, Map<String, List<String>> lessonsMap, Map<String, ArrayAdapter<String>> adapterMap) {
        this.context = context;
        this.xmlContainer = xmlContainer;
        this.xmlContentTextView = xmlContentTextView;
        this.lessonsMap = lessonsMap;
        this.adapterMap = adapterMap;
    }

    public void initializeLessonsData() {
        for (String xmlFile : xmlFiles) {
            List<String> lessonsList = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item, lessonsList);
            lessonsMap.put(xmlFile, lessonsList);
            adapterMap.put(xmlFile, adapter);
        }
    }

    public Map<String, ArrayAdapter<String>> getAdapterMap() {
        return adapterMap;
    }

    public void displayXmlFile(String fileName) {
        int xmlResourceId = context.getResources().getIdentifier(fileName, "layout", context.getPackageName());
        View view = LayoutInflater.from(context).inflate(xmlResourceId, null);
        xmlContainer.removeAllViews();
        xmlContainer.addView(view);
        xmlContentTextView.setText("XML Content: " + fileName);

        ListView listView = view.findViewById(R.id.listView);

        ArrayAdapter<String> adapter = adapterMap.get(fileName);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditItem editItem = new EditItem(context, lessonsMap.get(fileName), adapter);
                editItem.showEditLessonDialog(fileName, position);
            }
        });

        // Изменение цвета элементов списка на красный
        for (int i = 0; i < listView.getChildCount(); i++) {
            View item = listView.getChildAt(i);
            TextView textView = item.findViewById(android.R.id.text1);
            textView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
        }
    }
}
