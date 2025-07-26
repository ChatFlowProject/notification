package shop.flowchat.notification.infrastructure.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.category.CategoryReadModel;

public interface CategoryReadModelRepository extends JpaRepository<CategoryReadModel, Long> {
}