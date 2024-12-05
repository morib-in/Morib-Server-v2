package org.morib.server.domain.allowedGroup.application;

public interface DeleteAllowedGroupService {
    void deleteAllowedGroupById(Long groupId);

    AllowedGroup findById(Long groupId);
}
