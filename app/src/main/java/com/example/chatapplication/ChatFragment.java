package com.example.chatapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mInputeMessageView;
    private RecyclerView mMessaggesView;
    private OnFragmentInteractionListener mListener;
    private List<Message> mMessage = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private Socket socket;
    private String mParam1;
    private String mParam2;
    public ChatFragment() {
    }
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        socket.connect();
        socket.on("message",handlerIncomingMessages)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMessaggesView =view.findViewById(R.id.message);
        mMessaggesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessaggesView.setAdapter(mAdapter);
        ImageView sendButton=view.findViewById(R.id.send_Button);
        mInputeMessageView=view.findViewById(R.id.message_input);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message=mInputeMessageView.getText().toString().trim();
        mInputeMessageView.setText("");
        addMessage(message);
        JSONObject sendText=new JSONObject();
        try {
            sendText.put("text",message);
            socket.emit("message",sendText);
        }catch (JSONException e){

        }
    }
public void sendImage(String path){
        JSONObject sendData=new JSONObject();
        try {
            sendData.put("image",encodeImage(path));
            Bitmap bmp=decodeImage(sendData);
            socket.emit("message",sendData);
        }catch (JSONException e){

        }
}
 public void addMessage(String message){
        mMessage.add(new
                Message.Builder(Message.TYPE_MESSAGE).message(message).build());
        mAdapter=new MessageAdapter(mMessage);
        mAdapter.notifyItemInserted(0);
        scrollTOBottom();
    }
    public void addImage(Bitmap bmp){
        mMessage.add(new Message.
                Builder(Message.TYPE_MESSAGE).image(bmp).build());
        mAdapter=new MessageAdapter(mMessage);
        mAdapter.notifyItemInserted(0);
        scrollTOBottom();
    }

    private void scrollTOBottom() {
        mInputeMessageView.scrollToPosition(mAdapter.getItemCount()-1);
    }
    private String encodeImage (String path){
     File imageFile=new File(path);
        FileInputStream fis=null;
        try {
            fis =new FileInputStream(imageFile);
        }catch (FileNotFoundException e){

        }
        Bitmap bm= BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos =new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b,Base64.DEFAULT);

    }
    private Bitmap decodeImage(String data){
       byte [] b =Base64.decode(data,Base64.DEFAULT);
       Bitmap bmp=BitmapFactory.decodeByteArray(b,0,b.length);
       return bmp;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    {
        try {
            socket=IO.socket("http:127.0.0.1:4000");
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
