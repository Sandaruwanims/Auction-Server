import java.io.IOException;
import java.util.Timer ;
import java.util.TimerTask;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
*	Author: 	 I.M Sandaruwan
*				 E/13/315
*	Description: Auction Server
*/


public class AuctionServer extends JFrame{

	//welcome port
    public static final int BASE_PORT = 2000; 

    //button array
    private JButton button_Array[][] = new JButton[8][3];
    private JButton search_Array[] = new JButton[3];
    private JButton title_Array[] = new JButton[3];
    private JButton search_button;
    private JLabel label;
    private JTextArea text;

    //Dispay items in GUI
    private static String []symbol = {"FB","VRTU","MSFT","GOOGL","YHOO","XLNX","TSLA","TXN"};

    public void Gui(){
    	//JFrame for Auction server
    	JFrame mainWindow = new JFrame("Auction Server");
   	    //JPanels to set the buttons
    	JPanel top = new JPanel();
    	JPanel top1 = new JPanel();
    	JPanel top2 = new JPanel();
    	JPanel top3 = new JPanel();
    	JPanel bottom = new JPanel();

    	//set the game layout
    	GridLayout grid1 = new GridLayout(9,1);
    	GridLayout grid2 = new GridLayout(9,1);
    	GridLayout grid3 = new GridLayout(9,1);

    	top1.setLayout(grid1);
    	top2.setLayout(grid2);
    	top3.setLayout(grid3);
    	top.add(top1);
    	top.add(top2);
    	top.add(top3);


    	//set the sizes of frame and panels
    	top.setPreferredSize(new Dimension(760, 410));
    	top.setSize(760, 410);
    	top1.setPreferredSize(new Dimension(150, 400));
    	top1.setSize(150, 400);
    	top2.setPreferredSize(new Dimension(400, 400));
    	top2.setSize(400, 400);
    	top3.setPreferredSize(new Dimension(150, 400));
    	top3.setSize(150, 400);
    	top.setBackground(Color.gray);
    	bottom.setBackground(Color.gray);
    	bottom.setPreferredSize(new Dimension(300, 50));
    	bottom.setSize(340, 380);
    	mainWindow.setSize(740, 540);
    	mainWindow.setPreferredSize(new Dimension(740, 540));
    	//mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Did not implement. because when GUI is close the server should not stop.

    	mainWindow.add(bottom);
    	bottom.add(top);

    	//for title buttons
    	for (int i = 0; i < 3; i++) {
    		title_Array[i] = new JButton();
            title_Array[i].setFont(new Font(null, Font.PLAIN, 20));
            if(i==0){
            	top1.add(title_Array[i]);
            	title_Array[0].setPreferredSize(new Dimension(150, 40));
            	title_Array[0].setText("SYMBOL");
            }
        	else if(i==1){
        		top2.add(title_Array[i]);
        		title_Array[1].setPreferredSize(new Dimension(400, 40));
        		title_Array[1].setText("SECURITY NAME");
        	}
        	else{
        		top3.add(title_Array[i]);
        		title_Array[2].setPreferredSize(new Dimension(150, 40));
        		title_Array[2].setText("PRICE");
        	}
    	}

    	
    	
    	

    	//create the buttons and add them to the panel
    	for (int i = 0; i < 8; i++) {
        	for (int j=0; j<3; j++) {
            	button_Array[i][j] = new JButton();
            	button_Array[i][j].setFont(new Font(null, Font.PLAIN, 12));
            	if(j==0){
            		button_Array[i][j].setPreferredSize(new Dimension(150, 10));
            		top1.add(button_Array[i][j]);
            	}else if(j==1){
            		button_Array[i][j].setPreferredSize(new Dimension(400, 10));
            		top2.add(button_Array[i][j]);
            	}else{
            		button_Array[i][j].setPreferredSize(new Dimension(150, 10));
            		top3.add(button_Array[i][j]);
            	}
            	
        	}
    	}

    	label = new JLabel("Search current price using Symbol of the Item: ");
    	bottom.add(label);

    	text = new JTextArea();
    	text.setPreferredSize(new Dimension(200, 30));
    	bottom.add(text);

    	//for the search button
    	search_button = new JButton("search");
    	search_button.setPreferredSize(new Dimension(150, 30));
    	bottom.add(search_button);

    	for (int i = 0; i < 3; i++) {
    		search_Array[i] = new JButton();
            search_Array[i].setFont(new Font(null, Font.PLAIN, 12));
            bottom.add(search_Array[i]);
    	}

    	search_Array[0].setPreferredSize(new Dimension(150, 40));
    	search_Array[1].setPreferredSize(new Dimension(400, 40));
    	search_Array[2].setPreferredSize(new Dimension(150, 40));

    	mainWindow.pack();
    	mainWindow.setVisible(true);
    }

    //refersh the gui with current prices.
    public void setGui(Server server){
    	for (int i = 0; i < 8; i++) {
        	for (int j=0; j<3; j++) {
        		if(j==0){
        			this.button_Array[i][j].setText(symbol[i]);
        		}else if(j==1){
        			String text = server.find(1,i);
        			this.button_Array[i][j].setText(text);
        		}else{
					String text = server.find(2,i);
        			this.button_Array[i][j].setText(text);
        		}
        	}
    	}
    }

    //search current price of a item
    public void searchItem(Server server){
    		if(search_button.isEnabled()){
    	    search_button.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
           		try{
           			String findItem = text.getText();
           			for(int i=0; i<3; i++){
           				search_Array[i].setText("");
           			}	
           			search_Array[1].setText(server.forSearch(1,findItem));
           			search_Array[2].setText(server.forSearch(2,findItem));
           			search_Array[0].setText(findItem);
           		}catch(Exception ex){
           		}
           }
       });
    	}
    }


	public static void main(String [] args) throws IOException { 
    	Timer timer = new Timer();
		Server server = new Server(BASE_PORT);
		AuctionServer gui = new AuctionServer();

		timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
               gui.setGui(server);
               gui.searchItem(server);
            }
        }, 500, 500);

		gui.Gui();
		server.readFile();
		gui.setGui(server);
		server.server_loop(); 		
    }
}