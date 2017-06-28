package communicationLayer;

import java.io.IOException;

import serviceLayer.serviceLayer;
import serviceLayer.serviceLayerInterface;



public class MyServerProtocol implements ServerProtocol<String>{
    private	String name;
	private serviceLayerInterface serviceLayerr;
	
	public MyServerProtocol()
	{
		serviceLayerr = serviceLayer.getInstance();
		this.name = null;
	}
	
	
	public void processMessage(String msg, ProtocolCallback<String> callback,ConnectionHandler handler) {
		String[] parts=msg.split(" ");  //split the word(here we didn't care about the message with more than one space)
		try {
		if(parts[0].equals("REG")){
			
			callback.sendMessage(serviceLayerr.register(msg));
			
		}
		else if(parts[0].equals("LOGIN")){
			String response = serviceLayerr.login(msg,handler);
			if(response.contains("DONE")){
				this.name = parts[1];
				
			}
			callback.sendMessage(response);
		}
		else if(parts[0].equals("LOGOUT")){
			if(this.name!=null&&parts[1].equals(name)){
				this.name =null;
				callback.sendMessage(serviceLayerr.logout(msg));}
			else {
				callback.sendMessage("LOGOUT FAILED USER NOT LOGGED IN");
			}
			
		}
		else if(parts[0].equals("EDITPASS")){
	        if(this.name!=null&&parts[1].equals(name)){
	        	callback.sendMessage(serviceLayerr.editUserPassword(msg));
			
	        }
	        else{
	        	callback.sendMessage("EDITPASS FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("EDITUSERNAME")){
	        if(this.name!=null&&parts[1].equals(name)){
	        	callback.sendMessage(serviceLayerr.editUserName(msg));
			
	        }
	        else{
	        	callback.sendMessage("EDITUSERNAME FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("EDITUSEREMAIL")){
	        if(this.name!=null&&parts[1].equals(name)){
	        	callback.sendMessage(serviceLayerr.editUserEmail(msg));
			
	        }
	        else{
	        	callback.sendMessage("EDITUSEREMAIL FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("EDITUSERAVATAR")){
	        if(this.name!=null&&parts[1].equals(name)){
	        	callback.sendMessage(serviceLayerr.editUserAvatar(msg));
			
	        }
	        else{
	        	callback.sendMessage("EDITUSERAVATAR FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("CREATEGAME")){
			
	        if(this.name!=null&&parts[1].equals(name)){
	        	callback.sendMessage(serviceLayerr.createGame(msg));
			
	        }
	        else{
	        	callback.sendMessage("CREATEGAME FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("LISTJOINABLEGAMES")){
	        if(this.name!=null&&parts[1].equals(name)){
	        	callback.sendMessage(serviceLayerr.listJoinableGames(msg));
			
	        }
	        else{
	        	callback.sendMessage("LISTJOINABLEGAMES FAILED USER NOT LOGGED IN");
	        }
			
		}
		else if(parts[0].equals("SEARCHGAMESBYPREFS")){
	        if(this.name!=null){
	        	callback.sendMessage(serviceLayerr.searchGamesByPrefs(msg));
			
	        }
	        else{
	        	callback.sendMessage("SEARCHGAMESBYPREFS FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("SEARCHGAMESBYPLAYERNAME")){
	        if(this.name!=null){
	        	callback.sendMessage(serviceLayerr.searchGamesByPlayerName(msg));
			
	        }
	        else{
	        	callback.sendMessage("SEARCHGAMESBYPLAYERNAME FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("SEARCHGAMESBYPOTSIZE")){
	        if(this.name!=null){
	        	callback.sendMessage(serviceLayerr.searchGamesByPotSize(msg));
			
	        }
	        else{
	        	callback.sendMessage("SEARCHGAMESBYPOTSIZE FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("JOINGAME")){
	        if(this.name!=null&&parts[2].equals(name)){
	        	callback.sendMessage(serviceLayerr.joinGame(msg));
			
	        }
	        else{
	        	callback.sendMessage("JOINGAME FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("LISTSPECTATEABLEGAMES")){ 
	        if(this.name!=null){
	        	callback.sendMessage(serviceLayerr.listSpectatableGames(msg));
			
	        }
	        else{
	        	callback.sendMessage("LISTSPECTATEABLEGAMES FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("SPECTATEGAME")){
	        if(this.name!=null&&parts[2].equals(name)){
	        	callback.sendMessage(serviceLayerr.spectateGame(msg));
	        	serviceLayerr.updateGame(parts[1]);
			
	        }
	        else{
	        	callback.sendMessage("SPECTATEGAME FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("LEAVEGAME")){ 
	        if(this.name!=null&&parts[2].equals(name)){
	        	callback.sendMessage(serviceLayerr.leaveGame(msg));
	        	serviceLayerr.updateGame(parts[1]);
			
	        }
	        else{
	        	callback.sendMessage("LEAVEGAME FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("ACTION")){
	        if(this.name!=null&&parts[3].equals(name)){
	        	callback.sendMessage(serviceLayerr.Action(msg));
			
	        }
	        else{
	        	callback.sendMessage("ACTION FAILED USER NOT LOGGED IN");
	        }
		}
		else if(parts[0].equals("CHATMSG")){
	        	serviceLayerr.ChatMsg(msg);
		}
		else if(parts[0].equals("WHISPERMSG")){
        	serviceLayerr.WhisperMsg(msg);
	   }
		else if(parts[0].equals("REPLAY")){
			callback.sendMessage(serviceLayerr.ReplayGame(msg));
	   }
		else
			
				callback.sendMessage("SYSMSG UNIDENTIFIED");
			} catch (IOException e) {
				//delete the name if there's like this

			}
	}

	public boolean isEnd(String msg) {
      return false; 
	
	}

	
}
