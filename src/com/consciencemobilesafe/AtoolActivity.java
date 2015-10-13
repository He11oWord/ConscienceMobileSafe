package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AtoolActivity extends Activity implements OnClickListener{
	
	private Button number_quqry_button ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atool_layout);
		
		number_quqry_button = (Button) findViewById(R.id.number_quqry_button);
		
		number_quqry_button.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View v) {
		Intent intent ;
		
		switch(v.getId()){
		case R.id.number_quqry_button:
			intent = new Intent(this,NumberQueryActivity.class);
			startActivity(intent);
			
		}		
	}
	
}
