package com.example.tt;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EditItem {
    private AppCompatActivity activity;
    private List<String> lessonsList;
    private ArrayAdapter<String> adapter;

    public EditItem(AppCompatActivity activity, List<String> lessonsList, ArrayAdapter<String> adapter) {
        this.activity = activity;
        this.lessonsList = lessonsList;
        this.adapter = adapter;
    }

    public EditItem(Context context, List<String> lessonsList, ArrayAdapter<String> adapter) {
        this.activity = (AppCompatActivity) context;
        this.lessonsList = lessonsList;
        this.adapter = adapter;
    }

    public void showEditLessonDialog(String xmlFile, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_lesson, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        final EditText editTextInfo = dialogView.findViewById(R.id.editTextInfo);

        String lesson = lessonsList.get(position);
        String[] parts = lesson.split(" \\| ");
        if (parts.length == 4) {
            editTextName.setText(parts[0]);
            editTextTime.setText(parts[2]);
            editTextInfo.setText(parts[3]);
        }

        dialogBuilder.setTitle("Edit Lesson");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = editTextName.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();
                String info = editTextInfo.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(info)) {
                    String editedLesson = String.format("%-12s%s\n%s", time, name, info);
                    lessonsList.set(position, editedLesson);
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
}
