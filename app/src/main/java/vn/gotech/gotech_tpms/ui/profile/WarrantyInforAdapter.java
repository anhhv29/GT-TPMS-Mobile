package vn.gotech.gotech_tpms.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gotech_tpms.R;

import java.util.List;

import vn.gotech.gotech_tpms.base.response.CheckDetails;

public class WarrantyInforAdapter extends RecyclerView.Adapter<WarrantyInforAdapter.WarrantyViewHolder> {
    private List<CheckDetails> listCheckDetails;
    private Context mContext;

    public WarrantyInforAdapter(Context mContext, List<CheckDetails> listCheckDetails) {
        this.mContext = mContext;
        this.listCheckDetails = listCheckDetails;
    }

    @NonNull
    @Override
    public WarrantyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.item_warranty_infor, parent, false);
        return new WarrantyViewHolder(heroView);
    }

    @Override
    public void onBindViewHolder(@NonNull WarrantyViewHolder holder, int position) {
        CheckDetails checkDetails = listCheckDetails.get(position);
        if (checkDetails.getHanbaohanh() != null)
            holder.tvThoiGian.setText(checkDetails.getHanbaohanh());
        else holder.tvThoiGian.setText("");
        if (checkDetails.getMabaohanh() != null)
            holder.tvMaASL.setText(checkDetails.getMabaohanh());
        else holder.tvMaASL.setText("");
    }

    @Override
    public int getItemCount() {
        return listCheckDetails.size();
    }

    public class WarrantyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMaASL;
        public TextView tvThoiGian;

        public WarrantyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaASL = itemView.findViewById(R.id.textView_ma_asl);
            tvThoiGian = itemView.findViewById(R.id.textView_thoi_gian);
        }
    }
}
