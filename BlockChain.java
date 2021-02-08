import java.util.*;
import java.io.*;
import java.sql.*;
import java.lang.Math;
public class BlockChain{
	
	private ArrayList<Block> blocks = new ArrayList<Block>(20);
	private int hashTrials = 0;

	/**
    * Constructor to initilaize blockchain to a given array of blocks
    * @param blocks Given array of blocks
    */

	public BlockChain(ArrayList<Block> blocks){
		this.blocks= blocks;
	}

	/**
    * Sets currrent array of blocks to given array blocks
    * @param blocks Given array of blocks
    */
	public void setBlocks(ArrayList<Block> blocks){
		this.blocks=blocks;
	}

	/**
    * Returns the blockchain's array of blocks
    * @return ArrayList containing this blockchain's set of blocks
    */
	public ArrayList<Block> getBlocks(){
		return blocks;
	}

	/**
    * Returns index of last block in the chain
	* @return int for index of last block in the chain
    */
	public int getLastBlock(){
		return blocks.size()-1;
	}

	/**
    * Reads a blockchain from a given file and returns it
    * Precondition: File should describe blockchain using index, timestamp, sender, receiver, nonce, hash, each in a line and in this order
    * @param fileName Given file to be read
    * @return BlockChain that is initialized with blocks from the given file
    * @throws FileNotFoundException if file was not found
    * @throws IOException if there was an error in inputting/outputtinf to/from the file
    */
	public static BlockChain fromFile(String fileName){
		
		ArrayList<Block> newBlocks = new ArrayList<Block>(20);
		
		String line;
		
		int i=0;
		
		int index, amount;

		index=amount=0;
		
		String receiver, sender, nonce, hash;

		receiver=sender=nonce=hash=null;
		
		Timestamp timestamp;

		timestamp=null;
		try{
			File file = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line=br.readLine())!=null){

				switch(i){
					case 0:
						index = Integer.parseInt(line); 
						break;
					case 1:
						timestamp = new Timestamp(Long.parseLong(line.trim()));
						break;
					case 2:
						sender = line;
						break;
					case 3:
						receiver = line;
						break;
					case 4:
						amount = Integer.parseInt(line);
						break;
					case 5:
						nonce = line;
						break;
					case 6:
						hash = line;
						break;
				}

				if(i==6){

					Transaction newTrans = new Transaction(sender, receiver, amount);
					Block newBlock;
					if(index!=0){
						newBlock = new Block(index, timestamp, newTrans, nonce, newBlocks.get(index-1).getHash(), hash);
					}
					else{
						newBlock = new Block(index, timestamp, newTrans, nonce,"00000", hash);
					}
					newBlocks.add(index, newBlock);
				}

				i = (i+1)%7;
			}
		}
		catch(FileNotFoundException f){
			System.out.println("File not found");
			System.exit(0);
		}

		catch(IOException e){
			System.out.println("IO error");
			System.exit(0);
		}

		return new BlockChain(newBlocks);
	}

	/**
    * Writes the blockchain to the given file
    * @param fileName file to be written to
    * @throws FileNotFoundException if file was not found
    * @throws UnsupportedEncodingException if the given printwriter encoding format isn't supported
    */
	public void toFile(String fileName){
		
		try{
			String newFileName = fileName.substring(0,(fileName.length())-4);
			if(newFileName.equals("bitcoinBank")){
				newFileName="blockchain";
			}
			PrintWriter writer = new PrintWriter(newFileName+"_mshah043.txt", "UTF-8");
			for(int i=0; i<blocks.size(); i++){
				Block block = blocks.get(i);
				writer.println(Integer.toString(block.getIndex()));
				writer.println(block.getTimestamp().getTime());
				writer.println(block.getTransaction().getSender());
				writer.println(block.getTransaction().getReceiver());
				writer.println(Integer.toString(block.getTransaction().getAmount()));
				writer.println(block.getNonce());
				writer.println(block.getHash());		
			}
			writer.close();
		}
		catch(FileNotFoundException f){
			System.out.println("File not found");
			System.exit(0);
		}
		catch(UnsupportedEncodingException e){
			System.out.println("Unsupported Encoding");
			System.exit(0);
		}


	}
	/**
    * Returns true if blockchain is valid, that is has correct indices, compatible previous hashes, valid hashes, and senders are using money they have
    * Returns false otherwise
    * @return boolean indicating validity of chain
    * @throws UnsupportedEncodingException if the SHA1 hash function's encoding format isn't supported
    */
	public Boolean validateBlockChain(){
		for(int i=0 ; i<blocks.size(); i++){
			Block currentBlock = blocks.get(i);
			Transaction currentTrans = currentBlock.getTransaction();
			String sender = currentTrans.getSender();
			int amount = currentTrans.getAmount();
			if(currentBlock.getIndex()!=i){
				System.out.println("Invalid indices");
				return false;
			}
			if(i==0){
				if(!currentBlock.getPreviousHash().equals("00000")){
					System.out.println("Incompatible previous hashes");
					return false;
				}
			}
			else{
				Block previousBlock= blocks.get(i-1);
				if(!currentBlock.getPreviousHash().equals(previousBlock.getHash())){
					System.out.println("Incompatible previous hashes");
					return false;
				}
			}
			try{

			if(!Sha1.hash(currentBlock.toString()).equals(currentBlock.getHash())){
				System.out.println("Hash incompatible with information");
				return false;
				}
			}
			catch(UnsupportedEncodingException e){
				System.out.println("Unsupported Encoding");
				System.exit(0);
			}

			if(getBalanceUptoBlock(sender,i)<amount){
				System.out.println("Transaction exceeds sender's capabilities");
				return false;
			}
		}

		return true;
	}


	/**
    * Returns balance of a user prior to the transaction given his username, and index of the block that the transaction will take place in, if the block is
    * the genesis block (i.e first block in chain) we return the maximum integer value as to allow the first transaction to happen
    * @param username username for sender whom's balance will be checked
    * @param index index of block corresponding to the transaction for which the sender will be validated
    * @return int indicating balance of sender
    */
	public int getBalanceUptoBlock(String username, int index){

		if(username==null){
			throw new NullPointerException();
		}

		if(index==0){
			return Integer.MAX_VALUE;
		}


		int balance=0;
		for(int i=0; i<index; i++){
			Block currentBlock = blocks.get(i);
			Transaction currentTrans = currentBlock.getTransaction();
			String sender = currentTrans.getSender();
			String receiver = currentTrans.getReceiver();
			int amount = currentTrans.getAmount();
			if(username.equals(sender)){
				balance-=amount;
			}
			if(username.equals(receiver)){
				balance+=amount;
			}
		}
		return balance;
	}

	/**
    * Given a block, the method adds it to the chain
    * @param block the block to be added
    */
	public void add(Block block){
		int index = getLastBlock()+1;
		blocks.add(index,block);
	}

	/**
    * Returns a generated random string of size 10 with characters in the ASCII range 33-126 inclusive
    * @return String that was randomly generated
    */
	public String randomNonce(){
		int stringLength=4;
		String nonce = "";
		for(int i=0; i<stringLength; i++){
			int randAscii = 33 + (int)(Math.random()*(127-33));
			nonce += Character.toString((char) randAscii);
		}
		return nonce;
	}

	/**
    * Sets a nonce for a given block, that satisfies the hash requirements set
    * @param block The block that will be mined
    * @throws UnsupportedEncodingException if the SHA1 hash function's encoding format isn't supported
    */
	public void nonceHashing(Block block){
		hashTrials=0;
		String potNonce= randomNonce();
		block.setNonce(potNonce);
		hashTrials++;
		try{
			String resultHash = Sha1.hash(block.toString());
			while(!resultHash.substring(0,5).equals("00000")){
				potNonce= randomNonce();
				hashTrials++;
				block.setNonce(potNonce);
				resultHash = Sha1.hash(block.toString());
			}
			block.setHash(resultHash);
		}
		catch(UnsupportedEncodingException e){
			System.out.println("Unsupported Encoding");
			System.exit(0);
		}
		System.out.println("#Trials for this hash: " + hashTrials);

	}

	/**
    * Prompts the user for a transaction, where the user provides a sender, receiver, and amount, and these details are returned in an array of objects
    * where they are stored in that order
    * @return Array of objects of size 3 with sender, receiver, and amount stored in this order
    */
	public static Object[] transactionPrompt(){
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a new transaction");
		System.out.println("Enter the sender's username: ");
		String sender = in.nextLine();
		System.out.println("Enter the receiver's username: ");
		String receiver = in.nextLine();
		System.out.println("Enter the transaction amount: ");
		Integer amount = new Integer(in.nextInt());
		in.nextLine();
		Object[] transDetails = new Object[]{sender, receiver, amount};
		return transDetails;
		
	}

	/**
    * Main method which asks user to input a text file that contains a past blockchain, and asks the user for more transactions if the past chain is valid
    * and then continues on this process depending on the validity of the given transactions
    * @throws IllegalStateException if the entered past blockchain is invalid, according to the validateBlockChain method defined
    */
	public static void main(String[] args){

		Scanner in = new Scanner(System.in);
		System.out.println("Enter name of text file containting past blockchain:");
		String fileName = in.nextLine();
		BlockChain chain = fromFile(fileName);
		

		if(chain.validateBlockChain()){

			System.out.println("Do you wish to proceed with a transaction? Enter Yes or No:");
			String answer = in.nextLine();
			Object[] transDetails;
			String sender, receiver;
			int amount ;
			Transaction newTrans;
			String previousHash;
			Block newBlock;

			while(answer.toLowerCase().equals("yes")){
				transDetails = transactionPrompt();
				sender = (String)transDetails[0];
				receiver = (String)transDetails[1];
				amount = ((Integer)transDetails[2]).intValue();
				if(chain.getBalanceUptoBlock(sender, chain.getLastBlock()+1)<amount){
						System.out.println("Transaction exceeds sender's capabilities");
						System.out.println("Chain stands without the proposed transaction");
						chain.toFile(fileName);
						System.exit(0);
					}

				else{
						newTrans = new Transaction(sender, receiver, amount);
						previousHash = chain.getBlocks().get(chain.getLastBlock()).getHash();
						newBlock = new Block(chain.getLastBlock()+1,newTrans, null, previousHash, null);
						chain.nonceHashing(newBlock);
						chain.add(newBlock);
						System.out.println("Do you wish to proceed with another transaction? Enter Yes or No:");
						answer = in.nextLine();
					}
			}
			chain.toFile(fileName);
		}
		else{
			System.out.println("Invalid Past BlockChain");
			throw new IllegalStateException("Invalid Past BlockChain");
		}

	}
}