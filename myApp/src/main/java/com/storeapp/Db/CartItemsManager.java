package com.storeapp.Db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.storeapp.model.CartItem;
import com.storeapp.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CartItemsManager {

    public static final String TABLE_NAME = "tbl_cart_items";
    public static final String COL_ID = BaseColumns._ID;
    public static final String COL_PARSE_INVENTORY_ITEM_ID = "col_parse_inventory_item_id";
    public static final String COL_BARCODE = "col_barcode";
    public static final String COL_DESCRIPTION = "col_description";
    public static final String COL_AMOUNT = "col_amount";
    public static final String COL_SELL_PRICE = "col_sell_price";


    private SQLiteOpenHelper dbHelper;
    private ContentValues values;

    public CartItemsManager(SQLiteOpenHelper dbHelper) {

        this.dbHelper = dbHelper;
        this.values = new ContentValues();

    }

    public void onCreate(SQLiteDatabase db) {

        String sql = String
                .format("create table %s(%s integer primary key autoincrement, %s text not null, %s text not null, " +
                                " %s text not null,  %s integer not null,  %s real not null)",
                        TABLE_NAME, COL_ID, COL_PARSE_INVENTORY_ITEM_ID, COL_BARCODE, COL_DESCRIPTION,
                        COL_AMOUNT, COL_SELL_PRICE);

        db.execSQL(sql);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int addItemToCart(CartItem cartItem) {

        int rowId = -1;
        SQLiteDatabase db = null;

        try {

            values.clear();
            values.put(COL_PARSE_INVENTORY_ITEM_ID, cartItem.getParseIventoryItemId());
            values.put(COL_BARCODE, cartItem.getBarcode());
            values.put(COL_DESCRIPTION, cartItem.getDescription());
            values.put(COL_AMOUNT, cartItem.getAmount());
            values.put(COL_SELL_PRICE, cartItem.getSellPrice());

            db = dbHelper.getWritableDatabase();

            rowId = (int) db.insert(TABLE_NAME, null, values);

        } catch (Exception e) {
            String msg = e.getMessage();
        } finally {
            Utils.closeDb(db);
        }

        return rowId;
    }

    public void clearCart() {

        SQLiteDatabase db = null;

        try {

            String sql = "delete from " + TABLE_NAME;
            db = dbHelper.getWritableDatabase();

            db.execSQL(sql);

        } finally {
            Utils.closeDb(db);
        }
    }

    public Cursor getItemsInCart() {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {

            String sql = String.format("select * from %s",
                    TABLE_NAME);

            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(sql, null);

        } catch (Exception e) {
            String msg = e.getMessage();
        } finally {
        }

        return cursor;

    }

    public int getItmCount(String barcode) {
        int itmCount = 0;
        Cursor cursor = null;
        SQLiteDatabase db = null;

        try {

            String where = COL_BARCODE + " = " + barcode;

            db = dbHelper.getReadableDatabase();
            String[] columns = {COL_AMOUNT};
            cursor = db.query(TABLE_NAME, columns, where, null, null, null,
                    null);
            if (cursor.moveToNext()) {
                int colmIndex = cursor.getColumnIndex(COL_AMOUNT);
                itmCount = cursor.getInt(colmIndex);
            }

        } catch (Exception e) {
            String msg = e.getMessage();
        } finally {
            Utils.closeCursor(cursor);
            Utils.closeDb(db);

        }

        return itmCount;
    }

    public int updateCartItemAmount(String barcode, int amount) {

        int rowsAcffected = -1;
        SQLiteDatabase db = null;
        int currentAmount = getItmCount(barcode);

        try {

            String where = COL_BARCODE + "=" + barcode;

            values.clear();
            values.put(COL_AMOUNT, amount + currentAmount);
            db = dbHelper.getWritableDatabase();

            rowsAcffected = (int) db.update(TABLE_NAME, values, where, null);

        } catch (Exception e) {
            String msg = e.getMessage();
        } finally {
            Utils.closeDb(db);
        }

        return rowsAcffected;

    }

    public int deleteItem(int itemId) {

        int rowsAcffected = -1;
        SQLiteDatabase db = null;

        try {

            String where = COL_ID + "=" + itemId;

            values.clear();
            db = dbHelper.getWritableDatabase();

            rowsAcffected = (int) db.delete(TABLE_NAME, where, null);

        } catch (Exception e) {
            String msg = e.getMessage();
        } finally {
            Utils.closeDb(db);
        }

        return rowsAcffected;

    }

    public boolean itemExists(String barcode) {

        boolean itemExists = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            String where = COL_BARCODE + " = " + barcode;
            db = dbHelper.getReadableDatabase();
            cursor = db.query(TABLE_NAME, null, where, null, null, null, null);

            itemExists = cursor.moveToFirst();

        } catch (Exception e) {
            String msg = e.getMessage();
        } finally {
            Utils.closeDb(db);
            Utils.closeCursor(cursor);

        }

        return itemExists;

    }

    public List<CartItem> getAllItemsInCart() {

        List<CartItem> allItemsInCart = new ArrayList<CartItem>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {

            db = dbHelper.getReadableDatabase();
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            int colParseInventoryItemIdIndex = cursor.getColumnIndex(COL_PARSE_INVENTORY_ITEM_ID);
            int colBarcodeIndex = cursor.getColumnIndex(COL_BARCODE);
            int colDescriptionIndex = cursor.getColumnIndex(COL_DESCRIPTION);
            int colAmountIndex = cursor.getColumnIndex(COL_AMOUNT);
            int colSellPriceIndex = cursor.getColumnIndex(COL_SELL_PRICE);

            while (cursor.moveToNext()) {
                CartItem cartItem = new CartItem();
                cartItem.setParseIventoryItemId(cursor.getString(colParseInventoryItemIdIndex));
                cartItem.setBarcode(cursor.getString(colBarcodeIndex));
                cartItem.setDescription(cursor.getString(colDescriptionIndex));
                cartItem.setAmount(cursor.getInt(colAmountIndex));
                cartItem.setSellPrice(cursor.getDouble(colSellPriceIndex));

                allItemsInCart.add(cartItem);
            }

        } catch (Exception e) {
            String msg = e.getMessage();
        } finally {
            Utils.closeDb(db);
            Utils.closeCursor(cursor);
        }


        return allItemsInCart;

    }



}
