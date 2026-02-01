package com.santhanam.controller;

import com.santhanam.dto.MemberMovePayload;
import com.santhanam.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user movement between groups.
 * Author: Santhanam
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing user movements")
public class MemberController {
    private final GroupService groupService;

    @Autowired
    public MemberController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Move a user to another group",
               description = "Atomically moves a user from their current group to a target group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User moved successfully"),
        @ApiResponse(responseCode = "404", description = "Target group not found")
    })
    @PutMapping("/{userId}/move")
    public ResponseEntity<Void> moveMember(
            @Parameter(description = "ID of the user to move", required = true)
            @PathVariable String userId,
            @Valid @RequestBody MemberMovePayload request) {
        groupService.moveUser(userId, request.getTargetGroupUuid());
        return ResponseEntity.ok().build();
    }
}