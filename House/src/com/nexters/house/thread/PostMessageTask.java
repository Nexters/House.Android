package com.nexters.house.thread;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;

import com.nexters.house.R;
import com.nexters.house.activity.AbstractAsyncActivity;
import com.nexters.house.entity.JavaBean;
import com.nexters.house.entity.TransferMultipartFile;
import com.nexters.house.utils.BeanUtils;

// ***************************************
// Private classes
// ***************************************
public class PostMessageTask extends AsyncTask<MediaType, Void, String> {
	private AbstractAsyncActivity mAbstractAsyncActivity;
    private JavaBean javaBean;

    @Override
    protected void onPreExecute() {
    //    showLoadingProgressDialog();

        javaBean = new JavaBean();

        javaBean.setContent("ㅎt");
        javaBean.setName("이보빈");
        javaBean.setImage("sdfds".getBytes());
        javaBean.setUpload(new TransferMultipartFile("test", "test", "test".getBytes(), "test", 4, false));
        // String originalFilename, String name,
//        byte[] content, String contentType, long size, boolean isEmpt
    }

    @Override
    protected String doInBackground(MediaType... params) {
        try {
            if (params.length <= 0) {
                return null;
            }
            MediaType mediaType = params[0];

            // The URL for making the POST request
            final String url = mAbstractAsyncActivity.getString(R.string.base_uri) + "/json.app";

            HttpHeaders requestHeaders = new HttpHeaders();

            // Sending a JSON or XML object i.e. "application/json" or "application/xml"
            requestHeaders.setContentType(mediaType);

            // Populate the Message object to serialize and headers in an
            // HttpEntity object to use for the request
            HttpEntity<JavaBean> requestEntity = new HttpEntity<JavaBean>(javaBean, requestHeaders);
            
//            new HttpEntity<T>(body, headers)
            
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            if (mediaType == MediaType.APPLICATION_JSON) {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } else if (mediaType == MediaType.APPLICATION_XML) {
                restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
            }

            // Make the network request, posting the message, expecting a String in response from the server
            ResponseEntity<JavaBean> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    JavaBean.class);
            javaBean = response.getBody();

            // Return the response body to display to the user
            return "success";
        } catch (Exception e) {
//            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
    	mAbstractAsyncActivity.dismissProgressDialog();
        try {
        	mAbstractAsyncActivity.showResult(BeanUtils.getBeanGetValue(javaBean));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}