package org.morib.server.api.modalView.dto;

import org.morib.server.api.modalView.vo.AllowSiteForCalledByTask;
import org.morib.server.api.modalView.vo.TaskInfoInAllowedSite;

import java.util.List;

public record AllowedSiteByTaskResponseDto(
    TaskInfoInAllowedSite task,
    List<AllowSiteForCalledByTask> msets
) {
    public static AllowedSiteByTaskResponseDto of(TaskInfoInAllowedSite taskInfoInAllowedSite, List<AllowSiteForCalledByTask> msets) {
        return new AllowedSiteByTaskResponseDto(taskInfoInAllowedSite, msets);
    }
}
