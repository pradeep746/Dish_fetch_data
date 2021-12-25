package com.pradeep.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishInformationActivity extends AppCompatActivity {
    TextView mDishName,mDishInformation,mDishWikiLink;
    int mDishId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_information);
        mDishName = (TextView) findViewById(R.id.dish_name);
        mDishInformation = (TextView) findViewById(R.id.short_desc);
        mDishWikiLink = (TextView) findViewById(R.id.wiki_link);
        Bundle bundle = getIntent().getExtras();
        mDishId = bundle.getInt("id");
        getDishInformation(mDishId);
    }

    private void getDishInformation(final int id){
        Call<DishInformation> call = DishDataFetch.getInstance().getMyApi().getDishDetails(id);
        call.enqueue(new Callback<DishInformation>() {
            @Override
            public void onResponse(Call <DishInformation> call, Response<DishInformation> response) {
                DishInformation data = response.body();
                mDishName.setText(data.name);
                mDishInformation.setText(data.short_desc);
                mDishWikiLink.setText(data.wiki_link);
            }

            @Override
            public void onFailure(Call<DishInformation> call, Throwable t) {
                Log.e("TAG",""+t.toString());
                Toast.makeText(getApplicationContext(), "Failed to get dish information.", Toast.LENGTH_LONG).show();
            }

        });
    }
}