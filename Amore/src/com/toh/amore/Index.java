package com.toh.amore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.geemo.uart.driver.UART_DATA_Monitoring;
import com.geemo.uart.driver.UART_USB_DEVICE_Probe;
import com.geemo.uart.driver.UART_USB_Driver;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.gesture.GesturePoint;
import android.graphics.Typeface;
import android.hardware.usb.UsbManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Index extends Activity {

	// private static final String TAG = "WWW";

	VideoView videoView;
	ImageView weatherImage;
	int i1 = 0;
	int i2 = 0;
	int i3 = 0;
	int i4 = 0;
	int stopPosition;

	protected WeatherUtil mapUtil = null;
	// TextView myAddress;
	String dong;
	String cityName = null;
	String cityName1 = null;
	String cityName2 = null;
	String cityName3 = null;
	double latitude, longitude;
	TextView text, myAddress, tv2, tv4, high, low, speed, direct;
	Button button1, button2;

	private final String TAG = Index.class.getSimpleName(); // add pye
	private UART_USB_Driver mUART_Device; // add pye
	/**
	 * The system's USB service.
	 */
	private UsbManager mUsbManager;

	private final ExecutorService mExecutor = Executors
			.newSingleThreadExecutor();

	private UART_DATA_Monitoring mUART_Monitor;

	private final UART_DATA_Monitoring.UART_Monitoring mUART_Monitoring = new UART_DATA_Monitoring.UART_Monitoring() {

		@Override
		public void UART_RunError(Exception e) {
			Log.d(TAG, "Runner stopped.");
		}

		@Override
		public void UART_Receive_NewData(final byte[] data) {
			Index.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// Index.this.UART_Received_DATA(data);
				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			Log.e("###", "111");
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		videoView = (VideoView) findViewById(R.id.videoView);
		text = (TextView) findViewById(R.id.textView10); // ÅØ½ºÆ® °´Ã¼»ý¼º
		myAddress = (TextView) findViewById(R.id.area);
		mapUtil = WeatherUtil.newInstance();

		// video play by url
		// new YourAsyncTask().execute();

		// video play by file
		Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.cf);
		videoView.setVideoURI(video1);
		videoView.requestFocus();
		videoView.start();

		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.setLooping(true);

			}
		});

		GPSTracker gps = new GPSTracker(this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		}

		GeocoderHelper gh = new GeocoderHelper();
		gh.fetchCityName(getApplicationContext(), latitude, longitude);

		TextView tv1 = (TextView) findViewById(R.id.temperature);

		TextView tv22 = (TextView) findViewById(R.id.temp_index);
		Random r = new Random();
		i1 = (r.nextInt(40) + 1);
		tv22.setText(String.valueOf(i1) + "\u00b0");

		TextView tv3 = (TextView) findViewById(R.id.humidity);

		TextView tv44 = (TextView) findViewById(R.id.humid_index);
		Random ra = new Random();
		i2 = (ra.nextInt(99) + 1);
		tv44.setText(String.valueOf(i2) + "%");

		TextView wave = (TextView) findViewById(R.id.uvwave);
		wave.setText("ÀÚ¿Ü¼±");

		TextView skin = (TextView) findViewById(R.id.skin_index);
		skin.setText("ÇÇºÎÁö¼ö");

		button1 = (Button) findViewById(R.id.uv_number);
		Random ra1 = new Random();
		i3 = (ra1.nextInt(99) + 1);
		button1.setText(String.valueOf(i3));
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (i3 <= 30) {
					Intent k = new Intent(Index.this, uvResult1.class);
					startActivity(k);
				} else if (i3 > 30 && i3 <= 60) {
					Intent k = new Intent(Index.this, uvResult2.class);
					startActivity(k);
				} else if (i3 > 60 && i3 <= 99) {
					Intent k = new Intent(Index.this, uvResult3.class);
					startActivity(k);
				}
			}
		});

		button2 = (Button) findViewById(R.id.skinindex_number);
		Random ra2 = new Random();
		i4 = (ra2.nextInt(99) + 1);
		button2.setText(String.valueOf(i4));
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (i4 <= 20) {
					Intent k = new Intent(Index.this, skinResult1.class);
					startActivity(k);
				} else if (i4 > 30 && i4 <= 60) {
					Intent k = new Intent(Index.this, skinResult2.class);
					startActivity(k);
				} else if (i4 > 60 && i4 <= 99) {
					Intent k = new Intent(Index.this, skinResult3.class);
					startActivity(k);
				}
			}
		});

		new AsyncTask<String, Void, WeatherInfo>() {
			@Override
			protected void onPreExecute() {

			}

			@Override
			protected WeatherInfo doInBackground(String... params) {
				Log.e("###", "333");
				return mapUtil.getCurrentWeatherByCityLatLon(Index.this,
						latitude, longitude);
			}

			@Override
			protected void onPostExecute(WeatherInfo result) {
				if (mUART_Device == null) {
					tv2 = (TextView) findViewById(R.id.temp_index);
					// double st = result.getMain().getTemp();
					String strTemp = String.format("%.1f", result.getMain()
							.getTemp());
					tv2.setText(strTemp + "\u00b0");

					tv4 = (TextView) findViewById(R.id.humid_index);
					tv4.setText(result.getMain().getHumidity() + "%");

					TextView wave = (TextView) findViewById(R.id.uvwave);
					// wave.setTextSize(20);
					wave.setText("ÃÖ°í/ÃÖÀú");
					TextView skin = (TextView) findViewById(R.id.skin_index);
					// skin.setTextSize(20);
					skin.setText("Ç³Çâ/Ç³¼Ó");

					button1 = (Button) findViewById(R.id.uv_number);
					button1.setTextSize(18);
					String maxTemp = String.format("%.0f", result.getMain()
							.getTemp_max());
					String minTemp = String.format("%.0f", result.getMain()
							.getTemp_min());
					button1.setText(maxTemp + "\u00b0" + "/" + minTemp
							+ "\u00b0");

					button2 = (Button) findViewById(R.id.skinindex_number);
					button2.setTextSize(18);

					String winDirect = String.format("%.1f", result.getWind()
							.getDeg());
					double windir = Double.parseDouble(winDirect);
					String winDir = null;
					if (windir <= 22.5) {
						winDir = "ºÏ";
					} else if (windir > 22.5 && windir <= 67.5) {
						winDir = "ºÏµ¿";
					} else if (windir > 67.5 && windir <= 112.5) {
						winDir = "µ¿";
					} else if (windir > 112.5 && windir <= 157.5) {
						winDir = "³²µ¿";
					} else if (windir > 157.5 && windir <= 202.5) {
						winDir = "³²";
					} else if (windir > 202.5 && windir <= 247.5) {
						winDir = "³²¼­";
					} else if (windir > 247.5 && windir <= 292.5) {
						winDir = "¼­";
					} else if (windir > 292.5 && windir <= 337.5) {
						winDir = "ºÏ¼­";
					} else if (windir > 337.5) {
						winDir = "ºÏ";
					}

					String winSpeed = String.format("%.1f", result.getWind()
							.getSpeed());
					button2.setText(winDir + "/" + winSpeed);
				}

				// weatherImage.setImageBitmap(mapUtil.getIconBitmap(result));

				weatherImage = (ImageView) findViewById(R.id.imageWeather);
				String humd = result.getWeather().get(0).getDescription();
				System.out.println("humd" + humd);
				String icon = result.getWeather().get(0).getIcon();
				System.out.println("icon" + icon);
				if (icon.equals("01d")) {
					weatherImage.setImageResource(R.drawable.d01);
				} else if (icon.equals("01n")) {
					weatherImage.setImageResource(R.drawable.n01);
				} else if (icon.equals("02d")) {
					weatherImage.setImageResource(R.drawable.d02);
				} else if (icon.equals("02n")) {
					weatherImage.setImageResource(R.drawable.n02);
				} else if (icon.equals("03d")) {
					weatherImage.setImageResource(R.drawable.d03);
				} else if (icon.equals("03n")) {
					weatherImage.setImageResource(R.drawable.n03);
				} else if (icon.equals("04d")) {
					weatherImage.setImageResource(R.drawable.d04);
				} else if (icon.equals("04n")) {
					weatherImage.setImageResource(R.drawable.n04);
				} else if (icon.equals("09d")) {
					weatherImage.setImageResource(R.drawable.d09);
				} else if (icon.equals("09n")) {
					weatherImage.setImageResource(R.drawable.n09);
				} else if (icon.equals("10d")) {
					weatherImage.setImageResource(R.drawable.d10);
				} else if (icon.equals("10n")) {
					weatherImage.setImageResource(R.drawable.n10);
				} else if (icon.equals("11d")) {
					weatherImage.setImageResource(R.drawable.d11);
				} else if (icon.equals("11n")) {
					weatherImage.setImageResource(R.drawable.n11);
				} else if (icon.equals("13d")) {
					weatherImage.setImageResource(R.drawable.d13);
				} else if (icon.equals("13n")) {
					weatherImage.setImageResource(R.drawable.n13);
				} else if (icon.equals("50d")) {
					weatherImage.setImageResource(R.drawable.d50);
				} else if (icon.equals("50n")) {
					weatherImage.setImageResource(R.drawable.n50);
				} else {
					weatherImage.setImageResource(R.drawable.ic_launcher);
				}
			}
		}.execute(null, null, null);
	}

	public class GeocoderHelper {
		private final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient
				.newInstance(GeocoderHelper.class.getName());

		private boolean running = false;

		@SuppressLint("NewApi")
		public void fetchCityName(final Context context, final double lat,
				final double lon) {
			if (running)
				return;

			new AsyncTask<Void, Void, String>() {
				protected void onPreExecute() {
					running = true;
				};

				@Override
				protected String doInBackground(Void... params) {
					String cityName = null;
					String myCity = null;

					if (Geocoder.isPresent()) {
						try {
							Geocoder geocoder = new Geocoder(context,
									Locale.KOREAN);
							List<Address> addresses = geocoder.getFromLocation(
									lat, lon, 1);
							StringBuilder sb = new StringBuilder();

							if (addresses != null && addresses.size() > 0) {
								for (Address addr : addresses) {
									sb.append(addr.getMaxAddressLineIndex());
									for (int i = 0; i < addr
											.getMaxAddressLineIndex(); i++)
										sb.append(addr.getAddressLine(i));
								}
								// sb.append("===========\n");

								Address address1 = addresses.get(0);
								// sb.append(address.getCountryName()).append("\n");
								// sb.append(address.getPostalCode()).append("\n");
								sb.append(address1.getLocality()).append(" ");
								sb.append(address1.getSubLocality())
										.append(" ");
								sb.append(address1.getThoroughfare());
								// sb.append(address.getFeatureName()).append("\n\n");

								cityName = sb.toString();
								String str = cityName;
								str = str.substring(1);
								myCity = str.replaceAll("[null]+", "");
								// myAddress.setText(alphaAndDigits.toString());

							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// myAddress.setText("Can't get Address!");
						}
					}

					if (myCity != null) { // i.e., Geocoder succeed
						System.out.println("myCity" + myCity);
						Log.e("myCity", cityName);
						return myCity;
					} else {// i.e., Geocoder failed
						Log.e("myCity", "null");
						return fetchCityNameUsingGoogleMap();
					}
				}

				// Geocoder failed :-(
				// Our B Plan : Google Map
				private String fetchCityNameUsingGoogleMap() {
					String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
							+ lat + "," + lon + "&sensor=false&language=ko";

					try {
						JSONObject googleMapResponse = new JSONObject(
								ANDROID_HTTP_CLIENT.execute(new HttpGet(
										googleMapUrl),
										new BasicResponseHandler()));

						// many nested loops.. not great -> use expression
						// instead
						// loop among all results
						JSONArray results = (JSONArray) googleMapResponse
								.get("results");
						System.out.println("results" + results);
						for (int i = 0; i < results.length(); i++) {
							// loop among all addresses within this result
							JSONObject result = results.getJSONObject(i);
							System.out.println("result" + result);
							if (result.has("address_components")) {
								JSONArray addressComponents = result
										.getJSONArray("address_components");
								System.out.println("addressComponents"
										+ addressComponents);
								// loop among all address component to find a
								// 'locality' or 'sublocality'
								for (int j = 0; j < addressComponents.length(); j++) {
									JSONObject addressComponent = addressComponents
											.getJSONObject(j);
									System.out.println("addressComponent"
											+ addressComponent);
									if (result.has("types")) {
										JSONArray types = addressComponent
												.getJSONArray("types");
										System.out.println("types" + types);
										// search for locality and sublocality

										for (int k = 0; k < types.length(); k++) {
											Log.e("local", "111");
											if ("sublocality_level_3"
													.equals(types.getString(k))) {
												Log.e("local", "222");
												if (addressComponent
														.has("long_name")) {
													Log.e("local", "333");
													cityName = addressComponent
															.getString("long_name");
												} else if (addressComponent
														.has("short_name")) {
													Log.e("local", "444");
													cityName = addressComponent
															.getString("short_name");
												}
											}
										}
										System.out.println("cityName"
												+ cityName);

										for (int k = 0; k < types.length(); k++) {
											Log.e("local1", "111");
											if ("sublocality_level_2"
													.equals(types.getString(k))) {
												Log.e("local1", "222");
												if (addressComponent
														.has("long_name")) {
													Log.e("local1", "333");
													cityName1 = addressComponent
															.getString("long_name");
												} else if (addressComponent
														.has("short_name")) {
													Log.e("local1", "444");
													cityName1 = addressComponent
															.getString("short_name");
												}
											}
										}
										System.out.println("cityName1"
												+ cityName1);

										for (int k = 0; k < types.length(); k++) {
											Log.e("local2", "111");
											if ("sublocality_level_1"
													.equals(types.getString(k))) {
												Log.e("local2", "222");
												if (addressComponent
														.has("long_name")) {
													Log.e("local2", "333");
													cityName2 = addressComponent
															.getString("long_name");
												} else if (addressComponent
														.has("short_name")) {
													Log.e("local2", "444");
													cityName2 = addressComponent
															.getString("short_name");
												}
											}
										}
										System.out.println("cityName2"
												+ cityName2);

										for (int k = 0; k < types.length(); k++) {
											Log.e("local3", "111");
											if ("locality".equals(types
													.getString(k))) {
												Log.e("local3", "222");
												if (addressComponent
														.has("long_name")) {
													Log.e("local3", "333");
													cityName3 = addressComponent
															.getString("long_name");
												} else if (addressComponent
														.has("short_name")) {
													Log.e("local3", "444");
													cityName3 = addressComponent
															.getString("short_name");
												}
											}
										}
									}
								}

								StringBuilder sb = new StringBuilder();
								sb.append(cityName3).append(" ");
								sb.append(cityName2).append(" ");
								sb.append(cityName1).append(" ");
								sb.append(cityName);
								String address = sb.toString();
								String myCity = address.replaceAll("[null]+",
										"");

								if (myCity != null) {
									return myCity;
								}
							}
						}
					} catch (Exception ignored) {
						ignored.printStackTrace();
					}
					return null;
				}

				protected void onPostExecute(String cityName) {
					running = false;
					if (cityName != null) {
						// Do something with cityName
						myAddress.setText(cityName);
						Log.e("GeocoderHelper", cityName);
					}
				};
			}.execute();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopPosition = videoView.getCurrentPosition(); // stopPosition is an int
		videoView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		videoView.seekTo(stopPosition);
		videoView.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		videoView.stopPlayback();
	}
}