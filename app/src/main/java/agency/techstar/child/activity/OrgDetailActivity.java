package agency.techstar.child.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;

import agency.techstar.child.AppConfig;
import agency.techstar.child.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OrgDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView imgPreview;
    TextView txtText, txtSubText;
    WebView txtDescription;
    TextView txtAlert;
    CoordinatorLayout coordinatorLayout;
    Handler mHandler;
    String Organization_image, Organization_name, Organization_about, Organization_phone, Organization_email, Organization_Web, Organization_Fb, Organization_Location;
    MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
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

        FloatingActionButton webbutton = (FloatingActionButton) findViewById(R.id.btnWeb);
        FloatingActionButton callbutton = (FloatingActionButton) findViewById(R.id.btnCall);
        FloatingActionButton emailbutton = (FloatingActionButton) findViewById(R.id.btnEmail);
        FloatingActionButton fbbutton = (FloatingActionButton) findViewById(R.id.btnAdd);

        Intent iGet = getIntent();
        String org_id = iGet.getStringExtra("org_id");
        getOrganization();
        webbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent web = new Intent(OrgDetailActivity.this, WebActivity.class);
                web.putExtra("org_web", Organization_Web);
                startActivity(web);
            }
        });

        callbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Organization_phone));
                if (ActivityCompat.checkSelfPermission(OrgDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        emailbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",Organization_email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Гарчиг");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Техт");
                startActivity(Intent.createChooser(emailIntent, "Таны и-мэйл илгээгдэж байна..."));
            }
        });

        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent web = new Intent(OrgDetailActivity.this, WebActivity.class);
                web.putExtra("org_web", Organization_Fb);
                startActivity(web);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);

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

    public void getOrganization() {

        Intent iGet = getIntent();

        String uri = AppConfig.OrgService;

        RequestBody formBody = new FormBody.Builder()
                .add("org_id", iGet.getStringExtra("org_id"))
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

                Log.e("Res: ", "" + res);
                mHandler.post(() -> {
                    try {
                        JSONArray data = new JSONArray(res);
                        for (int i = 0; i < data.length(); i++) {
                            Organization_image = data.getJSONObject(i).getString("org_image");
                            Organization_name = data.getJSONObject(i).getString("org_name");
                            Organization_about = data.getJSONObject(i).getString("org_about");
                            Organization_phone = data.getJSONObject(i).getString("org_phone");
                            Organization_email = data.getJSONObject(i).getString("org_email");
                            Organization_Web = data.getJSONObject(i).getString("org_web");
                            Organization_Fb = data.getJSONObject(i).getString("org_fb");
                            Organization_Location = data.getJSONObject(i).getString("org_location");
                        }
                        coordinatorLayout.setVisibility(View.VISIBLE);
                        Picasso.with(getApplicationContext()).load(AppConfig.AdminPageURL + "/"+ Organization_image).placeholder(R.drawable.ic_image).into(imgPreview, new com.squareup.picasso.Callback() {
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

                        txtText.setText(Organization_name);
                        txtDescription.loadDataWithBaseURL("", Organization_about, "text/html", "UTF-8", "");
                        txtDescription.setBackgroundColor(Color.parseColor("#ffffff"));
                        txtDescription.getSettings().setDefaultTextEncodingName("UTF-8");

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        setUpMap();

    }

    public void setUpMap() {

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
