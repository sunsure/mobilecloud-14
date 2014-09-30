/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
public class VideoServiceController {

    public static final String VIDEO_PATH = "/video";
    public static final String SEARCH_PATH = "/search";

    @Autowired
    private VideoRepository videoRepository;

    /**
     * Returns the list of videos that have been added to the server as JSON.
     *
     * @return the list of Videos stored in the repository.
     */
    @RequestMapping(value = VIDEO_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideos() {
        return Lists.newArrayList(videoRepository.findAll());
    }

    /**
     * Saves the Video metadata provided by the client and returns the saved Video represented as JSON.
     *
     * @param video The JSON representation of the Video to store.
     * @return the JSON representation of the Video object that was stored along with any updates to that object made by the server.
     */
    @RequestMapping(value = VIDEO_PATH, method = RequestMethod.POST)
    public @ResponseBody Video addVideoMetadata(@RequestBody Video video) {
        return videoRepository.save(video);
    }

    @RequestMapping(value = VIDEO_PATH + "/{id}", method = RequestMethod.GET)
    public @ResponseBody Video getVideo(@PathVariable("id") long id, HttpServletResponse response) {
        Video v = videoRepository.findById(id);

        if (v == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return v;
    }

    /**
     * Likes a <code>Video</code> specified by its <code>id</code>.
     *
     * @param id the id of the <code>Video</code>
     * @param p the <code>Principal</code> object associated with this user.
     * @param response the servlet response object.
     */
    @RequestMapping(value = VIDEO_PATH + "/{id}/like", method = RequestMethod.POST)
    public void likeVideo(@PathVariable("id") long id,
                          Principal p,
                          HttpServletResponse response) {

        Video v = getVideo(id, response);

        if (v != null) {
            boolean success = v.likeVideo(p.getName());

            if (success) {
                videoRepository.save(v);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    /**
     * Unlikes a <code>Video</code> specified by its <code>id</code>.
     *
     * @param id the id of the <code>Video</code>
     * @param p the <code>Principal</code> object associated with this user.
     * @param response the servlet response object.
     */
    @RequestMapping(value = VIDEO_PATH + "/{id}/unlike", method = RequestMethod.POST)
    public void unlikeVideo(@PathVariable("id") long id,
                          Principal p,
                          HttpServletResponse response) {

        Video v = getVideo(id, response);

        if (v != null) {
            boolean success = v.unlikeVideo(p.getName());

            if (success) {
                videoRepository.save(v);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    /**
     * Retrieves the list of users that like the <code>Video</code> specified by the id.
     *
     * @param id the id of the Vdeip
     * @param response
     * @return
     */
    @RequestMapping(value = VIDEO_PATH + "/{id}/likedby", method = RequestMethod.GET)
    public @ResponseBody Set<String> getUserLikes(@PathVariable("id") long id,
                                                  HttpServletResponse response) {
        Video v = getVideo(id, response);
        Set<String> userLikes = null;

        if (v != null) {
            userLikes = v.getUserLikes();
        }

        return userLikes;
    }

    /**
     * Retrieves a list of <code>Video</code> objects with the specified title.
     *
     * @param title the title to search for.
     * @return the list of videos with the given title, may be empty if no videos are found.
     */
    @RequestMapping(value = VIDEO_PATH + SEARCH_PATH + "/findByName", method = RequestMethod.GET)
    public @ResponseBody List<Video> findVideoByName(@RequestParam("title") String title) {
        List<Video> videos = videoRepository.findByName(title);

        if (videos == null) {
            videos = Lists.newArrayList();
        }

        return videos;
    }

    /**
     * Retrieves a list of <code>Video</code> objects whose duration is less than that provided in the request parameter.
     * @param duration the duration the search against.
     * @return the list of videos whose duration is less than the that provided in the request parameter,
     *         may be empty if no videos match the search criteria.
     */
    @RequestMapping(value = VIDEO_PATH + SEARCH_PATH + "/findByDurationLessThan", method = RequestMethod.GET)
    public @ResponseBody List<Video> findVideoByDurationLessThan(@RequestParam("duration") long duration) {
        List<Video> videos = videoRepository.findByDurationLessThan(duration);

        if (videos == null) {
            videos = Lists.newArrayList();
        }

        return videos;
    }
}
