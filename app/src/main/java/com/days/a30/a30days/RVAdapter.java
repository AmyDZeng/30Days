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
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    public interface ChallengeButtonListener {
        void onChallengeClicked(Challenge challenge);
    }

    ArrayList<Challenge> mData = new ArrayList<>();
    ChallengeButtonListener mButtonListener;

    public RVAdapter(ArrayList<Challenge> data, ChallengeButtonListener listener) {
        mData = data;
        mButtonListener = listener;
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
        holder.mDayCountTV.setText(challenge.mDayCount + " Days");
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
            holder.mButton.setText("Tap here to complete todays challenge!");
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // only available if not complete
                    if (v.getId() == R.id.button) {
                        v.setEnabled(false);
                        ((Button)v).setText("Challenge completed for today!");
                        if (mButtonListener != null) mButtonListener.onChallengeClicked(challenge);
                    }

                }
            });
        }
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
