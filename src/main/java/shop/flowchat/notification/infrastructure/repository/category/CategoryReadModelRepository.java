package shop.flowchat.notification.infrastructure.repository.category;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.category.CategoryReadModel;

public interface CategoryReadModelRepository extends JpaRepository<CategoryReadModel, Long> {
    List<CategoryReadModel> findAllByTeamId(UUID teamId);
}