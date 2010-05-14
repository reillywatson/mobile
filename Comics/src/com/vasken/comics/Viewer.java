package com.vasken.comics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewer);
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
		selectComic(ComicList.comicList().get(comicNo));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (currentComicInfo != null && currentURL != null) {
			getPreferences(Context.MODE_PRIVATE).edit().putString("lastRead" + currentComicInfo.name, currentURL).commit();
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}

	public void selectComic(ComicInfo info) {
		currentComicInfo = info;
		try {
			currentDownloader = info.downloaderConstructor.call();
			currentDownloader.setDefaultUrl(info.startUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String startUrl = getPreferences(Context.MODE_PRIVATE).getString("lastRead" + info.name, info.startUrl);
		downloadComic(startUrl);
	}

	public void downloadComic(String url) {
		currentDownloader.setUrl(url);
		new DownloadTask().execute(currentDownloader);
	}
	
	void loadComic(final Comic comic) {
		if (comic != null) {
			currentComic = comic;
			if (comic.image == null) {
				if (!successfullyLoadedFirstComic && !currentDownloader.getUrl().equals(currentComicInfo.startUrl)) {
					downloadComic(currentComicInfo.startUrl);
					return;
				}
			}
			TextView alt = (TextView) Viewer.this.findViewById(R.id.alt_text);
			WebView webView = (WebView) Viewer.this.findViewById(R.id.WebView);
			webView.setVisibility((comic.image != null) ? View.VISIBLE : View.GONE);
			webView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View arg0) {
					if (comic.altText != null) {
						Toast t = Toast.makeText(Viewer.this, comic.altText, Toast.LENGTH_LONG);
						t.show();
					}
					return true;
				}});
			alt.setVisibility((comic.altText != null) ? View.VISIBLE : View.GONE);
			if (comic.altText != null) {
				alt.setText(StringUtils.unescapeHtml(comic.altText));
			}
			if (comic.image != null) {
				webView.loadUrl(comic.image);
				currentURL = comic.permalink;
				successfullyLoadedFirstComic = true;
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
