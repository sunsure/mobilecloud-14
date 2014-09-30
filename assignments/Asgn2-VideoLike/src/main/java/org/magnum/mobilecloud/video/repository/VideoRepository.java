package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A repository for storing Video objects.
 */
@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

    /**
     * Finds a video by the given ID.
     *
     * @param id the video ID.
     * @return the <code>Video</code> object if found, null if not found.
     */
    Video findById(long id);

    /**
     * Finds a video by the given name.
     *
     * @param name name of the video.
     * @return the <code>Video</code> if found.
     */
    List<Video> findByName(String name);

    /**
     * Returns a list of objects with a duration less than the passed-in duration value.
     *
     * @param duration the duration in seconds.
     * @return List of Videos with a duration less than <code>duration</code>.
     */
    List<Video> findByDurationLessThan(long duration);
}
