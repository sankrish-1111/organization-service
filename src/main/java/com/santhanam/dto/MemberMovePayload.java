package com.santhanam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for moving a member to another group.
 * Author: Santhanam
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to move a member to another group")
public class MemberMovePayload {
    @NotBlank(message = "Target group UUID is mandatory")
    @Schema(description = "UUID of the target group", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private String targetGroupUuid;

    public String getTargetGroupUuid() {
        return targetGroupUuid;
    }
}