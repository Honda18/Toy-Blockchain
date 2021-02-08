import java.sql.*;

public class Block{

	private int index;
	private Timestamp timestamp;
	private Transaction transaction;
	private String nonce;
	private String previousHash;
	private String hash;


	/**
    * Initalizes block to given block details
    * @param index given index
    * @param transaction given transaction
    * @param nonce given nonce
    * @param previousHash given copy of previous block's hash
    * @param hash given hash
    */
	public Block(int index,Transaction transaction, String nonce, String previousHash, String hash){
		this.index=index;
		this.timestamp= new Timestamp(System.currentTimeMillis());
		this.transaction=transaction;
		this.nonce=nonce;
		this.previousHash=previousHash;
		this.hash=hash;
	}

	/**
    * Initalizes block to given block details
    * @param index given index
    * @param timesatmp given timestamp
    * @param transaction given transaction
    * @param nonce given nonce
    * @param previousHash given copy of previous block's hash
    * @param hash given hash
    */
	public Block(int index, Timestamp timestamp, Transaction transaction, String nonce, String previousHash, String hash){
		this.index=index;
		this.timestamp=timestamp;
		this.transaction=transaction;
		this.nonce=nonce;
		this.previousHash=previousHash;
		this.hash=hash;
	}

	/**
    * Returns index of block
    * @return int representing index of block
    */
	public int getIndex(){
		return index;
	}

	/**
    * Sets block's index to given one
    * @param index given index
    */
	public void setIndex(int index){
		this.index=index;
	}

	/**
    * Returns Timestamp of block
    * @return TimeStamp containing timestamp details of block
    */
	public Timestamp getTimestamp(){
		return timestamp;
	}

	/**
    * Sets block's timestamp to given one
    * @param timestamp given timestamp
    */
	public void setTimestamp(Timestamp timestamp){
		this.timestamp=timestamp;
	}

	/**
    * Returns block's nonce
    * @return String containing block's nonce
    */
	public String getNonce(){
		return nonce;
	}

	/**
    * Sets block's nonce to given one
    * @param nonce given nonce
    */
	public void setNonce(String nonce){
		this.nonce = nonce;
	}

	/**
    * Returns block's hash
    * @return String containing block's hash
    */
	public String getHash(){
		return hash;
	}

	/**
    * Sets block's hash to given one
    * @param hash given hash
    */
	public void setHash(String hash){
		this.hash=hash;
	}

	/**
    * Returns block's transaction
    * @return Transaction containing the block's transaction details
    */
	public Transaction getTransaction(){
		return transaction;
	}

	/**
    * Returns stored copy of previous block's hash
    * @return String containing the stored copy of previous block's hash
    */
	public String getPreviousHash(){
		return previousHash;
	}

	/**
    * Sets stored copy of previous block's hash to given one
    * @param previousHash given previous hash
    */
	public void setPreviousHash(String previousHash){
		this.previousHash=previousHash;
	}

	/**
    * Returns block's string representation
    * @return String containing the block's details
    */
	public String toString(){

		return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
	}

	
}