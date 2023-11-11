package com.example.rentaloftools;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Программный класс для работы с Таблицей "Clients"
 */
public class ClientActivity extends MainActivity implements View.OnClickListener {
    //Кнопки для работы с Таблицей "Clients"
    Button btnAdd, btnEdit, btnDel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        //Установка заголовка окна
        setTitle("Клиенты");
        //Вкладки
        TabHost tabHost = (TabHost) findViewById(R.id.tabHostClient);
        tabHost.setup();
        //Формирование вкладки с таблицей данных
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tag1");
        tabSpec1.setContent(R.id.linearLayout1);
        tabSpec1.setIndicator("Таблица");
        tabHost.addTab(tabSpec1);
        //Формирование вкладки с настройками
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tag2");
        tabSpec2.setContent(R.id.linearLayout2);
        tabSpec2.setIndicator("Настройки");
        tabHost.addTab(tabSpec2);
        //Установка текущей вкладки
        tabHost.setCurrentTab(0);
        //Обработка выбора вкладки таблицы
        tabHost.setOnTabChangedListener(tabId -> {
            //Если выбрана вкладка "Таблица"
            if("tag1".equals(tabId)) {
                //Чтение данных из Таблицы: "Clients"
                initTableClient(dbHelper);
            }
        });
        //Обработка кнопок
        btnAdd = (Button) findViewById(R.id.addclient);
        btnAdd.setOnClickListener(this);
        btnEdit = (Button) findViewById(R.id.editclient);
        btnEdit.setOnClickListener(this);
        btnDel = (Button) findViewById(R.id.delclient);
        btnDel.setOnClickListener(this);
        //Чтение данных из Таблицы: "Clients"
        initTableClient(dbHelper);
    }

    /**
     * Функция чтения данных из Таблицы: "Clients"
     * @param dbHelper
     */
    public void initTableClient(DBHelper dbHelper){
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Список клиентов
        ArrayList<HashMap<String, Object>> clients = new ArrayList<HashMap<String, Object>>();
        //Список параметров каждого клиента
        HashMap<String, Object> client;
        //Отправляем запрос в БД для Таблицы: "Clients"
        Cursor cursor = db.rawQuery("SELECT * FROM Clients", null);
        cursor.moveToFirst();
        //Цикл по все клиентам
        while (!cursor.isAfterLast()) {
            client = new HashMap<String, Object>();
            //Заполняем клиента для отображения
            client.put("id", "id клиента: " + cursor.getString(0));
            client.put("name", "ФИО: " + cursor.getString(1));
            client.put("phones", "телефон: " + cursor.getString(2));
            client.put("individualDiscount", "скидка: " + cursor.getString(3) + " %");
            //Добавляем клиента в список
            clients.add(client);
            //Переходим к следующему
            cursor.moveToNext();
        }
        cursor.close();
        //Параметры клиента, которые будем отображать в соответствующих
        //элементах из разметки adapter_client.xml
        String[] from = {"id", "name", "phones", "individualDiscount"};
        int[] to = {R.id.textId, R.id.textName, R.id.textPhones, R.id.textIndividualDiscount};
        //Создаем адаптер для работы с ListView
        SimpleAdapter adapter = new SimpleAdapter(this, clients, R.layout.adapter_client, from, to);
        ListView listView = (ListView) findViewById(R.id.listViewClient);
        listView.setAdapter(adapter);
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Добавление клиента
            case R.id.addclient:
                Intent addClient = new Intent(this, AddClient.class);
                //Запуск activity AddClient
                startActivity(addClient);
                break;
            //Изменение клиента
            case R.id.editclient:
                Intent editClient = new Intent(this, EditClient.class);
                //Запуск activity EditClient
                startActivity(editClient);
                break;
            //Удаление клиента
            case R.id.delclient:
                Intent delClient = new Intent(this, DelClient.class);
                //Запуск activity DelClient
                startActivity(delClient);
                break;
        }
    }
}