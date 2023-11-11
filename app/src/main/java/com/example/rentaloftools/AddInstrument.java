package com.example.rentaloftools;

import android.content.ContentValues;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

/*
Программный класс для добавления инструмента в Таблицу: "Instruments" для БД
 */
public class AddInstrument extends MainActivity implements View.OnClickListener {
    //Поля для ввода данных
    EditText etId, etName, etRentalFees, etRentStatus;
    //Кнопка сохранения инструмента в Таблицу: "Instruments"
    Button btnSave;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();
    //Количество строк в Таблице: "Instruments"
    int countInstruments = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinstrument);
        etId = (EditText) findViewById(R.id.etId);
        etName = (EditText) findViewById(R.id.etName);
        etRentalFees = (EditText) findViewById(R.id.etRentalFees);
        etRentStatus = (EditText) findViewById(R.id.etRentStatus);
        btnSave = (Button) findViewById(R.id.saveinstrument);
        btnSave.setOnClickListener(this);
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Выполняем запрос на выборку данных из Таблицы: "Instruments"
        //Получаем интерфейс для чтения и записи значений результата запроса в БД
        Cursor cursor = db.rawQuery("SELECT * FROM Instruments", null);
        cursor.moveToLast();
        //Получаем количество строк из Таблицы: "Instruments"
        countInstruments = cursor.getInt(0);
        //Устанавливаем в поле для ввода id последнего инструмента + 1
        etId.setText(String.valueOf(countInstruments + 1));
    }

    @Override
    public void onClick(View v) {
        //Используется для добавления новых строк в Таблицу: "Instruments"
        ContentValues cv = new ContentValues();
        //Сообщения о ошибках
        Toast messageInt = Toast.makeText(getApplicationContext(), "Данные id инструмента, стоимости в сутки и статус аренды должны быть числовыми", Toast.LENGTH_LONG);
        Toast messageNull = Toast.makeText(getApplicationContext(), "Пустые поля ввода данных", Toast.LENGTH_LONG);
        Toast message = Toast.makeText(getApplicationContext(), "Не верный статус аренды инструмента", Toast.LENGTH_LONG);
        Toast messageSQL = Toast.makeText(getApplicationContext(), "Не верный запрос к базе данных", Toast.LENGTH_LONG);
        //Получаем данные из полей ввода
        String id = etId.getText().toString();
        String name = etName.getText().toString();
        String rentalFees = etRentalFees.getText().toString();
        String rentStatus = etRentStatus.getText().toString();
        //Подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.saveinstrument:
                //Обработка ошибок возникающих в случае ввода текстовых данных в числовые поля
                try{
                    int id1 = Integer.parseInt(etId.getText().toString());
                    int rentalFees1 = Integer.parseInt(etRentalFees.getText().toString());
                    int rentStatus1 = Integer.parseInt(etRentStatus.getText().toString());
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
                    }, 2000);// 5 sec
                    break;
                }
                //Добавляем данные в контекст
                cv.put("id", id);
                cv.put("name", name);
                cv.put("rentalFees", rentalFees);
                cv.put("rentStatus", rentStatus);
                //Обработка пустого ввода
                if ((id.equals("") && name.equals("")
                        && rentalFees.equals("") && rentStatus.equals("")) || (name.equals("")
                        && rentalFees.equals(""))) {
                    messageNull.show();
                    messageNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageNull.cancel();
                        }
                    }, 2000);
                }else{
                    //Обработка ввода статуса аренды инструмента (при создании инструмента он равен 0)
                    if (Integer.valueOf(rentStatus) != 0) {
                        message.show();
                        message.setGravity(Gravity.CENTER, 0, 0);
                        ((TextView)((LinearLayout)message.getView()).getChildAt(0))
                                .setGravity(Gravity.CENTER_HORIZONTAL);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                message.cancel();
                            }
                        }, 2000);
                        break;
                    }
                try{
                    //Вставляем в Таблицу: "Instruments" новый инструмент
                     int addCount = (int) db.insert("Instruments", null, cv);
                    //В случае удачного добавления инструмента возвращаемся на родительскую активность
                    if (addCount == (countInstruments + 1)) {onBackPressed();}
                }
                //В случае если произошла ошибка при добавлении инструмента в SQL запросе
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
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
