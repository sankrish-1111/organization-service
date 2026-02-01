package com.santhanam.controller;

import com.santhanam.dto.GroupCreatePayload;
import com.santhanam.dto.GroupDetailsResponse;
import com.santhanam.dto.GroupUpdatePayload;
import com.santhanam.dto.MemberAddPayload;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@Tag(name = "Group Management", description = "APIs for managing groups and their hierarchy")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Operation(summary = "Create a group", description = "Creates a new group. If displayName is missing, uses name. Parent validation and status defaulting handled.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Parent group not found")
    })
    @PostMapping
    public ResponseEntity<GroupDetailsResponse> createGroup(@Valid @RequestBody GroupCreatePayload payload) {
        return ResponseEntity.ok(groupService.createGroup(payload));
    }

    @Operation(summary = "Get a group with inheritance", description = "Retrieves a group and resolves inheritable fields from parent hierarchy.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group found"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<GroupDetailsResponse> getGroup(@Parameter(description = "UUID of the group") @PathVariable String uuid) {
        return ResponseEntity.ok(groupService.getGroupWithInheritance(uuid));
    }

    @Operation(summary = "Update a group", description = "Updates group details. Only name, displayName, and inheritable fields can be changed.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group updated successfully"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<GroupDetailsResponse> updateGroup(@Parameter(description = "UUID of the group") @PathVariable String uuid, @Valid @RequestBody GroupUpdatePayload payload) {
        return ResponseEntity.ok(groupService.updateGroup(uuid, payload));
    }

    @Operation(summary = "Delete a group", description = "Deletes a group if it has no children.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Group deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Group has children"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteGroup(@Parameter(description = "UUID of the group") @PathVariable String uuid) {
        groupService.deleteGroup(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add user to group", description = "Adds a user to the specified group.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User added to group"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @PostMapping("/{uuid}/users")
    public ResponseEntity<Void> addUserToGroup(@Parameter(description = "UUID of the group") @PathVariable String uuid, @Valid @RequestBody MemberAddPayload payload) {
        groupService.addUserToGroup(uuid, payload.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user from group", description = "Removes a user from the specified group.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User removed from group"),
        @ApiResponse(responseCode = "404", description = "Group or user not found")
    })
    @DeleteMapping("/{uuid}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@Parameter(description = "UUID of the group") @PathVariable String uuid, @Parameter(description = "User ID") @PathVariable String userId) {
        groupService.removeUserFromGroup(uuid, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List users in group", description = "Lists all users in the specified group.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users listed successfully"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @GetMapping("/{uuid}/users")
    public ResponseEntity<List<com.santhanam.model.User>> listUsersInGroup(@Parameter(description = "UUID of the group") @PathVariable String uuid) {
        return ResponseEntity.ok(groupService.getUsersInGroup(uuid));
    }
}