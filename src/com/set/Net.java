package com.set;

import java.io.*;
import java.net.*;

final class Net {
		
    static public ServerSocket createServer(int server_port){ 	
    	try {
		return new ServerSocket(server_port);
    	} catch (IOException e) {
    		// si on ne peut pas lancer le serveur sur ce
    		// port, on arrete le programme
    		throw new RuntimeException("Impossible d'attendre sur le port "+ server_port);
    	}
    }
	
	static Socket acceptConnection(ServerSocket s) {
		try {
		return s.accept();
		} catch (IOException e) {
    		throw new RuntimeException("Impossible de recevoir une connection");
		}
	}
	
	static Socket establishConnection(String ip, int port) {
		try {
			return new Socket(ip,port);
		} 
		catch (UnknownHostException e){
			throw new RuntimeException("Impossible de resoudre l'adresse");
		}
		catch (IOException e){
			throw new RuntimeException("Impossible de se connecter a l'adresse");	
		}
	}
	
    static PrintWriter connectionOut(Socket s){
    	try {
			return new PrintWriter(s.getOutputStream(),	true);
		} catch (IOException e) {
			throw new RuntimeException("Impossible d'extraire le flux sortant");
		}
    }
    
    static BufferedReader connectionIn(Socket s){
    	try {
    		return new BufferedReader(new InputStreamReader(s.getInputStream()));
    	} catch (IOException e) {
    		throw new RuntimeException("Impossible d'extraire le flux entrant");
    	}
    }
}
/*
public class TD3 extends Activity {
	    
	
	// Handler pour pouvoir appeler le tread principal gerant
	// l'interface graphique.
	final Handler Callback = new Handler();

	final static int nlabels = 6;
	// Variables stockant la valeur courante de l'etat memoire et cpu.
	private String[] infos = new String[nlabels];
	private int[] ids = new int[nlabels];

	// Objet contenant le code a executer pour mettre a jour la boite
	// de texte contenant la memoire disponible.
	final Runnable update_infos = new Runnable() {
		public void run() {
			for (int i = 0; i < nlabels; i++) {
				EditText txt_info = (EditText) findViewById(ids[i]);
				txt_info.setText(infos[i]);
			}
		}
	};

	void displayMessage(String s){
		for(int i=0; i < nlabels-1; i++) infos[i] = infos[i+1];
		infos[nlabels-1] = s;
		Callback.post(update_infos);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Appel de la methode de base Activity.onCreate.
		super.onCreate(savedInstanceState);

		ids[0] = R.id.txt1;
		ids[1] = R.id.txt2;
		ids[2] = R.id.txt3;
		ids[3] = R.id.txt4;
		ids[4] = R.id.txt5;
		ids[5] = R.id.txt6;
		assert (nlabels == 6);
		for (int i = 0; i < nlabels; i++) infos[i] = "";

		// Affichage du layout XML dans l'ecran associe a notre Activity.
		setContentView(R.layout.main);

		final Button button = (Button) findViewById(R.id.send_button);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText txt_info = (EditText) findViewById(R.id.editor);
				String message = txt_info.getText().toString();
				txt_info.setText("");

				sendMessage(message);
			}
		});
	}

	final static String my_login = "Fabrice";
	
	static PrintWriter server_out = null;
	static PrintWriter telnet_out = null;
	
	@Override
	public void onStart() {
		super.onStart();
		// TODO: code s'executant au debut de l'application
		try {
		Socket s = Net.establishConnection("10.0.2.2", 7777);
		PrintWriter s_out = Net.connectionOut(s);
		final BufferedReader s_in = Net.connectionIn(s);
		displayMessage("CONNECTED");
		
		s_out.println("LOGIN " + my_login);
		String line;
		try
                    { line = s_in.readLine(); }
                catch (IOException e)
                    { throw new RuntimeException("in readLine"); }
		displayMessage(line);
		Scanner sc = new Scanner(line);
		sc.useDelimiter(" ");
		if(sc.next().equals("Welcome")){
			displayMessage("ACCEPTED");
			server_out = s_out;	
		}
		final Thread from_server = new Thread(){
			public void run(){
				String line = null;
				while(true){
                                    try { 
                                        line = s_in.readLine();
                                        displayMessage(line);
                                        if(telnet_out != null)
                                            telnet_out.println(line);
                                    } catch (IOException e){
                                        throw new RuntimeException("in readLine - 2");
                                    }
				}
			}
                    };
		from_server.start();

		final Thread as_server = new Thread(){
			public void run(){
				ServerSocket server_socket = Net.createServer(8888);
				while(true){
					Socket telnet_socket = Net.acceptConnection(server_socket);
					
					final PrintWriter s_out = Net.connectionOut(telnet_socket);
					final BufferedReader s_in = Net.connectionIn(telnet_socket);
	
					telnet_out = s_out;
					
                                        while(true){
                                            try {
                                                String line = s_in.readLine();
                                                server_out.println("SEND "+line);
                                            } catch(IOException e){}
					}
				}
			}
			
		};
		as_server.start();
		
		} catch (RuntimeException msg){
			displayMessage("Error "+msg);
		}
	}
	
	private void sendMessage(String message){ 
		// TODO: code s'executant quand l'utilisateur clique le bouton Send
		// pour envoyer un nouveau message
		server_out.println("SEND "+message);
//		displayMessage(message);
	}
	
	
}*/
