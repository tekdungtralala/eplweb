/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.wiwit.eplweb.util.youtube.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Thumbnails.Set;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;

/**
 * Upload a video to the authenticated user's channel. Use OAuth 2.0 to
 * authorize the request. Note that you must add your video files to the
 * project folder to upload them with this application.
 *
 * @author Jeremy Walker
 */
public class VideoService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(VideoService.class);

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Define a global variable that specifies the MIME type of the video
     * being uploaded.
     */
    private static final String VIDEO_FILE_FORMAT = "video/*";
    
    /**
     * Define a global variable that specifies the MIME type of the image
     * being uploaded.
     */
    private static final String IMAGE_FILE_FORMAT = "image/png";

    /**
     * Upload the user-selected video to the user's YouTube channel. The code
     * looks for the video in the application's project folder and uses OAuth
     * 2.0 to authorize the API request.
     *
     * @param args command line args (not used).
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
    	String videoId = doUpload("Chelsea",
    		new FileInputStream("/home/wiwitaditya/Documents/workspace/api-samples/java/src/main/resources/sample-video.mp4"));
    	System.out.println("videoId : " + videoId);
    	
//    	doChangeThumbnail("fMF7-oTzAq4", new FileInputStream("/home/wiwitaditya/Downloads/eplcustomlogo.jpg"));
    }
    
    public static void doChangeThumbnail(String videoId, InputStream imageStream) {
        // This OAuth 2.0 access scope allows for full read/write access to the
        // authenticated user's account.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        try {
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "youtube");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
            	.setApplicationName("eplweb")
            	.build();


            // Create an object that contains the thumbnail image file's
            // contents.
            InputStreamContent mediaContent = new InputStreamContent(
                    IMAGE_FILE_FORMAT, imageStream);

            // Create an API request that specifies that the mediaContent
            // object is the thumbnail of the specified video.
            Set thumbnailSet = youtube.thumbnails().set(videoId, mediaContent);

            // Set the upload type and add an event listener.
            MediaHttpUploader uploader = thumbnailSet.getMediaHttpUploader();

            // Indicate whether direct media upload is enabled. A value of
            // "True" indicates that direct media upload is enabled and that
            // the entire media content will be uploaded in a single request.
            // A value of "False," which is the default, indicates that the
            // request will use the resumable media upload protocol, which
            // supports the ability to resume an upload operation after a
            // network interruption or other transmission failure, saving
            // time and bandwidth in the event of network failures.
            uploader.setDirectUploadEnabled(false);
            uploader.setChunkSize(MediaHttpUploader.MINIMUM_CHUNK_SIZE); 

            // Set the upload state for the thumbnail image.
            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                @Override
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                    case INITIATION_STARTED:
                    	logger.info("INITIATION_STARTED");
                        break;
                    case INITIATION_COMPLETE:
                    	logger.info("INITIATION_COMPLETE");
                        break;
                    case MEDIA_IN_PROGRESS:
                    	logger.info("MEDIA_IN_PROGRESS : " + uploader.getProgress());
                        break;
                    case MEDIA_COMPLETE:
                    	logger.info("MEDIA_COMPLETE");
                        break;
                    case NOT_STARTED:
                    	logger.info("NOT_STARTED");
                        break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);

            // Upload the image and set it as the specified video's thumbnail.
            thumbnailSet.execute();

        } catch (GoogleJsonResponseException e) {
        	logger.info("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
        	logger.info("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
        	logger.error("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
    }
    
    
    public static void doDelete(String videoId) {
        // This OAuth 2.0 access scope allows for full read/write access to the
        // authenticated user's account.
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
    	try {   		
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "youtube");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
            	.setApplicationName("eplweb")
            	.build();

            YouTube.Videos.Delete delete = youtube.videos().delete(videoId);
            
            delete.execute();
    	} catch(Exception e) {
    	}
    }
    
    public static String doUpload(String teamName, InputStream is) {
        // This OAuth 2.0 access scope allows an application to upload files
        // to the authenticated user's YouTube channel, but doesn't allow
        // other types of access.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        try {
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "youtube");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
            	.setApplicationName("eplweb")
            	.build();

            // Add extra information to the video before uploading.
            Video videoObjectDefiningMetadata = new Video();

            // Set the video to be publicly visible. This is the default
            // setting. Other supporting settings are "unlisted" and "private."
            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            videoObjectDefiningMetadata.setStatus(status);

            // Most of the video's metadata is set on the VideoSnippet object.
            VideoSnippet snippet = new VideoSnippet();
            snippet.setTitle(teamName + "'s video on eplweb.");
            snippet.setDescription(teamName + "'s video uploaded via YouTube Data API V3 using the Java library by eplweb.");
            // Set the keyword tags that you want to associate with the video.
            List<String> tags = new ArrayList<String>();
            tags.add("eplweb");
            tags.add(teamName);
            snippet.setTags(tags);

            // Add the completed snippet object to the video resource.
            videoObjectDefiningMetadata.setSnippet(snippet);

            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, is);

            // Insert the video. The command sends three arguments. The first
            // specifies which information the API request is setting and which
            // information the API response should return. The second argument
            // is the video resource that contains metadata about the new video.
            // The third argument is the actual video content.
            YouTube.Videos.Insert videoInsert = youtube.videos()
                    .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

            // Set the upload type and add an event listener.
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

            // Indicate whether direct media upload is enabled. A value of
            // "True" indicates that direct media upload is enabled and that
            // the entire media content will be uploaded in a single request.
            // A value of "False," which is the default, indicates that the
            // request will use the resumable media upload protocol, which
            // supports the ability to resume an upload operation after a
            // network interruption or other transmission failure, saving
            // time and bandwidth in the event of network failures.
            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                        	logger.info("INITIATION_STARTED");
                            break;
                        case INITIATION_COMPLETE:
                        	logger.info("INITIATION_COMPLETE");
                            break;
                        case MEDIA_IN_PROGRESS:
                        	logger.info("MEDIA_IN_PROGRESS : " + uploader.getProgress());
                            break;
                        case MEDIA_COMPLETE:
                        	logger.info("MEDIA_COMPLETE");
                            break;
                        case NOT_STARTED:
                        	logger.info("NOT_STARTED");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);

            // Call the API and upload the video.
            Video returnedVideo = videoInsert.execute();

            logger.info("Id: " + returnedVideo.getId());
            logger.info("Title: " + returnedVideo.getSnippet().getTitle());
            return returnedVideo.getId();
        } catch (GoogleJsonResponseException e) {
            logger.error("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
        	logger.error("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
        return null;
    }
}
