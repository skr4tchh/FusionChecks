package pw.fusionmine.fusionchecks.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pw.fusionmine.fusionchecks.Settings;
import pw.fusionmine.fusionchecks.managers.CheckManager;
import pw.fusionmine.fusionchecks.utils.Utils;

import java.util.Arrays;

@AllArgsConstructor
@CommandAlias("contact")
public class ContactCommand extends BaseCommand {

    private CheckManager checkManager;

    @Default
    public void onCommand(ProxiedPlayer player, String[] args) {
        if (args.length == 0) {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CONTACT.USAGE);
            return;
        }

        String message = String.join(" ", args);
        String format = Settings.IMP.MESSAGES.CONTACT.FORMAT.replace("%message%", message);
        ProxiedPlayer target = checkManager.getTargetByChecker(player.getUniqueId());
        if(target != null) {
            Utils.sendMessage(target, format.replace("%sender%", "&9" + player.getName()));
            Utils.sendMessage(player, format.replace("%sender%", "&9" + player.getName()));
        } else if(checkManager.isChecking(player)) {
            if(message.toLowerCase().startsWith("я читер")) {
                Utils.sendBanCommand(player, Settings.IMP.CHECK.DENY_BAN_COMMAND);
                checkManager.endCheck(player);
                return;
            }

            ProxiedPlayer checker = checkManager.getCheckerByTarget(player);
            Utils.sendMessage(checker, format.replace("%sender%", "&7" + player.getName()));
            Utils.sendMessage(player, format.replace("%sender%", "&7" + player.getName()));
        } else {
            Utils.sendMessage(player, Settings.IMP.MESSAGES.CHECK.NO);
        }
    }

}
