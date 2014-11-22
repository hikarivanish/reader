package me.s4h.repository;

import me.s4h.entity.RssChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by LENOVO on 2014/11/22.
 */
@RepositoryRestResource
public interface RssChannelRepository extends JpaRepository<RssChannel,Long> {
    RssChannel findByUrl(String url);
}
