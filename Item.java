/*
*   Author:      I.M Sandaruwan
*                E/13/315
*   Description: Auction Server
*/

//Profile class for create item object 
public class Item{
	protected String symbol;
	protected String security_name;
	protected String price;

//constuctors
	public Item(String symbol,String security_name,String price){
		this.symbol = symbol;
		this.security_name = security_name;
		this.price = price;
	}

//create the get and set for aal the fields in item class
	public Item(){}


    public String getsymbol() {
        return symbol;
    }
    public void setsymbol(String symbol) {
        this.symbol = symbol;
    }


    public String getsecurity_name() {
        return security_name;
    }
    public void setsecurity_name(String security_name) {
        this.security_name = security_name;
    }


    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }
}