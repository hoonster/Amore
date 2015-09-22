package com.toh.amore;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

public class uvResult3 extends Activity {

	ImageView image1, image2, image3, image4;
	Animation animationSlideInLeft, animationSlideOutRight;
	ImageView curSlidingImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uvresult3);

		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		image4 = (ImageView) findViewById(R.id.image4);
		
		int width = 400;
		int height = 370;
		Bitmap bmp1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.apimage1);
		Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.apimage2);
		Bitmap bmp3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.apimage3);
		Bitmap bmp4 = BitmapFactory.decodeResource(getResources(),
				R.drawable.apimage4);

		Bitmap resizedbitmap1 = Bitmap.createScaledBitmap(bmp1, width, height,
				true);
		Bitmap resizedbitmap2 = Bitmap.createScaledBitmap(bmp2, width, height,
				true);
		Bitmap resizedbitmap3 = Bitmap.createScaledBitmap(bmp3, width, height,
				true);
		Bitmap resizedbitmap4 = Bitmap.createScaledBitmap(bmp4, width, height,
				true);
		image1.setImageBitmap(resizedbitmap1);
		image2.setImageBitmap(resizedbitmap2);
		image3.setImageBitmap(resizedbitmap3);
		image4.setImageBitmap(resizedbitmap4);

		animationSlideInLeft = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		animationSlideOutRight = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
		animationSlideInLeft.setDuration(2000);
		animationSlideOutRight.setDuration(4000);
		animationSlideInLeft.setAnimationListener(animationSlideInLeftListener);
		animationSlideOutRight
				.setAnimationListener(animationSlideOutRightListener);

		curSlidingImage = image1;
		image1.startAnimation(animationSlideInLeft);
		image1.setVisibility(View.VISIBLE);

		Button button2 = (Button) findViewById(R.id.go_buy);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent openBrowser = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://www.amorepacificmall.com/main.do?source=https://www.google.co.kr/"));
				startActivity(openBrowser);

			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		image1.clearAnimation();
		image2.clearAnimation(); 
		image3.clearAnimation();
		image4.clearAnimation();
	}

	AnimationListener animationSlideInLeftListener = new AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub

			if (curSlidingImage == image1) {
				image1.startAnimation(animationSlideOutRight);
			} else if (curSlidingImage == image2) {
				image2.startAnimation(animationSlideOutRight);
			} else if (curSlidingImage == image3) {
				image3.startAnimation(animationSlideOutRight);
			} else if (curSlidingImage == image4) {
				image4.startAnimation(animationSlideOutRight);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
	};

	AnimationListener animationSlideOutRightListener = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			if (curSlidingImage == image1) {
				curSlidingImage = image2;
				image2.startAnimation(animationSlideInLeft);
				image1.setVisibility(View.INVISIBLE);
				image2.setVisibility(View.VISIBLE);
				image3.setVisibility(View.INVISIBLE);
				image4.setVisibility(View.INVISIBLE);
			} else if (curSlidingImage == image2) {
				curSlidingImage = image3;
				image3.startAnimation(animationSlideInLeft);
				image1.setVisibility(View.INVISIBLE);
				image2.setVisibility(View.INVISIBLE);
				image3.setVisibility(View.VISIBLE);
				image4.setVisibility(View.INVISIBLE);
			} else if (curSlidingImage == image3) {
				curSlidingImage = image4;
				image4.startAnimation(animationSlideInLeft);
				image1.setVisibility(View.INVISIBLE);
				image2.setVisibility(View.INVISIBLE);
				image3.setVisibility(View.INVISIBLE);
				image4.setVisibility(View.VISIBLE);
			} else if (curSlidingImage == image4) {
				curSlidingImage = image1;
				image1.startAnimation(animationSlideInLeft);
				image1.setVisibility(View.VISIBLE);
				image2.setVisibility(View.INVISIBLE);
				image3.setVisibility(View.INVISIBLE);
				image4.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}
	};
}
