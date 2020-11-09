package com.example.admin.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import jp.shts.android.storiesprogressview.StoriesProgressView;


public class StoryProgressView extends AppCompatActivity {
    StoriesProgressView storiesProgressView;
    ImageView imageView;
    int counter =0;

    int[] resources = new int[] {
            R.drawable.brownie,
            R.drawable.coffee,
            R.drawable.cake
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_progress_view);

        imageView = (ImageView) findViewById(R.id.imageView);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(3); // <- set stories
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {

            @Override
            public void onNext() {
                imageView.setImageResource(resources[++counter]);

            }

            @Override
            public void onPrev() {
                imageView.setImageResource(resources[counter= counter-1]);

            }

            @Override
            public void onComplete() {
                Toast.makeText(StoryProgressView.this, "onComplete", Toast.LENGTH_SHORT).show();

            }
        });
        storiesProgressView.startStories();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesProgressView.skip();
            }
        });
        imageView.setImageResource(resources[counter]);

    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }
}
