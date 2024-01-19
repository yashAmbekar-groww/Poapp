package com.poapp.Poapp.repository;

import com.poapp.Poapp.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {
    Optional<Holding> findByUserAccIdAndStockId(Long userAccId, Long stockId);
    Optional<List<Holding>> findByUserAccId(Long userAccId);
}
