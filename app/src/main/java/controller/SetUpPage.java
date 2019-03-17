package controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class SetUpPage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setuppage);

        Button nine_by_nine = findViewById(R.id.n9by9);
        nine_by_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n9by9 = new Intent(SetUpPage.this, MainActivity.class);
                startActivity(n9by9);
            }
        });


    }
}
