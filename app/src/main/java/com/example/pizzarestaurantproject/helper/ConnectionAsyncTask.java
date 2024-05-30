package com.example.pizzarestaurantproject.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.pizzarestaurantproject.LoginSignupActivity;
import com.example.pizzarestaurantproject.MainActivity;

import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        ((MainActivity) activity).setButtonText("connecting");
        super.onPreExecute();
        ((MainActivity) activity).setProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((MainActivity) activity).setProgress(false);

        if (s == null) {
            Toast.makeText(activity, "Failed to get a successful response", Toast.LENGTH_SHORT).show();
            ((MainActivity) activity).setButtonText("Get Started");
        } else {
            List<String> pizzaTypes = JsonParser.getObjectFromJson(s);
            ((MainActivity) activity).fillPizzaTypes(pizzaTypes);
            Intent intent = new Intent(activity, LoginSignupActivity.class);
            activity.startActivity(intent);

            ((MainActivity) activity).setButtonText("Get Started");
        }
    }
} // end class
