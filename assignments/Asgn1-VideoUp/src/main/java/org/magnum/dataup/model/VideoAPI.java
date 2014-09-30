package org.magnum.dataup.model;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.magnum.dataup.VideoFileManager;
import org.magnum.dataup.model.VideoStatus.VideoState;

public class VideoAPI {
	// store the videos
	private Map<Long, Video> videoMap = new HashMap<Long, Video>();
	private static final AtomicLong currentId = new AtomicLong(0L);
	private static VideoFileManager vfm;
	
	// API method implementing the GET operation.
	public Collection<Video> getVideoList() {
		return videoMap.values();
	}

	/*
	 * Store the video, overwriting the id for a generated id and setting the
	 * dataURL accordingly.
	 */
	public Video addVideo(String baseURL, Video v) {
		v.setId(currentId.incrementAndGet());
		v.setDataUrl(baseURL + "/" + v.getId() + "/data");

		videoMap.put(v.getId(), v);
		return v;
	}
	
	/* Store the video's binary data using the provided VideoFileManager
	 * 
	 */
	public VideoState setVideoData(long id, InputStream dataStream) throws Exception{
		if ( vfm == null)
			vfm = VideoFileManager.get();
		Video v = videoMap.get(id);
		vfm.saveVideoData(v, dataStream);
		return VideoState.READY;
	}
}
