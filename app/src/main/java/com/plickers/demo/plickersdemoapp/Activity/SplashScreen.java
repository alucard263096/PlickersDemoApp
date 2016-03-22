package com.plickers.demo.plickersdemoapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.plickers.demo.plickersdemoapp.R;

/*
Displays the splash screen that is shown to the user when the app is first booted.
This is where we could potentially have the login and other auth done.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /*
        Potential authentication logic here
        (if the user has already signed in, go to the next screen)
         */
        animateLogo();
        fadeInDemoButton();
    }

    /*
    Performs the logo animation of moving from the center of the layout to the top
     */
    private void animateLogo(){
        ImageView logo = (ImageView) findViewById(R.id.logo);
        int height = logo.getDrawable().getIntrinsicHeight();
        height *= -1;
        TranslateAnimation toTop = new TranslateAnimation(0, 0 , 0, height );
        toTop.setDuration(2000);
        toTop.setFillAfter(true);
        logo.startAnimation(toTop);
    }

    /*
    Method called in onCreate to fade in the button that prompts the user to sign in
     */
    private void fadeInDemoButton(){
        Button signInButton = (Button)findViewById(R.id.signInButton);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(2500);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        signInButton.startAnimation(animation);
    }

    /*
    This is the method called when the user decides to sign in(begin the demo). It
    takes the view as the parameter.
    @param view The view for the onClick to know which view is reference
     */
    public void transitionToClassScreen(View view){
        /*
        Potential authentication code goes here
         */
        Intent i = new Intent(this, ClassSelection.class);
        startActivity(i);
        finish();
    }

}
