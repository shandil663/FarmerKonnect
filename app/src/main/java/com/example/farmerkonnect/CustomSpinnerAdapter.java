package com.example.farmerkonnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> dataList;

    // Constructor
    public CustomSpinnerAdapter(@NonNull Context context, List<String> dataList) {
        super(context, 0, dataList); // Note: 0 as resource ID since we use a custom layout
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent); // This handles the selected item view
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent); // Here, we populate the dropdown
    }

    // Helper method to reuse code for both the selected view and dropdown views
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_spinner_dropdown_item, parent, false);
        }

        String itemText = getItem(position);

        TextView textView = convertView.findViewById(R.id.text1);
        textView.setText(itemText);

        // Assuming you have an ImageView with id 'icon' in your custom_spinner_dropdown_item.xml
        ImageView imageView = convertView.findViewById(R.id.home);
        // ... Set the image for the icon. You might need additional data for this ...

        return convertView;
    }
}
