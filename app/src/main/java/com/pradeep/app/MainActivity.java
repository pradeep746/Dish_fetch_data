package com.pradeep.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private TextView mDishName,mMoreImageText;
    private ImageView mDishImage;
    private DishDetail mDishDetail;
    private Activity mActivity;
    private Bitmap mImage;
    private ViewPager mMoreImage;
    private Context mContext;
    private TextView mShareData,mWikiOpen;
    private LocalDatabaseInter mLocalDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDishName = (TextView)findViewById(R.id.dish_name);
        mDishImage = (ImageView)findViewById(R.id.dish_img);
        mMoreImageText = (TextView)findViewById(R.id.more_image_text);
        mMoreImage = (ViewPager) findViewById(R.id.more_image);
        mShareData = (TextView)findViewById(R.id.share_link);
        mWikiOpen = (TextView)findViewById(R.id.wiki_link);
        getDishDetail();
        mActivity = this;
        mContext = this;
        mLocalDataBase = LocalDatabaseInter.getinstance(mContext);
        mMoreImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoreImageText.setVisibility(View.GONE);
                mMoreImage.setVisibility(View.VISIBLE);
                ImageAdapter adapterView = new ImageAdapter(mContext,mDishDetail.data);
                mMoreImage.setAdapter(adapterView);
            }
        });
        mWikiOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(mDishDetail.wiki_link); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        mShareData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","data vlil");
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dish details:"+mDishDetail.name);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mDishDetail.share_link);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mDishName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DishInformationActivity.class);
                intent.putExtra("id", mDishDetail.id);
                startActivity(intent);
            }
        });

    }


    private void getDishDetail() {
        Call<DishDetail> call = DishDataFetch.getInstance().getMyApi().getDish();
        call.enqueue(new Callback<DishDetail>() {
            @Override
            public void onResponse(Call <DishDetail> call, Response<DishDetail> response) {
                mDishDetail = response.body();
                mDishName.setText(mDishDetail.name);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            URL urlConnection = new URL(mDishDetail.image_url);
                            HttpURLConnection connection = (HttpURLConnection) urlConnection
                                    .openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            mImage = BitmapFactory.decodeStream(input);
                            mActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    mDishImage.setImageBitmap(mImage);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }

            @Override
            public void onFailure(Call<DishDetail> call, Throwable t) {
                Log.e("TAG",""+t.toString());
                Toast.makeText(getApplicationContext(), "Failed to get current dishes of the date.", Toast.LENGTH_LONG).show();
            }

        });
    }

}