package com.example.tt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddItem {
    private AppCompatActivity activity;
    private List<String> lessonsList;
    private ArrayAdapter<String> adapter;
    private Uri imageUri;

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

        Button galleryButton = dialogView.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        dialogBuilder.setTitle("Add Lesson");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = editTextName.getText().toString().trim();
                        String teacher = editTextTeacher.getText().toString().trim();
                        String time = editTextTime.getText().toString().trim();
                        String classroom = editTextClassroom.getText().toString().trim();

                        if (!name.isEmpty()) {
                            String lesson = "Name: " + name + "\nTeacher: " + teacher +
                                    "\nTime: " + time + "\nClassroom: " + classroom;

                            if (lessonsList != null && adapter != null) {
                                lessonsList.add(lesson);
                                adapter.notifyDataSetChanged();
                            }

                            if (imageUri != null) {
                                MainActivity activity = (MainActivity) AddItem.this.activity;
                                activity.displayImage(imageUri);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel adding lesson
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public static final int PICK_IMAGE_REQUEST = 1;
}