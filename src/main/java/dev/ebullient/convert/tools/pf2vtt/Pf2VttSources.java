package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ebullient.convert.qute.ImageRef;
import dev.ebullient.convert.tools.CompendiumSources;
import dev.ebullient.convert.tools.IndexType;
import dev.ebullient.convert.tools.JsonTextConverter.SourceField;
import dev.ebullient.convert.tools.ToolsIndex.TtrpgValue;
import dev.ebullient.convert.tools.pf2e.Pf2eIndex;
import io.quarkus.qute.TemplateData;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@TemplateData
public class Pf2VttSources extends CompendiumSources {

    private static final Map<String, Pf2VttSources> keyToSources = new HashMap<>();
    private static final Map<String, ImageRef> imageSourceToRef = new HashMap<>();

    public static Pf2VttSources findSources(String key) {
        return keyToSources.get(key);
    }

    public static Pf2VttSources findSources(JsonNode node) {
        String key = TtrpgValue.indexKey.getFromNode(node);
        return keyToSources.get(key);
    }

    public static Pf2VttSources constructSources(Pf2VttIndexType type, JsonNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Must pass a JsonNode");
        }
        String key = TtrpgValue.indexKey.getFromNode(node);
        return keyToSources.computeIfAbsent(key, k -> {
            Pf2VttSources s = new Pf2VttSources(type, key, node);
            s.checkKnown();
            return s;
        });
    }

    public static Pf2VttSources constructSyntheticSource(String name) {
        String key = Pf2VttIndexType.syntheticGroup.createKey(name, "mixed");
        return new Pf2VttSources(Pf2VttIndexType.syntheticGroup, key, null);
    }

    public static Pf2VttSources createEmbeddedSource(JsonNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Must pass a JsonNode");
        }
        String key = Pf2VttIndexType.bookReference.createKey(node);
        return new Pf2VttSources(Pf2VttIndexType.bookReference, key, node);
    }

    public static Pf2VttSources findOrTemporary(Pf2VttIndexType type, JsonNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Must pass a JsonNode");
        }
        String key = TtrpgValue.indexKey.getFromNode(node);
        if (key == null) {
            key = type.createKey(node);
        }
        Pf2VttSources sources = findSources(key);
        return sources == null
                ? new Pf2VttSources(type, key, node)
                : sources;
    }

    public static ImageRef buildStreamImageRef(Pf2VttIndex index, String sourcePath, Path relativeTarget, String title) {
        ImageRef imageRef = new ImageRef.Builder()
                .setStreamSource(sourcePath)
                .setRelativePath(Path.of("assets").resolve(relativeTarget))
                .setTitle(index.replaceText(title))
                .setRootFilepath(index.rulesFilePath())
                .setVaultRoot(index.rulesVaultRoot())
                .build();
        imageSourceToRef.put(imageRef.sourcePath().toString(), imageRef);
        return imageRef;
    }

    public static ImageRef buildImageRef(Pf2VttIndexType type, Pf2VttIndex index, Path sourcePath, String title) {
        return buildImageRef(type, index, sourcePath, sourcePath, title);
    }

    public static ImageRef buildImageRef(Pf2VttIndexType type, Pf2VttIndex index, Path sourcePath, Path relativeTarget,
            String title) {
        ImageRef imageRef = new ImageRef.Builder()
                .setSourcePath(sourcePath)
                .setRelativePath(Path.of("assets").resolve(relativeTarget))
                .setRootFilepath(type.getFilePath(index))
                .setVaultRoot(type.getVaultRoot(index))
                .setTitle(index.replaceText(title))
                .build();
        imageSourceToRef.put(imageRef.sourcePath().toString(), imageRef);
        return imageRef;
    }

    public static Collection<ImageRef> getImages() {
        return imageSourceToRef.values();
    }

    final Pf2VttIndexType type;

    private Pf2VttSources(Pf2VttIndexType type, String key, JsonNode node) {
        super(type, key, node);
        this.type = type;
    }

    public JsonNode findNode() {
        return Pf2VttIndex.findNode(this);
    }

    protected String findName(IndexType type, JsonNode node) {
        if (type == Pf2VttIndexType.syntheticGroup || type == Pf2VttIndexType.bookReference) {
            return this.key.replaceAll(".*\\|(.*)\\|", "$1");
        }
        String name = SourceField.name.getTextOrEmpty(node);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Unknown element, has no name: " + node.toString());
        }
        return name;
    }

    @Override
    protected String findSourceText(IndexType type, JsonNode jsonElement) {
        if (type == Pf2VttIndexType.syntheticGroup) {
            return this.key.replaceAll(".*\\|([^|]+)$", "$1");
        }
        return super.findSourceText(type, jsonElement);
    }

    @Override
    public Pf2VttIndexType getType() {
        return type;
    }

    /** Documents that have no primary source (compositions) */
    protected boolean isSynthetic() {
        return type == Pf2VttIndexType.syntheticGroup;
    }

    public boolean fromDefaultSource() {
        if (type == Pf2VttIndexType.data) {
            return true;
        }
        return type.defaultSourceString().equals(primarySource().toLowerCase());
    }

    public enum DefaultSource {
        apg,
        b1,
        crb,
        gmg,
        locg,
        lotg,
        som;

        public boolean sameSource(String source) {
            return this.name().equalsIgnoreCase(source);
        }
    }
}