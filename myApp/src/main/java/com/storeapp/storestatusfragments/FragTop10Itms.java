package com.storeapp.storestatusfragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.Activities.parse.SoldItem;
import com.storeapp.R;

import java.util.ArrayList;
import java.util.List;

public class FragTop10Itms extends Fragment {
    private View view;
    GridView top10ItemGridView;
    private BarChart mChart;
   //
   // private SeekBar mSeekBarX, mSeekBarY;
   // private TextView tvX, tvY;

    public FragTop10Itms() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragament_top_10_items, container,
                false);

        top10ItemGridView = (GridView) view.findViewById(R.id.top10ItemGridView);
        loadTop10Items();

        ///////////////////////////////
      //  tvX = (TextView) view.findViewById(R.id.tvXMax);
      //  tvY = (TextView) view.findViewById(R.id.tvYMax);

       // mSeekBarX = (SeekBar) view.findViewById(R.id.seekBar1);
      //  mSeekBarX.setOnSeekBarChangeListener(this);

       // mSeekBarY = (SeekBar) view.findViewById(R.id.seekBar2);
       // mSeekBarY.setOnSeekBarChangeListener(this);

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

        // setting data
//        mSeekBarX.setProgress(10);
 //       mSeekBarY.setProgress(100);

        // add a nice and smooth animation
        mChart.animateY(3500);

        //mChart.getLegend().setEnabled(false);

        ////////////////////////////////
       // draw();

        return view;
    }










    /////////////////////////////////////////////////////

   // List<SoldItem> top10ItemsList = new ArrayList<SoldItem>();

private void draw(List<SoldItem> top10ItemsList){


   // tvX.setText("" + (10 + 1));
    //tvY.setText("" + (100));

    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

    for (int i = 0; i < 10 + 1; i++) {
        float mult = (100 + 1);
         float val1 = (float) (Math.random() * mult) + mult;
        //    int a = top10ItemsList.get(i).getAmount();
       // float val1 = (float) a;
        yVals1.add(new BarEntry((int) val1, i));
    }

    ArrayList<String> xVals = new ArrayList<String>();
    for (int i = 0; i < 10 + 1; i++) {
        xVals.add((int) yVals1.get(i).getVal() + "");
    }

    BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
    set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
    set1.setDrawValues(false);

    ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
    dataSets.add(set1);

    BarData data = new BarData(xVals, dataSets);

    mChart.setData(data);
    mChart.invalidate();
}


    /////////////////////////////////////////////////////

    private void loadTop10Items() {


        ParseQuery<SoldItem> query = SoldItem.getQuery();
        query.addDescendingOrder("amount");
        query.findInBackground(new FindCallback<SoldItem>() {
            @Override
            public void done(List<SoldItem> list, ParseException e) {
                if (e == null) {

                    if (list != null && list.size() > 0) {
                        List<SoldItem> top10ItemsList = new ArrayList<SoldItem>();

                        for (int i = 0; i <= 9; i++) {

                            top10ItemsList.add(list.get(i));
                        }
                        draw(top10ItemsList);
                        Top10ItemsAdapter top10ItemsAdapter = new Top10ItemsAdapter(getActivity(), top10ItemsList);
                        top10ItemGridView.setAdapter(top10ItemsAdapter);

                    }
                }
            }
        });


    }


    public class Top10ItemsAdapter extends BaseAdapter {

        Context ctx;
        List<SoldItem> top10ItemsList;






        public Top10ItemsAdapter(Context ctx, List<SoldItem> top10ItemsList){
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



            if (view == null) {


                LayoutInflater layoutInflater = LayoutInflater.from(ctx);
                view = layoutInflater.inflate(R.layout.items_instock_grid_item, null);

                itemName = (TextView) view.findViewById(R.id.txtItemName);
              //  itemImage = (ImageView) view.findViewById(R.id.itemImage);

                ViewHolder holder = new ViewHolder();
                holder.itemDescription = itemName;
              //  holder.itemPicture = itemImage;


                view.setTag(holder);
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                itemName = holder.itemDescription;
              //  itemImage = holder.itemPicture;
            }


            SoldItem items = top10ItemsList.get(position);

            itemName.setText(items.getBarcode());


            return view;
        }
    }

    public static class ViewHolder {

        public TextView itemDescription;
       // public ImageView itemPicture;


    }

}
