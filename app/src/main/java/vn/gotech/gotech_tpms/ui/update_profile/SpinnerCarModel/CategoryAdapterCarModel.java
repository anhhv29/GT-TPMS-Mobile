package vn.gotech.gotech_tpms.ui.update_profile.SpinnerCarModel;

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

public class CategoryAdapterCarModel extends ArrayAdapter<CategoryCarModel> {
    public CategoryAdapterCarModel(@NonNull Context context, int resource, @NonNull List<CategoryCarModel> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_car_model, parent, false);
        TextView tvSelectedCarModel = convertView.findViewById(R.id.tv_selected_car_model);
        CategoryCarModel categoryCarModel = this.getItem(position);
        if (categoryCarModel != null) {
            tvSelectedCarModel.setText(categoryCarModel.getCarmodel());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_car_model, parent, false);
        TextView tvCategoryCarModel = convertView.findViewById(R.id.tv_category_car_model);
        CategoryCarModel categoryCarModel = this.getItem(position);
        if (categoryCarModel != null) {
            tvCategoryCarModel.setText(categoryCarModel.getCarmodel());
        }
        return convertView;
    }
}
