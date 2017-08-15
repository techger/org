package agency.techstar.yellowbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webb = (WebView)findViewById(R.id.web);
        webb.setWebViewClient(new WebViewClient());
        webb.loadUrl("http://www.google.com");
    }
}
