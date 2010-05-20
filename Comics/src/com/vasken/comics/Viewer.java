package com.vasken.comics;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vasken.comics.Downloaders.Downloader;
import com.vasken.util.StringUtils;
import com.vasken.util.UserTask;

public class Viewer extends Activity {
	Downloader currentDownloader;
	ComicInfo currentComicInfo;
	Comic currentComic;
	String currentURL;
	boolean successfullyLoadedFirstComic;
	boolean currentComicIsRandom;

	Dialog theAboutDialog;
	Dialog theLoadingDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewer);
		WebView webView = (WebView) Viewer.this.findViewById(R.id.WebView);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setBackgroundColor(0);
		webView.getSettings().setJavaScriptEnabled(false);
		
		prepareAboutDialog();
		prepareLoadingDialog();
		
		Viewer rotatedSelf = (Viewer)getLastNonConfigurationInstance();
		if (rotatedSelf != null) {
			currentDownloader = rotatedSelf.currentDownloader;
			currentComicInfo = rotatedSelf.currentComicInfo;
			currentComic = rotatedSelf.currentComic;
			currentURL = rotatedSelf.currentURL;
			successfullyLoadedFirstComic = rotatedSelf.successfullyLoadedFirstComic;
			if (currentComic != null) {
				loadComic(currentComic);
			}
			return;
		}
		int comicNo = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			comicNo = extras.getInt("com.vasken.comics.comicNo");
		}
		ComicInfo selectedComic;
		if (comicNo < Main.favourites.size()) {
			selectedComic = Main.favourites.get(comicNo);
		}
		else {
			selectedComic = Main.comics.get(comicNo - Main.favourites.size());
		}
		selectComic(selectedComic);
	}

	private void prepareAboutDialog() {
		theAboutDialog = new Dialog(this);
		theAboutDialog.setContentView(R.layout.about_dialog);
		theAboutDialog.setTitle("About");
	}

	private void prepareLoadingDialog() {
		theLoadingDialog = new Dialog(this);
		theLoadingDialog.setContentView(R.layout.loading_dialog);
		theLoadingDialog.setTitle("Loading...");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (currentComicInfo != null && currentURL != null && !currentComicIsRandom) {
			getPreferences(Context.MODE_PRIVATE).edit().putString("lastRead" + currentComicInfo.name, currentURL).commit();
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}

	public void selectComic(ComicInfo info) {
		currentComicInfo = info;
		Log.d("SELECTED", info.name);
		try {
			if (info.downloaderConstructor == null) {
				currentDownloader = new Downloader(info);
			}
			else {
				currentDownloader = info.downloaderConstructor.call();
			}
			currentDownloader.setDefaultUrl(info.startUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String startUrl = getPreferences(Context.MODE_PRIVATE).getString("lastRead" + info.name, info.startUrl);
		downloadComic(startUrl);
	}

	public void downloadComic(String url) {
		currentComicIsRandom = false;
		currentDownloader.setUrl(url);
		
		theLoadingDialog.show();
		
		new DownloadTask().execute(currentDownloader);
	}
	
	void loadComic(final Comic comic) {
		if (comic != null) {
			theLoadingDialog.hide();
			
			currentComic = comic;
			if (comic.image == null && comic.bitmap == null) {
				if (!successfullyLoadedFirstComic && !currentDownloader.getUrl().equals(currentComicInfo.startUrl)) {
					downloadComic(currentComicInfo.startUrl);
					return;
				}
			}
			TextView alt = (TextView) Viewer.this.findViewById(R.id.alt_text);
			WebView webView = (WebView) Viewer.this.findViewById(R.id.WebView);
			webView.setVisibility((comic.image != null || comic.bitmap != null) ? View.VISIBLE : View.GONE);
			webView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View arg0) {
					if (comic.altText != null) {
						Toast t = Toast.makeText(Viewer.this, StringUtils.unescapeHtml(comic.altText), Toast.LENGTH_LONG);
						t.show();
					}
					return true;
				}});

			if (comic.image != null) {
				webView.loadUrl(comic.image);
			}
			else if (comic.bitmap != null) {
				try {
					File file = new File(this.getCacheDir().getAbsolutePath() + "com.vasken.comics.tempimage.png");
					FileOutputStream outstream = new FileOutputStream(file);
					comic.bitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
					outstream.close();
					webView.loadUrl(file.toURL().toString());
				} catch (Exception e) {e.printStackTrace();}
			}
			if (comic.image != null || comic.bitmap != null) {
				currentURL = comic.permalink;
				successfullyLoadedFirstComic = true;
				alt.setVisibility(View.GONE);
			} else {
				currentURL = null;
				alt.setVisibility(View.VISIBLE);
				alt.setText("Unable to load comic");
			}
			Button prev = (Button) Viewer.this.findViewById(R.id.prev_comic);
			Button next = (Button) Viewer.this.findViewById(R.id.next_comic);
			boolean enablePrev = comic.prevUrl != null && !comic.prevUrl.equals(comic.url);
			boolean enableNext = comic.nextUrl != null && !comic.nextUrl.equals(comic.url);
			prev.setEnabled(enablePrev);
			next.setEnabled(enableNext);
			if (enablePrev) {
				prev.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						Viewer.this.downloadComic(comic.prevUrl);
					}
				});
			}
			if (enableNext) {
				next.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						Viewer.this.downloadComic(comic.nextUrl);
					}
				});
			}
			TextView tv = (TextView) Viewer.this.findViewById(R.id.title);
			if (comic.title != null) {
				tv.setText(StringUtils.unescapeHtml(comic.title));
			} else {
				tv.setText("");
			}
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (currentComic != null && currentComic.randomUrl != null) {
			menu.add("Random");
		}
		menu.add("Newest");
		menu.add("About");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Random") && currentComic != null && currentComic.randomUrl != null) {
			downloadComic(currentComic.randomUrl);
			currentComicIsRandom = true;
		} else if (item.getTitle().equals("Newest") && currentComic != null) {
			downloadComic(currentComicInfo.startUrl);
		} else if (item.getTitle().equals("About")) {
			theAboutDialog.show();
		}
		return true;
	}

	class DownloadTask extends UserTask<Downloader, Void, Comic> {
		@Override
		public Comic doInBackground(Downloader... params) {
			return params[0].getComic();
		}

		@Override
		public void onPostExecute(final Comic comic) {
			loadComic(comic);
		}
	}

}
