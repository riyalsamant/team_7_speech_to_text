package com.example.uploadeg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Retrieval extends Activity {

    SqlLiteDbHelper dbHelper;
    Contact contacts;
    ArrayList<HashMap<String, String>> userList;
    String con = "", txt = "";
    InputStream is = null;
    View v;
    LinearLayout layout;
    List<EditText> allEds;
    String ip;
    String url = "http://192.168.1.104/server/";
    String result = null;
    String line = null;
    String co_id;
    int code;
    String session,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        session=intent.getExtras().getString("session");
        id=intent.getExtras().getString("id");
        dbHelper = new SqlLiteDbHelper(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_text_input, null);
       // ip=getLocalIpv4Address();
       // Log.i("IP Address",ip);
        SharedPreferences sp =getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        co_id=sp.getString("cid","");

        layout = (LinearLayout) v.findViewById(R.id.v1);
        layout.setOrientation(LinearLayout.VERTICAL);
        dbHelper.openDataBase();
        contacts = new Contact();
        contacts = dbHelper.Get_ContactDetails();
        userList = new ArrayList<HashMap<String, String>>();
        new AsyncTaskParseJson().execute();
    }
    public static String getLocalIpv4Address(){
        try {
            String ipv4;
            List<NetworkInterface>  nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            if(nilist.size() > 0){
                for (NetworkInterface ni: nilist){
                    List<InetAddress>  ialist = Collections.list(ni.getInetAddresses());
                    if(ialist.size()>0){
                        for (InetAddress address: ialist){
                            if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())){
                                return ipv4;
                            }
                        }
                    }

                }
            }

        } catch (SocketException ex) {

        }
        return "";
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here
        String yourJsonStringUrl = url + "server.php?coid=" + co_id+"&session="+session+"&user_id="+id;

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {

                // instantiate our json parser
                JsonParser jParser = new JsonParser();

                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                // get the array of users
                dataJsonArr = json.getJSONArray("result");

                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    // Storing each json item in variable
                    String user_id = c.getString("user_id");
                    String session_no = c.getString("session_no");
                    String question_no = c.getString("question_no");
                    String transcribed_ans = c.getString("transcribed_ans");
                    // show the values in our logcat
                    Log.e(TAG, "user_id: " + user_id
                            + ", session_no: " + session_no
                            + ", question_no: " + (Integer.parseInt(question_no))
                            + ", transcribed_ans: " + transcribed_ans);

                    HashMap<String, String> user = new HashMap<String, String>();
                    con = contacts.getQuestion(Integer.parseInt(question_no));
                    txt += con + "->" + transcribed_ans + "\n";
                    // adding each child node to HashMap key => value
                    user.put("user_id", user_id);
                    user.put("session_no", session_no);
                    user.put("question_no", question_no);
                    user.put("transcribed_ans", transcribed_ans);
                    // adding user to user list
                    userList.add(user);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String strFromDoInBg) {
            int a = 100;
            float scale = getResources().getDisplayMetrics().density;
            TextView text = new TextView(Retrieval.this);
            text.setPadding(0, (int) (50 * scale + 0.5f), 0, 0);
            text.setText("Fill the form below");
            text.setTextColor(Color.parseColor("#5a7aa3"));
            text.setTextSize((int) (12 * scale + 0.5f));
            layout.addView(text);
            allEds = new ArrayList<EditText>();
            for (int i = 0; i < userList.size(); i++) {

                LinearLayout linearLayout = new LinearLayout(Retrieval.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                lp.setMargins(50, 20, 50, 0);
                linearLayout.setLayoutParams(lp);
                TextView textView = new TextView(Retrieval.this);
                EditText editText = new EditText(Retrieval.this);
                textView.setTextSize((int) (7 * scale + 0.5f));
                editText.setTextSize((int) (7 * scale + 0.5f));
                allEds.add(editText);
                editText.setId(i + 1);
                textView.setText(contacts.getQuestion(Integer.parseInt(userList.get(i).get("question_no"))).trim());
                editText.setText(userList.get(i).get("transcribed_ans"));

                textView.setTextColor(Color.parseColor("#5a7aa3"));
                editText.setTextColor(Color.parseColor("#000000"));
                editText.getBackground().setColorFilter(Color.parseColor("#5a7aa3"), PorterDuff.Mode.SRC_IN);
                linearLayout.addView(textView);
                linearLayout.addView(editText);
                layout.addView(linearLayout);
                Log.i("-1", contacts.getQuestion(Integer.parseInt(userList.get(i).get("question_no"))));

            }
            Button button = new Button(Retrieval.this);
            button.setText("Submit");
            button.hasOnClickListeners();
            layout.addView(button);
            setContentView(v);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    for (int i = 0; i < userList.size(); i++) {
                        String ans = allEds.get(i).getText().toString();
                        Log.i("->", ans);
                        insert(ans, i);
                    }
                }
            });
        }

        public void insert(String ans, int i) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("co_id", co_id));
            nameValuePairs.add(new BasicNameValuePair("user_id", userList.get(i).get("user_id")));
            nameValuePairs.add(new BasicNameValuePair("session_no", userList.get(i).get("session_no")));
            nameValuePairs.add(new BasicNameValuePair("question_no", userList.get(i).get("question_no")));
            nameValuePairs.add(new BasicNameValuePair("transcribed_ans", ans));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url + "update.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }

            try {
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("code"));

                if (code == 1) {
                    Toast.makeText(getBaseContext(), "Update Successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Sorry, Try Again",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }
    }

}
