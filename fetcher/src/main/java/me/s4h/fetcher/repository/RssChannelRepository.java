package me.s4h.fetcher.repository;

import me.s4h.fetcher.entity.RssChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by LENOVO on 2014/11/22.
 */
@Repository
public interface RssChannelRepository extends JpaRepository<RssChannel,Long> {
    RssChannel findByUrl(String url);
}
