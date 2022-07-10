package objects;

public class ChatMessage {

	private String chat;
	private String player;
	private String message;
	private String status;
	private String id;
	
	public ChatMessage(String[] message) {
		this.chat = message[0];
		this.player = message[1];
		this.message = message[2];
		this.status = message[3];
		this.id = message[4];
	}

	public String getId() {
		return id;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getChat() {
		return chat;
	}
	
	public String getStatus() {
		return status;
	}
	

}
