package com.storeapp.MainMenu.StoreStatus.fragments_store_status;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.storeapp.R;
import com.storeapp.parse.SoldItem;
import com.storeapp.util.Prefs;
import com.storeapp.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class FragTop10Itms extends Fragment {
    private View view;
    GridView topItemGridView;
    private BarChart mChart;
   Picasso picasso;
    int   itemsCount;

    public FragTop10Itms() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragament_top_10_items, container,
                false);


        int largeMemoryInMegaBytes = ((ActivityManager)
                getActivity().getSystemService(Context.ACTIVITY_SERVICE))
                .getLargeMemoryClass();
        int cacheSizeInBytes = (1024 * 1024 * largeMemoryInMegaBytes) / 4;


        Picasso.Builder pb = new Picasso.Builder(getActivity());
        picasso = pb.memoryCache(new LruCache(cacheSizeInBytes)).build();
        picasso.setIndicatorsEnabled(true);



        topItemGridView = (GridView) view.findViewById(R.id.top10ItemGridView);
        loadTopItems();



        mChart = (BarChart) view.findViewById(R.id.chart1);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        mChart.getAxisLeft().setDrawGridLines(false);

        // add a nice and smooth animation
        mChart.animateY(2500);

        return view;
    }

private void draw(ArrayList<BarEntry> yValues){


    ArrayList<String> xVals = new ArrayList<String>();
    for (int i = 0; i < itemsCount; i++) {
        xVals.add((int) yValues.get(i).getVal() + "");
    }

    BarDataSet set1 = new BarDataSet(yValues, "Data Set");
    set1.setColors(Utils.COLORFUL_COLORS);
    set1.setDrawValues(false);

    ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
    dataSets.add(set1);

    BarData data = new BarData(xVals, dataSets);

    mChart.setData(data);
    mChart.invalidate();
}

    private void loadTopItems() {


        ParseQuery<SoldItem> query = SoldItem.getQuery(Prefs.getBusinessCvr());
        query.orderByDescending(SoldItem.COL_AMOUNT);
        query.findInBackground(new FindCallback<SoldItem>() {
            @Override
            public void done(List<SoldItem> list, ParseException e) {

                if (e == null) {
                    if (list != null && list.size() > 0) {

                              List<SoldItem> soldItemList = new ArrayList<SoldItem>();

                            ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
                              itemsCount = Math.min(list.size(), 10);

                            for (int i = 0; i < itemsCount; i++) {
                                soldItemList.add(list.get(i));
                                yValues.add(new BarEntry(list.get(i).getAmount(), i));
                            }

                            draw(yValues);

                        TopItemsAdapter topItemsAdapter = new TopItemsAdapter(getActivity(), soldItemList);
                        topItemGridView.setAdapter(topItemsAdapter);
                    }
                }
            }
        });

    }


    public class TopItemsAdapter extends BaseAdapter {

        Context ctx;
        List<SoldItem> top10ItemsList;

        public TopItemsAdapter(Context ctx, List<SoldItem> top10ItemsList){
            this.ctx=ctx;
            this.top10ItemsList = top10ItemsList;
        }

        @Override
        public int getCount() {
            return top10ItemsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            TextView itemName = null;
            ImageView itemImage;


            if (view == null) {


                LayoutInflater layoutInflater = LayoutInflater.from(ctx);
                view = layoutInflater.inflate(R.layout.items_instock_grid_item, null);

                itemName = (TextView) view.findViewById(R.id.txtItemName);
               itemImage = (ImageView) view.findViewById(R.id.itemImage);

                ViewHolder holder = new ViewHolder();
                holder.itemDescription = itemName;
                holder.itemPicture = itemImage;

                view.setTag(holder);
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                itemName = holder.itemDescription;
                itemImage = holder.itemPicture;
            }


            SoldItem items = top10ItemsList.get(position);
            String  itemDecrip =   items.getInventoryItem().getDescription();
            itemName.setText(itemDecrip);
            itemName.setBackgroundColor(Utils.COLORFUL_COLORS[position]);
            itemImage.setImageResource(R.drawable.ic_image_white);
            ParseFile pf = items.getInventoryItem().getItemImg();
            if(pf!=null){
                picasso.load(pf.getUrl()).placeholder(R.drawable.ic_image_white).into(itemImage);
            }

            return view;
        }
    }

    public static class ViewHolder {

        public TextView itemDescription;
        public ImageView itemPicture;
    }

}
