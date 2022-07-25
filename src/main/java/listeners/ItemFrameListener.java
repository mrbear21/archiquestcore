package listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.BrainSpigot;

import org.bukkit.Server;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ItemFrameListener implements Listener {
	
	private BrainSpigot plugin;

	public ItemFrameListener(BrainSpigot plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEntityEvent event) {
    	
        if(event.isCancelled()) return;

        if(event.getHand() != EquipmentSlot.HAND) return;
        
        if(! (event.getRightClicked() instanceof ItemFrame)) return;
        
        if (true) {

	        ItemFrame entity = (ItemFrame) event.getRightClicked();
	
	        Block facing = entity.getLocation().getBlock().getRelative(entity.getAttachedFace());
	
	        if(facing.getType().isInteractable()) {
	            PlayerInteractEvent interactBlock = new PlayerInteractEvent(event.getPlayer(), Action.RIGHT_CLICK_BLOCK, event.getPlayer().getInventory().getItemInMainHand(), facing, entity.getAttachedFace());
	            plugin.getServer().getPluginManager().callEvent(interactBlock);
	
	            if (interactBlock.useInteractedBlock() == Event.Result.DENY) {
	                entity.setRotation(entity.getRotation().rotateCounterClockwise());
	                return;
	            }
	        }
	
	        if(event.getPlayer().isSneaking() && entity.getItem().getType() != Material.AIR) {
	            entity.setRotation(entity.getRotation().rotateCounterClockwise());
	
	            entity.setVisible(!entity.isVisible());
	            return;
	        }
	
	        if(entity.isVisible()) return;
	
	        entity.setRotation(entity.getRotation().rotateCounterClockwise());
	
	        if(! (facing.getState() instanceof InventoryHolder)) return;
	
	        if (facing.getState() instanceof Chest) {
	            if (getRelativeChestFace(facing) != null &&
	                facing.getRelative(getRelativeChestFace(facing)).getRelative(0, 1, 0).getType().isOccluding()
	            )
	                return;
	            if (facing.getRelative(0, 1, 0).getType().isOccluding()) return;
	
	        }
	
	        Inventory inv = ((InventoryHolder) facing.getState()).getInventory();
	        event.getPlayer().openInventory(inv);
	        
    	}
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(! Bukkit.getVersion().contains("1.16")) return;

        if(!(event.getInventory().getHolder() instanceof DoubleChest)) return;
        DoubleChest b = (DoubleChest) event.getInventory().getHolder();

        playChestCloseAnimation(((Chest) b.getLeftSide()).getBlock());
        playChestCloseAnimation(((Chest) b.getRightSide()).getBlock());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;

        if(! (event.getEntityType() == EntityType.ITEM_FRAME) /*&& ! (event.getEntityType() == EntityType.GLOW_ITEM_FRAME)*/) return;

        ItemFrame entity = (ItemFrame) event.getEntity();

        if(entity.isVisible()) return;

        event.setCancelled(true);
    }

    public static BlockFace getRelativeChestFace(Block block) {
        org.bukkit.block.data.type.Chest chest = (org.bukkit.block.data.type.Chest) block.getBlockData();
        BlockFace face = (((org.bukkit.block.data.type.Chest) block.getBlockData()).getFacing());
        BlockFace relativeFace = null;
        if (chest.getType() == org.bukkit.block.data.type.Chest.Type.LEFT) {
            if (face == BlockFace.NORTH) {
                relativeFace = BlockFace.EAST;
            } else if (face == BlockFace.SOUTH) {
                relativeFace = BlockFace.WEST;
            } else if (face == BlockFace.WEST) {
                relativeFace = BlockFace.NORTH;
            } else if (face == BlockFace.EAST) {
                relativeFace = BlockFace.SOUTH;
            }
        } else if (chest.getType() == org.bukkit.block.data.type.Chest.Type.RIGHT) {
            if (face == BlockFace.NORTH) {
                relativeFace = BlockFace.WEST;
            } else if (face == BlockFace.SOUTH) {
                relativeFace = BlockFace.EAST;
            } else if (face == BlockFace.WEST) {
                relativeFace = BlockFace.SOUTH;
            } else if (face == BlockFace.EAST) {
                relativeFace = BlockFace.NORTH;
            }
        }
        return relativeFace;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public  String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();

        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public  void playChestCloseAnimation(Block chest) {
       try {
            String version = getVersion(plugin.getInstance().getServer());

            String nms = "net.minecraft.server." + version + ".";
            String obc = "org.bukkit.craftbukkit." + version + ".";

            Class<?> classBlockPosition = Class.forName(nms + "BlockPosition");
            Class<?> classCraftBlock = Class.forName(obc + "block.CraftBlock");
            Class<?> classCraftWorld = Class.forName(obc + "CraftWorld");
            Class<?> classWorldServer = Class.forName(nms + "WorldServer");
            Class<?> classBlock = Class.forName(nms + "Block");

            Object craftWorld = classCraftWorld.cast(chest.getLocation().getWorld());
            Object craftBlock = classCraftBlock.cast(chest);

            Constructor<?> constructorBlockPosition = classBlockPosition.getConstructor(int.class, int.class, int.class);

            Method methodGetHandleBlock = classCraftBlock.getDeclaredMethod("getNMSBlock");
            Method methodGetHandleWorld = classCraftWorld.getDeclaredMethod("getHandle");
            Method methodPlayBlockAction = classWorldServer.getDeclaredMethod("playBlockAction", classBlockPosition, classBlock, int.class, int.class);

            methodGetHandleBlock.setAccessible(true);
            Object nmsBlock = methodGetHandleBlock.invoke(craftBlock);
            Object handleWorld = methodGetHandleWorld.invoke(craftWorld);
            Object blockPosition = constructorBlockPosition.newInstance(chest.getX(), chest.getY(), chest.getZ());

            methodPlayBlockAction.invoke(handleWorld, blockPosition, nmsBlock, 1, 0);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
}
