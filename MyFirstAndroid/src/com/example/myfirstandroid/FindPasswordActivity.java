package com.example.myfirstandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public final class FindPasswordActivity extends Activity{

	private TabHost mTabHost;
	private Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		initialiseTabHost();
		submit = (Button)findViewById(R.id.submit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_password, menu);
		return true;
	}

	private void initialiseTabHost() {
        mTabHost = (TabHost)findViewById(R.id.find_tabhost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("手机找回密码").setContent(R.id.find_phone_num));
        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.color.button_tab_bg);
        TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextSize(17);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("邮箱找回密码").setContent(R.id.find_email));
        mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.color.button_tab_bg);
        tv = (TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv.setTextSize(17);
        // Default to first tab
        //this.onTabChanged("Tab1");
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tab) {
            	Toast.makeText(getApplicationContext(), "tab changed",
        				Toast.LENGTH_SHORT).show();
        		if(tab.equals("tab2")){
        			submit.setText("发送验证邮件");
        		}else{
        			submit.setText("提交");
        		}
            }
        });
    }
 
   
    
    public void Configure(View view){
    	Intent intent = new Intent(this, LogInActivity.class);
    	startActivity(intent);
    }
    public void Send(View view){
    	
    }
}
