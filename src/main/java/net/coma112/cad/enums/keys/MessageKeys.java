package net.coma112.cad.enums.keys;

import net.coma112.cad.CAd;
import net.coma112.cad.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum MessageKeys {
    TEST("message.test"),
    LAST_PAGE("message.last-page"),
    FIRST_PAGE("message.first-page"),
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
