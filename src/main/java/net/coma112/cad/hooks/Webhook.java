package net.coma112.cad.hooks;

import lombok.Getter;
import lombok.Setter;
import net.coma112.cad.events.AdCreateEvent;
import net.coma112.cad.events.AdRemoveEvent;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Webhook {
    private final String url;
    private final List<EmbedObject> embeds = new ArrayList<>();
    @Setter
    private String content;
    @Setter
    private String username;
    @Setter
    private String avatarUrl;
    @Setter
    private boolean tts;

    public Webhook(@NotNull String url) {
        this.url = url;
    }

    public void addEmbed(@NotNull EmbedObject embed) {
        this.embeds.add(embed);
    }

    public static void sendWebhook(@NotNull String url,
                                   boolean isEnabled,
                                   @NotNull String description,
                                   @NotNull String color,
                                   @NotNull String authorName,
                                   @NotNull String authorURL,
                                   @NotNull String authorIconURL,
                                   @NotNull String footerText,
                                   @NotNull String footerIconURL,
                                   @NotNull String thumbnailURL,
                                   @NotNull String title,
                                   @NotNull String imageURL) throws IOException, NoSuchFieldException, IllegalAccessException, URISyntaxException {

        if (isEnabled) {
            Webhook webhook = new Webhook(url);

            webhook.addEmbed(new Webhook.EmbedObject()
                    .setDescription(description)
                    .setColor((Color) Color.class.getField(color.toUpperCase()).get(null))
                    .setFooter(footerText, footerIconURL)
                    .setThumbnail(thumbnailURL)
                    .setTitle(title)
                    .setAuthor(authorName, authorURL, authorIconURL)
                    .setImage(imageURL)
            );

            webhook.execute();
        }
    }

    public void execute() throws IOException, URISyntaxException {
        if (this.content == null && this.embeds.isEmpty()) throw new IllegalArgumentException("Hiba történt!");

        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            List<JSONObject> embedObjects = new ArrayList<>();

            this.embeds.forEach(embed -> {
                JSONObject jsonEmbed = new JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                if (embed.getColor() != null) {
                    Color color = embed.getColor();

                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();

                    jsonEmbed.put("color", rgb);
                }

                EmbedObject.Footer footer = embed.getFooter();
                EmbedObject.Image image = embed.getImage();
                EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                EmbedObject.Author author = embed.getAuthor();
                List<EmbedObject.Field> fields = embed.getFields();

                if (footer != null) {
                    JSONObject jsonFooter = new JSONObject();

                    jsonFooter.put("text", footer.text());
                    jsonFooter.put("icon_url", footer.iconUrl());
                    jsonEmbed.put("footer", jsonFooter);
                }

                if (image != null) {
                    JSONObject jsonImage = new JSONObject();

                    jsonImage.put("url", image.url());
                    jsonEmbed.put("image", jsonImage);
                }

                if (thumbnail != null) {
                    JSONObject jsonThumbnail = new JSONObject();

                    jsonThumbnail.put("url", thumbnail.url());
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }

                if (author != null) {
                    JSONObject jsonAuthor = new JSONObject();

                    jsonAuthor.put("name", author.name());
                    jsonAuthor.put("url", author.url());
                    jsonAuthor.put("icon_url", author.iconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                }

                List<JSONObject> jsonFields = new ArrayList<>();

                fields.forEach(field -> {
                    JSONObject jsonField = new JSONObject();

                    jsonField.put("name", field.name());
                    jsonField.put("value", field.value());
                    jsonField.put("inline", field.inline());

                    jsonFields.add(jsonField);
                });

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            });

            json.put("embeds", embedObjects.toArray());
        }

        URI uri = new URI(this.url);
        URL url = uri.toURL();
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-Webhook");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    @Getter
    public static class EmbedObject {
        private final List<Field> fields = new ArrayList<>();
        private String title;
        private String description;
        private String url;
        private Color color;
        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;

        public EmbedObject setDescription(@NotNull String description) {
            this.description = description;
            return this;
        }

        public EmbedObject setUrl(@NotNull String url) {
            this.url = url;
            return this;
        }

        public EmbedObject setTitle(@NotNull String title) {
            this.title = title;
            return this;
        }

        public EmbedObject setColor(@NotNull Color color) {
            this.color = color;
            return this;
        }

        public EmbedObject setThumbnail(@NotNull String url) {
            this.thumbnail = new Thumbnail(url);
            return this;
        }

        public EmbedObject setImage(@NotNull String url) {
            this.image = new Image(url);
            return this;
        }

        public EmbedObject setFooter(@NotNull String text, @NotNull String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        public EmbedObject setAuthor(@NotNull String name, @NotNull String url, @NotNull String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        public EmbedObject addField(@NotNull String name, @NotNull String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        private record Footer(@NotNull String text, @NotNull String iconUrl) {
        }

        private record Thumbnail(@NotNull String url) {
        }

        private record Image(@NotNull String url) {
        }

        private record Author(@NotNull String name, @NotNull String url, @NotNull String iconUrl) {
        }

        private record Field(@NotNull String name, @NotNull String value, boolean inline) {
        }
    }

    private static class JSONObject {
        private final HashMap<String, Object> map = new HashMap<>();

        void put(String key, Object value) {
            if (value != null) map.put(key, value);
        }

        @Override
        public String toString() {
            return "{" + map
                    .entrySet()
                    .stream()
                    .map(entry -> quote(entry.getKey()) + ": " + stringifyValue(entry.getValue()))
                    .collect(Collectors.joining(", ")) + "}";
        }

        private String stringifyValue(Object value) {
            if (value instanceof String) return quote((String) value);
            if (value instanceof JSONArray) return quote(value.toString());
            if (value != null && value.getClass().isArray()) return arrayToString((Object[]) value);
            if (value instanceof List<?>) return listToString((List<?>) value);

            return String.valueOf(value);
        }

        private String quote(@NotNull String string) {
            return "\"" + string.replace("\"", "\\\"") + "\"";
        }

        private String arrayToString(Object[] array) {
            return "[" + Arrays
                    .stream(array)
                    .map(element -> element instanceof String ? quote((String) element) : String.valueOf(element))
                    .collect(Collectors.joining(", ")) + "]";
        }

        private String listToString(List<?> list) {
            return "[" + list
                    .stream()
                    .map(element -> element instanceof String ? quote((String) element) : String.valueOf(element))
                    .collect(Collectors.joining(", ")) + "]";
        }
    }

    public static String replacePlaceholdersAdCreate(@NotNull String text, final AdCreateEvent event) {
        return text
                .replace("{player}", event.getPlayer())
                .replace("{title}", event.getTitle())
                .replace("{description}", event.getDescription())
                .replace("{endingDate}", event.getEndingDate());
    }

    public static String replacePlaceholdersAdRemove(@NotNull String text, final AdRemoveEvent event) {
        return text
                .replace("{player}", event.getPlayer())
                .replace("{title}", event.getTitle())
                .replace("{description}", event.getDescription())
                .replace("{endingDate}", event.getEndingDate())
                .replace("{startingDate}", event.getStartingDate())
                .replace("{id}", String.valueOf(event.getId()));
    }
}
