package com.phanaticmc.pknte;

import com.nametagedit.plugin.NametagEdit;
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
        new BukkitRunnable() {
            
            @Override
            public void run() {

                Player player = event.getPlayer();
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
            
        }.runTaskLater(this, 20L);
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

	if (bPlayer.getElements().isEmpty()) {
            prefix = prefix + ChatColor.WHITE;
	} else if (bPlayer.getElements().size() > 1) {
            prefix = prefix + Element.AVATAR.getColor();
	} else {
            prefix = prefix + bPlayer.getElements().get(0).getColor();
        }
        
        if(space){
            prefix = prefix + " ";
            return(prefix);
        } else {
            return(prefix);
        }
    }
}
