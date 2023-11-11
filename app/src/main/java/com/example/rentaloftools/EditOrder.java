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
Программный класс для изменения параметров заказа в Таблице: "Orders"
 */
public class EditOrder extends MainActivity implements View.OnClickListener {
    //Поля для ввода данных
    EditText editIdOrder, editIdClientOrder, editMoneyOrder, editStartdateOrder,
           editTimeOrder, editStatusOrder, editIdInstrumentsOrder;
    //Кнопка сохранения измененного заказа в Таблицу: "Orders"
    Button btnSave;
    //Кнопка отображения параметров выбранного заказа
    Button btnShow;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();
    //Количество строк в Таблице: "Orders"
    int countOrders = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorder);
        editIdOrder = (EditText) findViewById(R.id.editIdOrder);
        editIdClientOrder = (EditText) findViewById(R.id.editIdClientOrder);
        editMoneyOrder = (EditText) findViewById(R.id.editMoneyOrder);
        editStartdateOrder = (EditText) findViewById(R.id.editStartdateOrder);
        editTimeOrder = (EditText) findViewById(R.id.editTimeOrder);
        editStatusOrder = (EditText) findViewById(R.id.editStatusOrder);
        editIdInstrumentsOrder = (EditText) findViewById(R.id.editIdInstrumentsOrder);
        btnShow = (Button) findViewById(R.id.showOrder);
        btnShow.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.saveOrder);
        btnSave.setOnClickListener(this);
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Выполняем запрос на выборку данных из Таблицы: "Orders"
        //Получаем интерфейс для чтения и записи значений результата запроса в БД
        Cursor cursor = db.rawQuery("SELECT * FROM Orders", null);
        cursor.moveToLast();
        //Если в Таблице: "Orders" есть заказы
        if (cursor.getCount() != 0) {
            //Получаем количество строк из Таблицы: "Orders"
            countOrders = cursor.getInt(0);
            //Устанавливаем в поле для ввода id - номер последнего заказа
            editIdOrder.setText(String.valueOf(countOrders));
        }
    }

    @Override
    public void onClick(View v) {
        //Используется для обновления параметров заказа
        ContentValues cv = new ContentValues();
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
        String id = editIdOrder.getText().toString();
        String money = editMoneyOrder.getText().toString();
        String startdate = editStartdateOrder.getText().toString();
        String time = editTimeOrder.getText().toString();
        String idClient = editIdClientOrder.getText().toString();
        String idInstruments = editIdInstrumentsOrder.getText().toString();
        String status = editStatusOrder.getText().toString();
        //Подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.showOrder:
                //Отправляем запрос в БД для Таблицы: "Orders"
                Cursor cursor = db.rawQuery("SELECT * FROM Orders WHERE "+"id = " + id, null);
                cursor.moveToFirst();
                //Цикл по результату запроса в БД
                while (!cursor.isAfterLast()) {
                    editMoneyOrder.setText(cursor.getString(1));
                    editStartdateOrder.setText(cursor.getString(2));
                    editTimeOrder.setText(cursor.getString(3));
                    editIdClientOrder.setText(cursor.getString(4));
                    editIdInstrumentsOrder.setText(cursor.getString(5));
                    editStatusOrder.setText(cursor.getString(6));
                    cursor.moveToNext();
                }
                cursor.close();
                break;
            case R.id.saveOrder:
                //Обработка ошибок возникающих в случае ввода текстовых данных в числовые поля
                try{
                    int id1 = Integer.parseInt(editIdOrder.getText().toString());
                    int idClient1 = Integer.parseInt(editIdClientOrder.getText().toString());
                    int money1 = Integer.parseInt(editMoneyOrder.getText().toString());
                    int status1 = Integer.parseInt(editStatusOrder.getText().toString());
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
                    //Устанавливаем кнопке "Показать" свойство "setClickable(false)"
                    btnShow.setClickable(false);
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
                    //Изменяем параметры заказа в Таблице: "Orders"
                    int updateCount = (int) db.update("Orders", cv, "id = " + id, null);
                    //Обнавляем статусы аренды для инструментов
                    db.execSQL("UPDATE Instruments SET rentStatus = 0 WHERE id IN (" + idInstruments + ")");
                    //В случае удачного обновления параметров запроса возвращаемся на родительскую активность
                    if (updateCount == 1) {onBackPressed();}
                    if (updateCount == 0) {
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
                //В случае если произошла ошибка в запросе на обновление
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
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
