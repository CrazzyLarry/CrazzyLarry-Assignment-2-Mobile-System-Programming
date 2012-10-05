package no.hig.ass2_120297_120296;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	// TestCommment
	private SensorManager mSensorManager;
	private Sensor mCompass;
	private Sensor mLight;
	private TextView mTextView;
	private ImageView mImageView;
	private ImageView mDirekt;
	private char orits = 'A';
	private char orit;
	private float currentLuminosity;
	private boolean dayTheme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayoutNightTheme();
		
	}
	
	
	public void setLayoutDayTheme(){
		
		setContentView(R.layout.day_theme);
		dayTheme = true;
		intialComponents();
	}
	
	
	public void setLayoutNightTheme(){
		
		setContentView(R.layout.activity_main);
		dayTheme = false;
		intialComponents();
	}
	
	

	public void intialComponents(){
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mTextView = (TextView) findViewById(R.id.tvSensor);
		mImageView = (ImageView) findViewById(R.id.compass);
		mDirekt = (ImageView) findViewById(R.id.directionArrow);
						
		mDirekt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if(dayTheme == false){
					//Lock the direction and highlight the direction arrow
					if (orits == 'A') {
						orits = orit;
						int id = getResources().getIdentifier
						("no.hig.ass2_120297_120296:drawable/direction_night_activated", null, null);
						mDirekt.setImageResource(id);
					} else {
						//Unlock the direction and remove the highlight the direction arrow
						orits = 'A';
						int id = getResources().getIdentifier
								("no.hig.ass2_120297_120296:drawable/direction_night_deactivated", null, null);
								mDirekt.setImageResource(id);
					}
				}
				if(dayTheme == true){
					//Lock the direction and highlight the direction arrow
					if (orits == 'A') {
						orits = orit;
						int id = getResources().getIdentifier
						("no.hig.ass2_120297_120296:drawable/direction_day_activated", null, null);
						mDirekt.setImageResource(id);
					} else {
						//Unlock the direction and remove the highlight the direction arrow
						orits = 'A';
						int id = getResources().getIdentifier
								("no.hig.ass2_120297_120296:drawable/direction_day_deactivated", null, null);
								mDirekt.setImageResource(id);
					}
					
					
				}
		   }
		});
	}

	
	// The following method is required by the SensorEventListener interface;
	public void onAccuracyChanged(Sensor sensor, int accuracy){}

	// The following method is required by the SensorEventListener interface;
	// Hook this event to process updates;
	public void onSensorChanged(SensorEvent event) {
		//If function called by Light Sensor
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			currentLuminosity = Math.round(event.values[0]);
			if (currentLuminosity < 15 && dayTheme==true) {
				setLayoutNightTheme();
				
			}
			if (currentLuminosity > 40 && dayTheme==false) {			
				setLayoutDayTheme();
				
			}
		}
		//If function called by Orientation sensor
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			float azimuth = Math.round(event.values[0]);
			
			// direction north
			if (azimuth < 45.0 || azimuth > 315.0) {
				mTextView.setText(Float.toString(azimuth) + "°" + " "
						+ getString(R.string.north));
				mImageView.setRotation(0);
				mImageView.setRotation(360 - azimuth);
				orit = 'N';
			};
			
			// direction east
			if (azimuth > 45.0 && azimuth < 135.0) {
				mTextView.setText(Float.toString(azimuth) + "°" + " "
						+ getString(R.string.east));
				mImageView.setRotation(0);
				mImageView.setRotation(-azimuth);
				orit = 'E';
			};
			
			// direction south
			if (azimuth > 135.0 && azimuth < 225.0) {
				mTextView.setText(Float.toString(azimuth) + "°" + " "
						+ getString(R.string.south));
				mImageView.setRotation(0);
				mImageView.setRotation(-azimuth);
				orit = 'S';
			};
			
			// direction west
			if (azimuth > 225.0 && azimuth < 315.0) {
				mTextView.setText(Float.toString(azimuth) + "°" + " "
						+ getString(R.string.west));
				mImageView.setRotation(0);
				mImageView.setRotation(-azimuth);
				orit = 'W';
			};
			//If leave a set direction
			if (orits != 'A' && orit != orits) {
				Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibe.vibrate(1000);
			}

		}
		
	}

	@Override
	protected void onPause() {
		// Unregister the listener on the onPause() event to preserve battery
		// life;
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		mSensorManager.registerListener(this, mCompass,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mLight,
				SensorManager.SENSOR_DELAY_NORMAL);
	}
}
