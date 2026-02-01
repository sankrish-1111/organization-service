package com.santhanam.dto;

import com.santhanam.model.GroupStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating an existing group")
public class GroupUpdatePayload {
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

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public GroupStatus getStatus() { return status; }
    public String getSpaceId() { return spaceId; }
    public String getLocation() { return location; }
    public String getLanguage() { return language; }
    public java.util.List<String> getSegments() { return segments; }
}
