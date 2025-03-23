package org.morib.server.domain.allowedGroup.infra;

import org.morib.server.annotation.Manager;

@Manager
public class AllowedGroupManager {

    public void updateColorCode(AllowedGroup findAllowedGroup, String colorCode) {
        findAllowedGroup.updateColorCode(colorCode);
    }

    public void updateName(AllowedGroup findAllowedGroup, String name) {
        findAllowedGroup.updateName(name);
    }
}
