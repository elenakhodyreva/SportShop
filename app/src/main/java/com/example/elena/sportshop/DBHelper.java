package com.example.elena.sportshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    // данные для таблицы цветов
    private String[] colorNames = { "Красный", "Оранжевый", "Желтый", "Зеленый", "Синий", "Сиреневый",
            "Коричневый", "Серый", "Черный" };

    private String [] brandNames= {"adidas", "nike", "reebok", "asics", "saucony", "garmin", "polar"};
    private String [] categoryNames={"одежда", "обувь", "аксессуары"};
    private String [] sportNames={"бег", "футбол", "фитнес", "гимнастика", "скейтборд"};

    private String[] shopNames ={"tandem", "mega", "park house"};
    private String [] shopAddresses= {"ibragimova 56", "pobedy pr", "yamasheva"};
    private String [] shopPhones ={"11111", "22222", "33333"};


    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        // создаем таблицу цветов
        db.execSQL("create table colors ("
                + "idc integer primary key autoincrement,"
                + "colorName text"
                + ");");

        // заполняем ее
        for (int i = 0; i < colorNames.length; i++) {
            cv.clear();
            cv.put("colorName", colorNames[i]);

            db.insert("colors", null, cv);
        }

        // brand table
        db.execSQL("create table brands ("
                + "idb integer primary key autoincrement,"
                +"brandName text"
                +");");

        for (int i = 0; i < brandNames.length; i++) {
            cv.clear();
            cv.put("brandName", brandNames[i]);

            db.insert("brands", null, cv);
        }

        // category table
        db.execSQL("create table categories ("
                + "category_id integer primary key autoincrement,"
                + "categoryName text"
                + ");");

        for (int i = 0; i < categoryNames.length; i++) {
            cv.clear();
            cv.put("categoryName", categoryNames[i]);

            db.insert("categories", null, cv);
        }

        // sport table
        db.execSQL("create table sport ("
                + "ids integer primary key autoincrement,"
                + "sportName text"
                + ");");

        for (int i = 0; i < sportNames.length; i++) {
            cv.clear();
            cv.put("sportName", sportNames[i]);

            db.insert("sport", null, cv);
        }

        // shops table
        db.execSQL("create table shops ("
                + "idsps integer primary key autoincrement,"
                + "shopName text,"
                + "shopAddress text,"
                + "shopPhone text"
                + ");");

        for (int i = 0; i < shopNames.length; i++) {
            cv.clear();
            cv.put("shopName", shopNames[i]);
            cv.put("shopAddress", shopAddresses[i]);
            cv.put("shopPhone", shopPhones[i]);

            db.insert("shops", null, cv);
        }

        // создаем таблицу товаров
        db.execSQL("create table clothes ("
                + "_id integer primary key autoincrement,"
                + "name text,"
                + "colorId integer,"
                + "brandId integer,"
                + "categoryId integer,"
                + "sportId integer,"
                + "shopId integer,"
                + "cost integer,"
                + "count integer"
                + ");");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
