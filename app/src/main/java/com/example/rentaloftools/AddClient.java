package com.example.rentaloftools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
Программный класс для добавления клиента в Таблицу: "Clients"
 */
public class AddClient extends MainActivity implements View.OnClickListener {
    //Поля для ввода данных
    EditText addIdClient, addNameClient, addPhonesClient, addIndividualDiscount;
    //Кнопка сохранения клиента в Таблицу: "Clients"
    Button btnSave;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();
    //Количество строк в Таблице: "Clients"
    int countClients = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclient);
        addIdClient = (EditText) findViewById(R.id.addIdClient);
        addNameClient = (EditText) findViewById(R.id.addNameClient);
        addPhonesClient = (EditText) findViewById(R.id.addPhonesClient);
        addIndividualDiscount = (EditText) findViewById(R.id.addIndividualDiscount);
        btnSave = (Button) findViewById(R.id.saveclient);
        btnSave.setOnClickListener(this);
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Выполняем запрос на выборку данных из Таблицы: "Clients"
        //Получаем интерфейс для чтения и записи значений результата запроса в БД
        Cursor cursor = db.rawQuery("SELECT * FROM Clients", null);
        cursor.moveToLast();
        //Получаем количество строк из Таблицы: "Clients"
        countClients = cursor.getInt(0);
        //Устанавливаем в поле для ввода id последнего клиента + 1
        addIdClient.setText(String.valueOf(countClients + 1));
    }

    @Override
    public void onClick(View v) {
        //Используется для добавления новых строк в Таблицу: "Clients"
        ContentValues cv = new ContentValues();
        //Сообщения об ошибках
        Toast messageInt = Toast.makeText(getApplicationContext(), "Данные id клиента, индивидуальная скидка должны быть числовыми", Toast.LENGTH_LONG);
        Toast messageIdNull = Toast.makeText(getApplicationContext(), "Пустое поле - id клиента", Toast.LENGTH_LONG);
        Toast messageNameNull = Toast.makeText(getApplicationContext(), "Пустое поле - ФИО клиента", Toast.LENGTH_LONG);
        Toast messagePhonesNull = Toast.makeText(getApplicationContext(), "Пустое поле - Телефон клиента", Toast.LENGTH_LONG);
        Toast messageIndividualDiscountNull = Toast.makeText(getApplicationContext(), "Пустое поле - Индивидуальная скидка", Toast.LENGTH_LONG);
        Toast messageSQL = Toast.makeText(getApplicationContext(), "Не верный запрос к базе данных", Toast.LENGTH_LONG);
        //Получаем данные из полей ввода
        String id = addIdClient.getText().toString();
        String name = addNameClient.getText().toString();
        String phones = addPhonesClient.getText().toString();
        String individualDiscount = addIndividualDiscount.getText().toString();
        //Подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.saveclient:
                //Обработка ошибок возникающих в случае ввода текстовых данных в числовые поля
                try{
                    int id1 = Integer.parseInt(addIdClient.getText().toString());
                    int individualDiscount1 = Integer.parseInt(addIndividualDiscount.getText().toString());
                }catch (Exception e){
                    messageInt.show();
                    messageInt.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageInt.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageInt.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Добавляем данные в контекст
                cv.put("id", id);
                cv.put("name", name);
                //Формат номера телефона
                if(phones.length() > 5)
                phones = phones.substring(0,1) + "("+phones.substring(1,4) + ")"+phones.substring(4,phones.length());
                cv.put("phones", phones);
                cv.put("individualDiscount", individualDiscount);
                //Обработка пустого ввода id клиента
                if (id.equals("")) {
                    messageIdNull.show();
                    messageIdNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageIdNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageIdNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода ФИО клиента
                if (name.equals("")) {
                    messageNameNull.show();
                    messageNameNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageNameNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageNameNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода Телефона клиента
                if (phones.equals("")) {
                    messagePhonesNull.show();
                    messagePhonesNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messagePhonesNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messagePhonesNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода индивидуальной скидки клиента
                if (individualDiscount.equals("")) {
                    messageIndividualDiscountNull.show();
                    messageIndividualDiscountNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageIndividualDiscountNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageIndividualDiscountNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                try{
                    //Вставляем в Таблицу: "Clients" нового клиента
                    int addCount = (int) db.insert("Clients", null, cv);
                    //В случае удачного добавления клиента возвращаемся на родительскую активность
                    if (addCount == (countClients + 1)) {onBackPressed();}
                }
                catch(android.database.sqlite.SQLiteConstraintException e){
                    //В случае если произошла ошибка при добавлении клиента в SQL запросе
                    messageSQL.show();
                    messageSQL.setGravity(Gravity.CENTER, 0, 0);
                        ((TextView)((LinearLayout)messageSQL.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageSQL.cancel();
                        }
                    }, 2000);
                }
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
