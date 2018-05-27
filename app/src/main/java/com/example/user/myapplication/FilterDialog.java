package com.example.user.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

/**
 * Created by charanghumman on 15/05/18.
 */

public class FilterDialog extends Dialog {

    private Spinner location_spinner ;

    private Button search_btn ;

    public FilterDialog(@NonNull final Context context, int themeResId) {
        super(context, themeResId);

        setContentView(R.layout.filter_dialog_layout);

        location_spinner = findViewById(R.id.location_spinner);

        search_btn = findViewById(R.id.search_btn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.locations, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        location_spinner.setAdapter(adapter);


        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.price_range_bar);

// get min and max text view
        final TextView tvMin = (TextView) findViewById(R.id.min_price);
        final TextView tvMax = (TextView) findViewById(R.id.max_price);

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context.getClass().getSimpleName().equalsIgnoreCase("viewvenues")) {

                    viewVenues.get_filter_venue(location_spinner.getSelectedItem().toString(), rangeSeekbar.getSelectedMinValue().intValue(), rangeSeekbar.getSelectedMaxValue().intValue());

                    dismiss();
                }

                if(context.getClass().getSimpleName().equalsIgnoreCase("viewdecor")) {

                    viewDecor.get_filter_decorator(location_spinner.getSelectedItem().toString(), rangeSeekbar.getSelectedMinValue().intValue(), rangeSeekbar.getSelectedMaxValue().intValue());

                    dismiss();
                }

                if(context.getClass().getSimpleName().equalsIgnoreCase("viewCaterer")) {

                    viewCaterer.get_filter_caterer(location_spinner.getSelectedItem().toString(), rangeSeekbar.getSelectedMinValue().intValue(), rangeSeekbar.getSelectedMaxValue().intValue());

                    dismiss();
                }

                if(context.getClass().getSimpleName().equalsIgnoreCase("viewDesigners")) {

                    viewDesigners.get_filter_caterer(location_spinner.getSelectedItem().toString(), rangeSeekbar.getSelectedMinValue().intValue(), rangeSeekbar.getSelectedMaxValue().intValue());

                    dismiss();
                }

            }
        });

    }
}
