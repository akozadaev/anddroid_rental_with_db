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
Программный класс для изменения параметров инструмента в Таблице: "Instruments"
 */
public class EditInstrument extends MainActivity implements View.OnClickListener {
    //Метки полей для ввода данных
    TextView nameInst, rentalFeesInst, rentStatusInst;
    //Поля для ввода данных
    EditText etId, etName, etRentalFees, etRentStatus;
    //Кнопка сохранения измененного инструмента в Таблицу: "Instruments"
    Button btnSave;
    //Кнопка отображения параметров выбранного инструмента
    Button btnShow;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();
    //Количество строк в Таблице: "Instruments"
    int countInstruments = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinstrument);
        etId = (EditText) findViewById(R.id.etId);
        etName = (EditText) findViewById(R.id.etName);
        etRentalFees = (EditText) findViewById(R.id.etRentalFees);
        etRentStatus = (EditText) findViewById(R.id.etRentStatus);
        btnShow = (Button) findViewById(R.id.showinstrument);
        btnShow.setOnClickListener(this);
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
        //Устанавливаем в поле для ввода id - номер последнего инструмента
        etId.setText(String.valueOf(countInstruments));
    }

    @Override
    public void onClick(View v) {
        //Используется для обновления параметров инструмента
        ContentValues cv = new ContentValues();
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
            case R.id.showinstrument:
                //Отправляем запрос в БД для Таблицы: "Instruments"
                Cursor cursor = db.rawQuery("SELECT * FROM Instruments WHERE "+"id = " + id, null);
                cursor.moveToFirst();
                //Цикл по результату запроса в БД
                while (!cursor.isAfterLast()) {
                    etName.setText(cursor.getString(1));
                    etRentalFees.setText(cursor.getString(2));
                    etRentStatus.setText(cursor.getString(3));
                    cursor.moveToNext();
                }
                cursor.close();
                break;
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
                    }, 2000);
                    btnShow.setClickable(false);
                    break;
                }
                //Добавляем данные в контекст
                cv.put("id", etId.getText().toString());
                cv.put("name", etName.getText().toString());
                cv.put("rentalFees", etRentalFees.getText().toString());
                cv.put("rentStatus", etRentStatus.getText().toString());
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
                    //Если статус аренды не равен 0 и 1
                    if (Integer.valueOf(rentStatus) != 0 && Integer.valueOf(rentStatus) != 1) {
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
                    //Изменяем параметры инструмента в Таблице: "Instruments"
                    int updateCount = (int) db.update("Instruments", cv, "id = " + id, null);
                    System.out.println(updateCount);
                    //В случае удачного обновления параметров инструмента возвращаемся на родительскую активность
                    if (updateCount == 1) {onBackPressed();}
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
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
