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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] xmlFiles = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    private int currentIndex = 0;
    private FrameLayout xmlContainer;
    private TextView xmlContentTextView;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private List<String> lessonsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlContainer = findViewById(R.id.xmlContainer);
        xmlContentTextView = findViewById(R.id.xmlContentTextView);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        listView = findViewById(R.id.listView);

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

        lessonsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lessonsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditLessonDialog(position);
            }
        });

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddLessonDialog(xmlFiles[currentIndex]);
            }
        });
    }



    private void showEditLessonDialog(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_lesson, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextTeacher = dialogView.findViewById(R.id.editTextTeacher);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        final EditText editTextClassroom = dialogView.findViewById(R.id.editTextClassroom);

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
                    String lesson = name + " | " + teacher + " | " + time + " | " + classroom;
                    lessonsList.set(position, lesson);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                lessonsList.remove(position);
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
        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lessonsList);
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
                            if (selectedXmlFile.equals(xmlFiles[currentIndex])) {
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