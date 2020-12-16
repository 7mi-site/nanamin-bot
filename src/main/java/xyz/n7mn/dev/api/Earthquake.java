package xyz.n7mn.dev.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.n7mn.dev.api.data.EarthquakeResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Earthquake {

    private long LastEventID = -1;
    private String LastDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private EarthquakeResult result = null;

    public Earthquake(){

        Timer timer = new Timer();
        long time = 1000 * 60;

        TimerTask task = new TimerTask() {
            public void run() {

                new Thread(()->{
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url("https://dev.narikakun.net/webapi/earthquake/post_data.json").build();

                    try {
                        Response response = client.newCall(request).execute();
                        result = gson.fromJson(response.body().string(), EarthquakeResult.class);

                        if (result.getControl().getStatus().equals("通常")){

                            long eventID = result.getHead().getEventID();
                            if (LastEventID != eventID){

                                LastEventID = eventID;
                                LastDate = sdf.format(result.getControl().getDateTime());

                            }

                        }

                        // System.out.println("debug : " + LastEventID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }
        };

        timer.scheduleAtFixedRate(task, 0,time);

    }

    public EarthquakeResult getData(){

        return result;

    }

    public long getLastEventID() {
        return LastEventID;
    }
}
