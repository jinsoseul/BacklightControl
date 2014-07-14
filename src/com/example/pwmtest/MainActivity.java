package com.example.pwmtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String TAG = "MAINLOG";
	public static final int MIN_VALUE = 10;

	SeekBar seekbar;
	TextView percentText, indexText, expText;
	Button measureBtn, modifyBtn;
	EditText modifyEdit;
	InputMethodManager imm;

	int home_brightness = -1;
	int app_brightness = -1;
	boolean save_flag = true;

	int send_progress = -1;
	int[] resultExpArray;
	String str_Path_Full;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate");
		init();

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				send_progress = (int) (10 + (progress * 2.45));
				new BrightnessJNI().setBrightness(send_progress);
				percentText.setText("" + progress + "%"); // 0~100
				indexText.setText("[" + send_progress + "]"); // 10~255
				expText.setText("" + new BrightnessJNI().getBrightness());
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

	}

	public void init() {
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		seekbar = (SeekBar) findViewById(R.id.seek01);
		percentText = (TextView) findViewById(R.id.percent_text);
		indexText = (TextView) findViewById(R.id.index_text);
		expText = (TextView) findViewById(R.id.exp_text);
		measureBtn = (Button) findViewById(R.id.measure_btn);
		modifyBtn = (Button) findViewById(R.id.modify_btn);
		modifyEdit = (EditText) findViewById(R.id.modify_edit);

		home_brightness = new BrightnessJNI().getBrightness();

		resultExpArray = new int[256];
		for (int i = 0; i < resultExpArray.length; i++) {
			new BrightnessJNI().setBrightness(i);
			resultExpArray[i] = new BrightnessJNI().getBrightness();
		}

	}

	public String getNowDateTime() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return new SimpleDateFormat("MMddHHmmss").format(date);
	}

	void writeLog(String str) {
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(
					str_Path_Full, true));
			bfw.write(str + "\n");
			bfw.flush();
			bfw.close();
		} catch (Exception e) {
		}

	}

	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.measure_btn:
			save_flag = false;
			Intent i = new Intent(MainActivity.this, MeasureActivity.class);
			startActivity(i);
			break;

		case R.id.modify_btn:
			String modifyStr = modifyEdit.getText().toString();
			if (send_progress == -1) {
				Toast.makeText(MainActivity.this, "SEEKBAR를 움직이세요",
						Toast.LENGTH_SHORT).show();
			} else if (modifyStr.equals("")) {
				Toast.makeText(MainActivity.this, "숫자를 입력하세요",
						Toast.LENGTH_SHORT).show();

			} else {
				int modifyInt = Integer.parseInt(modifyStr);
				if ((modifyInt < 0) || (modifyInt > 255)) {
					Toast.makeText(MainActivity.this, "0~255 입력하세요",
							Toast.LENGTH_SHORT).show();
				} else {
					imm.hideSoftInputFromWindow(modifyBtn.getWindowToken(), 0);
					int _send = (modifyInt << 16) + send_progress;
					resultExpArray[send_progress] = modifyInt;
					if (new BrightnessJNI().setBrightness(_send) == 0)
						Log.d(TAG, "setExpCurve err");
					else
						Log.d(TAG, "setExpCurve ok");
				}
			}
			break;

		case R.id.view_btn:
			save_flag = false;
			Intent intent = new Intent(MainActivity.this, TableActivity.class);
			intent.putExtra("expArray", resultExpArray);
			startActivity(intent);
			break;

		case R.id.save_btn:

			str_Path_Full = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/backlight"
					+ getNowDateTime()
					+ ".txt";
			File file = new File(str_Path_Full);
			if (file.exists() == false) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
			}

			for (int j = 0; j < resultExpArray.length; j++) {
				writeLog("" + resultExpArray[j]);
			}
			Toast.makeText(MainActivity.this, "[" + str_Path_Full + "] 저장 완료!",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		if ((save_flag == true)){ //home에서 들어온 경우
			home_brightness = new BrightnessJNI().getBrightness();
			new BrightnessJNI().setBrightness(app_brightness);
		}
		else
			save_flag =true;

		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		for (int i = 0; i < resultExpArray.length; i++) {
			if (resultExpArray[i] == home_brightness) {
				home_brightness = i;
				break;
			}
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		if (save_flag == true){
			app_brightness = new BrightnessJNI().getBrightness();
			new BrightnessJNI().setBrightness(home_brightness);
		
		}
		super.onStop();
	}

	@Override
	protected void onUserLeaveHint() {

		Log.d(TAG, "home");


//		save_flag = true;

		super.onUserLeaveHint();

	}

}