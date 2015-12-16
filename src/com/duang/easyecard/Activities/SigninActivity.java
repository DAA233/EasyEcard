package com.duang.easyecard.Activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.duang.easyecard.R;
import com.duang.easyecard.GlobalData.HttpClientData;
import com.duang.easyecard.GlobalData.UrlConstant;
import com.duang.easyecard.Utils.ImageUtil;
import com.duang.easyecard.Utils.ImageUtil.OnLoadImageListener;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SigninActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {
	
	private HttpClientData httpClientData;
	private HttpClient httpClient = new DefaultHttpClient();
	
	private Spinner signinTypeSpinner;
	private EditText accountInput;
	private EditText passwordInput;
	private EditText checkcodeInput;
	private TextView accountText;
	private TextView hintText;
	private Button signinButton;
	private ImageView checkcodeImage;
	
	private String signtype;  // {"SynSno", "SynCard"}
	private String username;
	private String password;
	private String checkcode;
	
	private List<String> spinnerList = new ArrayList<String>();
	private ArrayAdapter<String> spinnerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		
		initView();
	}

	public void initView() {
		// 实例化控件
		signinTypeSpinner = (Spinner) findViewById(R.id.signin_type_spinner);
		accountInput = (EditText) findViewById(R.id.signin_account_input);
		passwordInput = (EditText) findViewById(R.id.signin_password_input);
		checkcodeInput = (EditText) findViewById(R.id.signin_checkcode_input);
		accountText = (TextView) findViewById(R.id.signin_account_text);
		hintText = (TextView) findViewById(R.id.signin_hint_text);
		signinButton = (Button) findViewById(R.id.signin_signin_button);
		checkcodeImage = (ImageView) findViewById(R.id.signin_checkcode_image);
		
		// 设置Spinner
		// 添加列表项
		spinnerList.add("学工号");
		spinnerList.add("校园卡账号");
		// 新建适配器，利用系统内置的layout
		spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
		// 设置下拉菜单样式，利用系统内置的layout
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定适配器到控件
		signinTypeSpinner.setAdapter(spinnerAdapter);
		// 设置选择响应事件
		signinTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// 选中响应事件
				if (position == 0) {
					accountText.setText("学 工 号");
					accountInput.setHint("请输入学（工）号");
					signtype = "SynSno";
					hintText.setText("提示：请输入学（工）号");
				} else if (position == 1) {
					accountText.setText("校园卡号");
					accountInput.setHint("请输入校园卡账号");
					signtype = "SynCard";
					hintText.setText("提示：请输入校园卡账号");
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// 什么都没选中
			}
		});
		
		getCheckcodeImage();  // 获取验证码
		
		// 监听EditText的焦点改变事件
		accountInput.setOnFocusChangeListener(this);
		passwordInput.setOnFocusChangeListener(this);
		checkcodeInput.setOnFocusChangeListener(this);
		
		// 监听控件的点击事件
		signinButton.setOnClickListener(this);
		checkcodeImage.setOnClickListener(this);
	}
	
	private void getCheckcodeImage() {
		// 获取验证码图片
		ImageUtil.onLoadImage(UrlConstant.GET_CHECKCODE_IMG, httpClient, new OnLoadImageListener() {
			
			@Override
			public void OnLoadImage(Bitmap bitmap, String bitmapPath) {
				// TODO Auto-generated method stub
				if (bitmap != null) {
					checkcodeImage.setImageBitmap(bitmap);
					checkcodeInput.setText(null);
				}
			}
		});
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// 控件焦点的改变事件
		switch (v.getId()) {
		case R.id.signin_account_input:
			if (!hasFocus) {
				if (!accountInput.getText().toString().isEmpty()) {
					if (passwordInput.getText().toString().isEmpty()) {
						hintText.setText("提示：请输入密码");
					}
				}
			}
			break;
		case R.id.signin_password_input:
			if (hasFocus) {
				if (accountInput.getText().toString().isEmpty()) {
					if (signtype.equals("SynSno")) {
						hintText.setText("提示：请输入学（工）号");
					} else {
						hintText.setText("提示：请输入校园卡账号");
					}
				}
			} else {
				// 失去焦点
				if (!accountInput.getText().toString().isEmpty()) {
					if (passwordInput.getText().toString().isEmpty()) {
						hintText.setText("提示：请输入密码");
					}
				}
			}
			break;
		case R.id.signin_checkcode_input:
			if (hasFocus) {
				if (accountInput.getText().toString().isEmpty()) {
					if (signtype.equals("SynSno")) {
						hintText.setText("提示：请输入学（工）号");
					} else {
						hintText.setText("提示：请输入校园卡账号");
					}
				} else if (passwordInput.getText().toString().isEmpty()) {
					hintText.setText("提示：请输入密码");
				} else {
					hintText.setText("提示：如果看不清，试试点击图片换一张");
				}
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		// 控件的点击事件
		switch (v.getId()) {
		// 验证码图片
		case R.id.signin_checkcode_image:
			getCheckcodeImage();
			break;
		// 点击登录按钮
		case R.id.signin_signin_button:
			if (!accountInput.getText().toString().isEmpty()) {
				if (!passwordInput.getText().toString().isEmpty()) {
					if (!checkcodeInput.getText().toString().isEmpty()) {
						// 发送POST请求
						sendPostRequest();
					} else {
						hintText.setText("提示：请输入验证码");
					}
				} else {
					hintText.setText("提示：请输入密码");
				}
			} else {
				hintText.setText("提示：请输入学（工）号");
			}
			break;
		default:
			break;
		}
	}

	private void sendPostRequest() {
		// 发送POST请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 创建一个HttpPost对象
				HttpPost httpPost = new HttpPost(UrlConstant.MINI_CHECK_IN);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				checkcode = checkcodeInput.getText().toString();
				username = accountInput.getText().toString();
				password = passwordInput.getText().toString();
				try {
					// 装填POST数据
					params.add(new BasicNameValuePair("checkcode", checkcode));
					params.add(new BasicNameValuePair("IsUsedKeyPad", "False"));
					params.add(new BasicNameValuePair("signtype", signtype));
					params.add(new BasicNameValuePair("username", username));
					params.add(new BasicNameValuePair("password", password));
					
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
					httpPost.setEntity(entity);
					// 执行POST请求
					HttpResponse httpResponse = httpClient.execute(httpPost);
					// 如果服务器成功地返回响应
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
				        String httpResponseString = EntityUtils.toString(httpResponse.getEntity());
				        if (httpResponseString.equals("success|False")) {
				        	// 登录成功
				        	Log.d("response", "success");
				        	hintText.setText("提示：登录成功！");
				        } else {
				        	// 登录发生错误
				        	hintText.setText("提示：" + httpResponseString);
				        	if (httpResponseString.contains("查询密码")) {
				        		passwordInput.setText("");
				        	} else if (httpResponseString.contains("验证码")) {
				        		getCheckcodeImage();
				        	}
				        }
				        Log.d("Response", httpResponseString);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
