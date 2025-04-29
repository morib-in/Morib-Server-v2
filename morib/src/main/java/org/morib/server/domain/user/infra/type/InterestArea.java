package org.morib.server.domain.user.infra.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum InterestArea {
    BUSINESS_OWNER_EXECUTIVE("Business Owner/Executive"),
    DESIGNER("Designer"),
    MARKETER("Marketer"),
    PM_PO("PM/PO"),
    DEV("Dev"),
    STUDENT("Student"),
    OTHERS("Others");

    private final String interestArea;

    public static InterestArea fromValue(String value){
        for(InterestArea area : values()){
            if(area.getInterestArea().equalsIgnoreCase(value))
                return area;
        }
        return null;
    }

}
