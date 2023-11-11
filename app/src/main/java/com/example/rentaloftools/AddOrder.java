package com.example.rentaloftools;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Insets;
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
Программный класс для добавления заказа в Таблицу: "Orders"
 */
public class AddOrder extends MainActivity implements View.OnClickListener {
    //Поля для ввода данных
    EditText addIdOrder, addIdClientOrder, addMoneyOrder, addStartdateOrder,
            addTimeOrder, addStatusOrder, addIdInstrumentsOrder;
    //Кнопка сохранения заказа в Таблицу: "Orders"
    Button btnSave;
    //Кнопка расчета заказа
    Button btnCalculate;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();
    //Количество строк в Таблице: "Orders"
    int countOrders = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorder);
        addIdOrder = (EditText) findViewById(R.id.addIdOrder);
        addMoneyOrder = (EditText) findViewById(R.id.addMoneyOrder);
        addStartdateOrder = (EditText) findViewById(R.id.addStartdateOrder);
        addTimeOrder = (EditText) findViewById(R.id.addTimeOrder);
        addIdClientOrder = (EditText) findViewById(R.id.addIdClientOrder);
        addIdInstrumentsOrder = (EditText) findViewById(R.id.addIdInstrumentsOrder);
        addStatusOrder = (EditText) findViewById(R.id.addStatusOrder);
        btnSave = (Button) findViewById(R.id.saveorder);
        btnSave.setOnClickListener(this);
        btnCalculate= (Button) findViewById(R.id.calculateOrder);
        btnCalculate.setOnClickListener(this);
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Выполняем запрос на выборку данных из Таблицы: "Orders"
        //Получаем интерфейс для чтения и записи значений результата запроса в БД
        Cursor cursor = db.rawQuery("SELECT * FROM Orders", null);
        cursor.moveToLast();
        //Получение количества строк из Таблицы: "Orders"
        if(cursor.getCount() == 0){
            countOrders = 0;
        }else {
            countOrders = cursor.getInt(0);
        }
        //Устанавливаем в поле для ввода id последнего заказа + 1
        addIdOrder.setText(String.valueOf(countOrders + 1));
    }

    @Override
    public void onClick(View v) {
        //Используется для добавления новых строк в Таблицу: "Orders"
        ContentValues cv = new ContentValues();
        Toast messageStatusInstruments = Toast.makeText(getApplicationContext(), "Данные инструменты в аренде", Toast.LENGTH_LONG);
        Toast messageInt = Toast.makeText(getApplicationContext(), "Данные id заказа, стоимость заказа, id клиента, статус заказа должны быть числовыми", Toast.LENGTH_LONG);
        Toast messageIdNull = Toast.makeText(getApplicationContext(), "Пустое поле - id заказа", Toast.LENGTH_LONG);
        Toast messageMoneyNull = Toast.makeText(getApplicationContext(), "Пустое поле - стоимость заказа", Toast.LENGTH_LONG);
        Toast messageStartdateNull = Toast.makeText(getApplicationContext(), "Пустое поле - дата заказа", Toast.LENGTH_LONG);
        Toast messageTimeNull = Toast.makeText(getApplicationContext(), "Пустое поле - время аренды", Toast.LENGTH_LONG);
        Toast messageIdClientNull = Toast.makeText(getApplicationContext(), "Пустое поле - id клиента", Toast.LENGTH_LONG);
        Toast messageIdInstrumentsNull = Toast.makeText(getApplicationContext(), "Пустое поле - id инструментов", Toast.LENGTH_LONG);
        Toast messageStatusNull = Toast.makeText(getApplicationContext(), "Пустое поле - статус заказа", Toast.LENGTH_LONG);
        Toast messageSQL = Toast.makeText(getApplicationContext(), "Не верный запрос к базе данных", Toast.LENGTH_LONG);
        //Получаем данные из полей ввода
        String id = addIdOrder.getText().toString();
        String money = addMoneyOrder.getText().toString();
        String startdate = addStartdateOrder.getText().toString();
        String time = addTimeOrder.getText().toString();
        String idClient = addIdClientOrder.getText().toString();
        String idInstruments = addIdInstrumentsOrder.getText().toString();
        String status = addStatusOrder.getText().toString();
        //Подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.saveorder:
                //Обработка ошибок возникающих в случае ввода текстовых данных в числовые поля
                try{
                    int id1 = Integer.parseInt(addIdOrder.getText().toString());
                    int idClient1 = Integer.parseInt(addIdClientOrder.getText().toString());
                    int money1 = Integer.parseInt(addMoneyOrder.getText().toString());
                    int status1 = Integer.parseInt(addStatusOrder.getText().toString());
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
                cv.put("money", money);
                cv.put("startdate", startdate);
                cv.put("time", time);
                cv.put("idClient", idClient);
                cv.put("idInstruments", idInstruments);
                cv.put("status", status);
                //Обработка пустого ввода id заказа
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
                //Обработка пустого ввода стоимости заказа
                if (money.equals("")) {
                    messageMoneyNull.show();
                    messageMoneyNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageMoneyNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageMoneyNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода даты заказа
                if (startdate.equals("")) {
                    messageStartdateNull.show();
                    messageStartdateNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageStartdateNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageStartdateNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода времени аренды
                if (time.equals("")) {
                    messageTimeNull.show();
                    messageTimeNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageTimeNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageTimeNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода id клиента для заказа
                if (idClient.equals("")) {
                    messageIdClientNull.show();
                    messageIdClientNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageIdClientNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageIdClientNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода id инструментов для заказа
                if (idInstruments.equals("")) {
                    messageIdInstrumentsNull.show();
                    messageIdInstrumentsNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageIdInstrumentsNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageIdInstrumentsNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                //Обработка пустого ввода статуса заказа
                if (status.equals("")) {
                    messageStatusNull.show();
                    messageStatusNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageStatusNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageStatusNull.cancel();
                        }
                    }, 2000);
                    break;
                }
                try{
                    //Вставляем в Таблицу: "Orders" новый заказ
                     int addCount = (int) db.insert("Orders", null, cv);
                    //В случае удачного добавления заказа возвращаемся на родительскую активность
                    if (addCount == (countOrders + 1)) {onBackPressed();}
                }
                //В случае если произошла ошибка при добавлении заказа в SQL запросе
                catch(android.database.sqlite.SQLiteConstraintException e){
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
                break;
                //расчет заказа
            case R.id.calculateOrder:
                try {
                    //Определяем статусы введенных инструментов (для оформления заказа сумма статусов = 0, т.е. по данным инструментам нет аренды)
                    Cursor cursorInstruments = db.rawQuery("SELECT SUM(rentStatus) AS sum FROM Instruments WHERE id IN (" + idInstruments + ")", null);
                    cursorInstruments.moveToFirst();
                    @SuppressLint("Range") int sumStatus = Integer.parseInt(cursorInstruments.getString(cursorInstruments.getColumnIndex("sum")));
                    //Если инструменты не в других заказах
                    if(sumStatus == 0) {
                        //Стоимость заказа
                        int calculateMoney = 0;
                        //Получаем скидку для id клиента
                        Cursor cursor1 = db.rawQuery("SELECT individualDiscount FROM Clients WHERE id = " + idClient, null);
                        cursor1.moveToFirst();
                        //Стоимсоть инструментов в заказе
                        @SuppressLint("Range") int individualDiscount = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("individualDiscount")));
                        Cursor cursor2 = db.rawQuery("SELECT SUM(rentalFees) AS sum FROM Instruments WHERE id IN (" + idInstruments + ")", null);
                        cursor2.moveToFirst();
                        @SuppressLint("Range") int moneyInstruments = Integer.parseInt(cursor2.getString(cursor2.getColumnIndex("sum")));
                        //Время заказа
                        int t = Integer.parseInt(time);
                        calculateMoney = moneyInstruments * t - ((moneyInstruments * t) / 100) * individualDiscount;
                        addMoneyOrder.setText(String.valueOf(calculateMoney));
                        //изменяем статусы выбранных инструментов
                        db.execSQL("UPDATE Instruments SET rentStatus = 1 WHERE id IN (" + idInstruments + ")");
                    }else{
                        //Ошибка: "Данные инструменты в аренде"
                        messageStatusInstruments.show();
                        messageStatusInstruments.setGravity(Gravity.CENTER, 0, 0);
                        ((TextView)((LinearLayout)messageStatusInstruments.getView()).getChildAt(0))
                                .setGravity(Gravity.CENTER_HORIZONTAL);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                messageStatusInstruments.cancel();
                            }
                        }, 2000);
                    break;
                    }
                }catch (Exception e){
                    //В случае если произошла ошибка при добавлении заказа в SQL запросе
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
                break;
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
