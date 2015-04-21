package com.storeapp.MainMenu.StoreStatus.fragments_store_status;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.storeapp.AppContextProvider;
import com.storeapp.MainMenu.StoreStatus.Diagrams.BarChartItem;
import com.storeapp.MainMenu.StoreStatus.Diagrams.ChartItem;
import com.storeapp.MainMenu.StoreStatus.Diagrams.LineChartItem;
import com.storeapp.R;
import com.storeapp.model.TodaysTurnoverModel;
import com.storeapp.parse.ParseDateComparing;
import com.storeapp.parse.TodaysTurnover;
import com.storeapp.util.Prefs;
import com.storeapp.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;


public class FragProfitStatus extends Fragment {
	private View view;

    Hashtable<Integer, Double> monthValues;
    List<TodaysTurnoverModel> todaysTurnoverList ;
    TextView txtCashAmount, txtCreditCardAmount, txttotalSaleAmount,txtDateOfTheday;
    ParseDateComparing pdc= new ParseDateComparing();

    int daysCount;
    int monthsCount;
    ArrayList<String> months = new ArrayList<String>();

    public FragProfitStatus() {

        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Okt");
        months.add("Nov");
        months.add("Dec");


	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {

		view = inflater.inflate(R.layout.fragmen_profit_status, container,
				false);
        getSaleOfLast30Days();

        txtCashAmount = (TextView) view.findViewById(R.id.txtCashAmount);
        txtCreditCardAmount = (TextView) view.findViewById(R.id.txtCreditCardAmount);
        txttotalSaleAmount = (TextView) view.findViewById(R.id.txttotalSaleAmount);
        txtDateOfTheday = (TextView) view.findViewById(R.id.txtDateOfTheday);


        saleOfToday();

		return view;
	}

    private void draw(){
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

               list.add(new LineChartItem(generateDataLine(), getActivity()));
               list.add(new BarChartItem(generateDataBar( ), getActivity()));

        ListView lv = (ListView) view.findViewById(R.id.listView1);

        ChartDataAdapter cda = new ChartDataAdapter(AppContextProvider.getContext(), list);
        lv.setAdapter(cda);

    }



 private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 2; // we have 2 different item-types
        }
    }


        private LineData generateDataLine() {


            ArrayList<Entry> entryCash = new ArrayList<Entry>();

            for (int i = 0; i < daysCount ; i++) {
                entryCash.add(new Entry((int) todaysTurnoverList.get(i).getCash(), i));

            }

            LineDataSet d1 = new LineDataSet(entryCash, "Cash");

            d1.setLineWidth(2.5f);
            d1.setCircleSize(4.5f);
            d1.setHighLightColor(Color.rgb(244, 117, 117));
            d1.setDrawValues(false);

            ArrayList<Entry> entryCreditCard = new ArrayList<Entry>();

            for (int i = 0; i < daysCount; i++) {
                entryCreditCard.add(new Entry( (int)todaysTurnoverList.get(i).getCreditCard(), i));
            }


            LineDataSet d2 = new LineDataSet(entryCreditCard, "Credit Card");
            d2.setLineWidth(2.5f);
            d2.setCircleSize(4.5f);
            d2.setHighLightColor(Color.rgb(244, 117, 117));
            d2.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);
            d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1]);
            d2.setDrawValues(false);


        ArrayList<Entry> entryTotal = new ArrayList<Entry>();

            for (int i = 0; i < daysCount; i++) {
                double totalSale =todaysTurnoverList.get(i).getCreditCard() +todaysTurnoverList.get(i).getCash();
                entryTotal.add(new Entry( (int) totalSale , i));
            }

            LineDataSet d3 = new LineDataSet(entryTotal, "Total");
            d3.setLineWidth(2.5f);
            d3.setCircleSize(4.5f);
            d3.setHighLightColor(Color.rgb(244, 117, 117));
            d3.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
            d3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[4]);
            d3.setDrawValues(false);

            ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
            sets.add(d1);
            sets.add(d2);
            sets.add(d3);

            LineData cd = new LineData(getDaysOfMonth(todaysTurnoverList), sets);
            return cd;
        }


        private BarData generateDataBar() {

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

            for(Integer key : monthValues.keySet()){
                double value =  monthValues.get(key);
                entries.add(new BarEntry(((int) value), key));

            }


            BarDataSet d = new BarDataSet(entries, "Months" );
            d.setBarSpacePercent(20f);
            d.setColors(ColorTemplate.VORDIPLOM_COLORS);
            d.setHighLightAlpha(255);

            BarData cd = new BarData(months, d);
            return cd;
        }

    private  void saleOfToday(){

        ParseQuery<TodaysTurnover> todaysTurnQuery = TodaysTurnover.getQuery(Prefs.getBusinessCvr());
        todaysTurnQuery.whereGreaterThanOrEqualTo (TodaysTurnover.TODAYS_TURNOVER_DATE , pdc.getFromDate() );
        todaysTurnQuery.whereLessThanOrEqualTo (TodaysTurnover.TODAYS_TURNOVER_DATE , pdc.getToDate() );
        todaysTurnQuery.findInBackground(new FindCallback<TodaysTurnover>() {
            @Override
            public void done(List<TodaysTurnover> todaysTurnovers, ParseException e) {
                if(e==null){
                    if(todaysTurnovers!=null && todaysTurnovers.size()>0){

                        TodaysTurnover tto = todaysTurnovers.get(0);
                        txtCashAmount.setText(tto.getCash() + "");
                        txtCreditCardAmount.setText(tto.getCreditCard()+"");
                        txttotalSaleAmount.setText(tto.getCash()+tto.getCreditCard()+"");
                        txtDateOfTheday.setText(Utils.getCurrentDate()+"");

                    }else{

                        txtCashAmount.setText("0");
                        txtCreditCardAmount.setText("0");
                        txttotalSaleAmount.setText("0");
                        txtDateOfTheday.setText(Utils.getCurrentDate()+"");
                    }

                }else {

                    txtCashAmount.setText("No Data Found");
                    txtCreditCardAmount.setText("No Data Found");
                    txttotalSaleAmount.setText("No Data Found");
                    txtDateOfTheday.setText(Utils.getCurrentDate()+"");
                }
            }
        });

    }


    private void getSaleOfLast12Month(){

        ParseQuery<TodaysTurnover> query = TodaysTurnover.getQuery(Prefs.getBusinessCvr());
        query.findInBackground(new FindCallback<TodaysTurnover>() {
            @Override
            public void done(List<TodaysTurnover> list, ParseException e) {

                if (e == null) {
                    if (list != null && list.size() > 0) {
                        monthsCount = Math.min(list.size(), 12);
                        monthValues = new Hashtable<Integer, Double>();
                        List<Integer> monthsList = new ArrayList<Integer>();
                        Calendar cal = Calendar.getInstance();

                        for (int i = 0; i < monthsCount; i++) {

                            TodaysTurnover tt = list.get(i);

                            TodaysTurnoverModel tdm = new TodaysTurnoverModel();
                            tdm.setCash(tt.getCash());
                            tdm.setCreditCard(tt.getCreditCard());
                            tdm.setTotalSale(tt.getCreditCard() + tt.getCash());

                            cal.setTime(list.get(i).getDate());
                            int month = cal.get(Calendar.MONTH);

                            double total =  tt.getCreditCard() + tt.getCash();
                            if (monthValues.containsKey(month)) {
                                total =  monthValues.get(month) + total;
                            }
                           monthValues.put(month, total);
                            draw();
                        }
                    }
                }
            }
        });
    }




    private ArrayList<String> getDaysOfMonth(List<TodaysTurnoverModel> todaysTurnoverList) {

        ArrayList<String> dm = new ArrayList<String>();

        for(int i = 0; i<daysCount; i++){
            dm.add(todaysTurnoverList.get(i).getDayOfMonth()+"");

        }

        return dm;

    }


    private void getSaleOfLast30Days(){

        todaysTurnoverList = new ArrayList<TodaysTurnoverModel>();
        ParseQuery<TodaysTurnover> query = TodaysTurnover.getQuery(Prefs.getBusinessCvr());
        query.orderByAscending(TodaysTurnover.TODAYS_TURNOVER_DATE);
        query.findInBackground(new FindCallback<TodaysTurnover>() {
            @Override
            public void done(List<TodaysTurnover> list, ParseException e) {

                if (e == null) {
                    if (list != null && list.size() > 0) {

                        Calendar cal = Calendar.getInstance();
                        daysCount = Math.min(list.size(), 31);

                        for(int i = 0; i<daysCount;i++){
                            TodaysTurnoverModel tdm = new TodaysTurnoverModel();
                            tdm.setCash(list.get(i).getCash());
                            tdm.setCreditCard(list.get(i).getCreditCard());
                            tdm.setTotalSale(list.get(i).getCreditCard()+list.get(i).getCash());

                            cal.setTime(list.get(i).getDate());
                            tdm.setDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
                            todaysTurnoverList.add(tdm);
                        }
                        getSaleOfLast12Month();


                    }
                }
            }
        });


    }


}

