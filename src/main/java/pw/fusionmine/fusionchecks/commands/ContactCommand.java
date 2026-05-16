package pw.fusionmine.fusionchecks.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pw.fusionmine.fusionchecks.managers.CheckManager;

@AllArgsConstructor
@CommandAlias("contact")
public class ContactCommand extends BaseCommand {

    private CheckManager checkManager;

    @Default
    public void onCommand(ProxiedPlayer player, String[] args) {
        if(!checkManager.isChecking(player)) {
            player.sendMessage("Вы не на проверке");
            return;
        }
    }

}
