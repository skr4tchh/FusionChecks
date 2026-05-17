package pw.fusionmine.fusionchecks.listeners;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pw.fusionmine.fusionchecks.Settings;
import pw.fusionmine.fusionchecks.managers.CheckManager;
import pw.fusionmine.fusionchecks.utils.Utils;

@AllArgsConstructor
public class CheckListener implements Listener {

    private CheckManager checkManager;

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        ProxiedPlayer target = checkManager.getTargetByChecker(player.getUniqueId());
        if (target != null) {
            checkManager.endCheck(target);
        } else if (checkManager.isChecking(player)) {
            Utils.sendBanCommand(player, Settings.IMP.CHECK.LEAVE_BAN_COMMAND);
            checkManager.endCheck(player);
            ProxiedPlayer checker = checkManager.getCheckerByTarget(player);
            Utils.sendMessage(checker, Settings.IMP.MESSAGES.CHECK.LEAVE);
        }
    }

    @EventHandler
    public void onCommand(ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        if (!checkManager.isChecking(player))
            return;
        String msg = e.getMessage().toLowerCase();
        if (!msg.startsWith("/contact")) {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CONTACT.USAGE);
            e.setCancelled(true);
        }
    }

}
