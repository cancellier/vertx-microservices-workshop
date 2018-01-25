package io.vertx.workshop.share;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Structure representing a share.
 */
@DataObject(generateConverter = true)
public class Share {

    private String name;

    private String code;

    /**
     * Creates a new instance of {@link Share}.
     */
    public Share() {
        // Empty constructor
    }

    public Share(Share other) {
        this.code = other.code;
        this.name = other.name;
    }

    public Share(JsonObject json) {
        ShareConverter.fromJson(json, this);
    }

    /**
     * @return a JSON representation of the share computed using the converter.
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ShareConverter.toJson(this, json);
        return json;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Share{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
