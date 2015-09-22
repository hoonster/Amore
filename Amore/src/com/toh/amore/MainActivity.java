package com.toh.amore;

import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class MainActivity extends ActionBarActivity {

	VideoView videoView;
	Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		videoView = (VideoView) findViewById(R.id.videoView);

		// video play by file
		Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.ap3);
		videoView.setVideoURI(video1);
		videoView.requestFocus();
		videoView.start();

		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				// mp.setLooping(true);
				timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						Intent k = new Intent(MainActivity.this, Index.class);
						startActivity(k);
					}
				}, 10000);
			}
		});
	}

	@Override
	protected void onResume() {
		videoView.resume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		videoView.suspend();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		videoView.stopPlayback();
		timer.cancel();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
