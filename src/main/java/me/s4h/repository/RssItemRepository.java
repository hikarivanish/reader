package me.s4h.repository;

import me.s4h.entity.RssItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by LENOVO on 2014/11/22.
 */
@Repository
public interface RssItemRepository extends JpaRepository<RssItem,Long>{
    Page<RssItem> findByChannelId(Long channelId, Pageable pageable);
}
