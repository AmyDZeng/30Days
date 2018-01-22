package com.days.a30.a30days;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Malzberry on 12/16/2017.
 * Adapter for our main recyclerview
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    public interface ChallengeListener {
        void onChallengeClicked(Challenge challenge);
        void onChallengeLongClick(Challenge challenge);
    }

    ArrayList<Challenge> mData = new ArrayList<>();
    ChallengeListener mListener;

    public RVAdapter(ArrayList<Challenge> data, ChallengeListener listener) {
        mData = data;
        mListener = listener;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Challenge challenge = mData.get(position);
        holder.mDayCountTV.setText("Day " + challenge.getDayCount());
        holder.mChallengeName.setText(challenge.mName);
        holder.mChallengeDesc.setText(challenge.mDesc);

        boolean completedForToday = challenge.completeForToday();

        // set button conditions
        holder.mButton.setEnabled(!completedForToday);
        if (challenge.failedChallenge()) {
            holder.mButton.setText("Challenge Failed! Try again :(");
        } else if (completedForToday) {
            holder.mButton.setText("Challenge completed for today!");
        } else {
            holder.mButton.setText("Tap here to complete challenge!");
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // only available if not complete
                    if (v.getId() == R.id.button) {
                        v.setEnabled(false);
                        ((Button)v).setText("Challenge completed for today!");
                        if (mListener != null) mListener.onChallengeClicked(challenge);
                    }

                }
            });
        }

        holder.itemView.setLongClickable(true);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onChallengeLongClick(challenge);
                return true;
            }
        });
    }

    public void removeChallenge(Challenge challenge) {
        mData.remove(challenge);
        notifyDataSetChanged();
    }

    public void addChallenge(Challenge challenge) {
        mData.add(challenge);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDayCountTV;
        TextView mChallengeName;
        TextView mChallengeDesc;
        Button mButton;

        ViewHolder(View view) {
            super(view);
            mDayCountTV = (TextView) view.findViewById(R.id.textview_day_count);
            mChallengeName = (TextView) view.findViewById(R.id.textview_challenge_name);
            mChallengeDesc = (TextView) view.findViewById(R.id.textview_challenge_description);
            mButton = (Button) view.findViewById(R.id.button);
        }

    }

}
