package com.example.elena.sportshop;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String clr= "";
    String brnd="";
    String ctgr="";
    String sprt= "";
    String shp="";
    int cost=0;

    DBHelper dbh;

    SQLiteDatabase db;
    SimpleCursorAdapter simpleCursorAdapter;

    ListView lvShowClothes;
    Cursor c;

    //rec view

    RecyclerView rvShowItems;
    ItemAdapter adapter;

    private static final int CM_PURCHASE_ID = 1;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.shop_item, container, false);
//        lvShowClothes= (ListView)v.findViewById(R.id.lvShowClothes);
//        registerForContextMenu(lvShowClothes);



        savedInstanceState= getArguments();
        if(savedInstanceState!= null)
        {
            clr= savedInstanceState.getString("color");
            brnd= savedInstanceState.getString("brand");
            ctgr= savedInstanceState.getString("category");
            sprt= savedInstanceState.getString("sport");
            shp= savedInstanceState.getString("shop");
            cost= savedInstanceState.getInt("cost");

        }

        dbh = new DBHelper(getContext());
        db = dbh.getReadableDatabase();

//        String sqlQuery = "SELECT clothes._id, clothes.name, clothes.cost, clothes.count, "
//                    + "colors.colorName, colors.idc, "
//
//                    + "brands.brandName, brands.idb, "
//                    + "categories.categoryName, categories.category_id, "
//                    + "sport.sportName, sport.ids, "
//
//
//                    + "shops.shopName, shops.idsps, "
//                    + "shops.shopAddress, shops.shopPhone "
//                    + "FROM clothes, colors, brands, categories, shops, sport "
//                    + "WHERE clothes.colorId=colors.idc "
//
//                    + "AND clothes.brandId= brands.idb "
//                    + "AND clothes.categoryId= categories.category_id "
//                    + "AND clothes.sportId= sport.ids "
//
//                    + "AND clothes.shopId= shops.idsps "
//
//                    + "AND colors.colorName LIKE '%"+clr  +"' "
//                    + "AND brands.brandName LIKE '%"+brnd+ "' "
//                    + "AND categories.categoryName LIKE '%"+ctgr+ "' "
//                    + "AND sport.sportName LIKE '%"+sprt+"' "
//                    + "AND shops.shopName LIKE '%"+shp+"' "
//
//                    +"AND clothes.cost> "+ cost;
//
//        c = db.rawQuery(sqlQuery, null);

//        String from[] = {"name", "colorName", "count", "brandName", "categoryName", "sportName", "cost", "shopName",
//                "shopAddress", "shopPhone"};
//        int to[] = {R.id.tvItem, R.id.tvColor, R.id.tvCount, R.id.tvBrandItem, R.id.tvCatItem, R.id.tvSportItem,
//                R.id.tvCostItem, R.id.tvShopItem, R.id.tvShopAddressItem, R.id.tvShopPhoneItem};
//
//        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.clothes_item, null,
//                from, to, 0);

//        lvShowClothes.setAdapter(simpleCursorAdapter);

        rvShowItems= (RecyclerView)v.findViewById(R.id.rvShowItems);
        rvShowItems.setHasFixedSize(true);
        rvShowItems.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter= new ItemAdapter(getActivity(), null);
        rvShowItems.setAdapter(adapter);


        registerForContextMenu(rvShowItems);

        getLoaderManager().initLoader(0, null, this);

//        lvShowClothes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + id, Toast.LENGTH_SHORT).show();
//            }
//        });



        return v;
    }

//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, CM_PURCHASE_ID, 0, R.string.purchase);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getItemId()== CM_PURCHASE_ID)
        {

            int id= ((ItemAdapter)rvShowItems.getAdapter()).getId();

            Toast.makeText(getContext(), "id "+ id, Toast.LENGTH_SHORT).show();
            //AdapterView.AdapterContextMenuInfo id= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

            //Toast.makeText(getContext(), ""+ acmi.id, Toast.LENGTH_SHORT).show();
            int count=-1;

            String countQuery= "SELECT clothes.count FROM clothes WHERE clothes._id= "+ id;
            c= db.rawQuery(countQuery, null);

            if(c.moveToFirst())
                count= c.getInt(c.getColumnIndex("count"));
            Log.d("tag", "count= "+ count );

            count--;

            if(count==0)
                db.delete("clothes", "_id" + " = " + id, null);
            else if(count>0)
            {
                String sql= "UPDATE clothes SET count="+ count+" WHERE _id= "+id;
                db.execSQL(sql);
            }


          getLoaderManager().getLoader(0).forceLoad();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbh.close();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getContext(), db, clr, brnd, ctgr, sprt, shp, cost);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
        //simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private static class MyCursorLoader extends CursorLoader {

        SQLiteDatabase db;
        String clr= "";
        String brnd="";
        String ctgr="";
        String sprt= "";
        String shp="";
        int cost=0;

        public MyCursorLoader(Context context, SQLiteDatabase db, String clr, String brnd,
                              String ctgr, String sprt, String shp, int cost) {
            super(context);
            this.db = db;
            this.clr= clr;
            this.brnd= brnd;
            this.ctgr= ctgr;
            this.sprt= sprt;
            this.shp= shp;
            this.cost= cost;
        }

        @Override
        public Cursor loadInBackground() {

            String sqlQuery = "SELECT clothes._id, clothes.name, clothes.cost, clothes.count, "
                + "colors.colorName, colors.idc, "

                + "brands.brandName, brands.idb, "
                + "categories.categoryName, categories.category_id, "
                + "sport.sportName, sport.ids, "


                + "shops.shopName, shops.idsps, "
                + "shops.shopAddress, shops.shopPhone "
                + "FROM clothes, colors, brands, categories, shops, sport "
                + "WHERE clothes.colorId=colors.idc "

                + "AND clothes.brandId= brands.idb "
                + "AND clothes.categoryId= categories.category_id "
                + "AND clothes.sportId= sport.ids "

                + "AND clothes.shopId= shops.idsps "

                + "AND colors.colorName LIKE '%"+clr  +"' "
                + "AND brands.brandName LIKE '%"+brnd+ "' "
                + "AND categories.categoryName LIKE '%"+ctgr+ "' "
                + "AND sport.sportName LIKE '%"+sprt+"' "
                + "AND shops.shopName LIKE '%"+shp+"' "

                +"AND clothes.cost> "+ cost;
             Cursor cursor = db.rawQuery(sqlQuery, null);

            return cursor;
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        TextView tvItem;
        TextView tvColor;
        TextView tvCount;
        TextView tvBrandItem;
        TextView tvCatItem;
        TextView tvSportItem;
        TextView tvCostItem;
        TextView tvShopItem;
        TextView tvShopAddressItem;
        TextView tvShopPhoneItem;

        public ItemHolder(View itemView) {
            super(itemView);

            tvItem= (TextView)itemView.findViewById(R.id.tvItem);
            tvColor= (TextView)itemView.findViewById(R.id.tvColor);
            tvCount= (TextView)itemView.findViewById(R.id.tvCount);
            tvBrandItem= (TextView)itemView.findViewById(R.id.tvBrandItem);
            tvCatItem= (TextView)itemView.findViewById(R.id.tvCatItem);
            tvSportItem= (TextView)itemView.findViewById(R.id.tvSportItem);
            tvCostItem= (TextView)itemView.findViewById(R.id.tvCostItem);
            tvShopItem= (TextView)itemView.findViewById(R.id.tvShopItem);
            tvShopAddressItem= (TextView)itemView.findViewById(R.id.tvShopAddressItem);
            tvShopPhoneItem= (TextView)itemView.findViewById(R.id.tvShopPhoneItem);

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(0, CM_PURCHASE_ID, 0, R.string.purchase);
                }
            });


        }

    }

    public  class ItemAdapter extends RecyclerView.Adapter<ItemHolder>
    {
        Cursor dataCursor;
        Context context;

        private int itemId;

        public int getId()
        {
            return itemId;
        }

        public void setID(int itemId) {
            this.itemId = itemId;
        }

        public ItemAdapter(Activity mContext, Cursor cursor) {

            dataCursor = cursor;
            context = mContext;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_item,
                                                        parent, false);
            return new ItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ItemHolder holder, final int position) {

            dataCursor.moveToPosition(position);
            String name= dataCursor.getString(dataCursor.getColumnIndex("name"));
            String color= dataCursor.getString(dataCursor.getColumnIndex("colorName"));
            int count= dataCursor.getInt(dataCursor.getColumnIndex("count"));
            String brand= dataCursor.getString(dataCursor.getColumnIndex("brandName"));
            String category= dataCursor.getString(dataCursor.getColumnIndex("categoryName"));
            String sport= dataCursor.getString(dataCursor.getColumnIndex("sportName"));
            int cost= dataCursor.getInt(dataCursor.getColumnIndex("cost"));
            String shop= dataCursor.getString(dataCursor.getColumnIndex("shopName"));
            String shopAddress= dataCursor.getString(dataCursor.getColumnIndex("shopAddress"));
            String shopPhone= dataCursor.getString(dataCursor.getColumnIndex("shopPhone"));

            holder.tvItem.setText(name);
            holder.tvColor.setText(color);
            holder.tvCount.setText(""+count);
            holder.tvBrandItem.setText(brand);
            holder.tvCatItem.setText(category);
            holder.tvSportItem.setText(sport);
            holder.tvCostItem.setText(""+ cost);
            holder.tvShopItem.setText(shop);
            holder.tvShopAddressItem.setText(shopAddress);
            holder.tvShopPhoneItem.setText(shopPhone);


            //int id= dataCursor.getInt(dataCursor.getColumnIndex("_id"));

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    dataCursor.moveToPosition(position);
                    int id= dataCursor.getInt(dataCursor.getColumnIndex("_id"));

                   // Toast.makeText(getContext(), "id "+ id, Toast.LENGTH_SHORT).show();
                    setID(id);

                    return false;
                }
            });

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    dataCursor.moveToPosition(position);
//                    int id= dataCursor.getInt(dataCursor.getColumnIndex("_id"));
//
//                    Toast.makeText(getContext(), "id "+ id, Toast.LENGTH_SHORT).show();
//                }
//            });
       }

        public Cursor swapCursor(Cursor cursor) {
            if (dataCursor == cursor) {
                return null;
            }
            Cursor oldCursor = dataCursor;
            this.dataCursor = cursor;
            if (cursor != null) {
                this.notifyDataSetChanged();
            }
            return oldCursor;
        }

        @Override
        public int getItemCount() {

            return (dataCursor == null) ? 0 : dataCursor.getCount();
        }
    }

}
