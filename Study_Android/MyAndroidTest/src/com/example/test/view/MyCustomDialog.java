package com.example.test.view;

import com.example.test.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *
 * Create custom Dialog windows for your application Custom dialogs rely on
 * custom layouts wich allow you to create and use your own look & feel.
 *
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * <a href="http://my.oschina.net/arthor" target="_blank"
 * rel="nofollow">@author</a> antoine vianey
 *
 */
public class MyCustomDialog extends Dialog {

	public MyCustomDialog(Activity activity, int theme) {
		super(activity, theme);
	}

	public MyCustomDialog(Activity activity) {
		super(activity);
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private Activity activity;
		private String title;
		private String contentMessage;
		private int contentImageId;
		private String contentGifName;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;

		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

		public Builder(Activity activity) {
			this.activity = activity;
		}

		/**
		 * Set the Dialog message from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setContentMessage(String message) {
			this.contentMessage = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setContentMessage(int message) {
			this.contentMessage = (String) activity.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) activity.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		/**
		 * set the contentImageView from resource
		 * 
		 * @param contentText
		 * @return
		 */
		public Builder setContentImageView(int imgId) {
			contentImageId = imgId;
			return this;
		}
		
		/**
		 * set the contentWebView from name
		 * 
		 * @param contentText
		 * @return
		 */
		public Builder setContentWebView(String name) {
			contentGifName = name;
			return this;
		}

		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * contentView is not added to the Dialog...
		 * 
		 * @param v
		 * @return
		 */
		public Builder setCustomView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) activity.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) activity.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Create the custom dialog
		 */
		@SuppressLint("InflateParams")
		public MyCustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final MyCustomDialog dialog = new MyCustomDialog(activity, R.style.center_float_dialog);
			dialog.setCanceledOnTouchOutside(false);
			View layout = inflater.inflate(R.layout.dialog_custom_layout, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// get views
			Button positiveButton = (Button) layout.findViewById(R.id.customDialog_positiveButton);
			Button negativeButton = (Button) layout.findViewById(R.id.customDialog_negativeButton);
			
			// set the dialog title
			((TextView) layout.findViewById(R.id.customDialog_title)).setText(title);
			
			// set the confirm button
			if (positiveButtonText != null) {
				positiveButton.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					positiveButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.customDialog_positiveButton).setVisibility(View.GONE);
			}

			// set the cancel button
			if (negativeButtonText != null) {
				negativeButton.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					negativeButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.customDialog_negativeButton).setVisibility(View.GONE);
			}

			// set the content message
			if (contentMessage != null) {
				TextView contentTextView = (TextView) layout.findViewById(R.id.customDialog_content_message);
				contentTextView.setVisibility(View.VISIBLE);
				contentTextView.setText(contentMessage);
			} 
			if (contentImageId != 0) {
				ImageView contentTextView = (ImageView) layout.findViewById(R.id.customDialog_content_imageView);
				contentTextView.setVisibility(View.VISIBLE);
				contentTextView.setImageResource(contentImageId);
			}
			if (contentGifName != null) {
				WebView contentTextView = (WebView) layout.findViewById(R.id.customDialog_content_webView);
				contentTextView.setVisibility(View.VISIBLE);
				StringBuilder sb = new StringBuilder();
				sb.append("<HTML><body bgcolor='#f3f3f3'><div align=center><IMG src='file:///android_asset/");
				sb.append(contentGifName);
				sb.append("'/></div></body></HTML>");
				contentTextView.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8",null);
			}
			if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.customDialog_content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.customDialog_content)).addView(contentView, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}

}
