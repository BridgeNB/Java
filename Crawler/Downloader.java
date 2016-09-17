package webcrawler;

/**
 * Created by zhengyangqiao on 9/5/16.
 */
import java.io.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;


public class Downloader {
    public String downloader(String url) {
        String filePath = null;

        HttpClient httpclient = new HttpClient();
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
//        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultMethodRetryHandler());

        try{
            int statusCode = httpclient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
                filePath = null;
            }
            byte[] responseBody = getMethod.getResponseBody();
            filePath = "\\webcrawler\\" + url;
            // Save file
            saveToLocal(responseBody, filePath);
        } catch(HttpException e) {
            System.out.println("Check input http address");
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return filePath;
    }

    public void saveToLocal(byte[] data, String filePath) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            for (int i = 0; i < data.length; i++) out.write(data[i]);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

