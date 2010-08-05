package com.vasken.admob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.vasken.admob.manager.LoginResponse;
import com.vasken.admob.manager.Worker;

public class Welcome extends Activity {
	private String token;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        prepareLoginButton((Button)findViewById(R.id.signin));
    }

	private void prepareLoginButton(Button signin) {
		signin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String email = ((EditText)findViewById(R.id.email)).getText().toString();
				String password = ((EditText)findViewById(R.id.password)).getText().toString();
				
				LoginResponse response = Worker.login(getApplicationContext(), email, password);
				token = response.getToken();
				
				if (token!= null && token.length() > 0) {
					Log.d("------", token);
					Intent intent = new Intent(Welcome.this, Sites.class);
					intent.putExtra(LoginResponse.TOKEN, token);
					startActivity(intent);
				}
				
			}
		});
	}
    
}