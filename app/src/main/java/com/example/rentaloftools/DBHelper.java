package com.example.rentaloftools;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

/**
 * Класс для работы с БД "ProcatDB"
 */
public class DBHelper extends SQLiteOpenHelper {
    //Контекст данных
    private final Context fContext;
    //Название базы данных
    private static final String DATABASE_NAME = "ProcatDB";
    //Название таблицы с инструментами
    public static final String TABLE_INSTRUMENTS = "Instruments";
    //Название таблицы с клиентами
    public static final String TABLE_CLIENTS = "Clients";
    //Название таблицы с заказами
    public static final String TABLE_ORDERS = "Orders";

    /**
     * Конструктор класса
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Создание таблицы с инструментами
        db.execSQL("CREATE TABLE " + TABLE_INSTRUMENTS +"("
                + "id integer primary key,"
                + "name text,"
                + "rentalFees integer,"
                + "rentStatus integer" + ");");
        //Создание таблицы с клиентами
        db.execSQL("CREATE TABLE " + TABLE_CLIENTS +"("
                + "id integer primary key,"
                + "name text,"
                + "phones text,"
                + "individualDiscount integer" + ");");
        //Создание таблицы с заказами
        db.execSQL("CREATE TABLE " + TABLE_ORDERS +"("
                + "id integer primary key,"
                + "money integer,"
                + "startdate text,"
                + "time text,"
                + "idClient integer,"
                + "idInstruments text,"
                + "status integer" + ");");
        //Добавляем записи в таблицу инструментов
        ContentValues values_instrument = new ContentValues();
        //Получаем файл из ресурсов
        Resources res_instrument = fContext.getResources();
        //Открываем xml-файл
        XmlResourceParser instruments_records_xml = res_instrument.getXml(R.xml.instruments_records);
        try {
            //Ищем конец документа
            int eventType = instruments_records_xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Ищем теги record
                if ((eventType == XmlPullParser.START_TAG)
                        && (instruments_records_xml.getName().equals("record"))) {
                    //Получаем атрибуты Тега Record и вставляем в таблицу
                    String id = instruments_records_xml.getAttributeValue(0);
                    String name = instruments_records_xml.getAttributeValue(1);
                    String rentalFees = instruments_records_xml.getAttributeValue(3);
                    String rentStatus = instruments_records_xml.getAttributeValue(2);
                    values_instrument.put("id", id);
                    values_instrument.put("name", name);
                    values_instrument.put("rentalFees", Integer.valueOf(rentalFees));
                    values_instrument.put("rentStatus", Integer.valueOf(rentStatus));
                    db.insert(TABLE_INSTRUMENTS, null, values_instrument);
                }
                eventType = instruments_records_xml.next();
            }
        }
        //Обработка ошибок работы с файлом xml
        catch (XmlPullParserException e) {
            Log.e("Test", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Test", e.getMessage(), e);
        } finally {
            //Закрываем xml файл
            instruments_records_xml.close();
        }
        //Добавляем записи в таблицу клиентов
        ContentValues values_clients = new ContentValues();
        //Получаем файл из ресурсов
        Resources res_clients = fContext.getResources();
        //Открываем xml-файл
        XmlResourceParser clients_records_xml = res_clients.getXml(R.xml.clients_records);
        try {
            //Ищем конец документа
            int eventType = clients_records_xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Ищем теги record
                if ((eventType == XmlPullParser.START_TAG)
                        && (clients_records_xml.getName().equals("record"))) {
                    //Получаем атрибуты Тега Record и вставляем в таблицу
                    String id = clients_records_xml.getAttributeValue(0);
                    String name = clients_records_xml.getAttributeValue(2);
                    String phones = clients_records_xml.getAttributeValue(3);
                    String individualDiscount = clients_records_xml.getAttributeValue(1);
                    values_clients.put("id", id);
                    values_clients.put("name", name);
                    values_clients.put("phones", phones);
                    values_clients.put("individualDiscount", Integer.valueOf(individualDiscount));
                    db.insert(TABLE_CLIENTS, null, values_clients);
                }
                eventType = clients_records_xml.next();
            }
        }
        //Обработка ошибок работы с файлом xml
        catch (XmlPullParserException e) {
            Log.e("Test", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Test", e.getMessage(), e);
        } finally {
            //Закрываем xml файл
            clients_records_xml.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_INSTRUMENTS);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_CLIENTS);
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_ORDERS);
        onCreate(db);
    }
}
