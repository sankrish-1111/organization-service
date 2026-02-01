package com.santhanam.service;

import com.santhanam.model.Group;
import com.santhanam.model.GroupStatus;
import com.santhanam.dto.GroupCreatePayload;
import com.santhanam.dto.GroupUpdatePayload;
import com.santhanam.dto.GroupDetailsResponse;
import com.santhanam.repository.GroupRepository;
import com.santhanam.exception.ParentGroupNotFoundException;
import com.santhanam.exception.GroupNotFoundException;
import com.santhanam.exception.GroupHasChildrenException;
import com.santhanam.exception.MemberNotFoundException;
import com.santhanam.model.User;
import com.santhanam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class GroupService {
    private static final Logger log = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GroupService(GroupRepository groupRepository, RedisTemplate<String, Object> redisTemplate) {
        this.groupRepository = groupRepository;
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private UserRepository userRepository;

    private static final String USER_MEMBERSHIP_KEY = "group:%s:users";
    private static final String USER_GROUP_KEY = "user:%s:group";

    @Transactional
    public GroupDetailsResponse createGroup(GroupCreatePayload request) {
        log.info("Creating group with name: {}", request.getName());
        if (request.getParentUuid() != null && !request.getParentUuid().isEmpty()) {
            groupRepository.findById(request.getParentUuid())
                    .orElseThrow(() -> new ParentGroupNotFoundException(request.getParentUuid()));
        }
        Group group = Group.builder()
                .uuid(UUID.randomUUID().toString())
                .parentUuid(request.getParentUuid())
                .name(request.getName())
                .displayName(request.getDisplayName() != null ? request.getDisplayName() : request.getName())
                .status(request.getStatus() != null ? request.getStatus() : GroupStatus.ACTIVE)
                .spaceId(request.getSpaceId())
                .location(request.getLocation())
                .language(request.getLanguage())
                .segments(request.getSegments())
                .build();
        Group savedGroup = groupRepository.save(group);
        log.info("Group created with UUID: {}", savedGroup.getUuid());
        return mapToResponse(savedGroup);
    }

    public GroupDetailsResponse getGroupWithInheritance(String uuid) {
        log.info("Fetching group with UUID: {}", uuid);
        Group group = groupRepository.findById(uuid)
                .orElseThrow(() -> new GroupNotFoundException(uuid));
        Group resolvedGroup = resolveInheritance(group);
        return mapToResponse(resolvedGroup);
    }

    private Group resolveInheritance(Group group) {
        Group resolved = Group.builder()
                .uuid(group.getUuid())
                .parentUuid(group.getParentUuid())
                .name(group.getName())
                .displayName(group.getDisplayName())
                .status(group.getStatus())
                .spaceId(group.getSpaceId())
                .location(group.getLocation())
                .language(group.getLanguage())
                .segments(group.getSegments())
                .build();
        if (resolved.getSpaceId() == null ||
                resolved.getLocation() == null ||
                resolved.getLanguage() == null ||
                resolved.getSegments() == null) {
            String currentParentUuid = group.getParentUuid();
            Set<String> visited = new HashSet<>();
            visited.add(group.getUuid());
            while (currentParentUuid != null && !currentParentUuid.isEmpty()) {
                if (visited.contains(currentParentUuid)) {
                    log.warn("Circular reference detected in group hierarchy at UUID: {}", currentParentUuid);
                    break;
                }
                visited.add(currentParentUuid);
                Optional<Group> parentOpt = groupRepository.findById(currentParentUuid);
                if (parentOpt.isEmpty()) {
                    log.warn("Parent group not found: {}", currentParentUuid);
                    break;
                }
                Group parent = parentOpt.get();
                if (resolved.getSpaceId() == null && parent.getSpaceId() != null) {
                    resolved.setSpaceId(parent.getSpaceId());
                }
                if (resolved.getLocation() == null && parent.getLocation() != null) {
                    resolved.setLocation(parent.getLocation());
                }
                if (resolved.getLanguage() == null && parent.getLanguage() != null) {
                    resolved.setLanguage(parent.getLanguage());
                }
                if (resolved.getSegments() == null && parent.getSegments() != null) {
                    resolved.setSegments(parent.getSegments());
                }
                if (resolved.getSpaceId() != null &&
                        resolved.getLocation() != null &&
                        resolved.getLanguage() != null &&
                        resolved.getSegments() != null) {
                    break;
                }
                currentParentUuid = parent.getParentUuid();
            }
        }
        return resolved;
    }

    @Transactional
    public GroupDetailsResponse updateGroup(String uuid, GroupUpdatePayload request) {
        log.info("Updating group with UUID: {}", uuid);
        Group group = groupRepository.findById(uuid)
                .orElseThrow(() -> new GroupNotFoundException(uuid));
        if (request.getName() != null) {
            group.setName(request.getName());
        }
        if (request.getDisplayName() != null) {
            group.setDisplayName(request.getDisplayName());
        }
        if (request.getStatus() != null) {
            group.setStatus(request.getStatus());
        }
        if (request.getSpaceId() != null) {
            group.setSpaceId(request.getSpaceId());
        }
        if (request.getLocation() != null) {
            group.setLocation(request.getLocation());
        }
        if (request.getLanguage() != null) {
            group.setLanguage(request.getLanguage());
        }
        if (request.getSegments() != null) {
            group.setSegments(request.getSegments());
        }
        Group updatedGroup = groupRepository.save(group);
        log.info("Group updated successfully: {}", uuid);
        return mapToResponse(updatedGroup);
    }

    @Transactional
    public void deleteGroup(String uuid) {
        log.info("Deleting group with UUID: {}", uuid);
        Group group = groupRepository.findById(uuid)
                .orElseThrow(() -> new GroupNotFoundException(uuid));
        List<Group> children = groupRepository.findByParentUuid(uuid);
        if (!children.isEmpty()) {
            throw new GroupHasChildrenException(uuid);
        }
        String membershipKey = String.format(USER_MEMBERSHIP_KEY, uuid);
        redisTemplate.delete(membershipKey);
        groupRepository.delete(group);
        log.info("Group deleted successfully: {}", uuid);
    }

    @Transactional
    public void addUserToGroup(String groupUuid, String userId) {
        log.info("Adding user {} to group {}", userId, groupUuid);
        groupRepository.findById(groupUuid)
                .orElseThrow(() -> new GroupNotFoundException(groupUuid));
        String membershipKey = String.format(USER_MEMBERSHIP_KEY, groupUuid);
        String userGroupKey = String.format(USER_GROUP_KEY, userId);
        redisTemplate.opsForSet().add(membershipKey, userId);
        redisTemplate.opsForValue().set(userGroupKey, groupUuid);
        log.info("User {} added to group {}", userId, groupUuid);
    }

    @Transactional
    public void removeUserFromGroup(String groupUuid, String userId) {
        log.info("Removing user {} from group {}", userId, groupUuid);
        groupRepository.findById(groupUuid)
                .orElseThrow(() -> new GroupNotFoundException(groupUuid));
        String membershipKey = String.format(USER_MEMBERSHIP_KEY, groupUuid);
        String userGroupKey = String.format(USER_GROUP_KEY, userId);
        Boolean isMember = redisTemplate.opsForSet().isMember(membershipKey, userId);
        if (Boolean.FALSE.equals(isMember)) {
            throw new MemberNotFoundException(userId);
        }
        redisTemplate.opsForSet().remove(membershipKey, userId);
        redisTemplate.delete(userGroupKey);
        log.info("User {} removed from group {}", userId, groupUuid);
    }

    @Transactional
    public void moveUser(String userId, String targetGroupUuid) {
        log.info("Moving user {} to group {}", userId, targetGroupUuid);
        groupRepository.findById(targetGroupUuid)
                .orElseThrow(() -> new GroupNotFoundException(targetGroupUuid));
        String userGroupKey = String.format(USER_GROUP_KEY, userId);
        String currentGroupUuid = (String) redisTemplate.opsForValue().get(userGroupKey);
        if (currentGroupUuid != null) {
            String currentMembershipKey = String.format(USER_MEMBERSHIP_KEY, currentGroupUuid);
            redisTemplate.opsForSet().remove(currentMembershipKey, userId);
        }
        String targetMembershipKey = String.format(USER_MEMBERSHIP_KEY, targetGroupUuid);
        redisTemplate.opsForSet().add(targetMembershipKey, userId);
        redisTemplate.opsForValue().set(userGroupKey, targetGroupUuid);
        log.info("User {} moved from group {} to group {}", userId, currentGroupUuid, targetGroupUuid);
    }

    public List<User> getUsersInGroup(String groupUuid) {
        Set<Object> userIds = redisTemplate.opsForSet().members(String.format(USER_MEMBERSHIP_KEY, groupUuid));
        if (userIds == null) return List.of();
        List<User> users = new ArrayList<>();
        for (Object id : userIds) {
            userRepository.findById(id.toString()).ifPresent(users::add);
        }
        return users;
    }

    private GroupDetailsResponse mapToResponse(Group group) {
        return GroupDetailsResponse.builder()
                .uuid(group.getUuid())
                .parentUuid(group.getParentUuid())
                .name(group.getName())
                .displayName(group.getDisplayName())
                .status(group.getStatus())
                .spaceId(group.getSpaceId())
                .location(group.getLocation())
                .language(group.getLanguage())
                .segments(group.getSegments())
                .build();
    }
}