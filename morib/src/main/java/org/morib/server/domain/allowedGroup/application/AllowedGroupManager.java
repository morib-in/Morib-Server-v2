package org.morib.server.domain.allowedGroup.application;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

@Manager
public class AllowedGroupManager {

    public void updateAll(AllowedGroup findAllowedGroup, String colorCode, String name) {
        findAllowedGroup.updateAll(colorCode, name);
    }

    public void updateColorCode(AllowedGroup findAllowedGroup, String colorCode) {
        findAllowedGroup.updateColorCode(colorCode);
    }

    public void updateName(AllowedGroup findAllowedGroup, String name) {
        findAllowedGroup.updateName(name);
    }
}
