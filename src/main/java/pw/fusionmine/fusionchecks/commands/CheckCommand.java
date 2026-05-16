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
        checkManager.startCheck(target.getPlayer(), player);
    }

    @Subcommand("stop")
    @CommandPermission("fusionchecks.check.stop")
    public void onStop(ProxiedPlayer player, OnlinePlayer target) {
        checkManager.endCheck(target.getPlayer());
    }

    @Subcommand("soft")
    @CommandPermission("fusionchecks.check.soft")
    public void onStop(ProxiedPlayer player) {
        ProxiedPlayer target = checkManager.getTargetByChecker(player.getUniqueId());
        Utils.sendBanCommand(target, Settings.IMP.CHECK.BAN_COMMAND);
        checkManager.endCheck(target);
    }

    @Subcommand("reload")
    @CommandPermission("fusionchecks.check.reload")
    public void onReload(ProxiedPlayer player) {
        plugin.reloadSettings();
        player.sendMessage("Вы перезагрузили плагин");
    }

    @Subcommand("confess")
    public void onConfess(ProxiedPlayer player) {
        Utils.sendBanCommand(player, Settings.IMP.CHECK.DENY_BAN_COMMAND);
        checkManager.endCheck(player);
        player.sendMessage("Вы признались в читах");
    }

}
