import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;


/*
*	Author: 	 I.M Sandaruwan
*				 E/13/315
*	Description: Auction Server
*/


public class Server implements Runnable { 

    private static ServerSocket serverSocket; 
    private static int socketNumber; 

    private Socket connectionSocket; 

    //hashmap for store the data
	public static HashMap<String,Item> stock = new HashMap<>();

	/*hashmap for store the change records
	 *If any client bid for a item then that is store in this hashmap.
	 *The key of the hashmap is Client_name,Item_symbol.
	 */
	public static HashMap<String,Item> change = new HashMap<>();

	//symbols for display in the gui.
	private static String []symbol = {"FB","VRTU","MSFT","GOOGL","YHOO","XLNX","TSLA","TXN"};



    public Server(int socket) throws IOException { 
		serverSocket = new ServerSocket(socket); 
		socketNumber = socket; 
    }


    public Server(Socket socket) { 
		this.connectionSocket = socket; 
    }


    public void server_loop() throws IOException { 
		while(true) { 
	    	Socket socket = serverSocket.accept(); 	    
	    	Thread worker = new Thread(new Server(socket)); 
	    	worker.start();     
		}
    }






//read the stock file and store the data in a hashmap
    public void readFile(){
		try{
			BufferedReader br = new BufferedReader(new FileReader("stocks.csv"));
			String line;
			//insert the records to the hashmap
			while ((line = br.readLine()) != null) {
				String[] colm = line.split(",");
				if(stock.containsKey(colm[1]) == false){
					stock.put(colm[0],new Item(colm[0],colm[1],colm[2]));
				}
			}
		}catch(Exception e){
		}
	}



	public void run() { 
		this.bid();
    }

    //Find the current price of some items and display it in gui
    public String find(int x, int y){
    	try{
    		Item current = new Item();
    		current = (Item)stock.get(symbol[y]);
    		if(x==1){
    			return current.getsecurity_name();
    		}else{
    			return current.getprice();
    		}
    	}catch(Exception ex){
    		return null;
    	}
    }

    //If someone need to find current price of a some item.
    public String forSearch(int x,String security){
    	try{
    		Item current = new Item();
    		current = (Item)stock.get(security);
    		if(x==1){
    			return current.getsecurity_name();
    		}else{
    			return current.getprice();
    		}
    	}catch(Exception ex){
    		return null;
    	}
    }

//bid function for client
    public synchronized void bid(){
    	Item current = new Item();
    	try { 
    		//input stream and output stream to communicate with the server
 			BufferedReader in = new BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));
	    	PrintWriter out = new PrintWriter(new OutputStreamWriter(this.connectionSocket.getOutputStream()));	
    	
    		String line;
    		String name = null;
    		String clientName = null;
    		int i = 0;
    		double currentPrice = 0;
    		boolean itemOk = false;

    		//read the argument from the client in terminal
    		for(line = in.readLine(); 
			line != null && !line.equals("quit"); 
			line = in.readLine()) { 
    			//get the client name in to the system
				if(i == 0){
					clientName = line;
					out.flush();
					i++;
				}
				//Check whether an item is there for the user given symbol
				else if(i > 0 && itemOk == false){
					String search = line;
					name = search;
					current = (Item)stock.get(search);
					if(current==null){
						out.print("-1\n"); 
						out.flush();
					}
					else{
						out.print(current.getprice() + "\n");
						currentPrice = Double.parseDouble(current.getprice()); 
						itemOk = true;
						out.flush();
					}
				}else{
					try{
						double tempPrice = Double.parseDouble(line);
						//if the bid value is ok then save it in the data collection
						if(tempPrice > currentPrice){
							current.setprice(line); 
							stock.put(name,current);

							//create a key to store a change in price of the item
							String key = clientName+","+name;

							if(change.containsKey(key) == false){
								change.put(key,new Item(current.getsymbol(),current.getsecurity_name(),current.getprice()));
							}
							out.print("You successfully bid for "+name+"\n"); 
							out.flush();
							itemOk = false;
						}else{
							out.print("Your bid was reject\n"); 
							out.flush();
						}
					}catch(NumberFormatException e){
						out.print("Your bid was reject\n"); 
						out.flush();
					}
				} 
	    	}

	    } 
		catch (IOException e) { 
	    	System.out.println(e); 
		}	 
		try { 	    
	    	this.connectionSocket.close(); 
		} catch(IOException e) {}
    } 
}
	   