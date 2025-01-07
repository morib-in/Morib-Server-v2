package org.morib.server.domain.user.infra.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InterestAreaConverter implements AttributeConverter<InterestArea, String> {

    @Override
    public String convertToDatabaseColumn(InterestArea attribute) {
        if(attribute != null) {
            return attribute.getInterestArea();
        }
        return null;
    }

    @Override
    public InterestArea convertToEntityAttribute(String dbData) {
        return InterestArea.fromValue(dbData);
    }
}
