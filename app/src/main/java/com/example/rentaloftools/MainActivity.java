package com.example.rentaloftools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//Программный класс главного окна приложения "RentalOfTools"
public class MainActivity extends Activity implements OnClickListener {
    //Кнопки управления объектами
    Button btnClients, btnInstruments, btnOrders;
    //Объект для создания и управления таблицами в БД
    public DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Создание объекта для БД
        //dbHelper = new DBHelper(this);
        //Установка заголовка
        setTitle("Прокат инструментов");
        //Обработка кнопок
        btnClients = (Button) findViewById(R.id.clients);
        btnClients.setOnClickListener(this);
        btnInstruments = (Button) findViewById(R.id.instruments);
        btnInstruments.setOnClickListener(this);
        btnOrders = (Button) findViewById(R.id.orders);
        btnOrders.setOnClickListener(this);
        //Поток для подключения к БД
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper = new DBHelper(getApplicationContext());
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clients:
                //Работа с клиентами
                Intent clientActivity = new Intent(this, ClientActivity.class);
                //Запуск ClientActivity
                startActivity(clientActivity);
                break;
            case R.id.instruments:
                //Работа с инструментами
                Intent instrumentActivity = new Intent(this, InstrumentActivity.class);
                //Запуск InstrumentActivity
                startActivity(instrumentActivity);
                break;
            case R.id.orders:
                //Работа с заказами
                Intent orderActivity = new Intent(this, OrderActivity.class);
                //Запуск OrderActivity
                startActivity(orderActivity);
                break;
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}