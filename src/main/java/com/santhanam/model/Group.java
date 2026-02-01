package com.santhanam.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import java.util.List;

@RedisHash("Group")
public class Group {
    @Id
    @Indexed
    private String uuid;
    @Indexed
    private String parentUuid;
    private String name;
    private String displayName;
    private GroupStatus status;
    private String spaceId;
    private String location;
    private String language;
    private List<String> segments;

    public Group() {}
    public Group(String uuid, String parentUuid, String name, String displayName, GroupStatus status, String spaceId, String location, String language, List<String> segments) {
        this.uuid = uuid;
        this.parentUuid = parentUuid;
        this.name = name;
        this.displayName = displayName;
        this.status = status;
        this.spaceId = spaceId;
        this.location = location;
        this.language = language;
        this.segments = segments;
    }
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getParentUuid() { return parentUuid; }
    public void setParentUuid(String parentUuid) { this.parentUuid = parentUuid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public GroupStatus getStatus() { return status; }
    public void setStatus(GroupStatus status) { this.status = status; }
    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public List<String> getSegments() { return segments; }
    public void setSegments(List<String> segments) { this.segments = segments; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String uuid;
        private String parentUuid;
        private String name;
        private String displayName;
        private GroupStatus status;
        private String spaceId;
        private String location;
        private String language;
        private List<String> segments;
        public Builder uuid(String uuid) { this.uuid = uuid; return this; }
        public Builder parentUuid(String parentUuid) { this.parentUuid = parentUuid; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder displayName(String displayName) { this.displayName = displayName; return this; }
        public Builder status(GroupStatus status) { this.status = status; return this; }
        public Builder spaceId(String spaceId) { this.spaceId = spaceId; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder language(String language) { this.language = language; return this; }
        public Builder segments(List<String> segments) { this.segments = segments; return this; }
        public Group build() {
            return new Group(uuid, parentUuid, name, displayName, status, spaceId, location, language, segments);
        }
    }
}