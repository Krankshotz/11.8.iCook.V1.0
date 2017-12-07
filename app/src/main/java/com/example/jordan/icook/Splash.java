package com.example.jordan.icook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Henry on 12/6/2017.
 */

public class Splash extends AppCompatActivity{
    private ImageView iv;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);

        iv = findViewById(R.id.loadingBanner);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        iv.startAnimation(myanim);
        final Intent intent = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    startActivity(intent);
                    finish();
                }
            }
        };
       /* ic = findViewById(R.id.icook);
        Animation myanim2 = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        ic.startAnimation(myanim);
        final Intent intent2 = new Intent(this,MainActivity.class);
        Thread timer2 = new Thread(){
            public void run(){
                try{
                    sleep(300);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    startActivity(intent);
                    finish();
                }
            }
        };*/
            timer.start();

    }
}
