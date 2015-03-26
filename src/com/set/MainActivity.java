package com.set;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;

import java.util.Collections;

public class MainActivity extends ActionBarActivity {
	private ImageView[] cards;
	private TableRow[] rows;
	private boolean[] marked;
	private int[] value;
	int cont = 0;
	//private int[] deck;
	int N;
	ArrayList<Integer> aux;
	int posAux;
	
	boolean test(int val[], int n){
		for(int i = 0;i < n;++i)
			for(int j = i + 1;j < n;++j)
				for(int k = j + 1;k < n;++k)
					if(Cards.isSet(val[i], val[j], val[k])){
						System.out.println(i + " " + j + " " + k);
						return true;
					}
		System.out.println("Fin");
		return false;
	}
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//deck = new int[81];
		aux = new ArrayList<Integer>();
		
		for(int i = 0;i < 3;++i)
			for(int j = 0;j < 3;++j)
				for(int k = 0;k < 3;++k)
					for(int l = 0;l < 3;++l)
						//deck[i + 3 * j + 9 * k + 27 * l] = Cards.valueOf(1 + i, 1 + j, 1 + k, 1 + l);
						aux.add(Cards.valueOf(1 + i, 1 + j, 1 + k, 1 + l));
		
		N = 12;
		
		boolean ok = false;
		
		value = new int[15];
		
		while(!ok){
			Collections.shuffle(aux);
			
			for(int i = 0;i < N;++i)
				//value[i] = deck[ aux.get(i) ];
				value[i] = aux.get(i);
			
			if(test(value,N))
				ok = true;
		}
		
		posAux = 12;
		
		rows = new TableRow[4];
		cards = new ImageView[16];
		marked = new boolean[16];
		
		rows[0] = (TableRow)findViewById(R.id.tableRow1);
		rows[1] = (TableRow)findViewById(R.id.tableRow2);
		rows[2] = (TableRow)findViewById(R.id.tableRow3);
		rows[3] = (TableRow)findViewById(R.id.tableRow4);
		
		for(int i = 0;i < 4;++i){
			for(int j = 0;j < 4;++j){
				if(i < 3 || (i == 3 && j < 3))
					cards[4 * i + j] = (ImageView)rows[i].getChildAt(j);
				
				if(i < 3)
					cards[4 * i + j].setImageDrawable(new CardDrawable(value[4 * i + j]));
			
			}
		}
		
		for(int i = 0;i < 15;++i){
			final int i2 = i;
			
			cards[i].setOnClickListener(new OnClickListener () {
		        @Override
		        public void onClick(View v) {
		        	if(i2 >= N) return;
		        	
		        	//System.out.println("cont = " + cont);
		        	if(!marked[i2]){
		                cards[i2].setColorFilter(Color.argb(50, 0, 0, 0));
		                marked[i2] = true;
		                ++cont;
		                
		                if(cont == 3){
		                	int id[] = new int[3],pos = 0;
		                	
		                	for(int k = 0;k < N;++k){
		                		if(marked[k]){
		                			id[pos++] = k;
		                		}
		                	}
		                	
		                	if(Cards.isSet(value[ id[0] ], value[ id[1] ], value[ id[2] ])){
		                		for(int k = 0;k < 3;++k)
		                			cards[ id[k] ].setColorFilter(Color.GREEN);
		                		N -= 3;
		                		
		                		// TODO: Sleep
		                		
		                		for(int k = 0;k < 3;++k){
                					cards[ id[k] ].setColorFilter(Color.argb(0, 0, 0, 0));
                					//marked[ id[k] ] = false;
                					--cont;
		                		}
		                		
		                		//setImageResource(android.R.color.transparent);
		                		if(N == 12)
		                			cleanCards(15);
		                		
		                		if((N == 9 && 81 - posAux >= 3) || (N == 12 && !test(value,N))){
		                			// N == 9
	                				for(int k = 0;k < 3;++k){
	                					value[ id[k] ] = aux.get(posAux); posAux++;
	                					cards[ id[k] ].setImageDrawable(new CardDrawable(value[ id[k] ]));
	                					marked[ id[k] ] = false;
	                					//System.out.println(value[ id[k] ]);
	                					/*cards[ id[k] ].setImageDrawable(new CardDrawable(value[ id[k] ]));
	                					cards[ id[k] ].setColorFilter(Color.argb(0, 0, 0, 0));
	                					marked[ id[k] ] = false;
	                					--cont;*/
	                				}
	                				
	                				N += 3;
	                					
	                				if(!test(value,N) && 81 - posAux >= 3){
	                					for(int k = 0;k < 3;++k){
	                						value[12 + k] = aux.get(posAux); posAux++;
	                						cards[12 + k].setImageDrawable(new CardDrawable(value[12 + k]));
	                					}
	                					
	                					N += 3;
	                					
	                					if(!test(value,N)){
	                						//TODO:finir le jeu
	                					}
	                				}
		                		}else if(posAux == 81){
		                			cleanCards(N + 3);
		                			
		                			if(!test(value,N)){
		                				//TODO:finir le jeu
		                			}
		                			// TODO: supprimer
		                		}
		                	}else{
		                		for(int k = 0;k < 3;++k){
		                			final int cardId = id[k];
		                			
		                			cards[cardId].setColorFilter(Color.RED);
		                			
		                			cards[cardId].postDelayed(new Runnable(){
		                				public void run(){
		                					cards[cardId].setColorFilter(Color.argb(0, 0, 0, 0));
		                					marked[cardId] = false;
		                					--cont;
		                				}
		                			}, 500);
		                		}
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