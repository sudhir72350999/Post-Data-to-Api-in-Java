package com.test.postdatatoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText nameEdt, ageEdt, genderEdt, phoneNumberEdt, emailEdt, addressEdt, countryEdt;

    private Button postDataBtn;
    private TextView responseTV;
    private ProgressBar loadingPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initializing our views

        nameEdt = findViewById(R.id.edtName);
        ageEdt = findViewById(R.id.edtAge);
        genderEdt = findViewById(R.id.edtGender);
        phoneNumberEdt = findViewById(R.id.edtPhoneNumber);
        emailEdt = findViewById(R.id.edtEmail);
        addressEdt = findViewById(R.id.edtAddress);
        countryEdt = findViewById(R.id.edtCountry);


        postDataBtn = findViewById(R.id.idBtnPost);
        responseTV = findViewById(R.id.idTVResponse);
        loadingPB = findViewById(R.id.idLoadingPB);


        // adding on click listener to our button.
        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the text field is empty or not.
                if (nameEdt.getText().toString().isEmpty() || ageEdt.getText().toString().isEmpty() || genderEdt.getText().toString().isEmpty() || phoneNumberEdt.getText().toString().isEmpty() || emailEdt.getText().toString().isEmpty() || addressEdt.getText().toString().isEmpty() || countryEdt.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the field", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to post the data and passing our name , job and password
                postData(nameEdt.getText().toString(), ageEdt.getText().toString(), genderEdt.getText().toString(), phoneNumberEdt.getText().toString(), emailEdt.getText().toString(), addressEdt.getText().toString(), countryEdt.getText().toString());


            }
        });
    }

    private void postData(String name, String age, String gender, String phoneNumber, String email, String address, String country) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
//                .baseUrl("https://bespokeapp.xyz/practical/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        DataModal modal = new DataModal(name, age, gender, phoneNumber, email, address, country);

        // calling a method to create a post and passing our modal class.
        Call<DataModal> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty text

                nameEdt.setText("");
                ageEdt.setText("");
                genderEdt.setText("");
                phoneNumberEdt.setText("");
                emailEdt.setText("");
                addressEdt.setText("");
                countryEdt.setText("");


                // we are getting response from our body
                // and passing it to our modal class.
                DataModal responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Age : " + responseFromAPI.getAge() + "\n" + "gender :" + responseFromAPI.getGender() + "\n" + "phoneNumber :" + responseFromAPI.getPhoneNumber() + "\n" + "email :" + responseFromAPI.getEmail() + "\n" + "address :" + responseFromAPI.getAddress() + "\n" + "country :" + responseFromAPI.getCountry();

                // below line we are setting our
                // string to our text view.
                responseTV.setText(responseString);
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}
