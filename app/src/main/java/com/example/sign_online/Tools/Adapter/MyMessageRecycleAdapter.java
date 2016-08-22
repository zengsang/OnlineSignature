package com.example.sign_online.Tools.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sign_online.R;

import java.util.List;


/**
 * Created by 曾志强 on 2016/3/17.
 */
public class MyMessageRecycleAdapter extends RecyclerView.Adapter<MyMessageRecycleAdapter.myViewHolder>{
    private Context context=null;
    //传输过来的发送方,文件名，备注
    private List<String> listmsg=null;

    private ReceiveBtnListener receiveBtnListener=null;


    //回调类的接口
    public interface  ReceiveBtnListener{
        public void receivebtnonclick(String filename);
    }
    public MyMessageRecycleAdapter(Context context,List<String> listmsg,ReceiveBtnListener receiveBtnListener){
        this.context=context;
        this.receiveBtnListener=receiveBtnListener;
        this.listmsg=listmsg;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=(LayoutInflater.from(context).inflate(R.layout.layoutmymessageitem,parent,false));
        return new myViewHolder(view, receiveBtnListener);

    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        position+=3*position;
        String filename=(listmsg.get(position));
        String receiveport=context.getSharedPreferences("use_for_login",Context.MODE_PRIVATE).getString("username1", null);
        filename=(filename.substring(receiveport.length()));
        holder.filenametextview.setText(filename.substring(0, filename.indexOf(".")));
        holder.remarktextview.setText(listmsg.get(position + 2));
        holder.sendporttextview.setText(listmsg.get(position+1));
    }

    @Override
    public int getItemCount() {
        return listmsg.size()/3;
    }

    //myViewHolder
    class myViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView filenametextview=null;
        private TextView sendporttextview=null;
        private TextView remarktextview=null;
        private Button receivebutton=null;
        private ReceiveBtnListener receiveBtnListener;
        public myViewHolder(View view,ReceiveBtnListener receiveBtnListener){
            super(view);
            this.receiveBtnListener=receiveBtnListener;
            filenametextview= (TextView) view.findViewById(R.id.filenametextview);
            sendporttextview= (TextView) view.findViewById(R.id.sendporttextview);
            remarktextview=(TextView)view.findViewById(R.id.remarktextview);
            receivebutton= (Button) view.findViewById(R.id.receivebutton);
            receivebutton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            receiveBtnListener.receivebtnonclick(filenametextview.getText().toString());
        }
    }


}
