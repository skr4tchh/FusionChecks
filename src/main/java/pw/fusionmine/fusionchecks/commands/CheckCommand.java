package pw.fusionmine.fusionchecks.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bungee.contexts.OnlinePlayer;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pw.fusionmine.fusionchecks.FusionChecks;
import pw.fusionmine.fusionchecks.Settings;
import pw.fusionmine.fusionchecks.managers.CheckManager;
import pw.fusionmine.fusionchecks.utils.Utils;

@AllArgsConstructor
@CommandAlias("check|ss")
public class CheckCommand extends BaseCommand {

    private FusionChecks plugin;

    private CheckManager checkManager;

    @Subcommand("start")
    @CommandPermission("fusionchecks.check.start")
    public void onStart(ProxiedPlayer player, OnlinePlayer target) {
        if(checkManager.isCheckerActive(player)) {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.ACTIVE);
            return;
        }

        ProxiedPlayer targetPlayer = target.getPlayer();
        if(targetPlayer.getName().equals(player.getName())) {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.NO_YOURSELF);
            return;
        }

        if(targetPlayer.hasPermission("fusionchecks.check.bypass")) {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.BYPASS);
            return;
        }

        checkManager.startCheck(targetPlayer, player);
        Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.START.replace("%player%", targetPlayer.getName()));
    }

    @Subcommand("stop")
    @CommandPermission("fusionchecks.check.stop")
    public void onStop(ProxiedPlayer player, OnlinePlayer target) {
        if(!checkManager.isCheckerActive(player)) {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.NO);
            return;
        }

        ProxiedPlayer targetPlayer = target.getPlayer();
        checkManager.endCheck(targetPlayer);
        Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.STOP.replace("%player%", targetPlayer.getName()));
    }

    @Subcommand("soft")
    @CommandPermission("fusionchecks.check.soft")
    public void onStop(ProxiedPlayer player) {
        ProxiedPlayer target = checkManager.getTargetByChecker(player.getUniqueId());
        if(target == null) {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.NO);
            return;
        }
        Utils.sendBanCommand(target, Settings.IMP.CHECK.BAN_COMMAND);
        checkManager.endCheck(target);
        Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.SOFT.replace("%player%", target.getName()));
    }

    @Subcommand("reload")
    @CommandPermission("fusionchecks.check.reload")
    public void onReload(ProxiedPlayer player) {
        plugin.reloadSettings();
        Utils.sendMessage(player, Settings.IMP.MESSAGES.SETTINGS_RELOADED);
    }

}
