package com.aia.webservices;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.aia.interfaces.ApiServiceCaller;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class WebRequest
{
    public static String TAG = "WebRequest";

    public static JsonObjectRequest callPostMethod(Context context, String reader_id, JSONObject jsonObject,
            int request_type, String url, final String label, final ApiServiceCaller caller, final String token)
    {

        if(ApiConstants.LOG_STATUS == 0)
        {
            Log.d("Api_Calling", "" + url);
            Log.d("JSONObject", "" + jsonObject);
            Log.d("TOKENN", "" + token);
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(request_type, url, jsonObject,
                new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    Log.d(TAG, response.toString());
                    Gson gson = new Gson();
                    JsonResponse jsonResponse = gson.fromJson(response.toString(), JsonResponse.class);
                    caller.onAsyncSuccess(jsonResponse, label);
                }
                catch (Exception e)
                {
//                    e.printStackTrace();
                    caller.onAsyncCompletelyFail("Failed", label);
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null)
                {
                    try
                    {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                        VolleyLog.d(TAG, "Error: " + res);
                        Gson gson = new Gson();
                        JsonResponse jsonResponse = gson.fromJson(res, JsonResponse.class);
                        caller.onAsyncSuccess(jsonResponse, label);
                    }
                    catch (Exception je)
                    {
                        caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", label, response);
                    }
                }
                else
                    caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", label, response);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Accept", "application/json");
                params.put("Authorization", token);
                return params;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(100000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return jsonObjReq;
    }
}
