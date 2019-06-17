package com.tamimi.sundos.restpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendCloud {

    private Context context;
    private ProgressDialog progressDialog;
    private JSONObject obj;

    public SendCloud(Context context, JSONObject obj) {
        this.obj = obj;
        this.context = context;
    }

    public void startSending(String flag) {

        if (flag.equals("kitchen"))
            new JSONTaskKitchen().execute();

        if (flag.equals("Order"))
            new JSONTaskOrder().execute();

        if (flag.equals("FamilyCategory"))
            new JSONTaskFamilyCategory().execute();

        if (flag.equals("MenuRegistration"))
            new JSONTaskMenuRegistration().execute();

        if (flag.equals("Modifier"))
            new JSONTaskModifier().execute();

        if (flag.equals("ForceQuestion"))
            new JSONTaskForceQuestion().execute();

        if (flag.equals("ItemWithModifier"))
            new JSONItemWithModifier().execute();

        if (flag.equals("CategoryWithModifier"))
            new JSONCategoryWithModifier().execute();

        if (flag.equals("ItemWithFQ"))
            new JSONItemWithFQ().execute();

        if (flag.equals("Announcement"))
            new JSONAnnouncement().execute();
    }

    private class JSONTaskKitchen extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveKitchenScreen?";

                String data = "compno=" + URLEncoder.encode("302", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2018", "UTF-8") + "&" +
                        "voucher=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("Voucher Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
//
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    private class JSONTaskOrder extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveOrder?";

                String data = "compno=" + URLEncoder.encode("302", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2018", "UTF-8") + "&" +
                        "voucher=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("Voucher Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    private class JSONTaskFamilyCategory extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveGroup?";

                String data = "Compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "CompYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "POSNO=" + URLEncoder.encode("1", "UTF-8") + "&" +
                        "Group=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                //http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveGroup?Compno=736&CompYear=2019&POSNO=1&Group={%22SERIAL%22:1,%22ITYPE%22:1,%22NAME_CATEGORY_FAMILY%22:%22NEW%20FOOD%22}
                //http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveGroup?Compno=736&CompYear=2019&POSNO=1&Group=%7B%22SERIAL%22%3A26%2C%22TYPE%22%3A2%2C%22NAME_CATEGORY_FAMILY%22%3A%22cat9+%22%7D

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("Group Or Family Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONTaskMenuRegistration extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveItemCard?";

                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ITEMCARD=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("Item Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    private class JSONTaskModifier extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://falconssoft.net/RestService/FSAppServiceDLL.dll/SaveModifier?";

                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "Modifier=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("Modifier Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONTaskForceQuestion extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://falconssoft.net/RestService/FSAppServiceDLL.dll/SaveForceQuestion?";

                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "FORCEQ=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("FORCE_QUESTIONS Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONItemWithModifier extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://falconssoft.net/RestService/FSAppServiceDLL.dll/SaveItemWModifier?";

                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ITEMMODIF=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("ITEM_WITH_MODIFIER Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONCategoryWithModifier extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://falconssoft.net/RestService/FSAppServiceDLL.dll/SaveCategWModifier?";

                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "CATEGMODIF=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("CATEGORY_WITH_MODIFIER Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONItemWithFQ extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://falconssoft.net/RestService/FSAppServiceDLL.dll/SaveItemWFQ?";

                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ITEMFQ=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("ITEM_WITH_FQ Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONAnnouncement extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://falconssoft.net/RestService/FSAppServiceDLL.dll/SaveANNOUNCEMENT?";

                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ANNOUNC=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("ANNOUNCEMENT_TABLE Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

}


