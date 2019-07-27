package com.example.chatapplication;

import android.graphics.Bitmap;

public class Message {
    public static final int TYPE_MESSAGE=0;
    public static final int TYPE_LOG=1;
    public static final int TYPE_ACTION=2;
    private int mType;
    private String mMessage;
    private Bitmap mImage;

    public Message() {
    }

    public int getmType() {
        return mType;
    }

    public String getmMessage() {
        return mMessage;
    }

    public Bitmap getmImage() {
        return mImage;
    }
    public static class Builder {
        private int mType;
        private String mMessage;
        private Bitmap mImage;

        public Builder(int mType) {
            this.mType = mType;
        }

        public Builder message(String mMessage) {
            this.mMessage = mMessage;
           return this;
        }

        public Builder  image(Bitmap mImage) {
            this.mImage = mImage;
            return this;
        }
        public Message build(){
            Message message=new Message();
            message.mImage=mImage;
            message.mMessage=mMessage;
            message.mType=mType;
            return message;

        }
    }


}
