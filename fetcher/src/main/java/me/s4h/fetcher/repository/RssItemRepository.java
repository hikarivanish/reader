package me.s4h.fetcher.repository;

import me.s4h.fetcher.entity.RssItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by LENOVO on 2014/11/22.
 */
@Repository
public interface RssItemRepository extends JpaRepository<RssItem,Long>{
}
