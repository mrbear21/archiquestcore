package integrations;

import java.net.*;
import java.util.Arrays;
import java.util.List;

import brain.BrainSpigot;
import objects.ChatMessage;

import java.io.*;

public class ArchiQuestJournalist {

	public BrainSpigot spigot;
	
	public ArchiQuestJournalist(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	String[] keywords = {"степан", "строительство", "строительства", "строительству", "строительством", "завершено", "завершен", "завершена", "завершены", "готово", "готова", "готовы", "сделал", "сделано", "сделала", "сделали", "построил", "построила", "построили", "готовый", "готовая", "готовые", "строительный проект", "строительных проектов", "строительному проекту", "строительным проектом", "обновления", "обновлений", "обновлению", "обновлением", "новость", "новости", "новостью", "новостями", "архитектура", "архитектуры", "архитектуре", "архитектурой", "инженер", "инженера", "инженеру", "инженером", "организация", "организации", "организации", "организацию", "мероприятие", "мероприятия", "мероприятию", "мероприятием", "планирую", "планирует", "планировать", "планирования", "место", "места", "месту", "местом", "РПГ", "РПГ-игра", "игра в РПГ-стиле", "ролевая игра", "ролевые игры", "игре в ролевом стиле", "фэнтези", "фэнтезийная", "фэнтезийное", "средневековье", "средневековья", "средневековью", "рп", "рп-игра", "игре в рп-стиле", "игровой процесс", "игровому процессу", "игровым процессом", "персонажи", "персонажей", "персонажам", "персонажами", "начало", "начале", "старт", "старте", "запуск", "запуске", "проект", "проекта", "проекту", "проектом", "разработка", "разработки", "разработке", "разработкой", "прогресс", "прогресса", "прогрессу", "прогрессом", "новости", "новостей", "новостям", "новостями", "обновление", "обновлений", "обновлению", "обновлением", "информация", "информации", "информации", "информацией", "объявление", "объявлений", "объявлению", "объявлением", "сообщество", "сообщества", "сообществу", "сообществом", "развлечения", "развлечений", "развлечению", "развлечением", "социальное", "социального", "социальному", "социальным", "чат", "чата", "чату", "чатом", "обсуждение", "обсуждений", "обсуждению", "обсуждением", "идея", "идей", "идее", "идеей", "творчество", "творчества", "творчеству", "творчеством", "вдохновение", "вдохновения", "вдохновению", "вдохновением"};
	
    public void check(ChatMessage message) throws Exception {
    	if (message.getChat().equals("local")) {
	    	for (String word : message.getMessage().toLowerCase().split(" ")) {
	    		if (Arrays.asList(keywords).contains(word)) {
	    	    	
	    			List<String[]> history = spigot.getBread(message.getPlayer()).getMessagesHistory();
	    			
	    			List<String[]> last20 = history.subList(Math.max(0, history.size() - 50), history.size());
	
	    			StringBuilder sb = new StringBuilder();
	    			for (int i = Math.max(0, last20.size() - 50); i < last20.size(); i++) {
	    				
	    				String chat = new ChatMessage(last20.get(i)).getChat();
	    				String player = new ChatMessage(last20.get(i)).getPlayer();
	    				String msg = new ChatMessage(last20.get(i)).getMessage();
	    				
	    				if (!msg.contains("\"text\"") && (chat.equals("local"))) {
	    					sb.append(player).append(": '").append(msg).append(".' ");
	    				}
	    			}
	    			String text = sb.toString();
	    			
	    			spigot.log(text);
	    			
	    	        String response;
	
					response = makeRequest(text);
	    	        spigot.log(response);

	    	        break;
	    		}
	    	}
    	}

    }
    
    public static String makeRequest(String text) throws Exception {

        String url = "https://archi.quest/api/gpt/journalist.php";
        URL endpoint = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        
        String requestBody = "{\"text\":\"" + text + "\"}";
        
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(requestBody);
        writer.flush();
        writer.close();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        return response.toString();
    }
	
	
	
}
