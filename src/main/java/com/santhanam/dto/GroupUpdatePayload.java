package com.santhanam.dto;

import com.santhanam.model.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Request object for updating an existing group")
public class GroupUpdatePayload {
    @Schema(description = "UUID of the parent group", example = "550e8400-e29b-41d4-a716-446655440000")
    private String parentUuid;
    @Schema(description = "Internal name of the group", example = "Engineering Group Updated")
    private String name;
    @Schema(description = "Public display name", example = "Engineering Group - US Updated")
    private String displayName;
    @Schema(description = "Group status", example = "DEACTIVATED")
    private GroupStatus status;
    @Schema(description = "ID of the linked Client Space", example = "space-456")
    private String spaceId;
    @Schema(description = "Location of the group", example = "France")
    private String location;
    @Schema(description = "Language code", example = "fr-FR")
    private String language;
    @Schema(description = "List of business verticals", example = "[\"Corporate\", \"Frontline\"]")
    private List<String> segments;

    public GroupUpdatePayload() {}

    public GroupUpdatePayload(String parentUuid, String name, String displayName, GroupStatus status,
                             String spaceId, String location, String language, List<String> segments) {
        this.parentUuid = parentUuid;
        this.name = name;
        this.displayName = displayName;
        this.status = status;
        this.spaceId = spaceId;
        this.location = location;
        this.language = language;
        this.segments = segments;
    }

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
}
