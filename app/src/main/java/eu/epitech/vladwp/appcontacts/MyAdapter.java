package eu.epitech.vladwp.appcontacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public List<Model> mDataset;
    private static ClickListener clickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView mTextName;
        public TextView mTextNumber;
        public TextView mTextEmail;
        public ImageView mImageView;
        public Button buttonDelete;
        public int IdDBElement;
        View root;


        public MyViewHolder(View v) {
            super(v);
            root = v;
            mTextName = (TextView)itemView.findViewById(R.id.textName);
            mTextNumber = (TextView)itemView.findViewById(R.id.textNumber);
            mTextEmail = (TextView)itemView.findViewById(R.id.textEmail);
            mImageView = (ImageView)itemView.findViewById(R.id.imageList);
//            buttonDelete = (Button) v.findViewById(R.id.delete);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 1, 10, "Delete");
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MyAdapter.clickListener = clickListener;
    }

    public MyAdapter(Context context, List<Model> myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.mTextName.setText(mDataset.get(position).getName());
        holder.mTextNumber.setText(mDataset.get(position).getNumber());
        holder.mTextEmail.setText(mDataset.get(position).getEmail());
        if (mDataset.get(position).getImage() != null) {
            byte[] getImage = mDataset.get(position).getImage();
            Bitmap mybitmap = BitmapFactory.decodeByteArray(getImage, 0, getImage.length);
            holder.mImageView.setImageBitmap(mybitmap);
        }
        holder.IdDBElement = mDataset.get(position).getId();
//        holder.buttonDelete = (Button) holder.root.findViewById(R.id.delete);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onClick(holder, position);
            }
        });

  /*      holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.clickDelete(position, holder);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public int getIDitemRemove(int position){
        return mDataset.get(position).getId();
    }

    public void remove (int position){
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void update(int position, Model List){
        mDataset.set(position, List);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    public void updateList(List<Model> newList){
        mDataset = newList;
        notifyDataSetChanged();
    }

    public void add(int position, Model List){
        mDataset.add(position, List);
        notifyItemInserted(position);
    }

    public interface ClickListener {
        void onClick(MyViewHolder holder, int position);
//        void clickDelete(int position, MyViewHolder holder);
    }

}
