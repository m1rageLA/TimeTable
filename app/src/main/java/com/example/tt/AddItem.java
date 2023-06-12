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
        final EditText editTextInfo = dialogView.findViewById(R.id.editTextInfo);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);

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
                String info = editTextInfo.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();

                if (!name.isEmpty()) {
                    String lesson = String.format("%-12s%s\n%s", time, name, info);
                    if (lessonsList != null && adapter != null) {
                        lessonsList.add(lesson);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public static final int PICK_IMAGE_REQUEST = 1;
}
