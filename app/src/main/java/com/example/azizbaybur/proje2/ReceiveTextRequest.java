package com.example.azizbaybur.proje2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AzizBaybur on 13.11.2017.
 */

public class ReceiveTextRequest extends StringRequest {

    private static final String RECEIVE_TEXT_REQUEST_URL = "http://www.azzbybr.tk/SendText.php";
    private Map<String, String> params;

    public ReceiveTextRequest(String username, Response.Listener<String> listener){

        super(Method.POST, RECEIVE_TEXT_REQUEST_URL, listener, null );
        params = new HashMap<>();
        params.put("username", username);

    }

    @Override
    public Map<String, String> getParams() {

        return params;
    }
}