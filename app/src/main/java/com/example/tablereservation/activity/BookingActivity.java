package com.example.tablereservation.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tablereservation.api.reservationAPI.ReservationAPI;
import com.example.tablereservation.api.reservationAPI.ReservationRequest;
import com.example.tablereservation.constant.GlobalFunction;
import com.example.tablereservation.databinding.ActivityBookingBinding;
import com.example.tablereservation.databinding.ActivityRestaurantDetailBinding;
import com.example.tablereservation.databinding.ItemBookingInfoDialogBinding;
import com.example.tablereservation.model.Reservation;
import com.example.tablereservation.model.Restaurant;
import com.example.tablereservation.prefs.DataStoreManager;
import com.example.tablereservation.util.GlideUtils;
import com.example.tablereservation.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends BaseActivity{
    private final Calendar mCalendar = Calendar.getInstance();
    private ActivityBookingBinding mActivityBookingBinding;
    private Restaurant mRestaurant;
    private String mTimeDuration;
    private int mNumberOfPeople;
    private static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBookingBinding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(mActivityBookingBinding.getRoot());
        mActivityBookingBinding.rdbOther.setChecked(true);
        mProgressDialog = new ProgressDialog(this);
        initToolbar();
        getDataIntent();
        setDataRestaurantDetailBooking();
        selectDate();
        selectTime();
        selectNumberOfPeople();
        mActivityBookingBinding.tvBooking.setOnClickListener(view -> onClickBooking());
    }

    private void initToolbar() {
        mActivityBookingBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityBookingBinding.toolbar.tvTitle.setText("Đặt bàn");

        mActivityBookingBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRestaurant = (Restaurant) bundle.get("restaurant_object");
        }
    }

    private void setDataRestaurantDetailBooking(){
        if(mRestaurant == null){
            return;
        }
        mActivityBookingBinding.tvRestaurantName.setText(mRestaurant.getName());
        mActivityBookingBinding.tvAddress.setText(mRestaurant.getAddress());
    }

    private void selectNumberOfPeople(){
        mActivityBookingBinding.tvSubtract.setOnClickListener(view -> {
            int count = Integer.parseInt(mActivityBookingBinding.tvCount.getText().toString());
            if(count <=1) return;
            int newCount = Integer.parseInt(mActivityBookingBinding.tvCount.getText().toString()) - 1;
            mActivityBookingBinding.tvCount.setText(String.valueOf(newCount));
            mNumberOfPeople = newCount;
        });
        mActivityBookingBinding.tvAdd.setOnClickListener(view -> {
            int count = Integer.parseInt(mActivityBookingBinding.tvCount.getText().toString());
            if(count >= 4) return;
            int newCount = Integer.parseInt(mActivityBookingBinding.tvCount.getText().toString()) + 1;
            mActivityBookingBinding.tvCount.setText(String.valueOf(newCount));
            mNumberOfPeople = newCount;
        });
    }

    private void selectDate(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                mCalendar.set(Calendar.YEAR,year);
                mCalendar.set(Calendar.MONTH,month);
                mCalendar.set(Calendar.DATE,date);
                mActivityBookingBinding.edtDay.setText(updateDate());
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this,date,
                mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DATE));
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DATE,3);
        dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        mActivityBookingBinding.edtDay.setOnClickListener(view -> dialog.show());
    }

    private void selectTime(){
        mActivityBookingBinding.spinnerTimeDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTimeDuration = mActivityBookingBinding.spinnerTimeDuration.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String updateDate(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(mCalendar.getTime());
    }

    private void onClickBooking(){
        String arrivedDate = mActivityBookingBinding.edtDay.getText().toString();
        String note = mActivityBookingBinding.note.getText().toString();
        String area = "";
        if(mActivityBookingBinding.rdbOther.isChecked()){
            area = mActivityBookingBinding.rdbOther.getText().toString();
        } else if(mActivityBookingBinding.rdbInside.isChecked()){
            area = mActivityBookingBinding.rdbInside.getText().toString();
        } else {
            area = mActivityBookingBinding.rdbOutside.getText().toString();
        }
        mNumberOfPeople = Integer.parseInt(mActivityBookingBinding.tvCount.getText().toString());
        ItemBookingInfoDialogBinding itemBookingInfoDialogBinding = ItemBookingInfoDialogBinding.inflate(getLayoutInflater());
        itemBookingInfoDialogBinding.tvName.setText("Nhà hàng: " + mRestaurant.getName());
        itemBookingInfoDialogBinding.tvArrivedDate.setText("Ngày tới: " + arrivedDate);
        itemBookingInfoDialogBinding.tvTimeDuration.setText("Khung giờ: "+mTimeDuration);
        itemBookingInfoDialogBinding.tvNumberOfPeople.setText("Số lượng khách: " + mNumberOfPeople);
        ReservationRequest reservation = new ReservationRequest(arrivedDate,
                mTimeDuration,mNumberOfPeople,note,mRestaurant.get_id(),area);
        Log.d("date",reservation.getArrivedDate());
        Log.d("duration",reservation.getDuration());
        Log.d("num",String.valueOf(reservation.getGuessNum()));
        Log.d("note",reservation.getNote());
        Log.d("resid",reservation.getRestaurant());
        Log.d("area",area);
        if(StringUtil.isEmpty(arrivedDate)){
            Toast.makeText(BookingActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Thông tin đặt bàn")
                    .setView(itemBookingInfoDialogBinding.getRoot())
                    .setPositiveButton("Đặt bàn", ((dialogInterface, i) -> {
                        book(reservation);
                    }))
                    .setNegativeButton("Hủy bỏ",null)
                    .show();

        }
    }

    private void book(ReservationRequest reservation){
        mProgressDialog.show();
        String jwt = "Bearer "+DataStoreManager.getToken();
        Call<ReservationRequest> call = ReservationAPI.retrofitInstance.createReservation(jwt,reservation);
        call.enqueue(new Callback<ReservationRequest>() {
            @Override
            public void onResponse(Call<ReservationRequest> call, Response<ReservationRequest> response) {
                Log.d("error",response.toString());
                if(response.isSuccessful()){
                    Toast.makeText(BookingActivity.this, "Đặt bàn thành công",
                            Toast.LENGTH_SHORT).show();
                    GlobalFunction.gotoMainActivity(BookingActivity.this);
                    finishAffinity();
                } else {
                    Toast.makeText(BookingActivity.this, "Không còn bàn trống, vui lòng chọn lại",
                            Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReservationRequest> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
