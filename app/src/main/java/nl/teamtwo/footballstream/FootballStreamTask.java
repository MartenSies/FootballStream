package nl.teamtwo.footballstream;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marten on 12/04/16.
 */
public class FootballStreamTask extends AsyncTask<Void, Void , JSONObject> {

    private LeaguesActivity la;
    private String urlString = "http://footballstream-api.jordibeen.nl/api/v1";

    public FootballStreamTask (LeaguesActivity la, String url) {
        this.la = la;
        this.urlString += url;
    }

    public JSONObject getData() {
        JSONObject result = new JSONObject();
        HttpURLConnection con = null;

        Log.v("data", "Getting your football data..");

        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8")
            );

            String payload = reader.readLine();
            reader.close();

            result = new JSONObject(payload);
        } catch (IOException e) {
            Log.e("data", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.d("data", "Something went wrong... ", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        Log.d("data", "   -> returned: " + result);

        return result;

    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject data = getData();
        return data;
    }

    @Override
    protected void onPostExecute(JSONObject data) {
        super.onPostExecute(data);
        la.updateLeagues(data);
    }
}
