package com.example.memerzz;

//import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Object RequestListener;
    private String currentImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MemeLoad();

    }

    private void MemeLoad() {

        ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(ProgressBar.VISIBLE);
        ImageView Meme = findViewById(R.id.Meme);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =  "https://meme-api.herokuapp.com/gimme";
        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentImageUrl = response.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                pb.setVisibility(ProgressBar.INVISIBLE);
                ImageView Meme = findViewById(R.id.Meme);
                Glide.with(MainActivity.this).load(currentImageUrl).fitCenter().listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       pb.setVisibility(ProgressBar.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pb.setVisibility(ProgressBar.INVISIBLE);
                        return false;
                    }
                }).into(Meme);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(json);
    }
    public void NextMeme(View view) {
    MemeLoad();
    }

    public void ShareMeme(View view) {
        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey,Check out this cool Meme I got from Reddit "+ currentImageUrl);
        Intent chooser=Intent.createChooser(intent,"Share this meme using...");
        startActivity(chooser);
    }
}