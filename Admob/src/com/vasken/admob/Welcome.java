package com.vasken.admob;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.vasken.admob.manager.Response;
import com.vasken.admob.manager.Worker;

public class Welcome extends Activity {
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
				
				Response response = Worker.login(getApplicationContext(), email, password);
				
				Log.d("------", response.toString());
			}
		});
	}
    
}