package agency.techstar.yellowbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class BaiguullagaActivity extends AppCompatActivity {

    ImageView imgPreview;
    TextView txtText, txtSubText;
    WebView txtDescription;
    TextView txtAlert;
    CoordinatorLayout coordinatorLayout;
    Handler mHandler;
    String Project_image, Project_name, Project_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baiguullaga);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        txtText = (TextView) findViewById(R.id.txtText);
        txtSubText = (TextView) findViewById(R.id.txtSubText);

        txtDescription = (WebView) findViewById(R.id.txtDescription);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mHandler = new Handler(Looper.getMainLooper());

        FloatingActionButton webbutton = (FloatingActionButton)findViewById(R.id.btnWeb);

            webbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaiguullagaActivity.this, WebActivity.class));
                }
            });

//        com.github.clans.fab.FloatingActionButton fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.send_sms);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//
//                    return;
//                }
//                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:99968096")));
//            }
//        });
//
//        com.github.clans.fab.FloatingActionButton fab3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.send_mail);
//        fab3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        Intent iGet = getIntent();
        String project_id  = iGet.getStringExtra("project_id");

        getProduct();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getProduct () {

        Intent iGet = getIntent();

        String uri = AppConfig.ProjectService;

        RequestBody formBody = new FormBody.Builder()
                .add("project_id", iGet.getStringExtra("project_id"))
                .build();

        Log.e("Дуудсан холбоос: ", uri);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(uri)
                .post(formBody)
                .build();

        Log.e("Request: ", request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Error ", "Алдаа:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();

                Log.e("Res: ", ""+res);

                mHandler.post(() -> {
                    try {
                        JSONArray data = new JSONArray(res);
                        for (int i = 0; i < data.length(); i++) {
                            Project_image = data.getJSONObject(i).getString("project_image");
                            Project_name = data.getJSONObject(i).getString("project_name");
                            Project_description = data.getJSONObject(i).getString("project_about");

                        }
                        coordinatorLayout.setVisibility(View.VISIBLE);
                        Picasso.with(getApplicationContext()).load(AppConfig.AdminPageURL + Project_image).placeholder(R.drawable.ic_image).into(imgPreview, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                    }
                                });
                            }

                            @Override
                            public void onError() {

                            }
                        });

                        txtText.setText(Project_name);
                        txtDescription.loadDataWithBaseURL("", Project_description, "text/html", "UTF-8", "");
                        txtDescription.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtDescription.getSettings().setDefaultTextEncodingName("UTF-8");

                    } catch (Exception ex){
                        ex.printStackTrace();
                    }

                });
            }
        });
    }
}
