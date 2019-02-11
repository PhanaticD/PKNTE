package com.phanaticmc.pknte;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.events.NametagEvent;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PKNTE extends JavaPlugin implements Listener{
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this); 
    }
    
    @EventHandler
    public void ElementChange(PlayerChangeElementEvent event){
        new BukkitRunnable() {
            
            @Override
            public void run() {

                Player player = event.getTarget();
                if(!player.isOnline()){
                    return;
                }

                String prefix = NametagEdit.getApi().getNametag(player).getPrefix();

                BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
                if(bPlayer == null){
                    return;
                }

                prefix = injectColor(bPlayer, prefix);
                NametagEdit.getApi().setPrefix(player, prefix);

            }
            
        }.runTaskLater(this, 2L);
    }
    
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event){
        injectBending(event.getPlayer());
    }
    
    @EventHandler
    public void onNametagEvent(NametagEvent event) {
            if (event.getChangeType() == NametagEvent.ChangeType.RELOAD) {
                injectBending(Bukkit.getPlayerExact(event.getPlayer()));
            }
    }
    
    private void injectBending(Player player){
        new BukkitRunnable() {
            
            @Override
            public void run() {
                if(!player.isOnline()){
                    return;
                }

                String prefix = NametagEdit.getApi().getNametag(player).getPrefix();
                
                BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
                if(bPlayer == null){
                    return;
                }
                
                prefix = injectColor(bPlayer, prefix);
                NametagEdit.getApi().setPrefix(player, prefix);
            }
            
        }.runTaskLater(this, 10L); 
    }
    
    private String injectColor(BendingPlayer bPlayer, String prefix){
        Boolean space = true;
        prefix = prefix.trim();
        if(prefix.length() >=2 && prefix.charAt(prefix.length() - 2) == '§'){
            prefix = prefix.substring(0, prefix.length() - 2);
        }
        if(ChatColor.stripColor(prefix).length() == 0){
            space = false;
        }

        
        if(bPlayer.getPlayer().hasPermission("bending.avatar") || (bPlayer.hasElement(Element.AIR) && bPlayer.hasElement(Element.EARTH) && bPlayer.hasElement(Element.FIRE) && bPlayer.hasElement(Element.WATER))){
            prefix = prefix + Element.AVATAR.getColor();
	} else if (bPlayer.getElements().size() > 0) {
            prefix = prefix + bPlayer.getElements().get(0).getColor();
	} else {
            prefix = prefix + ChatColor.WHITE;
        }
        
        if(space){
            prefix = prefix + " ";
            return(prefix);
        } else {
            return(prefix);
        }
    }
}
