package com.set;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableRow;

public class MainActivity extends ActionBarActivity {
	private ImageView[] cards;
	private TableRow[] rows;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		rows = new TableRow[4];
		cards = new ImageView[16];
		
		rows[0] = (TableRow)findViewById(R.id.tableRow1);
		rows[1] = (TableRow)findViewById(R.id.tableRow2);
		rows[2] = (TableRow)findViewById(R.id.tableRow3);
		rows[3] = (TableRow)findViewById(R.id.tableRow4);
		
		for(int i = 0;i < 4;++i){
			for(int j = 0;j < 4;++j){
				cards[4 * i + j] = (ImageView)rows[i].getChildAt(j);
				cards[4 * i + j].setImageDrawable(new CardDrawable(Cards.valueOf(1 + (int)(Math.random() * 3), 1 + (int)(Math.random() * 3), 1 + (int)(Math.random() * 3), 1 + (int)(Math.random() * 3))));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		if(id == R.id.action_exit){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}