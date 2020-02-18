package com.fitareq.travel_mate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewFullImageActivity extends AppCompatActivity {

    private ImageView BackBtn, Image;
    String imageLink;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_image);
        imageLink = getIntent().getStringExtra("imageLink");
        position=getIntent().getIntExtra("position",0);


        BackBtn = findViewById(R.id.full_image_view_activity_back_icon);
        Image = findViewById(R.id.full_image_view_activity_image);

        Picasso.get().load(imageLink).into(Image);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewFullImageActivity.this, HomeActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }
}
