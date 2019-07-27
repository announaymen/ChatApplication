package com.example.chatapplication;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mMessages;
    private int[] mUsernameColors;

    public MessageAdapter(List<Message> mMessages) {
        this.mMessages = mMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_message,parent,false);
       return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getmType();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
         Message message=mMessages.get(position);
         viewHolder.setMessage(message.getmMessage());
         viewHolder.setImage(message.getmImage());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       public ImageView mImageView;
       public TextView mMessageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView=(ImageView)itemView.findViewById(R.id.image);
            mMessageView=itemView.findViewById(R.id.message);
        }
        public void setMessage(String message){
            if((mMessageView==null)||(message==null)){
                return;
            }
            mMessageView.setText(message);

        }
        public void setImage(Bitmap bmp){
            if((mImageView==null)||(bmp==null)){
                return;
            }
            mImageView.setImageBitmap(bmp);
        }
        private int getUsernameColor(String username){
            int hash=8;
            for(int i=0,len=username.length();i<len;i++){
                hash=username.codePointAt(i)+(hash<<5)-hash;
            }
            int index=Math.abs(hash%mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
