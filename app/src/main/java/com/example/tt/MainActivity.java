package com.example.tt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private Map<String, Uri> imageUriMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xmlContainer = findViewById(R.id.xmlContainer);
        xmlContentTextView = findViewById(R.id.xmlContentTextView);

        fileManager = new FileManagerXML(this, xmlContainer, xmlContentTextView, lessonsMap, adapterMap);
        fileManager.initializeLessonsData();

        if (savedInstanceState != null) {
            // Восстанавливаем изображения для каждого дня недели из сохраненного состояния
            for (String xmlFile : xmlFiles) {
                String imageUriString = savedInstanceState.getString(KEY_IMAGE_URI + "_" + xmlFile);
                if (imageUriString != null) {
                    Uri imageUri = Uri.parse(imageUriString);
                    imageUriMap.put(xmlFile, imageUri);
                }
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

        Button wednesdayButton = findViewById(R.id.wednesdayButton);
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 2;
                displayXmlFile(xmlFiles[currentIndex]);
            }
        });

        Button thursdayButton = findViewById(R.id.thursdayButton);
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 3;
                displayXmlFile(xmlFiles[currentIndex]);
            }
        });

        Button fridayButton = findViewById(R.id.fridayButton);
        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 4;
                displayXmlFile(xmlFiles[currentIndex]);
            }
        });

        Button saturdayButton = findViewById(R.id.saturdayButton);
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 5;
                displayXmlFile(xmlFiles[currentIndex]);
            }
        });

        Button sundayButton = findViewById(R.id.sundayButton);
        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = 6;
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

        // Проверяем, есть ли сохраненное изображение для текущего файла XML
        if (imageUriMap.containsKey(fileName)) {
            Uri imageUri = imageUriMap.get(fileName);
            displayImage(imageUri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddItem.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageUriMap.put(xmlFiles[currentIndex], imageUri); // Сохраняем изображение для текущего дня недели
            displayImage(imageUri);
        }
    }

    public void displayImage(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(); // Отображение диалогового окна по щелчку на изображении
            }
        });

        xmlContainer.removeAllViews();
        xmlContainer.addView(imageView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Сохраняем изображения для каждого дня недели
        for (String xmlFile : xmlFiles) {
            Uri imageUri = imageUriMap.get(xmlFile);
            if (imageUri != null) {
                outState.putString(KEY_IMAGE_URI + "_" + xmlFile, imageUri.toString());
            }
        }
    }
    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Действие с изображением")
                .setMessage("Выберите одно из следующих действий:")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Выполнить действие удаления изображения
                        imageUriMap.remove(xmlFiles[currentIndex]);
                        xmlContainer.removeAllViews();
                        displayXmlFile(xmlFiles[currentIndex]); // Добавьте эту строку для обновления пользовательского интерфейса
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Закрыть диалоговое окно
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Восстанавливаем изображения для каждого дня недели
        for (String xmlFile : xmlFiles) {
            String imageUriString = savedInstanceState.getString(KEY_IMAGE_URI + "_" + xmlFile);
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                imageUriMap.put(xmlFile, imageUri);
            }
        }
    }

    public static class AddItem {
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
}