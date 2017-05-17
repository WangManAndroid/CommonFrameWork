package com.youliao.commonframework.log;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by zhy on 16/3/1.
 */
public class LoggerInterceptor implements Interceptor
{
    private boolean showResponse;
    public LoggerInterceptor(boolean showResponse)
    {
        this.showResponse = showResponse;
    }

    public LoggerInterceptor(String tag)
    {
        this(true);
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private Response logForResponse(Response response)
    {
        try
        {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            StringBuilder buffer=new StringBuilder();
            buffer.append("HttpResponse..........").append("\n");
            buffer.append("url : " ).append(clone.request().url()).append("\n");
            buffer.append("code :").append(clone.code()).append("\n");
            buffer.append("protocol : ").append(clone.protocol()).append("\n");
//            Logger.d(buffer.toString());
            if (!TextUtils.isEmpty(clone.message())){
                buffer.append("message:").append(clone.message()).append("\n");
            }
            if (showResponse)
            {
                ResponseBody body = clone.body();
                if (body != null)
                {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null)
                    {
                        buffer.append("responseBody's contentType :").append(mediaType.toString()).append("\n");
                        if (isText(mediaType))
                        {
                            String resp = body.string();
                            buffer.append("responseBody's content : ").append(resp).append("\n");

                            body = ResponseBody.create(mediaType, resp);
                            Logger.d(buffer.toString());
                            return response.newBuilder().body(body).build();
                        } else
                        {
                            Logger.d("responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }


        } catch (Exception e)
        {
//            e.printStackTrace();
        }

        return response;
    }

    private void logForRequest(Request request)
    {
        try
        {
            String url = request.url().toString();
            Headers headers = request.headers();
            StringBuilder buffer=new StringBuilder();
            buffer.append("HttpRequest..........").append("\n");
            buffer.append("request method : ").append(request.method()).append("\n");
            buffer.append("request url : ").append(url).append("\n");

//            if (request.body() instanceof FormBody){
//                FormBody formBody= (FormBody) request.body();
//                StringBuffer buffer=new StringBuffer();
//                buffer.append("request params:").append("\n");
//                for (int i = 0; i <formBody.size() ; i++) {
//                    buffer.append(formBody.encodedName(i)).append("=").append(formBody.encodedValue(i)).append("\n");
//
//                }
//                Logger.d(buffer.toString());
//            }

            if (headers != null && headers.size() > 0)
            {
                buffer.append("request headers : " ).append(headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null)
            {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null)
                {
                    if (isText(mediaType))
                    {
                        buffer.append("request params:").append(bodyToString(request)).append("\n");
                    } else
                    {
                        Logger.d("requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            Logger.d(buffer.toString());
        } catch (Exception e)
        {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType)
    {
        if (mediaType.type() != null && mediaType.type().equals("text"))
        {
            return true;
        }
        if (mediaType.subtype() != null)
        {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")||
                    mediaType.subtype().equals("x-www-form-urlencoded")

                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request)
    {
        try
        {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e)
        {
            return "something error when show requestBody.";
        }
    }
}
