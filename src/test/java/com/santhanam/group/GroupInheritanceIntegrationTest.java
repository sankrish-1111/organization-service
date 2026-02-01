package com.santhanam.group;

import com.santhanam.dto.GroupCreatePayload;
import com.santhanam.dto.GroupDetailsResponse;
import com.santhanam.model.GroupStatus;
import com.santhanam.service.GroupService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for verifying the inheritance logic of the Group Management Service.
 * This test ensures that child groups correctly inherit properties from their parent hierarchy.
 * Author: Santhanam
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.redis.host=localhost",
    "spring.data.redis.port=6379"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupInheritanceIntegrationTest {
    @Autowired
    private GroupService groupService;

    private String rootGroupId;
    private String parentGroupId;
    private String childGroupId;

    @Test
    @Order(1)
    @DisplayName("Create root group with all attributes")
    void createRootGroup() {
        GroupCreatePayload rootPayload = GroupCreatePayload.builder()
                .name("Global Division")
                .displayName("Global Division")
                .status(GroupStatus.ACTIVE)
                .spaceId("space-global")
                .location("India")
                .language("ta-IN")
                .segments(Arrays.asList("Technology", "Research"))
                .build();
        GroupDetailsResponse rootGroup = groupService.createGroup(rootPayload);
        assertNotNull(rootGroup);
        assertNotNull(rootGroup.getUuid());
        assertEquals("Global Division", rootGroup.getName());
        assertEquals("space-global", rootGroup.getSpaceId());
        assertEquals("India", rootGroup.getLocation());
        assertEquals("ta-IN", rootGroup.getLanguage());
        assertEquals(2, rootGroup.getSegments().size());
        rootGroupId = rootGroup.getUuid();
    }

    @Test
    @Order(2)
    @DisplayName("Create parent group with partial attributes")
    void createParentGroup() {
        GroupCreatePayload parentPayload = GroupCreatePayload.builder()
                .parentUuid(rootGroupId)
                .name("Engineering Hub")
                .displayName("Engineering Hub")
                .status(GroupStatus.ACTIVE)
                .spaceId("space-eng-hub")
                .language("ta-IN")
                .build();
        GroupDetailsResponse parentGroup = groupService.createGroup(parentPayload);
        assertNotNull(parentGroup);
        assertNotNull(parentGroup.getUuid());
        assertEquals("Engineering Hub", parentGroup.getName());
        assertEquals("space-eng-hub", parentGroup.getSpaceId());
        assertNull(parentGroup.getLocation());
        assertEquals("ta-IN", parentGroup.getLanguage());
        assertNull(parentGroup.getSegments());
        parentGroupId = parentGroup.getUuid();
    }

    @Test
    @Order(3)
    @DisplayName("Create child group with minimal attributes")
    void createChildGroup() {
        GroupCreatePayload childPayload = GroupCreatePayload.builder()
                .parentUuid(parentGroupId)
                .name("Frontend Guild")
                .displayName("Frontend Guild")
                .status(GroupStatus.ACTIVE)
                .build();
        GroupDetailsResponse childGroup = groupService.createGroup(childPayload);
        assertNotNull(childGroup);
        assertNotNull(childGroup.getUuid());
        assertEquals("Frontend Guild", childGroup.getName());
        assertNull(childGroup.getSpaceId());
        assertNull(childGroup.getLocation());
        assertNull(childGroup.getLanguage());
        assertNull(childGroup.getSegments());
        childGroupId = childGroup.getUuid();
    }

    @Test
    @Order(4)
    @DisplayName("Child group should inherit attributes from parent/root")
    void testChildGroupInheritance() {
        GroupDetailsResponse childGroup = groupService.getGroupWithInheritance(childGroupId);
        assertNotNull(childGroup);
        assertEquals("Frontend Guild", childGroup.getName());
        assertEquals("space-eng-hub", childGroup.getSpaceId());
        assertEquals("India", childGroup.getLocation());
        assertEquals("ta-IN", childGroup.getLanguage());
        assertNotNull(childGroup.getSegments());
        assertEquals(2, childGroup.getSegments().size());
        assertTrue(childGroup.getSegments().contains("Technology"));
        assertTrue(childGroup.getSegments().contains("Research"));
    }

    @Test
    @Order(5)
    @DisplayName("Parent group should inherit missing attributes from root")
    void testParentGroupInheritance() {
        GroupDetailsResponse parentGroup = groupService.getGroupWithInheritance(parentGroupId);
        assertNotNull(parentGroup);
        assertEquals("Engineering Hub", parentGroup.getName());
        assertEquals("space-eng-hub", parentGroup.getSpaceId());
        assertEquals("India", parentGroup.getLocation());
        assertEquals("ta-IN", parentGroup.getLanguage());
        assertNotNull(parentGroup.getSegments());
        assertEquals(2, parentGroup.getSegments().size());
        assertTrue(parentGroup.getSegments().contains("Technology"));
        assertTrue(parentGroup.getSegments().contains("Research"));
    }

    @Test
    @Order(6)
    @DisplayName("Root group should not inherit any attributes")
    void testRootGroupNoInheritance() {
        GroupDetailsResponse rootGroup = groupService.getGroupWithInheritance(rootGroupId);
        assertNotNull(rootGroup);
        assertEquals("Global Division", rootGroup.getName());
        assertEquals("space-global", rootGroup.getSpaceId());
        assertEquals("India", rootGroup.getLocation());
        assertEquals("ta-IN", rootGroup.getLanguage());
        assertNotNull(rootGroup.getSegments());
        assertEquals(2, rootGroup.getSegments().size());
    }
}
