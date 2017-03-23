package com.example.getchannels;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private EditText et;
	private EditText et_channel_name;
	private Button bt;
	private TextView tv;
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pm = getPackageManager();
		initView();
	}

	private void initView() {
		et = (EditText) findViewById(R.id.et);
		et_channel_name = (EditText) findViewById(R.id.et_channel_name);
		bt = (Button) findViewById(R.id.bt);
		tv = (TextView) findViewById(R.id.tv);
		bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		tv.setText(null);
		File file = new File(Environment.getExternalStorageDirectory(), et.getText().toString());
//		File file = new File("/storage/sdcard1", et.getText().toString());外置sd卡
		String[] apks = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".apk");
			}
		});

		int length = apks.length;
		for (int i = 0; i < length; i++) {
			PackageInfo packageInfo = pm.getPackageArchiveInfo(new File(file,
					apks[i]).getAbsolutePath(), PackageManager.GET_META_DATA);
			Object channel = packageInfo.applicationInfo.metaData
					.get(et_channel_name.getText().toString());
			if (channel != null) {
//				tv.append(apks[i].substring(0, apks[i].indexOf(".")) + "\n");从前往后去截取字符串
				tv.append(apks[i].substring(0, apks[i].lastIndexOf(".")) + "\n");
				tv.append(packageInfo.packageName+"\n");
				tv.append(channel + "\n");
			} else {
				Toast.makeText(this, "请检查渠道号的name是否输入正确", Toast.LENGTH_LONG)
						.show();
			}
		}
		Toast.makeText(this, "渠道号获取成功，一共" + length + "个，长按渠道号区域可进行复制", Toast.LENGTH_LONG)
				.show();

	}

}
