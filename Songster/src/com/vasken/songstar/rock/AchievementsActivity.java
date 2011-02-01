package com.vasken.songstar.rock;

import com.vasken.songstar.rock.R;
import com.vasken.songstar.rock.manager.State;
import com.vasken.songstar.rock.model.Achievement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AchievementsActivity extends Activity {

	private State theState;
	private static final int NUM_COLUMNS = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.achievements);
		
		theState = State.loadSavedInstance(this);
		showAchivements();
	}

	private void showAchivements() {
		TableLayout tableLayout = (TableLayout)findViewById(R.id.achievements);
		Log.d(this.getClass().toString(), theState.getAchievements().toString());
		for (String key : theState.getAchievements().keySet()) {
			if (theState.getAchievements().get(key)) { 
				Achievement ach = new Achievement(key);
				
				setAchievementImage(tableLayout, ach.getIndex(), ach.getSmallIcon());				
			}
		}
	}

	private void setAchievementImage(TableLayout tableLayout, int index, int icon) {
		int rowForIndex = (int)(Math.floor(index / (double)NUM_COLUMNS));
		int imageRowInTable =  rowForIndex + (1*rowForIndex);
		int columnInRow = index % NUM_COLUMNS;		
		Log.d(this.getClass().toString(), String.valueOf(imageRowInTable) + String.valueOf(columnInRow));
		
		TableRow row = (TableRow)tableLayout.getChildAt(imageRowInTable);
		ImageView image = (ImageView)row.getChildAt(columnInRow);
		image.setImageResource(icon);
	}
}
