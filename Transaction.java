public class Transaction{
	
	private String sender;
	private String receiver;
	private int amount;

	/**
    * Constructor to initilaize transaction with given details
    * @param sender sender username
    * @param receiver receiver username
    * @param amount transaction amount
    */
	public Transaction(String sender, String receiver, int amount){
		this.sender= sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	/**
    * Returns username of sender
    * @return String containing username of sender
    */
	public String getSender(){
		return sender;
	}

	/**
    * Sets sender username to the given one
    * @param sender given username
    */
	public void setSender(String sender){
		this.sender=sender;
	}

	/**
    * Returns username of receiver
    * @return String containing username of receiver
    */
	public String getReceiver(){
		return receiver;
	}

	/**
    * Sets receiver username to the given one
    * @param receiver given username
    */
	public void setReceiver(String receiver){
		this.receiver= receiver;
	}

	/**
    * Returns transaction amount
    * @return int representing transaction amount
    */
	public int getAmount(){
		return amount;
	}

	/**
    * Sets transaction amount to the given one
    * @param amount given amount
    */
	public void setAmount(int amount){
		this.amount=amount;
	}
	
	/**
    * Returns string representation of transaction
    * @return String describing transaction details
    */
	public String toString(){
		return sender + ":" + receiver + "=" + amount;
	}



	
}