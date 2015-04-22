package ch.hslu.bierapp.webservice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Performs an asynchronous http GET request.
 */
public class AsyncHTTPRequest extends AsyncTask<String, Integer, String> {

    private IAsyncHTTPRequest delegate;
    private static final String BASE_URL = "http://ratebeer.duckdns.org:8080/beers";

    public void setDelegate(IAsyncHTTPRequest delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        try{

            URL url = new URL(createURL(params[0]));

            HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
            if(httpCon.getResponseCode() != 200)
                throw new Exception("Failed to connect");

            InputStream is = httpCon.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;

            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            return total.toString();
        }catch(Exception e){
            Log.w("BierApp:AsyncHTTPReques", "Unable to execute Request due to " + e.toString());
            return null;
        }
    }

    protected void onProgressUpdate(Integer... params){
        //Update a progress bar here, or ignore it, it's up to you
    }

    protected void onPostExecute(String response){
        delegate.webserviceResponse(response);
    }

    /**
     * Creates a ratebeer webservice-conform REST URL.
     * Removes special chars (Umlaute) and adds '/' where needed.
     * i.e.: "http://ratebeer.duckdns.org:8080/beers/quollfrisch"
     * @param restResource REST-Ressource to request from webservice.
     * @return full REST URL.
     */
    private String createURL(String restResource) {

        restResource = restResource.toLowerCase().replaceAll("ü", "u").replaceAll("ö","o").replaceAll("ä","a");

        if(restResource.isEmpty() || restResource.charAt(0) != '/') {
            restResource = "/" + restResource;
        }

        Log.d("DEBUG::RestResource ", restResource);

        return BASE_URL + restResource;
    }
}
