package org.morib.server.api.homeView.vo;

import lombok.Value;
import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;
import java.util.List;

@Value
public class CategoriesByDate {
    LocalDate date;
    List<Category> categories;

    public static CategoriesByDate of(LocalDate date, List<Category> categories) {
        return new CategoriesByDate(date, categories);
    }
}
