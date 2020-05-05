package com.fc2.blog.karepuri39.shopmemo.IO;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Http {
    static private RequestQueue queue;
    static final String POST_PARAM = "android_param";

    public interface Listener{
        void Response(String response);
        void Error();
    }

    static public void Init(Context context){
        queue = Volley.newRequestQueue(context);
    }
    static public void Post(String url,final String param,final Listener listener){
        // Request a string response from the provided URL.
        Mylog.d("HTTP Post. URL:"+url);
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                listener.Response(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //String error_mes = new String(error.networkResponse.data);
                                Mylog.i("HTTP_Error:");
                                listener.Error();
                            }
                        }
                )
                {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String,String>();
                        params.put(POST_PARAM,param);
                        return params;
                    }
                };

        queue.add(stringRequest);
    }
}
