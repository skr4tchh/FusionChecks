package pw.fusionmine.fusionchecks;

import net.elytrium.commons.config.YamlConfig;

public class Settings extends YamlConfig {

    @Ignore
    public static final Settings IMP = new Settings();

    public String PREFIX = "&6Проверка &7»&f";

    @Create
    public CHECK CHECK;

    public static class CHECK {

        public String SERVER = "auth";

        public String BAN_COMMAND = "banip %target% 30d 3.1";

        public String LEAVE_BAN_COMMAND = "banip %target% 30d 3.1.2 - Уход от проверки";

        public String DENY_BAN_COMMAND = "banip %target% 28d 3.1.2 - Признание в читах";

    }

    @Create
    public TITLE TITLE;

    public static class TITLE {

        public String TITLE = "&4⚠ &c&lПРОВЕРКА &4⚠";

        public String SUBTITLE = "&f&lСмотрите информацию в чате";

    }

    @Create
    public MESSAGES MESSAGES;

    public static class MESSAGES {

        public String SETTINGS_RELOADED = "{PRFX} Настройки плагина обновлены.";

        @Create
        public CHECK CHECK;

        public static class CHECK {

            public String RELEASED = "{PRFX} &fВас отпустили с проверки.";

            public String BYPASS = "{PRFX} Вы не можете вызвать этого игрока на проверку.";

            public String NO_YOURSELF = "{PRFX} Вы не можете вызвать самого себя на проверку.";

            public String START = "{PRFX} Вы вызвали игрока &6%player% &fна проверку.";

            public String STOP = "{PRFX} Вы завершили проверку игрока &6%player%&f.";

            public String SOFT = "{PRFX} Вы забанили игрока &6%player% &fза читы.";

            public String LEAVE = "{PRFX} Игрок &6%player% &fвышел с сервера, проверка завершена.";

            public String ACTIVE = "{PRFX} Вы уже проверяете игрока.";

            public String NO_CHECK = "{PRFX} Вы не находитесь на проверке.";

            public String INFO =
            "\n&f Модератор &9%checker% &fвызвал вас на проверку!\n"
            + "&c Выход с сервера приведёт к блокировке аккаунта!\n\n"
            + "&a ▶ &fУстановите программу &bAnyDesk &f- &b&nanydesk.com&r &7(нажмите)\n"
            + "&a ▶ &fОтправьте код &7(это рабочее место) &fмодератору\n\n"
            + "&e /&e&lcontact &e[текст] &f- отправить сообщение модератору\n"
            + "&e /&e&lcontact &cя читер &f- признаться в читах &7(срок будет уменьшен)\n";

        }

        @Create
        public CONTACT CONTACT;

        public static class CONTACT {

            public String USAGE = "{PRFX} Использование: &6/contact [текст]";

            public String FORMAT = "&8[&6Проверка&8] %sender% &8⇒ &f%message%";

        }

    }

}
