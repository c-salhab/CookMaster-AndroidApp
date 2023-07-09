package com.example.cookmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LessonDetailActivity extends Activity {
    private TextView lessonTitleTextView;
    private TextView lessonContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LessonDetailActivity.this, LessonsActivity.class);
                startActivity(i);
                finish();
            }
        });

        String lessonTitle = getIntent().getStringExtra("lessonTitle");

        lessonTitleTextView = findViewById(R.id.lesson_title_text_view);
        lessonContentTextView = findViewById(R.id.lesson_content_text_view);

        lessonTitleTextView.setText(lessonTitle);
    }
}
