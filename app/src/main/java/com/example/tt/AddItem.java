package com.example.tt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tt.R;

import java.util.List;

public class AddItem {
    private AppCompatActivity activity;
    private List<String> lessonsList;
    private ArrayAdapter<String> adapter;

    public AddItem(AppCompatActivity activity, List<String> lessonsList, ArrayAdapter<String> adapter) {
        this.activity = activity;
        this.lessonsList = lessonsList;
        this.adapter = adapter;
    }

    public void showAddLessonDialog(final String selectedXmlFile) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        int xmlResourceId = activity.getResources().getIdentifier("dialog_add_lesson", "layout", activity.getPackageName());
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
