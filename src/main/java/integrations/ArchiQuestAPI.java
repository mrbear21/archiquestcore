package integrations;

import brain.BrainSpigot;

public class ArchiQuestAPI {

	  private static BrainSpigot instance = null;

	  public static BrainSpigot get() {
	    if (instance == null) {
	      throw new IllegalStateException("archiquestcore is not loaded.");
	    }
	    return instance;
	  }

	  public static void register(BrainSpigot instance) { ArchiQuestAPI.instance = instance; }

	  public static void unregister() { instance = null; }
	  
	  private ArchiQuestAPI() { throw new UnsupportedOperationException("This class cannot be instantiated."); }
}
