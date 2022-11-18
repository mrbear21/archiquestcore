```        _                       
       | |                      
  _ __ | | __ _ _   _  ___ _ __ 
 | '_ \| |/ _` | | | |/ _ \ '__|
 | |_) | | (_| | |_| |  __/ |   
 | .__/|_|\__,_|\__, |\___|_|   
 | |             __/ |          
 |_|            |___/           

BreadMaker bread = plugin.getBread("player");

//щоб взяти якусь інформацію з бази даних
bread.getData("level").getAsInt()
bread.getData("language").getAsString()

//щоб зберегти якусь інформацію тимчасово
bread.setData("назва", "щось");

//щоб зберегти інформацію в базу даних
bread.setData("назва", "щось").save();


                    
  _ __ ___   ___ _ __  _   _ 
 | '_ ` _ \ / _ \ '_ \| | | |
 | | | | | |  __/ | | | |_| |
 |_| |_| |_|\___|_| |_|\__,_|
                             
                       

MenuBuilder menu = new MenuBuilder(spigot, player, "назва");
menu.setOption("назва", 0, "команда", Material.PAPER, new String [] {"опис"});
menu.setOption("назва", 1, new String[] {"дві","команди"}, Material.DIAMOND, new String [] {"опис"});
menu.build();


                  _     _                     
                 | |   | |                    
   ___ ___   ___ | | __| | _____      ___ __  
  / __/ _ \ / _ \| |/ _` |/ _ \ \ /\ / / '_ \ 
 | (_| (_) | (_) | | (_| | (_) \ V  V /| | | |
  \___\___/ \___/|_|\__,_|\___/ \_/\_/ |_| |_|
                                              
                                        


Cooldown cooldown = new Cooldown(spigot, sender.getName());

if (cooldown.hasCooldown(command.getName())) {
  sender.sendMessage("archiquest.waitcooldown "+((cooldown.getTimeLeft(command.getName())/ 1000)) +" sec");
  return true;
}

cooldown.setCooldown(command.getName(), 60); //60 секунд
```  
