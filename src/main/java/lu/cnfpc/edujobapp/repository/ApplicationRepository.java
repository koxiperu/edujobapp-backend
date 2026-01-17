package lu.cnfpc.edujobapp.repository;

import java.util.List;
import lu.cnfpc.edujobapp.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserId(Long userId);
    List<Application> findByCompanyId(Long companyId);
    List<Application> findByDocumentsId(Long documentId);
    long countByUserId(Long userId);
}
