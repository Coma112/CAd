package net.coma112.cad.enums.keys;

import net.coma112.cad.CAd;
import net.coma112.cad.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum MessageKeys {
    LAST_PAGE("message.last-page"),
    FIRST_PAGE("message.first-page"),
    RELOAD("message.reload"),
    REACHED_MAX_ADS("message.reached-max-ads"),
    NEW_USAGE("message.new-usage"),
    NOT_EXISTS("message.not-exists"),
    SUCCESSFUL_REMOVE("message.successful-remove"),
    NOT_ENOUGH_MONEY("message.not-enough-money"),
    HELP("message.help");

    private final String path;

    MessageKeys(@NotNull String path) {
        this.path = path;
    }

    public String getMessage() {
        return MessageProcessor.process(CAd.getInstance().getLanguage().getString(path));
    }

    public List<String> getMessages() {
        return CAd.getInstance().getLanguage().getList(path)
                .stream()
                .map(MessageProcessor::process)
                .toList();
    }

}
