package com.example.pizzarestaurantproject.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.pizzarestaurantproject.LoginSignupActivity;
import com.example.pizzarestaurantproject.GetStartedActivity;

import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        ((GetStartedActivity) activity).setButtonText("connecting");
        super.onPreExecute();
        ((GetStartedActivity) activity).setProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((GetStartedActivity) activity).setProgress(false);

        if (s == null) {
            Toast.makeText(activity, "Failed to get a successful response", Toast.LENGTH_SHORT).show();
            ((GetStartedActivity) activity).setButtonText("Get Started");
        } else {
            List<String> pizzaTypes = JsonParser.getObjectFromJson(s);
            ((GetStartedActivity) activity).fillPizzaTypes(pizzaTypes);
            Intent intent = new Intent(activity, LoginSignupActivity.class);
            activity.startActivity(intent);

            ((GetStartedActivity) activity).setButtonText("Get Started");
        }
    }
} // end class
