package vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gotech_tpms.R;

import java.util.List;

public class CategoryAdapterCarName extends ArrayAdapter<CategoryCarName> {
    public CategoryAdapterCarName(@NonNull Context context, int resource, @NonNull List<CategoryCarName> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_car_name, parent, false);
        TextView tvSelectedCarName = convertView.findViewById(R.id.tv_selected_car_name);
        CategoryCarName categoryCarName = this.getItem(position);
        if (categoryCarName != null) {
            tvSelectedCarName.setText(categoryCarName.getCarName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_car_name, parent, false);
        TextView tvCategoryCarName = convertView.findViewById(R.id.tv_category_car_name);
        CategoryCarName categoryCarName = this.getItem(position);
        if (categoryCarName != null) {
            tvCategoryCarName.setText(categoryCarName.getCarName());
        }
        return convertView;
    }
}
