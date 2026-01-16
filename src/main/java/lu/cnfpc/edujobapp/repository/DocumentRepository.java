package lu.cnfpc.edujobapp.repository;

import lu.cnfpc.edujobapp.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
