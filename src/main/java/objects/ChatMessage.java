package objects;

public class ChatMessage {

	private String chat;
	private String player;
	private String message;
	private String status;
	private String id;
	private String hover;
	private String language;
	
	public ChatMessage(String[] message) {
		this.chat = message[0];
		this.player = message[1];
		this.message = message[2];
		this.status = message[3];
		this.id = message[4];
		this.language = message[5];
	}
	
	public ChatMessage() {
		
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
		return status != null ? status : "";
	}
	
	public String getHoverText() {
		return hover;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public ChatMessage setId(String id) {
		this.id = id;
		return this;
	}
	
	public ChatMessage setPlayer(String player) {
		this.player = player;
		return this;
	}
	
	public ChatMessage setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public ChatMessage setChat(String chat) {
		this.chat = chat;
		return this;
	}
	
	public ChatMessage setStatus(String status) {
		this.status = status;
		return this;
	}

	public ChatMessage setHoverText(String hover) {
		this.hover = hover;
		return this;
	}

	public ChatMessage setLanguage(String language) {
		this.language = language;
		return this;
	}

}
