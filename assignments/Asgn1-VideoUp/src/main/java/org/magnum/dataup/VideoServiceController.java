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
package org.magnum.dataup;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoAPI;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VideoServiceController {
	private static VideoAPI videoAPI = new VideoAPI();
	private static String baseURL;

	// Get will simply retrieve the list of videos which -by default- will be
	// represented as JSON.
	@RequestMapping(value = "/video", method = RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideos() {
		// init();
		return videoAPI.getVideoList();
	}

	
	/*
	 * Store the video, returning the stored video object with the id
	 * set/updated by the backing API. Note: for the PostMan chrome extension,
	 * explicitly set the contentType to "application/json" on the request
	 * header. Just setting the type to Raw, format JSON will not produce the
	 * header in the outbound POST request !
	 */
	@RequestMapping(value = "/video", method = RequestMethod.POST)
	public @ResponseBody Video saveVideo(@RequestBody Video videoToStore) {
		if (baseURL == null || baseURL == "")
			baseURL = getUrlBaseForLocalServer();
		return videoAPI.addVideo(baseURL, videoToStore);
	}

	/* Method to upload the actual video data for an already existing video
	 */
	@RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
	public @ResponseBody VideoState uploadVideoData( @PathVariable("id") long id, @RequestParam("data") MultipartFile videoData) throws Exception{
		
		return videoAPI.setVideoData(id, videoData.getInputStream());
	}
	
	// dummy init method to provide some data for manual testing ...
	@SuppressWarnings("unused")
	private void init() {
		if (videoAPI.getVideoList().size() == 0) {
			Video v = new Video();
			v.setContentType("HOLADEBOLA");
			v.setDataUrl("/HOP");
			v.setDuration(100);
			v.setTitle("Nu is het genoeg");
			videoAPI.addVideo(getUrlBaseForLocalServer(),v);
		}
	}

	private String getUrlBaseForLocalServer() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String base = "http://"
				+ request.getServerName()
				+ ((request.getServerPort() != 80) ? ":"
						+ request.getServerPort() : "")
						+ "/video";
		return base;
	}
}
