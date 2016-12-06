package com.ekhanei.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.ekhanei.mvp.interfaces.DetailsActivityView;
import com.ekhanei.mvp.presenter.DetailsActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ekhanei.mvp.ConstantValues.INTENT_HREF;
import static com.ekhanei.mvp.ConstantValues.INTENT_TITLE;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, DetailsActivityView {

    @BindView(R.id.wvDetails)
    WebView wvDetails;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.btnTitle)
    Button btnTitle;

    String title, href;
    ProgressDialog progressDialog;
    DetailsActivity detailsActivity;
    DetailsActivityPresenter detailsActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailsActivity = this;
        ButterKnife.bind(this);
        detailsActivityPresenter = new DetailsActivityPresenter(this);

        if (getIntent().getExtras() != null) {
            title = getIntent().getStringExtra(INTENT_TITLE);
            href = getIntent().getStringExtra(INTENT_HREF);
            LoadDetails();
        }

        btnSearch.setOnClickListener(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private void LoadDetails() {
        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.setWebChromeClient(new WebChromeClient());
        wvDetails.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        detailsActivityPresenter.getRecipeDetails(href, title);


        wvDetails.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                detailsActivityPresenter.stopProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                detailsActivityPresenter.gotoMainScreen();
                break;
        }
    }

    @Override
    public void startLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public Context getAppContext() {
        return detailsActivity;
    }

    @Override
    public void loadRecipeDetail(String url, String title) {
        wvDetails.loadUrl(url);
        btnTitle.setText(title);
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(detailsActivity, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
