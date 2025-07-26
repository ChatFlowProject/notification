package shop.flowchat.notification.common.dto;

import shop.flowchat.notification.domain.category.CategoryReadModel;

public record CategoryInfo(
        Long id,
        String name
) {
    public static CategoryInfo from(CategoryReadModel category) {
        return new CategoryInfo(
                category.getId(),
                category.getName()
        );
    }
}