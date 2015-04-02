package com.set;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	public ImageView[] cards = new ImageView[16];
	public TableRow[] rows = new TableRow[4];
	public boolean[] marked = new boolean[16];
	public boolean[] active = new boolean[16];
	public int[] value = new int[15];
	public int cont = 0;
	public int N;
	ArrayList<Integer> deck;
	public int posDeck;
	public int id[] = new int[3];
	
	public Handler handler = new Handler();
	
	Chronometer chrono;
	boolean started = false;
	TextView scoreText,endText;
	public int score = 0,score2 = 0;
	ImageView scoreImage[] = new ImageView[3];
	
	void cleanCards(int n){
		for(int i = 0,j = 0;i < n;++i){
			if(!marked[i]){
				value[j] = value[i];
				++j;
			}else marked[i] = false;
		}
		
		for(int i = 0;i < n - 3;++i)
			cards[i].setImageDrawable(new CardDrawable(value[i]));
		
		for(int i = n - 3;i < n;++i)
			cards[i].setImageResource(android.R.color.transparent);
	}
	
	void endGame(){
		chrono.stop();
		endText.setText("Fin du jeu");
	}
	
	Client client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		chrono = (Chronometer)findViewById(R.id.chronometer1);
		scoreText = (TextView)findViewById(R.id.textView1);
		
		scoreImage[0] = (ImageView)findViewById(R.id.imageView16);
		scoreImage[1] = (ImageView)findViewById(R.id.imageView17);
		scoreImage[2] = (ImageView)findViewById(R.id.imageView18);
		
		endText = (TextView)findViewById(R.id.textView2);
		
		rows[0] = (TableRow)findViewById(R.id.tableRow1);
		rows[1] = (TableRow)findViewById(R.id.tableRow2);
		rows[2] = (TableRow)findViewById(R.id.tableRow3);
		rows[3] = (TableRow)findViewById(R.id.tableRow4);
		
		startSolo();
	}
	
	public void clean(){
		started = false;
		score = 0;
		score2 = 0;
		posDeck = 0;
		
		scoreImage[0].setImageResource(android.R.color.transparent);
		scoreImage[1].setImageResource(android.R.color.transparent);
		scoreImage[2].setImageResource(android.R.color.transparent);
		
		chrono.stop();
		chrono.setBase(SystemClock.elapsedRealtime());
		chrono.start();
		chrono.stop();
		
		int it = N / 3;
		
		for(int i = 0;i < it;++i)
			cleanCards(N - 3 * i);
		
		for(int i = 0;i < 15;++i){
			marked[i] = false;
			active[i] = false;
		}
	}
	
	public void paintCards(ArrayList<Integer> deck){
		for(int i = 0;i < N;++i)
			value[i] = deck.get(i);
		
		for(int i = 0;i < 4;++i){
			for(int j = 0;j < 4;++j){
				if(i < 3 || (i == 3 && j < 3))
					cards[4 * i + j] = (ImageView)rows[i].getChildAt(j);
				
				if(i < 3)
					cards[4 * i + j].setImageDrawable(new CardDrawable(value[4 * i + j]));
			
			}
		}
	}

	void startSolo(){
		scoreText.setText("Score : " + 0);
		
		deck = Cards.generateDeck();
		N = 12;
		posDeck = 12;
		
		paintCards(deck);
		
		for(int i = 0;i < 15;++i){
			final int i2 = i;
			
			cards[i].setOnClickListener(new OnClickListener () {
		        @Override
		        public void onClick(View v) {
		        	if(i2 >= N || active[i2]) return;
		        	
		        	if(!started){
		        		chrono.setBase(SystemClock.elapsedRealtime());
		        		chrono.start();
		        		started = true;
		        	}
		        	
		        	System.out.println("cont = " + cont);
		        	
		        	if(!marked[i2]){
		                cards[i2].setColorFilter(Color.argb(50, 0, 0, 0));
		                marked[i2] = true;
		                ++cont;
		                
		                if(cont == 3){
		                	int pos = 0;
		                	
		                	for(int k = 0;k < N;++k){
		                		if(marked[k]){
		                			id[pos++] = k;
		                		}
		                	}
		                	
		                	if(Cards.isSet(value[ id[0] ], value[ id[1] ], value[ id[2] ])){
		                		for(int k = 0;k < 3;++k){
		                			cards[ id[k] ].setColorFilter(Color.argb(100, 0, 200, 0));
		                			active[ id[k] ] = true;
		                			scoreImage[k].setImageDrawable(new CardDrawable(value[ id[k] ]));
		                		}
		                		N -= 3;
	                			score++;
	                			scoreText.setText("Score : " + score);
		                		
		                		handler.postDelayed(new Runnable(){
		                			public void run(){
		                				for(int k = 0;k < 3;++k){
		                					cards[ id[k] ].setColorFilter(Color.argb(0, 0, 0, 0));
		                					active[ id[k] ] = false;
		                					--cont;
				                		}
		                				
		                				if(N == 12)
				                			cleanCards(15);
				                		
				                		if((N == 9 && 81 - posDeck >= 3) || (N == 12 && !Cards.test(value,N))){
			                				for(int k = 0;k < 3;++k){
			                					value[ id[k] ] = deck.get(posDeck); posDeck++;
			                					cards[ id[k] ].setImageDrawable(new CardDrawable(value[ id[k] ]));
			                					marked[ id[k] ] = false;
			                				}
			                				
			                				N += 3;
			                					
			                				if(!Cards.test(value,N) && 81 - posDeck >= 3){
			                					for(int k = 0;k < 3;++k){
			                						value[12 + k] = deck.get(posDeck); posDeck++;
			                						cards[12 + k].setImageDrawable(new CardDrawable(value[12 + k]));
			                					}
			                					
			                					N += 3;
			                					
			                					if(!Cards.test(value,N)){
			                						//TODO:finir le jeu
			                						endGame();
			                					}
			                				}
				                		}else if(posDeck == 81){
				                			cleanCards(N + 3);
				                			
				                			if(!Cards.test(value,N)){
				                				//TODO:finir le jeu
				                				endGame();
				                			}
				                		}
		                			}
		                		}, 500);
		                		
		                	}else{
		                		for(int k = 0;k < 3;++k){
		                			cards[ id[k] ].setColorFilter(Color.argb(100, 200, 0, 0));
		                			active[ id[k] ] = true;
		                		}

	                			score--;
	                			scoreText.setText("Score : " + score);
		                		
	                			handler.postDelayed(new Runnable(){
	                				public void run(){
	                					for(int k = 0;k < 3;++k){
		                					cards[ id[k] ].setColorFilter(Color.argb(0, 0, 0, 0));
		                					marked[ id[k] ] = false;
		                					active[ id[k] ] = false;
		                					--cont;
	                					}
	                				}
	                			}, 500);
		                	}
		                }
		        	}else{
		        		cards[i2].setColorFilter(Color.argb(0, 0, 0, 0));
		        		marked[i2] = false;
		        		--cont;
		        	}
		        }
		   });
		}
	}
	
	void startMulti(){
		client = new Client(this,handler);
		client.start();
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
		
		if(id == R.id.action_solo){
			clean();
			startSolo();
		}
		
		if(id == R.id.action_multi){
			clean();
			startMulti();
		}
		
		return super.onOptionsItemSelected(item);
	}
}