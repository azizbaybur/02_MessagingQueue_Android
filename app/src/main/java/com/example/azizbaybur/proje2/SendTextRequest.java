package com.example.azizbaybur.proje2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AzizBaybur on 13.11.2017.
 */

public class SendTextRequest extends StringRequest {

    private static final String SEND_TEXT_REQUEST_URL = "http://www.azzbybr.tk/SendText.php";
    private Map<String, String> params;

    public SendTextRequest(String username, String text, Response.Listener<String> listener){

        super(Method.POST, SEND_TEXT_REQUEST_URL, listener, null );
        params = new HashMap<>();
        params.put("username", username);
        params.put("text", text);

    }

    @Override
    public Map<String, String> getParams() {

        return params;
    }
}