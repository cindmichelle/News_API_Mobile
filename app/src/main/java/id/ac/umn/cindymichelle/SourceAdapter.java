package id.ac.umn.cindymichelle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceHolder>{

    private Context context;
    private ArrayList<Source> sources;

    public SourceAdapter() {
    }

    public SourceAdapter(Context context, ArrayList<Source> sources) {
        this.context = context;
        this.sources = sources;
    }

    @Override
    public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.source_row, parent, false);
        return new SourceHolder(view);
    }

    @Override
    public void onBindViewHolder(SourceHolder holder, int position) {
        Source source = sources.get(position);
        holder.setDetails(source);
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public class SourceHolder extends RecyclerView.ViewHolder {
        private TextView txtSource, txtDesc;

        public SourceHolder(final View itemView) {
            super(itemView);
            txtSource = itemView.findViewById(R.id.txtSource);
            txtDesc = itemView.findViewById(R.id.txtDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(itemView.getContext(),Integer.toString(position), Toast.LENGTH_SHORT).show();

                int position = getAdapterPosition();

                    Intent intent = new Intent(itemView.getContext(), NewsListActivity.class);
                    intent.putExtra("sourceId", sources.get(position).getId());
                    intent.putExtra("sourceName", sources.get(position).getName());

                    view.getContext().startActivity(intent);

                }
            });
        }

        public void setDetails(Source source){
            txtSource.setText(source.getName());
            txtDesc.setText(source.getDescription());
        }
    }
}
