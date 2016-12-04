package com.androidtime.mvp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    //@BindView(R.id.wvDetails)
    WebView wvDetails;
    String title, href;
    ProgressDialog progressDialog;
    Button btnSearch,btnTitle;
    DetailsActivity detailsActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        detailsActivity = this;
        //ButterKnife.bind(this);
        wvDetails = (WebView) findViewById(R.id.wvDetails);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnTitle = (Button) findViewById(R.id.btnTitle);

        if (getIntent().getExtras() != null) {
            title = getIntent().getStringExtra("TITLE");
            href = getIntent().getStringExtra("HREF");
            LoadImageSearchData();
        }

        btnSearch.setOnClickListener(this);
    }

    private void LoadImageSearchData() {
        btnTitle.setText(title);
        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.setWebChromeClient(new WebChromeClient());
        wvDetails.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvDetails.loadUrl(href);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading information...Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        wvDetails.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                Intent intent = new Intent(detailsActivity, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
