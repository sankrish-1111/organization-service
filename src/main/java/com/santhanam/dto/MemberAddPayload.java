package com.santhanam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for adding a member to a team.
 * Author: Santhanam
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to add a member to a team")
public class MemberAddPayload {
    @NotBlank(message = "User ID is mandatory")
    @Schema(description = "ID of the member to add", example = "user-123", required = true)
    private String userId;

    public String getUserId() {
        return userId;
    }
}