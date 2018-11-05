package com.example.adriano.androidlabs;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {
    protected String ACTIVITY_NAME        = "WeatherForecast";
    private final String API_URL          = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=dbbc24aaef66041c02e43275901a972c&mode=xml&units=metric";
    private static final String URL_IMAGE = "http://openweathermap.org/img/w/";
    private TextView currentTempTV;
    private TextView minTempTV;
    private TextView maxTempTV;
    private TextView speedTV;
    private ProgressBar progress;
    private ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        init();

        ForecastQuery getForecast = new ForecastQuery();
        getForecast.execute();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        Bitmap bitmap;
        String weatherIconString;
        String weatherIconFile;
        String currentTemp;
        String minTemp;
        String maxTemp;
        String speed;
        String name;


        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inStream = connection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25, 50, 75);
                            minTemp = xpp.getAttributeValue(null, "min");
                            publishProgress(25, 50, 75);
                            maxTemp = xpp.getAttributeValue(null, "max");
                            publishProgress(25, 50, 75);
                        }
                        if (xpp.getName().equalsIgnoreCase("speed")) {
                            speed = xpp.getAttributeValue(null, "value");
                            publishProgress(25, 50, 75);
                        }
                        if(xpp.getName().equalsIgnoreCase("weather")) {
                            weatherIconString = xpp.getAttributeValue(null, "icon");
                            publishProgress(25, 50, 75);
                        }
                    }
                    xpp.next();

                }
                connection.disconnect();
                if(fileExistance(weatherIconString + ".png")){
                    Log.i(ACTIVITY_NAME, "Weather image exists, read file");
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(weatherIconString + ".png");
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    }  bitmap = BitmapFactory.decodeStream(fis);

                }else {
                    Log.i(ACTIVITY_NAME, "Weather image does not exist, download URL");

                    URL imageUrl = new URL(URL_IMAGE + weatherIconString + ".png");
                    connection = (HttpURLConnection) imageUrl.openConnection();
                    connection.connect();
                    inStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inStream);

                    FileOutputStream fos = openFileOutput(weatherIconString + ".png", Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
                    fos.flush();
                    fos.close();
                    connection.disconnect();
                }
                publishProgress(100);


                } catch(MalformedURLException e) {
                e.printStackTrace();
                Log.e("error", e.getMessage());
                } catch (IOException e) {
                e.printStackTrace();
                Log.e("error", e.getMessage());
                } catch (XmlPullParserException e){
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }
                return null;
            }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }



        @Override
        protected void onProgressUpdate(Integer ... value){
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String args){
            currentTempTV.setText("Current: " + currentTemp + "C");
            speedTV.setText("Wind Speed: " + speed);
            minTempTV.setText("Min: " + minTemp + "C");
            maxTempTV.setText("Max: " + maxTemp + "C");
            weatherImage.setImageBitmap(bitmap);
            progress.setVisibility(View.INVISIBLE);
        }
    }

    private void init(){
        currentTempTV = (TextView) findViewById(R.id.currentTemp);
        minTempTV     = (TextView) findViewById(R.id.minTemp);
        maxTempTV     = (TextView) findViewById(R.id.maxTemp);
        speedTV       = (TextView) findViewById(R.id.windSpeed);
        progress      = (ProgressBar) findViewById(R.id.progress);
        weatherImage  = (ImageView) findViewById(R.id.weatherImage);
        progress.setVisibility(View.VISIBLE);
    }
}


