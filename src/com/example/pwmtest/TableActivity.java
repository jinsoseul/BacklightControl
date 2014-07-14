package com.example.pwmtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TableActivity extends Activity {
	public static final String TAG = "TABLELOG";

	TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);

		t1 = (TextView) findViewById(R.id.table01);
		t2 = (TextView) findViewById(R.id.table02);
		t3 = (TextView) findViewById(R.id.table03);
		t4 = (TextView) findViewById(R.id.table04);
		t5 = (TextView) findViewById(R.id.table05);
		t6 = (TextView) findViewById(R.id.table06);
		t7 = (TextView) findViewById(R.id.table07);
		t8 = (TextView) findViewById(R.id.table08);
		t9 = (TextView) findViewById(R.id.table09);
		t10 = (TextView) findViewById(R.id.table10);

		Intent intent = new Intent(this.getIntent());
		int[] expArray = intent.getIntArrayExtra("expArray");

		for (int i = 0; i < expArray.length; i++) {

			switch (i % 10) {
			case 0:
				t1.append("" + expArray[i] + "\n");
				break;
			case 1:
				t2.append("" + expArray[i] + "\n");
				break;
			case 2:
				t3.append("" + expArray[i] + "\n");
				break;
			case 3:
				t4.append("" + expArray[i] + "\n");
				break;
			case 4:
				t5.append("" + expArray[i] + "\n");
				break;
			case 5:
				t6.append("" + expArray[i] + "\n");
				break;
			case 6:
				t7.append("" + expArray[i] + "\n");
				break;
			case 7:
				t8.append("" + expArray[i] + "\n");
				break;
			case 8:
				t9.append("" + expArray[i] + "\n");
				break;
			case 9:
				t10.append("" + expArray[i] + "\n");
				break;

			}

		}

	}
}
