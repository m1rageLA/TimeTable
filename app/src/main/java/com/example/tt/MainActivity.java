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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlContainer = findViewById(R.id.xmlContainer);
        xmlContentTextView = findViewById(R.id.xmlContentTextView);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);

        initializeLessonsData();

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


        ArrayAdapter<String> adapter = adapterMap.get(xmlFiles[currentIndex]);





        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddLessonDialog(xmlFiles[currentIndex]);
            }
        });
    }

    private void initializeLessonsData() {
        for (String xmlFile : xmlFiles) {
            List<String> lessonsList = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lessonsList);
            lessonsMap.put(xmlFile, lessonsList);
            adapterMap.put(xmlFile, adapter);
        }
    }

    private void showEditLessonDialog(String xmlFile, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_lesson, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextTeacher = dialogView.findViewById(R.id.editTextTeacher);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        final EditText editTextClassroom = dialogView.findViewById(R.id.editTextClassroom);

        List<String> lessonsList = lessonsMap.get(xmlFile);
        String lesson = lessonsList.get(position);
        String[] parts = lesson.split(" \\| ");
        if (parts.length == 4) {
            editTextName.setText(parts[0]);
            editTextTeacher.setText(parts[1]);
            editTextTime.setText(parts[2]);
            editTextClassroom.setText(parts[3]);
        }

        dialogBuilder.setTitle("Edit Lesson");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = editTextName.getText().toString().trim();
                String teacher = editTextTeacher.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();
                String classroom = editTextClassroom.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(teacher) &&
                        !TextUtils.isEmpty(time) && !TextUtils.isEmpty(classroom)) {
                    String editedLesson = name + " | " + teacher + " | " + time + " | " + classroom;
                    lessonsList.set(position, editedLesson);
                    ArrayAdapter<String> adapter = adapterMap.get(xmlFile);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                lessonsList.remove(position);
                ArrayAdapter<String> adapter = adapterMap.get(xmlFile);
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void displayXmlFile(String fileName) {
        int xmlResourceId = getResources().getIdentifier(fileName, "layout", getPackageName());
        View view = getLayoutInflater().inflate(xmlResourceId, xmlContainer, false);
        xmlContainer.removeAllViews();
        xmlContainer.addView(view);
        xmlContentTextView.setText("XML Content: " + fileName);

        ListView listView = view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = adapterMap.get(fileName);
        listView.setAdapter(adapter);
    }

    private void showAddLessonDialog(final String selectedXmlFile) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        int xmlResourceId = getResources().getIdentifier("dialog_add_lesson", "layout", getPackageName());
        View dialogView = inflater.inflate(xmlResourceId, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextTeacher = dialogView.findViewById(R.id.editTextTeacher);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        final EditText editTextClassroom = dialogView.findViewById(R.id.editTextClassroom);

        dialogBuilder.setTitle("Add Lesson");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = editTextName.getText().toString().trim();
                        String teacher = editTextTeacher.getText().toString().trim();
                        String time = editTextTime.getText().toString().trim();
                        String classroom = editTextClassroom.getText().toString().trim();

                        if (!TextUtils.isEmpty(name)) {
                            String lesson = "Name: " + name + "\nTeacher: " + teacher + "\nTime: " + time + "\nClassroom: " + classroom;

                            // Добавить урок только в выбранный XML-файл
                            List<String> lessonsList = lessonsMap.get(selectedXmlFile);
                            ArrayAdapter<String> adapter = adapterMap.get(selectedXmlFile);
                            if (lessonsList != null && adapter != null) {
                                lessonsList.add(lesson);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Отмена добавления урока
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
