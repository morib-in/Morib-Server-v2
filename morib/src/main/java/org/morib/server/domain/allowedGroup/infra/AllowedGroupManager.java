package org.morib.server.domain.allowedGroup.infra;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.allowedGroup.infra.AllowedGroup;

@Manager
public class AllowedGroupManager {

    public void updateColorCode(AllowedGroup findAllowedGroup, String colorCode) {
        findAllowedGroup.updateColorCode(colorCode);
    }

    public void updateName(AllowedGroup findAllowedGroup, String name) {
        findAllowedGroup.updateName(name);
    }
}
