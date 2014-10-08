package rs.sourcecode.loyaltyqr.util;

import java.util.ArrayList;

import rs.sourcecode.loyaltyqr.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GiftsAdapter extends BaseAdapter{

	ArrayList<Gift> myList = new ArrayList<Gift>();
    LayoutInflater inflater;
    Context context;
    
    public GiftsAdapter(Context context, ArrayList<Gift> myList) {
    	this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class MyViewHolder {
		TextView name, price;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder mViewHolder;        
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.gifts_layout, null);
            mViewHolder = new MyViewHolder();
            mViewHolder.name = (TextView) convertView.findViewById(R.id.textViewName);
            mViewHolder.price = (TextView) convertView.findViewById(R.id.textViewPrice1);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        Gift g = myList.get(position);
        mViewHolder.name.setText(g.name);
        mViewHolder.price.setText(""+g.price+" qrl koda");
        
        
        
        return convertView;
	}

}
